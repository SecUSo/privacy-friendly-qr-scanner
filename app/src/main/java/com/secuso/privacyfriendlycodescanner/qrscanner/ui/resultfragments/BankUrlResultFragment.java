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

package com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a scanned {@code bank://singlepaymentsepa} payment URL in a structured form.
 */
public class BankUrlResultFragment extends PaymentResultFragment {

    public BankUrlResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_result_payment_bank_url, container, false);
        if (payment == null) {
            return v;
        }

        bindField(v, R.id.result_field_bank_name, R.string.payment_recipient_value, payment.getRecipientName());
        bindField(v, R.id.result_field_bank_iban, R.string.payment_iban_value, payment.getIban());
        bindField(v, R.id.result_field_bank_bic, R.string.payment_bic_value, payment.getBic());
        bindField(v, R.id.result_field_bank_amount, R.string.payment_amount_value, payment.getFormattedAmount());
        bindField(v, R.id.result_field_bank_remittance, R.string.payment_purpose_value, payment.getRemittance());

        return v;
    }

    @Override
    public void onProceedPressed(Context context) {
        if (payment == null) {
            return;
        }
        List<CopyItem> items = new ArrayList<>();
        addCopyItem(items, R.string.payment_copy_iban, payment.getIban());
        addCopyItem(items, R.string.payment_copy_amount, payment.getAmount());
        addCopyItem(items, R.string.payment_copy_purpose, payment.getRemittance());
        addCopyItem(items, R.string.payment_copy_recipient, payment.getRecipientName());
        showCopyDialog(context, items);
    }

    @Override
    public String getProceedButtonTitle(Context context) {
        return context.getString(R.string.payment_copy_field);
    }
}
