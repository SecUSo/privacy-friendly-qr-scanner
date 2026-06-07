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

/**
 * Immutable, Android-independent representation of a parsed payment QR code.
 * <p>
 * The union of fields across all supported formats is held here; fields that are
 * not present in a given format (or were optional and omitted) stay {@code null}.
 * The UI hides empty fields. Instances are produced by {@link PaymentCodeParser}.
 */
public final class PaymentCode {

    public enum Type {
        /** European Payments Council Quick Response Code (a.k.a. GiroCode). */
        EPC,
        /** {@code bank://singlepaymentsepa?...} URL variant. */
        BANK_URL,
        /** EMVCo Merchant Presented Mode code, including the Brazilian Pix scheme. */
        EMV
    }

    private final Type type;
    private final boolean pixScheme;

    private final String version;
    private final String recipientName;
    private final String iban;
    private final String bic;
    private final String amount;
    private final String currency;
    private final String remittance;
    private final String purposeCode;
    private final String reference;
    private final String city;
    private final String countryCode;
    private final String pixKey;
    private final String pixInfo;

    private PaymentCode(Builder b) {
        this.type = b.type;
        this.pixScheme = b.pixScheme;
        this.version = b.version;
        this.recipientName = b.recipientName;
        this.iban = b.iban;
        this.bic = b.bic;
        this.amount = b.amount;
        this.currency = b.currency;
        this.remittance = b.remittance;
        this.purposeCode = b.purposeCode;
        this.reference = b.reference;
        this.city = b.city;
        this.countryCode = b.countryCode;
        this.pixKey = b.pixKey;
        this.pixInfo = b.pixInfo;
    }

    public Type getType() {
        return type;
    }

    /** {@code true} only for {@link Type#EMV} codes that declare the {@code br.gov.bcb.pix} GUI. */
    public boolean isPixScheme() {
        return pixScheme;
    }

    public String getVersion() {
        return version;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getRemittance() {
        return remittance;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public String getReference() {
        return reference;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPixKey() {
        return pixKey;
    }

    public String getPixInfo() {
        return pixInfo;
    }

    /**
     * Convenience: combines amount and currency for display, e.g. {@code "12.50 EUR"}.
     * Returns {@code null} if no amount is present.
     */
    public String getFormattedAmount() {
        if (amount == null || amount.isEmpty()) {
            return null;
        }
        if (currency == null || currency.isEmpty()) {
            return amount;
        }
        return amount + " " + currency;
    }

    static final class Builder {
        private final Type type;
        private boolean pixScheme;
        private String version;
        private String recipientName;
        private String iban;
        private String bic;
        private String amount;
        private String currency;
        private String remittance;
        private String purposeCode;
        private String reference;
        private String city;
        private String countryCode;
        private String pixKey;
        private String pixInfo;

        Builder(Type type) {
            this.type = type;
        }

        Builder pixScheme(boolean v) {
            this.pixScheme = v;
            return this;
        }

        Builder version(String v) {
            this.version = emptyToNull(v);
            return this;
        }

        Builder recipientName(String v) {
            this.recipientName = emptyToNull(v);
            return this;
        }

        Builder iban(String v) {
            this.iban = emptyToNull(v);
            return this;
        }

        Builder bic(String v) {
            this.bic = emptyToNull(v);
            return this;
        }

        Builder amount(String v) {
            this.amount = emptyToNull(v);
            return this;
        }

        Builder currency(String v) {
            this.currency = emptyToNull(v);
            return this;
        }

        Builder remittance(String v) {
            this.remittance = emptyToNull(v);
            return this;
        }

        Builder purposeCode(String v) {
            this.purposeCode = emptyToNull(v);
            return this;
        }

        Builder reference(String v) {
            this.reference = emptyToNull(v);
            return this;
        }

        Builder city(String v) {
            this.city = emptyToNull(v);
            return this;
        }

        Builder countryCode(String v) {
            this.countryCode = emptyToNull(v);
            return this;
        }

        Builder pixKey(String v) {
            this.pixKey = emptyToNull(v);
            return this;
        }

        Builder pixInfo(String v) {
            this.pixInfo = emptyToNull(v);
            return this;
        }

        PaymentCode build() {
            return new PaymentCode(this);
        }

        private static String emptyToNull(String v) {
            if (v == null) {
                return null;
            }
            String trimmed = v.trim();
            return trimmed.isEmpty() ? null : trimmed;
        }
    }
}
