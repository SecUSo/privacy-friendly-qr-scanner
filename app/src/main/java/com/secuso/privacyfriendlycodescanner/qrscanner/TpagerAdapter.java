package com.secuso.privacyFriendlyCodeScanner.qrscanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bassel on 11/20/2017.
 */

public class TpagerAdapter extends PagerAdapter {

    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;

    public TpagerAdapter(int [] layouts, Context context){

        this.layouts= layouts;
        this.context=context;
        layoutInflater=  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view= layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view= (View) object;
        container.removeView(view);
    }
}
