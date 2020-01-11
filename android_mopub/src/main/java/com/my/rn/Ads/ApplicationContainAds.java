package com.my.rn.ads;

import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.mopub.ad_native.MopubNativeManager;

public class ApplicationContainAds extends BaseApplicationContainAds {
    protected MopubInitUtils mopubInitUtils;
    protected AdsFullManager adsFullManager;
    protected INativeManager nativeManager;

    @Override public IAdInitUtils getIAdInitTapdaqUtils() {
        return null;
    }

    @Override public IAdInitUtils getIAdInitMopubUtils() {
        if (mopubInitUtils == null)
            mopubInitUtils = new MopubInitUtils();
        return mopubInitUtils;
    }

    @Override public INativeManager getNativeManager() {
        if (nativeManager == null)
            nativeManager = new MopubNativeManager(this);
        return nativeManager;
    }
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
