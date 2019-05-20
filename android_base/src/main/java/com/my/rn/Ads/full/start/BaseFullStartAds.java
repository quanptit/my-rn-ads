package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;
import com.facebook.react.bridge.UiThreadUtil;
import com.my.rn.Ads.BaseApplicationContainAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.full.center.PromiseSaveObj;

abstract class BaseFullStartAds {
    private PromiseSaveObj promise;

    protected abstract void adsInitAndLoad(Activity activity, IAdLoaderCallback iAdLoaderCallback) throws Exception;

    protected abstract void showAds() throws Exception;

    protected abstract boolean isCached();

    protected abstract void destroyAds();

    public abstract String getKeyAds();

    protected abstract String getLogTAG();

    private boolean isSkipThisAds() {
        return TextUtils.isEmpty(getKeyAds());
    }

    public void loadStartAds(Activity activity, final IAdLoaderCallback iAdLoaderCallback) {
        if (isSkipThisAds()) {
            if (iAdLoaderCallback != null)
                iAdLoaderCallback.onAdsFailedToLoad();
            return;
        }
        try {
            adsInitAndLoad(activity, iAdLoaderCallback);
            Log.d(getLogTAG(), "loadStartAds: start load Ads");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean showAdsIfCache(PromiseSaveObj promise) {
        if (isCached()) {
            try {
                this.promise = promise;
                showAds();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    //region =========== ads  Event ==========
    protected void onAdLoaded(final IAdLoaderCallback iAdLoaderCallback) {
        Log.d(getLogTAG(), "onAdLoaded");
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                try {
                    if (iAdLoaderCallback != null)
                        iAdLoaderCallback.onAdsLoaded();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onAdFailedToLoad(String errorMsg, final IAdLoaderCallback iAdLoaderCallback) {
        Log.d(getLogTAG(), "onAdFailedToLoad: " + errorMsg);
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                try {
                    if (iAdLoaderCallback != null)
                        iAdLoaderCallback.onAdsFailedToLoad();
                    destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onAdOpened() {
        Log.d(getLogTAG(), "onAdOpened");
        PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS, System.currentTimeMillis());
        if (this.promise != null) {
            this.promise.resolve(true);
            this.promise = null;
        }
    }

    protected void onAdClosed() {
        Log.d(getLogTAG(), "onAdClosed");
        BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
    }
    //endregion

    public void destroy() {
        this.promise = null;
        destroyAds();
    }

}
