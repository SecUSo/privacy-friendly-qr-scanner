/*
    Privacy Friendly QR Scanner
    Copyright (C) 2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.secuso.privacyfriendlycodescanner.qrscanner.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PaymentCodeParserTest {

    private static final String VALID_IBAN = "DE89370400440532013000";

    // ------------------------------------------------------------------
    // EPC / GiroCode
    // ------------------------------------------------------------------

    @Test
    public void parsesValidEpcVersion002() {
        String raw = "BCD\n002\n1\nSCT\nBFSWDE33MUE\nMax Mustermann\n" + VALID_IBAN
                + "\nEUR123.45\nGDDS\n\nThank you for your donation\n";
        PaymentCode code = PaymentCodeParser.parse(raw);
        assertNotNull(code);
        assertEquals(PaymentCode.Type.EPC, code.getType());
        assertEquals("Max Mustermann", code.getRecipientName());
        assertEquals(VALID_IBAN, code.getIban());
        assertEquals("BFSWDE33MUE", code.getBic());
        assertEquals("EUR", code.getCurrency());
        assertEquals("123.45", code.getAmount());
        assertEquals("123.45 EUR", code.getFormattedAmount());
        assertEquals("Thank you for your donation", code.getRemittance());
        assertEquals("002", code.getVersion());
    }

    @Test
    public void parsesValidEpcVersion001WithBic() {
        String raw = "BCD\n001\n1\nSCT\nCOBADEFFXXX\nAcme Inc\n" + VALID_IBAN + "\nEUR10\n";
        PaymentCode code = PaymentCodeParser.parse(raw);
        assertNotNull(code);
        assertEquals("10", code.getAmount());
    }

    @Test
    public void rejectsEpcVersion001WithoutBic() {
        // BIC is mandatory in EPC version 001.
        String raw = "BCD\n001\n1\nSCT\n\nAcme Inc\n" + VALID_IBAN + "\n";
        assertNull(PaymentCodeParser.parse(raw));
    }

    @Test
    public void rejectsEpcWithInvalidIban() {
        String raw = "BCD\n002\n1\nSCT\nBFSWDE33MUE\nMax Mustermann\nDE89370400440532013001\n";
        assertNull(PaymentCodeParser.parse(raw));
    }

    @Test
    public void rejectsEpcWithMalformedAmount() {
        String raw = "BCD\n002\n1\nSCT\nBFSWDE33MUE\nMax Mustermann\n" + VALID_IBAN + "\n12.50\n";
        assertNull(PaymentCodeParser.parse(raw));
    }

    @Test
    public void rejectsEpcWithWrongServiceTag() {
        String raw = "ABC\n002\n1\nSCT\nBFSWDE33MUE\nMax Mustermann\n" + VALID_IBAN + "\n";
        assertNull(PaymentCodeParser.parse(raw));
    }

    // ------------------------------------------------------------------
    // bank://singlepaymentsepa
    // ------------------------------------------------------------------

    @Test
    public void parsesValidBankUrl() {
        String raw = "bank://singlepaymentsepa?name=DER%20EMPFAENGER&iban=" + VALID_IBAN
                + "&amount=3.49&reason=EINKAUF%20BEI%20ALDI&currency=EUR";
        PaymentCode code = PaymentCodeParser.parse(raw);
        assertNotNull(code);
        assertEquals(PaymentCode.Type.BANK_URL, code.getType());
        assertEquals("DER EMPFAENGER", code.getRecipientName());
        assertEquals(VALID_IBAN, code.getIban());
        assertEquals("3.49", code.getAmount());
        assertEquals("EUR", code.getCurrency());
        assertEquals("EINKAUF BEI ALDI", code.getRemittance());
    }

    @Test
    public void bankUrlIsCurrencyAgnostic() {
        String raw = "bank://singlepaymentsepa?name=Shop&iban=" + VALID_IBAN + "&amount=5&currency=USD";
        PaymentCode code = PaymentCodeParser.parse(raw);
        assertNotNull(code);
        assertEquals("USD", code.getCurrency());
    }

    @Test
    public void rejectsBankUrlWithInvalidIban() {
        String raw = "bank://singlepaymentsepa?name=Shop&iban=DE00370400440532013000&amount=5";
        assertNull(PaymentCodeParser.parse(raw));
    }

    // ------------------------------------------------------------------
    // EMV / Pix
    // ------------------------------------------------------------------

    @Test
    public void parsesValidPix() {
        PaymentCode code = PaymentCodeParser.parse(buildPix());
        assertNotNull(code);
        assertEquals(PaymentCode.Type.EMV, code.getType());
        assertTrue(code.isPixScheme());
        assertEquals("Fulano de Tal", code.getRecipientName());
        assertEquals("BRASILIA", code.getCity());
        assertEquals("BRL", code.getCurrency());
        assertEquals("BR", code.getCountryCode());
        assertEquals("test@example.com", code.getPixKey());
    }

    @Test
    public void rejectsPixWithBrokenCrc() {
        String pix = buildPix();
        // Flip the last CRC character.
        char last = pix.charAt(pix.length() - 1);
        String broken = pix.substring(0, pix.length() - 1) + (last == '0' ? '1' : '0');
        assertNull(PaymentCodeParser.parse(broken));
    }

    /** Builds a valid static Pix payload, computing a correct CRC over the body. */
    private static String buildPix() {
        String body = "000201"
                + "2638" + "0014br.gov.bcb.pix" + "0116test@example.com"
                + "52040000"
                + "5303986"
                + "5802BR"
                + "5913Fulano de Tal"
                + "6008BRASILIA"
                + "6207" + "0503***"
                + "6304";
        return body + Crc16.computeHex(body);
    }

    // ------------------------------------------------------------------
    // Negative cases: ordinary content must NOT be detected as a payment code
    // ------------------------------------------------------------------

    @Test
    public void doesNotDetectPlainText() {
        assertNull(PaymentCodeParser.parse("Hello world"));
        assertNull(PaymentCodeParser.parse("BCD is a nice band"));
    }

    @Test
    public void doesNotDetectOrdinaryUrl() {
        assertNull(PaymentCodeParser.parse("https://example.com/path?a=1"));
        assertNull(PaymentCodeParser.parse("http://000201.example.com"));
    }

    @Test
    public void handlesNullAndEmpty() {
        assertNull(PaymentCodeParser.parse(null));
        assertNull(PaymentCodeParser.parse(""));
        assertNull(PaymentCodeParser.parse("   "));
    }
}
