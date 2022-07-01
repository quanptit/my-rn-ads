package com.my.rn.ads;

import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.mopub.ad_native.MopubNativeManager;

public abstract class ApplicationContainAds extends BaseApplicationContainAds {
    protected AdsFullManager adsFullManager;
    protected INativeManager nativeManager;
    protected MopubInitUtils mopubInitUtils;
    protected RewardedAdsManager rewardedAdsManager;

    @Override public IRewardedAdsManager getRewardedAdsManager() {
        if (rewardedAdsManager == null)
            rewardedAdsManager = new RewardedAdsManager();
        return rewardedAdsManager;
    }

    @Override public IAdInitUtils getIAdInitMopubUtils() {
        if (mopubInitUtils == null)
            mopubInitUtils = new MopubInitUtils();
        return mopubInitUtils;
    }

    @Override public IAdInitUtils getIAdInitTapdaqUtils() {
        return null;
    }

    @Override public INativeManager getNativeManager() {
        if (nativeManager == null)
            nativeManager = new MopubNativeManager();
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
