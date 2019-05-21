package com.my.rn.Ads;

import android.support.annotation.Nullable;
import com.baseLibs.BaseApplication;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;

public abstract class BaseApplicationContainAds extends BaseApplication {
    protected BaseShowStartAdsManager showStartAdsManager;
    private boolean isDestroyShowStart = false;

    public @Nullable BaseShowStartAdsManager getShowStartAdsManager(boolean needInstance) {
        if (needInstance) isDestroyShowStart = false;
        if (isDestroyShowStart) return showStartAdsManager;
        if (showStartAdsManager == null) {
            showStartAdsManager = createShowStartAdsManagerInstance();
        }
        return showStartAdsManager;
    }

    public void destroyBaseShowStartAdsManager() {
        if (showStartAdsManager == null) return;
        isDestroyShowStart = true;
        try {
            showStartAdsManager.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showStartAdsManager = null;
    }

    public abstract BaseAdsFullManager getAdsFullManager();

    public abstract BaseShowStartAdsManager createShowStartAdsManagerInstance();

    public static BaseApplicationContainAds getInstance() {
        return (BaseApplicationContainAds) BaseApplication.getInstance();
    }
}
