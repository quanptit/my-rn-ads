package com.my.rn.ads;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.my.rn.ads.tapdaq.TapdaqRewardedUtils;

public class RewardedAdsManager implements IRewardedAdsManager {
    private TapdaqRewardedUtils rewardedUtils;

    public static IRewardedAdsManager getInstcane() {
        return ApplicationContainAds.getInstance().getRewardedAdsManager();
    }

    public TapdaqRewardedUtils getRewardedUtils() {
        if (rewardedUtils == null)
            rewardedUtils = new TapdaqRewardedUtils();
        return rewardedUtils;
    }

    @Override public void showRewardedAds(Activity activity, boolean showLoaddingIfNotCache, @Nullable IAdsCalbackOpen adsCalbackOpen) {
        getRewardedUtils().showRewardedAds(activity, false, adsCalbackOpen);
    }

    @Override public void cacheRewardedAds(Activity activity) {
        getRewardedUtils().cacheRewardedAds(activity);
    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return getRewardedUtils().isCachedRewardedAds(activity);
    }
}
