package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.tapdaq.AdInitUtils;
import com.my.rn.ads.tapdaq.ad_native.TabpadNativeManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    //    protected MopubNativeManager mopubNativeManager;
    protected AdInitUtils adInitUtils;
    protected AdsFullManager adsFullManager;
    protected INativeManager nativeManager;

//    public static MopubNativeManager getMopubNativeManager() {
//        if (getInstance().mopubNativeManager == null)
//            getInstance().mopubNativeManager = new MopubNativeManager(getAppContext());
//        return getInstance().mopubNativeManager;
//    }

    @Override public BaseAdsFullManager getAdsFullManager() {
        if (adsFullManager == null)
            adsFullManager = new AdsFullManager();
        return adsFullManager;
    }

    @Override public IAdInitUtils getIAdInitUtils() {
        if (adInitUtils == null)
            adInitUtils = new AdInitUtils();
        return adInitUtils;
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
//        AudienceNetworkAds.initialize(this);
//        AdInternalSettings.addTestDevice("192e0d1d-2f06-4eaf-89f1-b8b27cfdc69b");


    }

    public static ApplicationContainAds getInstance() {
        return (ApplicationContainAds) BaseApplication.getInstance();
    }
}
