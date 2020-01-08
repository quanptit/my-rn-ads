package com.my.rn.ads.tapdaq.ad_native;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.INativeManager;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdOptions;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;


import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class TabpadNativeManager extends TMAdListener implements INativeManager {
    private boolean isLoading = false;
    private boolean isSkipWaitForComplete = false;
    private Queue<TDMediatedNativeAd> nativeAds = new LinkedList<>(); // Sẽ chứa NO_ADS_LOAD Ads

    @Override
    public void cacheNativeAndWaitForComplete(final Activity activity) throws Exception {
        if (!nativeAds.isEmpty()) return;
        isSkipWaitForComplete = false;
        Log.d(TAG, "cacheNativeAndWaitForComplete");
        long startTimeLoad = System.currentTimeMillis();
        _checkAndLoadAds(activity, false);
        while (true) {
            if (isSkipWaitForComplete) return;
            Thread.sleep(100);
            if (!nativeAds.isEmpty()) return;
            if (System.currentTimeMillis() - startTimeLoad > 3000) {
                Log.d(TAG, "cacheAndWaitForComplete Fail Time out");
                return;
            }
        }
    }

    @Override
    public boolean canShowNativeAds(int typeAds) {
        return isCached();
    }

    @Override
    public @Nullable View createNewAds(Context context, int typeAds, ViewGroup parent) {
        TDMediatedNativeAd nativeAd = nativeAds.poll();
        if (nativeAd == null) return null;
        return TapdaqNativeRenderUtils.createAdView(context, nativeAd, typeAds, parent);
    }

    @Override
    public void checkAndLoadAds(Activity activity) {
        _checkAndLoadAds(activity, false);
    }

    private void _checkAndLoadAds(final Activity activity, boolean isNotResetCountError) {
//        if (true) return; //TODOs

        if (isLoading || nativeAds.size() >= NO_ADS_LOAD) return;
        if (!isNotResetCountError)
            countLoadError = 0;
        BaseApplicationContainAds.getIAdInitUtilsInstance().initAds(activity, new IAdInitCallback() {
            @Override public void didInitialise() {
                try {
                    TDMediatedNativeAdOptions options = new TDMediatedNativeAdOptions(); //optional param
                    options.setAllowMultipleImages(false);
                    options.setStartVideoMuted(true);
                    Tapdaq.getInstance().loadMediatedNativeAd(activity, "default", options, TabpadNativeManager.this);
                    isLoading = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }

            @Override public void didFailToInitialise() {
                isLoading = false;
                isSkipWaitForComplete = true;
            }
        });
    }

    // region Event callback load Ads
    @Override public void didLoad(TDMediatedNativeAd ad) {
        Log.d("MopubNativeManager", "onNativeLoad");
        isLoading = false;
        nativeAds.add(ad);
        Activity activity = BaseAdsFullManager.getMainActivity();
        if (activity != null)
            _checkAndLoadAds(activity, false);
    }

    @Override public void didFailToLoad(TMAdError error) {
        String str = String.format(Locale.ENGLISH, "didFailToLoad: %d - %s", error.getErrorCode(), error.getErrorMessage());
        for (String key : error.getSubErrors().keySet()) {
            try {
                for (TMAdError value : error.getSubErrors().get(key)) {
                    String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key, value.getErrorCode(), value.getErrorMessage());
                    str = str.concat("\n ");
                    str = str.concat(subError);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Load native ads => onNativeFail: " + str);
        isSkipWaitForComplete = true;
        countLoadError++;
        isLoading = false;
        if (countLoadError < 3) {
            Activity activity = BaseAdsFullManager.getMainActivity();
            if (activity != null)
                _checkAndLoadAds(activity, true);
        }
    }
    //endregion

    //region utils

    public static INativeManager getInstance() {
        return BaseApplicationContainAds.getInstance().getNativeManager();
    }

    private static final int NO_ADS_LOAD = 2;
    private int countLoadError = 0;
    private static final String TAG = "TAPDAQ_NATIVE";

    @Override
    public boolean isCached() {
        try {
            return !nativeAds.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion
}