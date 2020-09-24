package com.my.rn.ads.tapdaq;

import android.app.Activity;
import android.util.Log;

import com.my.rn.ads.BaseRewardedAdsManager;
import com.my.rn.ads.IAdInitCallback;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

public class TapdaqRewardedUtils extends BaseRewardedAdsManager {
    private static final String TAG = "REWARDED_ADS_TAPDAQ";
    private boolean isLoading;

    @Override protected void _cacheRewardedAds(final Activity activity) {
        if (isLoading) return;
        isLoading = true;
        Log.d(TAG, "cacheRewardedAds");
        AdInitTapdaqUtils.getInstance().initAds(activity, new IAdInitCallback() {
            @Override public void didInitialise() {
                Tapdaq.getInstance().loadRewardedVideo(activity, new TMAdListener() {
                    @Override public void didFailToLoad(TMAdError error) {
                        Log.d(TAG, "didFailToLoad: "+error);
                        isLoading = false;
                    }

                    @Override public void didLoad(TDMediatedNativeAd ad) {
                        isLoading = false;
                        Log.d(TAG, "didLoad TDMediatedNativeAd");
                    }

                    @Override public void didLoad() {
                        isLoading = false;
                        Log.d(TAG, "didLoad");
                    }
                });
            }

            @Override public void didFailToInitialise() {
                isLoading = false;
            }
        });
    }

    @Override protected void showRewardedAdsIfCache(Activity activity) {
        Log.d(TAG, "showRewardedAds");
        Tapdaq.getInstance().showRewardedVideo(activity, new TMAdListener());
    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return Tapdaq.getInstance().isRewardedVideoReady(activity, null);
    }
}
