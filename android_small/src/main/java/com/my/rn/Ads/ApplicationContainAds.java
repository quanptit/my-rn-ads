package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.nativeads.NativeAdsManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    protected AdsFullManager adsFullManager;
    private NativeAdsManager nativeAdsManager;

    public NativeAdsManager getNativeAdsManager() {
        if (nativeAdsManager==null)
            nativeAdsManager = new NativeAdsManager();
        return nativeAdsManager;
    }

    @Override public BaseAdsFullManager getAdsFullManager() {
        if (adsFullManager == null)
            adsFullManager = new AdsFullManager();
        return adsFullManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
    }

    public static ApplicationContainAds getInstance() {
        return (ApplicationContainAds) BaseApplication.getInstance();
    }
}
