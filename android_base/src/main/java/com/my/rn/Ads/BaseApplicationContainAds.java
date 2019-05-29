package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.my.rn.Ads.full.center.BaseAdsFullManager;

public abstract class BaseApplicationContainAds extends BaseApplication {
    public abstract BaseAdsFullManager getAdsFullManager();


    public static BaseApplicationContainAds getInstance() {
        return (BaseApplicationContainAds) BaseApplication.getInstance();
    }
}
