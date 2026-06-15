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

/**
 * CRC-16/CCITT-FALSE implementation as required by the EMVCo Merchant Presented Mode
 * QR specification (polynomial 0x1021, initial value 0xFFFF, no reflection, no final XOR).
 * <p>
 * Pure Java with no Android dependencies so it can be exercised with JVM unit tests.
 */
public final class Crc16 {

    private static final int POLYNOMIAL = 0x1021;
    private static final int INITIAL_VALUE = 0xFFFF;

    private Crc16() {
    }

    /**
     * Computes the CRC-16/CCITT-FALSE over the UTF-8 bytes of the given string.
     *
     * @return the CRC as an unsigned 16-bit value
     */
    public static int compute(String data) {
        int crc = INITIAL_VALUE;
        // EMV / Pix payloads are encoded as UTF-8; compute the CRC over those bytes.
        byte[] bytes;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // UTF-8 is guaranteed to be available on every platform.
            bytes = data.getBytes();
        }
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return crc & 0xFFFF;
    }

    /**
     * Returns the CRC as an upper case, zero-padded four digit hex string, matching the
     * representation used inside EMV / Pix payloads.
     */
    public static String computeHex(String data) {
        return String.format("%04X", compute(data));
    }
}
