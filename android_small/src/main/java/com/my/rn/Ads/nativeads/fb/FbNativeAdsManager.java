package com.my.rn.Ads.nativeads.fb;

import com.baseLibs.BaseApplication;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;

public class FbNativeAdsManager extends BaseFbNativeAdsManager<NativeAd> {
    private String keyAds;

    public FbNativeAdsManager(int NO_ADS_LOAD, String keyAds, NativeAdBase.MediaCacheFlag mediaCacheFlag) {
        super(true, NO_ADS_LOAD, mediaCacheFlag);
        TAG = "FbNativeAdsManager";
        this.keyAds = keyAds;
    }

    @Override NativeAd createNativeAdsOj() {
//        if (isNATIVE_BANNER_LARGE)
//            return new NativeAd(MyApplication.getAppContext(), KeysAds.FB_NATIVE_BANNER_LARGE);
//        return new NativeAd(MyApplication.getAppContext(), KeysAds.FB_NATIVE);
        return new NativeAd(BaseApplication.getAppContext(), keyAds);
    }
}
