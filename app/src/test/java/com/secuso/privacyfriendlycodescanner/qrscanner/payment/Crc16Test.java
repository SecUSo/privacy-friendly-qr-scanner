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

import org.junit.Test;

public class Crc16Test {

    @Test
    public void matchesKnownCcittFalseVector() {
        // Standard CRC-16/CCITT-FALSE check value for the ASCII string "123456789".
        assertEquals(0x29B1, Crc16.compute("123456789"));
        assertEquals("29B1", Crc16.computeHex("123456789"));
    }

    @Test
    public void hexIsZeroPaddedAndUppercase() {
        // Result must always be exactly four upper-case hex characters.
        String hex = Crc16.computeHex("A");
        assertEquals(4, hex.length());
        assertEquals(hex.toUpperCase(), hex);
    }
}
