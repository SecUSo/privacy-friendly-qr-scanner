package com.secuso.privacyfriendlycodescanner.qrscanner.ui.activities.generator;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.generator.Contents;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers.GeneratorKeyboardListener;

public class TelEnterActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_enter);

        final EditText qrResult = (EditText) findViewById(R.id.editPhone);

        int maxLength = 75;
        qrResult.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        ExtendedFloatingActionButton generate = (ExtendedFloatingActionButton) findViewById(R.id.btnGenerate);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);
        GeneratorKeyboardListener listener = new GeneratorKeyboardListener(rootView, generate, R.id.btnGenerate, getApplicationContext().getResources().getDisplayMetrics().densityDpi);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        generate.setOnClickListener(new View.OnClickListener() {
            String result;

            @Override
            public void onClick(View v) {
                result = qrResult.getText().toString();
                if (result.isEmpty()) {
                    Toast.makeText(TelEnterActivity.this, R.string.activity_enter_toast_missing_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(TelEnterActivity.this, QrGeneratorDisplayActivity.class);
                i.putExtra("gn", result);
                i.putExtra("type", Contents.Type.PHONE);
                startActivity(i);
            }

        });

        findViewById(R.id.selectContactButton).setOnClickListener(view -> selectPhoneNumber());

    }

    private void selectPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_PHONE && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection =
                    {
                            ContactsContract.CommonDataKinds.Phone._ID,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.LABEL
                    };
            try {
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    int phoneNoIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNo = cursor.getString(phoneNoIdx);
                    ((EditText) findViewById(R.id.editPhone)).setText(phoneNo);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
