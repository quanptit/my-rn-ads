package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.facebook.react.bridge.Promise;
import com.my.rn.Ads.IAdLoaderCallback;

abstract class BaseFullCenterAds {
    private boolean isCaching = false;
    private PromiseSaveObj promise;

    protected abstract String getLogTAG();

    public abstract String getKeyAds();
    private boolean isSkipThisAds() {
        return TextUtils.isEmpty(getKeyAds());
    }

    protected abstract boolean isCachedCenter();

    protected abstract void showAds();

    protected abstract void adsInitAndLoad(Activity activity, IAdLoaderCallback iAdLoaderCallback) throws Exception;

    protected abstract void destroyAds();

    public void loadCenterAds(Activity activity, final IAdLoaderCallback iAdLoaderCallback) {
        if (isSkipThisAds()) {
            if (iAdLoaderCallback != null)
                iAdLoaderCallback.onAdsFailedToLoad();
            return;
        }
        if (isCachedCenter() || isCaching) return;

        destroy();
        Log.d(getLogTAG(), "loadCenterAds");
        isCaching = true;
        try {
            adsInitAndLoad(activity, iAdLoaderCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean showAdsCenterIfCache(PromiseSaveObj promise) {
        if (isCachedCenter()) {
            this.promise = promise;
            showAds();
            return true;
        }
        return false;
    }

    private void reslovePromise(boolean result) {
        if (this.promise != null) {
            this.promise.resolve(result);
            this.promise = null;
        }
    }

    public void destroy() {
        this.promise = null;
        this.isCaching = false;
        destroyAds();
    }

    //region =========== ads  Event ==========
    protected void onAdLoaded(final IAdLoaderCallback iAdLoaderCallback) {
        Log.d(getLogTAG(), "onAdLoaded");
        isCaching = false;
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsLoaded();
            }
        });
    }

    protected void onAdFailedToLoad(String errorMsg, final IAdLoaderCallback iAdLoaderCallback) {
        Log.d(getLogTAG(), "onAdFailedToLoad: " + errorMsg);
        isCaching = false;
        destroy();
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
            }
        });
    }

    protected void onAdOpened() {
        Log.d(getLogTAG(), "onAdOpened");
        PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS, System.currentTimeMillis());
        reslovePromise(true);
    }

    protected void onAdClosed() {
        Log.d(getLogTAG(), "onAdClosed");
        destroy();
    }
    //endregion
}
