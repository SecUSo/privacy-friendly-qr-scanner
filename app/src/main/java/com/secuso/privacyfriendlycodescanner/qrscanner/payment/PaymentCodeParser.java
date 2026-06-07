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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Detects and parses payment QR codes into a {@link PaymentCode}.
 * <p>
 * Detection is <b>strict</b>: a code is only recognised when its signature matches and
 * all mandatory fields are present and valid (IBAN MOD 97-10 for EPC / bank URLs,
 * CRC-16 for EMV). Anything else yields {@code null}, leaving the caller to fall back
 * to the unchanged default handling. Parsing is exception safe and never throws.
 * <p>
 * Currency is treated purely as informational data and is never used as an acceptance
 * criterion, so non-euro EMV codes (e.g. Pix in BRL) are supported.
 * <p>
 * Pure Java with no Android dependencies so it can be exercised with JVM unit tests.
 */
public final class PaymentCodeParser {

    private static final String BANK_URL_PREFIX = "bank://singlepaymentsepa";

    /** ISO 4217 numeric to alphabetic mapping for the most common currencies. */
    private static final Map<String, String> CURRENCY_CODES = new HashMap<>();

    static {
        CURRENCY_CODES.put("986", "BRL");
        CURRENCY_CODES.put("978", "EUR");
        CURRENCY_CODES.put("840", "USD");
        CURRENCY_CODES.put("826", "GBP");
        CURRENCY_CODES.put("756", "CHF");
        CURRENCY_CODES.put("392", "JPY");
        CURRENCY_CODES.put("156", "CNY");
        CURRENCY_CODES.put("036", "AUD");
        CURRENCY_CODES.put("124", "CAD");
        CURRENCY_CODES.put("752", "SEK");
        CURRENCY_CODES.put("578", "NOK");
        CURRENCY_CODES.put("208", "DKK");
        CURRENCY_CODES.put("985", "PLN");
    }

    private PaymentCodeParser() {
    }

    /**
     * Attempts to parse the raw decoded QR content as a payment code.
     *
     * @param raw the raw text of the scanned code
     * @return a {@link PaymentCode} or {@code null} if the content is not a recognised,
     * fully valid payment code
     */
    public static PaymentCode parse(String raw) {
        if (raw == null) {
            return null;
        }
        try {
            String trimmed = raw.trim();
            if (trimmed.isEmpty()) {
                return null;
            }
            String lower = trimmed.toLowerCase();
            if (trimmed.startsWith("BCD\n") || trimmed.startsWith("BCD\r\n")) {
                return parseEpc(trimmed);
            }
            if (lower.startsWith(BANK_URL_PREFIX)) {
                return parseBankUrl(trimmed);
            }
            if (trimmed.startsWith("000201") || trimmed.startsWith("000202")) {
                return parseEmv(trimmed);
            }
            return null;
        } catch (RuntimeException e) {
            // Strict parsing: any unexpected condition means "not a payment code".
            return null;
        }
    }

    // ------------------------------------------------------------------
    // EPC / GiroCode
    // ------------------------------------------------------------------

    private static PaymentCode parseEpc(String raw) {
        // EPC uses LF separators; tolerate CRLF by stripping the CR.
        String[] lines = raw.replace("\r\n", "\n").split("\n", -1);
        // Mandatory elements run up to and including the IBAN (index 6).
        if (lines.length < 7) {
            return null;
        }
        String serviceTag = lines[0].trim();
        String version = lines[1].trim();
        String charset = lines[2].trim();
        String identification = lines[3].trim();
        String bic = lines[4].trim();
        String name = lines[5].trim();
        String iban = lines[6].trim();

        if (!"BCD".equals(serviceTag)) {
            return null;
        }
        if (!"001".equals(version) && !"002".equals(version)) {
            return null;
        }
        if (!charset.matches("[1-8]")) {
            return null;
        }
        if (!"SCT".equals(identification)) {
            return null;
        }
        if (name.isEmpty()) {
            return null;
        }
        if (!Iban.isValid(iban)) {
            return null;
        }
        // BIC is mandatory in version 001 and optional in version 002.
        if ("001".equals(version) && bic.isEmpty()) {
            return null;
        }

        PaymentCode.Builder builder = new PaymentCode.Builder(PaymentCode.Type.EPC)
                .version(version)
                .bic(bic)
                .recipientName(name)
                .iban(Iban.normalize(iban));

        // Amount (optional), formatted as a three letter currency followed by the value.
        if (lines.length > 7) {
            String amountField = lines[7].trim();
            if (!amountField.isEmpty()) {
                if (!amountField.matches("[A-Z]{3}\\d+(\\.\\d+)?")) {
                    return null; // present but malformed -> strict reject
                }
                builder.currency(amountField.substring(0, 3));
                builder.amount(amountField.substring(3));
            }
        }
        if (lines.length > 8) {
            builder.purposeCode(lines[8].trim());
        }
        // Either a structured reference (index 9) or unstructured remittance text (index 10).
        if (lines.length > 9) {
            builder.reference(lines[9].trim());
        }
        if (lines.length > 10) {
            builder.remittance(lines[10].trim());
        }
        return builder.build();
    }

