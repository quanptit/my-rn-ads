package com.my.rn.ads;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.DeviceTestID;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.mopub.ad_native.MopubNativeManager;

import java.util.Arrays;
import java.util.List;

public abstract class ApplicationContainAds extends BaseApplicationContainAds {
    protected AdsFullManager adsFullManager;
    protected MopubInitUtils mopubInitUtils;
    protected RewardedAdsManager rewardedAdsManager;

    @Override public BaseNativeViewUtils createNativeViewUtilsInstance(IAdLoaderCallback loaderCallback) {
        return new NativeViewUtils(getAppContext(), loaderCallback);
    }

    @Override public BaseAppOpenAdsManager getAppOpenAdsManager() {
//        if (admobAppOpenManager==null)
//            admobAppOpenManager = new AdmobAppOpenManager();
//        return admobAppOpenManager;
        return null;
    }

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

    @Override public BaseAdsFullManager getAdsFullManager() {
        if (adsFullManager == null)
            adsFullManager = new AdsFullManager();
        return adsFullManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        List<String> testDeviceIds = Arrays.asList(KeysAds.DEVICE_TESTS);
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        AdSettings.addTestDevices(Arrays.asList(DeviceTestID.FB_TEST));
    }

    public static ApplicationContainAds getInstance() {
        return (ApplicationContainAds) BaseApplication.getInstance();
    }
}
