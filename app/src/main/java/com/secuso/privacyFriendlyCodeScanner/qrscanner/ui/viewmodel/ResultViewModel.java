package com.secuso.privacyfriendlycodescanner.qrscanner.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.google.zxing.client.result.ParsedResult;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.database.HistoryItem;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.resultfragments.ResultFragment;

public class ResultViewModel extends ViewModel {

    public BarcodeResult currentBarcodeResult = null;
    public ResultFragment currentResultFragment = null;

    public HistoryItem currentHistoryItem = null;
    public ParsedResult mParsedResult = null;
    public Bitmap mCodeImage = null;
    public boolean mSavedToHistory = false;

}
