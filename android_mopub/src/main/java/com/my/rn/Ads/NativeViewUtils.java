package com.my.rn.ads;

import android.content.Context;

import com.my.rn.ads.fb.FbNativeView;
import com.my.rn.ads.mopub.ad_native.MopubNativeView;
import com.my.rn.ads.settings.AdsSetting;

/**
 * Mỗi NativeViewUtils sẽ sử dụng cho 1 View Native
 * */
public class NativeViewUtils extends BaseNativeViewUtils {

    public NativeViewUtils(Context context, IAdLoaderCallback loaderCallback) {
        super(context, loaderCallback);
    }


    @Override protected BaseNativeView getBaseNativeView(int indexOrder, int typeAds, IAdLoaderCallback loaderCallback) {
        if (AdsSetting.isNativeBanner(typeAds)) {
            if (indexOrder == 0)
                return new FbNativeView(context, typeAds, loaderCallback);
            else if (indexOrder == 1)
                return new AdmobNativeView(context, typeAds, loaderCallback);
            return null;
        } else {
            switch (indexOrder) {
                case 0:
                    return new MopubNativeView(context, typeAds, loaderCallback);
                case 1:
                    return new FbNativeView(context, typeAds, loaderCallback);
                case 2:
                    return new AdmobNativeView(context, typeAds, loaderCallback);
                default:
                    return null;
            }
        }
    }
}
