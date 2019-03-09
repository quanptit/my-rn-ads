package com.my.rn.Ads;

import com.baseLibs.BaseApplication;
import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.mopub.MopubInitUtils;
import com.my.rn.Ads.mopub.MopubNativeManager;

public class ApplicationContainAds extends BaseApplication {
    protected AdsFullManager adsFullManager;
    protected MopubInitUtils mopubInitUtils;
    protected MopubNativeManager mopubNativeManager;

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
