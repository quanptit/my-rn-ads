package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;

public abstract class BaseApplicationContainAds extends BaseApplication {
    protected BaseShowStartAdsManager showStartAdsManager;

    public void destroyBaseShowStartAdsManager() {
        if (showStartAdsManager == null) return;
        try {
            showStartAdsManager.destroy();
            showStartAdsManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract BaseAdsFullManager getAdsFullManager();

    public static BaseApplicationContainAds getInstance() {
        return (BaseApplicationContainAds) BaseApplication.getInstance();
    }
}
