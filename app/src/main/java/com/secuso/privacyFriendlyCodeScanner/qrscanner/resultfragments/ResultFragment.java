package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public abstract class ResultFragment extends Fragment {

    @LayoutRes
    int layout = R.layout.fragment_result_text;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private static final String ARG_QRCODE = "qrCodeString";

    protected String qrCodeString;

    protected TextResultFragment.OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrCodeString = getArguments().getString(ARG_QRCODE);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.share,menu);
        inflater.inflate(R.menu.copy,menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void putQRCode(String qrCode, QRResultType qrType) {
        Bundle args = new Bundle();
        args.putString(ARG_QRCODE, qrCode);
        setArguments(args);

        layout = qrType.getLayoutId();
    }

    public abstract void onProceedPressed(Context context, String content);
}
