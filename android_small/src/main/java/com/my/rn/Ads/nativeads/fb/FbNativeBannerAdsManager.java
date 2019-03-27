package com.my.rn.Ads.nativeads.fb;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeBannerAd;

public class FbNativeBannerAdsManager extends BaseFbNativeAdsManager<NativeBannerAd> {
    public FbNativeBannerAdsManager(NativeAdBase.MediaCacheFlag mediaCacheFlag) {
        super(false, 2, mediaCacheFlag);
        TAG = "FbNativeBannerAdsManager";
    }

    @Override NativeBannerAd createNativeAdsOj() {
        return new NativeBannerAd(BaseApplication.getAppContext(), KeysAds.FB_NATIVE_BANNER);
    }
}
