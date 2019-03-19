package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.Ads.full.center.AdsFullManager;

public class ApplicationContainAds extends BaseApplication {
    protected AdsFullManager adsFullManager;

    public static AdsFullManager getAdsFullManager() {
        if (getInstance().adsFullManager == null)
            getInstance().adsFullManager = new AdsFullManager();
        return getInstance().adsFullManager;
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
