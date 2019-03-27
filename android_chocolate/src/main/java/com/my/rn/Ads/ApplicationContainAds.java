package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.my.rn.Ads.chocolate.ChocolateFullManager;
import com.my.rn.Ads.chocolate.ChocolateInitUtils;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.center.BaseAdsFullManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    public ChocolateFullManager chocoFullManager;
    protected AdsFullManager adsFullManager;
    public ChocolateInitUtils chocolateInitUtils;

    @Override public BaseAdsFullManager getAdsFullManager() {
        if (adsFullManager == null)
            adsFullManager = new AdsFullManager();
        return adsFullManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static ApplicationContainAds getInstance() {
        return (ApplicationContainAds) BaseApplication.getInstance();
    }
}
