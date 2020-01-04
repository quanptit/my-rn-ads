package com.my.rn.ads;

import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.settings.AdsSetting;

public abstract class BaseApplicationContainAds extends BaseApplication {
    private AdsSetting adsSetting;

    public abstract BaseAdsFullManager getAdsFullManager();

    public abstract IAdInitUtils getIAdInitUtils();

    public abstract INativeManager getNativeManager();

    public static IAdInitUtils getIAdInitUtilsInstance() {
        return getInstance().getIAdInitUtils();
    }

    public static INativeManager getNativeManagerInstance() {
        return getInstance().getNativeManager();
    }

    public static AdsSetting getAdsSetting() {
        return getInstance()._GetAdsSetting();
    }

    private AdsSetting _GetAdsSetting() {
        if (adsSetting == null) adsSetting = new AdsSetting();
        return adsSetting;
    }

    public static BaseApplicationContainAds getInstance() {
        return (BaseApplicationContainAds) BaseApplication.getInstance();
    }
}
