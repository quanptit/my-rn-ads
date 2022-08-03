package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.center.BaseAdsFullManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    protected AdsFullManager adsFullManager;

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
