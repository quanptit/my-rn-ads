package com.my.rn.ads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class BaseRewardedAdsManager {
    static final String TAG = "REWARDED_ADS_MANAGER";
    private IAdsCalbackOpen adsCalbackOpen;
    private boolean isLoading, needShowAds;
    private ProgressDialog dialog;
    private WeakReference<Activity> activityRef;

    public void showRewardedAds(Activity activity, boolean showLoaddingIfNotCache, @Nullable IAdsCalbackOpen adsCalbackOpen) {
        Log.d("RewardedAdsManager", "Call showRewardedAds");
        activityRef = new WeakReference<>(activity);
        this.adsCalbackOpen = adsCalbackOpen;
        boolean isReady = isCachedRewardedAds(activity);
        removeDialog();
        if (isReady) {
            showRewardedAdsIfCache(activity);
            return;
        }

        needShowAds = true;
        cacheRewardedAds(activity);
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading..");
        dialog.setCancelable(true);
        dialog.show();
    }

    private void removeDialog() {
        needShowAds = false;
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void cacheRewardedAds(Activity activity) {
        if (isLoading || isCachedRewardedAds(activity)) return;
        isLoading = true;
        Log.d(TAG, "cacheRewardedAds =================");
        _cacheRewardedAds(activity);
    }

    //region callback
    public void onRewardedVideoLoadSuccess() {
        Log.d(TAG, "onRewardedVideoLoadSuccess");
        isLoading = false;
        if (needShowAds) {
            removeDialog();
            if (activityRef != null && activityRef.get() != null)
                showRewardedAdsIfCache(activityRef.get());
        }

    }

    public void onRewardedVideoLoadFailure() {
        Log.d(TAG, "onRewardedVideoLoadFailure");
        isLoading = false;
        if (adsCalbackOpen != null && needShowAds) {
            adsCalbackOpen.noAdsCallback();
            adsCalbackOpen = null;
        }
        removeDialog();
    }

    public void onRewardedVideoCompleted() {
        Log.d(TAG, "onRewardedVideoCompleted");
        if (adsCalbackOpen != null) {
            adsCalbackOpen.onAdOpened();
            adsCalbackOpen = null;
        }
        removeDialog();
    }

    public void onRewardedVideoStarted() {}

    public void onRewardedVideoPlaybackError() {}

    public void onRewardedVideoClosed() {}

    //endregion

    // region abstract
    protected abstract void showRewardedAdsIfCache(Activity activity);

    public abstract boolean isCachedRewardedAds(Activity activity);

    protected abstract void _cacheRewardedAds(Activity activity);
    //endregion
}
