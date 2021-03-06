package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.internal.settings.AdInternalSettings;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.mopub.MopubInitUtils;
import com.my.rn.Ads.mopub.MopubNativeManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    protected MopubInitUtils mopubInitUtils;
    protected MopubNativeManager mopubNativeManager;
    protected AdsFullManager adsFullManager;

    public static MopubInitUtils getMopubInitUtils() {
        if (getInstance().mopubInitUtils == null)
            getInstance().mopubInitUtils = new MopubInitUtils();
        return getInstance().mopubInitUtils;
    }

    public static MopubNativeManager getMopubNativeManager() {
        if (getInstance().mopubNativeManager == null)
            getInstance().mopubNativeManager = new MopubNativeManager(getAppContext());
        return getInstance().mopubNativeManager;
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
        AdInternalSettings.addTestDevice("192e0d1d-2f06-4eaf-89f1-b8b27cfdc69b");


    }

    public static ApplicationContainAds getInstance() {
        return (ApplicationContainAds) BaseApplication.getInstance();
    }
}
