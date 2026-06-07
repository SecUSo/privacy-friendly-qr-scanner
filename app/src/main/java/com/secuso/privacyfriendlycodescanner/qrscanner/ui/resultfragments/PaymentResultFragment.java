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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.payment.PaymentCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared base for the payment code result fragments (EPC, bank URL, EMV/Pix).
 * <p>
 * Holds the parsed {@link PaymentCode} taken from the shared {@code ResultViewModel} and
 * provides helpers to bind labelled fields and to offer a "copy single field" dialog,
 * which is the proceed action chosen for all payment codes.
 */
public abstract class PaymentResultFragment extends ResultFragment {

    protected PaymentCode payment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (viewModel != null) {
            payment = viewModel.mPaymentCode;
        }
    }

    /**
     * Sets {@code "label: value"} on the given text view, or hides it when the value is empty.
     *
     * @param labelRes a string resource with a single {@code %1$s} placeholder
     */
    protected void bindField(View root, int textViewId, @StringRes int labelRes, String value) {
        TextView tv = root.findViewById(textViewId);
        if (value == null || value.trim().isEmpty()) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(getString(labelRes, value));
        }
    }

    /**
     * A single entry in the copy dialog: a human readable label and the value to copy.
     */
    protected static final class CopyItem {
        final String label;
        final String value;

        CopyItem(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }

    /**
     * Adds a copy entry only when the value is present.
     */
    protected void addCopyItem(List<CopyItem> items, @StringRes int labelRes, String value) {
        if (value != null && !value.trim().isEmpty()) {
            items.add(new CopyItem(getString(labelRes), value));
        }
    }

    /**
     * Shows a dialog letting the user copy one of the given fields (plus "copy everything").
     */
    protected void showCopyDialog(final Context context, List<CopyItem> items) {
        final List<CopyItem> all = new ArrayList<>(items);
        // Always offer to copy the full raw content.
        all.add(new CopyItem(getString(R.string.payment_copy_all),
                viewModel.currentHistoryItem.getText()));

        String[] labels = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            labels[i] = all.get(i).label;
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.choose_action)
                .setItems(labels, (dialog, which) -> copyToClipboard(context, all.get(which).value))
                .show();
    }

    private void copyToClipboard(Context context, String value) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("Text", value));
            android.widget.Toast.makeText(context, R.string.copied_to_clipboard,
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