    // ------------------------------------------------------------------
    // bank://singlepaymentsepa?...
    // ------------------------------------------------------------------

    private static PaymentCode parseBankUrl(String raw) {
        int queryStart = raw.indexOf('?');
        if (queryStart < 0 || queryStart == raw.length() - 1) {
            return null;
        }
        Map<String, String> params = new HashMap<>();
        for (String pair : raw.substring(queryStart + 1).split("&")) {
            int eq = pair.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = decode(pair.substring(0, eq)).toLowerCase();
            String value = decode(pair.substring(eq + 1));
            params.put(key, value);
        }

        String name = params.get("name");
        String iban = params.get("iban");
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (!Iban.isValid(iban)) {
            return null;
        }

        return new PaymentCode.Builder(PaymentCode.Type.BANK_URL)
                .recipientName(name)
                .iban(Iban.normalize(iban))
                .bic(params.get("bic"))
                .amount(params.get("amount"))
                .currency(params.get("currency"))
                .remittance(params.get("reason"))
                .build();
    }

    private static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    // ------------------------------------------------------------------
    // EMVCo Merchant Presented Mode (incl. Pix)
    // ------------------------------------------------------------------

    private static PaymentCode parseEmv(String raw) {
        // The CRC tag "63" with length "04" must be present; validate it strictly.
        int crcTag = raw.lastIndexOf("6304");
        if (crcTag < 0 || crcTag + 8 != raw.length()) {
            return null;
        }
        String provided = raw.substring(crcTag + 4);
        String computed = Crc16.computeHex(raw.substring(0, crcTag + 4));
        if (!provided.equalsIgnoreCase(computed)) {
            return null;
        }

        Map<String, String> root = parseTlv(raw);
        if (root == null || !root.containsKey("00")) {
            return null;
        }

        String name = root.get("59");
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        // Locate a merchant account information template (tags 02..51) and read the scheme.
        boolean pixScheme = false;
        String pixKey = null;
        String pixInfo = null;
        for (int tag = 2; tag <= 51; tag++) {
            String key = String.format("%02d", tag);
            String template = root.get(key);
            if (template == null) {
                continue;
            }
            Map<String, String> account = parseTlv(template);
            if (account == null) {
                continue;
            }
            String gui = account.get("00");
            if (gui != null && gui.toLowerCase().contains("br.gov.bcb.pix")) {
                pixScheme = true;
                if (pixKey == null) {
                    pixKey = account.get("01");
                }
                if (pixInfo == null) {
                    pixInfo = account.get("02");
                }
            } else if (pixKey == null) {
                // Generic EMV: keep the first identifier we encounter as a fallback.
                pixKey = account.get("01");
            }
        }

        String reference = null;
        String additional = root.get("62");
        if (additional != null) {
            Map<String, String> additionalData = parseTlv(additional);
            if (additionalData != null) {
                reference = additionalData.get("05");
            }
        }

        return new PaymentCode.Builder(PaymentCode.Type.EMV)
                .pixScheme(pixScheme)
                .recipientName(name)
                .city(root.get("60"))
                .amount(root.get("54"))
                .currency(resolveCurrency(root.get("53")))
                .countryCode(root.get("58"))
                .pixKey(pixKey)
                .pixInfo(pixInfo)
                .reference(reference)
                .build();
    }

    /**
     * Parses a flat EMV TLV string into a tag to value map. Returns {@code null} when the
     * structure is malformed (e.g. a declared length exceeds the remaining input).
     */
    private static Map<String, String> parseTlv(String data) {
        Map<String, String> result = new HashMap<>();
        int i = 0;
        int n = data.length();
        while (i + 4 <= n) {
            String tag = data.substring(i, i + 2);
            if (!tag.matches("\\d{2}")) {
                return null;
            }
            String lengthStr = data.substring(i + 2, i + 4);
            if (!lengthStr.matches("\\d{2}")) {
                return null;
            }
            int length = Integer.parseInt(lengthStr);
            int valueStart = i + 4;
            int valueEnd = valueStart + length;
            if (valueEnd > n) {
                return null;
            }
            result.put(tag, data.substring(valueStart, valueEnd));
            i = valueEnd;
        }
        if (i != n) {
            return null; // trailing bytes that do not form a complete TLV
        }
        return result;
    }

    private static String resolveCurrency(String numeric) {
        if (numeric == null) {
            return null;
        }
        String code = CURRENCY_CODES.get(numeric.trim());
        return code != null ? code : numeric.trim();
    }
}
