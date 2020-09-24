package com.my.rn.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.my.rn.ads.tapdaq.AdInitTapdaqUtils;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

public class RewardedAdsManager extends BaseRewardedAdsManager implements IRewardedAdsManager{
    private static final String TAG = "REWARDED_ADS_MANAGER";
    private boolean isLoading;

    @Override protected void showRewardedAdsIfCache(Activity activity) {
        Tapdaq.getInstance().showRewardedVideo(activity, new TMAdListener());
    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return Tapdaq.getInstance().isRewardedVideoReady(activity, "default");
    }

    @Override protected void _cacheRewardedAds(final Activity activity) {
        if (isLoading) return;
        isLoading = true;
        Log.d(TAG, "cacheRewardedAds");
        AdInitTapdaqUtils.getInstance().initAds(activity, new IAdInitCallback() {
            @Override public void didInitialise() {
                Tapdaq.getInstance().loadRewardedVideo(activity, new TMAdListener() {
                    @Override public void didFailToLoad(TMAdError error) {
                        isLoading = false;
                    }

                    @Override public void didLoad(TDMediatedNativeAd ad) {
                        isLoading = false;
                    }

                    @Override public void didLoad() {
                        isLoading = false;
                    }
                });
            }

            @Override public void didFailToInitialise() {
                isLoading = false;
            }
        });
    }
}
