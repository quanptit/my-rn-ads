package com.my.rn.ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.tapdaq.AdInitTapdaqUtils;
import com.my.rn.ads.tapdaq.ad_native.TabpadNativeManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    protected IAdInitUtils adInitTapdaqUtils;
    protected AdsFullManager adsFullManager;
    protected INativeManager nativeManager;

    @Override public BaseAdsFullManager getAdsFullManager() {
        if (adsFullManager == null)
            adsFullManager = new AdsFullManager();
        return adsFullManager;
    }

    @Override public IAdInitUtils getIAdInitTapdaqUtils() {
        if (adInitTapdaqUtils == null)
            adInitTapdaqUtils = new AdInitTapdaqUtils();
        return adInitTapdaqUtils;
    }

    @Override public IAdInitUtils getIAdInitMopubUtils() {
        return null;
    }

    @Override public INativeManager getNativeManager() {
        if (nativeManager == null)
            nativeManager = new TabpadNativeManager();
        return nativeManager;
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
