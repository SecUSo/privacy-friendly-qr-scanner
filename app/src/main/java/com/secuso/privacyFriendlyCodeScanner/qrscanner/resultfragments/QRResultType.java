package com.secuso.privacyfriendlycodescanner.qrscanner.resultfragments;

import android.support.annotation.LayoutRes;

import com.secuso.privacyfriendlycodescanner.qrscanner.R;

public enum QRResultType {
    TEXT(R.layout.fragment_result_text),
    EMAIL(R.layout.fragment_result_email),
    PHONE(R.layout.fragment_result_tel),
    SEND_EMAIL(R.layout.fragment_result_send_email),
    URL(R.layout.fragment_result_url),
    SMS(R.layout.fragment_result_sms),
    MMS(R.layout.fragment_result_mms),
    CONTACT(R.layout.fragment_result_contact),
    ME_CARD(R.layout.fragment_result_me_card),
    BIZ_CARD(R.layout.fragment_result_biz_card),
    WIFI(R.layout.fragment_result_wifi),
    GEO(R.layout.fragment_result_geo_info),
    PRODUCT(R.layout.fragment_result_product);

    private final @LayoutRes int layoutId;
    //private final Class<? extends ResultFragment> fragmentClazz;

    QRResultType(@LayoutRes int layoutId /*, Class<? extends ResultFragment> fragmentClazz */) {
        this.layoutId = layoutId;
        //this.fragmentClazz = fragmentClazz;
    }

    public int getLayoutId() {
        return layoutId;
    }

//    public @NonNull ResultFragment createFragmentInstance() {
//        try {
//            Constructor ct = fragmentClazz.getConstructor();
//            return (ResultFragment) ct.newInstance();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        return new TextResultFragment();
//    }
}
