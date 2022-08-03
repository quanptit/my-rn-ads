package com.my.rn.ads.tapdaq.ad_native;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;

class TapdaqNativeRenderUtils {
    static View createAdView(Context context, TDMediatedNativeAd nativeAd, int typeAds, ViewGroup parent) {
        NativeAdLayout adView = new NativeAdLayout(context);
        adView.populate(nativeAd);
        parent.removeAllViews();
        parent.addView(adView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.requestLayout();

        return adView;
    }
}
