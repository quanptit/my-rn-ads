package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.facebook.react.bridge.UiThreadUtil;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.SplashActivity;
import com.my.rn.Ads.full.center.AdsFullManager;

abstract class BaseFullStartAds {
    protected abstract void adsInitAndLoad(Activity activity, IAdLoaderCallback iAdLoaderCallback) throws Exception;

    protected abstract void adsShow() throws Exception;

    public abstract void destroy();
    public abstract String getKeyAds();

    protected abstract String getLogTAG();

    private boolean isSkipThisAds() {
        return TextUtils.isEmpty(getKeyAds());
    }

    public void showStartAds(Activity activity, final IAdLoaderCallback iAdLoaderCallback) {
        if (isSkipThisAds()) {
            if (iAdLoaderCallback != null)
                iAdLoaderCallback.onAdsFailedToLoad();
            return;
        }
        try {
            adsInitAndLoad(activity, iAdLoaderCallback);
            Log.d(getLogTAG(), "Show start: start load Ads");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region =========== ads  Event ==========
    protected void onAdLoaded() {
        Log.d(getLogTAG(), "onAdLoaded");
        if (SplashActivity.isRunning()) {
            try {
                adsShow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(getLogTAG(), "Time out: ==> Not show. time load = "
                    + (System.currentTimeMillis() - AdsFullManager.timeCallShowStart));
            destroy();
        }
    }

    protected void onAdFailedToLoad(String errorMsg, final IAdLoaderCallback iAdLoaderCallback) {
        Log.d(getLogTAG(), "onAdFailedToLoad: " + errorMsg);
        if (SplashActivity.isRunning()) {
            if (System.currentTimeMillis() - AdsFullManager.timeCallShowStart < 3000) {
                BaseApplication.getHandler().post(new Runnable() {
                    @Override public void run() {
                        if (iAdLoaderCallback != null)
                            iAdLoaderCallback.onAdsFailedToLoad();
                    }
                });
            } else
                SplashActivity.finishActivity();
        }
        destroy();
    }

    protected void onAdOpened() {
        Log.d(getLogTAG(), "onAdOpened");
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                SplashActivity.finishActivity();
            }
        });
        PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS, System.currentTimeMillis());
    }

    protected void onAdClosed() {
        destroy();
    }
    //endregion


}
