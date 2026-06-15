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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IbanTest {

    @Test
    public void acceptsValidIbans() {
        assertTrue(Iban.isValid("DE89370400440532013000"));
        assertTrue(Iban.isValid("GB82WEST12345698765432"));
        // Spaces are tolerated.
        assertTrue(Iban.isValid("DE89 3704 0044 0532 0130 00"));
        // Lower case is tolerated.
        assertTrue(Iban.isValid("de89370400440532013000"));
    }

    @Test
    public void rejectsInvalidIbans() {
        assertFalse(Iban.isValid(null));
        assertFalse(Iban.isValid(""));
        assertFalse(Iban.isValid("DE89370400440532013001")); // wrong check digits
        assertFalse(Iban.isValid("DE0037040044053201")); // too short
        assertFalse(Iban.isValid("12345678901234567890")); // no country letters
        assertFalse(Iban.isValid("DE89-3704-0044")); // illegal characters
    }

    @Test
    public void normalizeStripsSpacesAndUppercases() {
        assertEquals("DE89370400440532013000", Iban.normalize("de89 3704 0044 0532 0130 00"));
    }
}
