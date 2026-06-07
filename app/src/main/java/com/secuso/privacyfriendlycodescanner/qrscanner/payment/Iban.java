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

import java.math.BigInteger;

/**
 * Validation helper for International Bank Account Numbers (IBAN) following the
 * ISO 13616 structure and the ISO 7064 MOD 97-10 check.
 * <p>
 * Pure Java with no Android dependencies so it can be exercised with JVM unit tests.
 */
public final class Iban {

    private static final BigInteger NINETY_SEVEN = BigInteger.valueOf(97);

    private Iban() {
    }

    /**
     * Removes spaces and converts to upper case, leaving the IBAN otherwise untouched.
     */
    public static String normalize(String iban) {
        if (iban == null) {
            return null;
        }
        return iban.replace(" ", "").trim().toUpperCase();
    }

    /**
     * Checks whether the given string is a structurally valid IBAN that passes the MOD 97-10 check.
     *
     * @param input the candidate IBAN (spaces are tolerated)
     * @return {@code true} if the input is a valid IBAN
     */
    public static boolean isValid(String input) {
        String iban = normalize(input);
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return false;
        }
        // Two letters (country), two digits (check), then alphanumeric.
        if (!iban.matches("[A-Z]{2}[0-9]{2}[A-Z0-9]+")) {
            return false;
        }
        // Move the four initial characters to the end of the string.
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        // Replace each letter with two digits: A = 10, B = 11, ..., Z = 35.
        StringBuilder numeric = new StringBuilder(rearranged.length() * 2);
        for (int i = 0; i < rearranged.length(); i++) {
            char c = rearranged.charAt(i);
            if (c >= '0' && c <= '9') {
                numeric.append(c);
            } else if (c >= 'A' && c <= 'Z') {
                numeric.append(c - 'A' + 10);
            } else {
                return false;
            }
        }
        try {
            return new BigInteger(numeric.toString()).mod(NINETY_SEVEN).intValue() == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
