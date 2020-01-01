package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.IAdsCalbackOpen;

public abstract class BaseFullCenterAds {
    private boolean isCaching = false;
    private IAdsCalbackOpen promise;

    protected abstract String getLogTAG();

    public abstract String getKeyAds(boolean isFromStart);

    protected abstract boolean isCachedCenter(Activity activity);

    protected abstract void showAds(Activity activity);

    protected abstract void adsInitAndLoad(Activity activity, String keyAds, IAdLoaderCallback iAdLoaderCallback) throws Exception;

    protected abstract void destroyAds();

    public void loadCenterAds(Activity activity, boolean isFromStart, final IAdLoaderCallback iAdLoaderCallback) {
        String keyAds = getKeyAds(isFromStart);
        if (TextUtils.isEmpty(keyAds)) {
            if (iAdLoaderCallback != null)
                iAdLoaderCallback.onAdsFailedToLoad();
            return;
        }
        if (isCachedCenter(activity) || isCaching) return;

        destroy();
        Log.d(getLogTAG(), "loadCenterAds");
        isCaching = true;
        try {
            adsInitAndLoad(activity, keyAds, iAdLoaderCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean showAdsCenterIfCache(Activity activity, IAdsCalbackOpen promise) {
        if (isCachedCenter(activity)) {
            this.promise = promise;
            showAds(activity);
            return true;
        }
        return false;
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
        if (this.promise != null) {
            this.promise.onAdOpened();
            this.promise = null;
        }
    }

    protected void onAdClosed() {
        Log.d(getLogTAG(), "onAdClosed");
        destroy();
    }
    //endregion
}
