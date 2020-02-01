package com.my.rn.ads;

import android.app.Activity;
import android.util.Log;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.listeners.TMAdListener;

public class RewardedAdsManager extends BaseRewardedAdsManager {
    private static final String TAG = "REWARDED_ADS_MANAGER";

    @Override public void cacheRewardedAds(Activity activity) {
        Log.d(TAG, "cacheRewardedAds");
        Tapdaq.getInstance().loadRewardedVideo(activity, new TMAdListener());
    }

    @Override public void showRewardedAds(Activity activity) {
        Log.d(TAG, "showRewardedAds");
        boolean isReady = isCachedRewardedAds(activity);
        if (isReady)
            Tapdaq.getInstance().showRewardedVideo(activity, new TMAdListener());
    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return Tapdaq.getInstance().isRewardedVideoReady(activity, "default");
    }
}
