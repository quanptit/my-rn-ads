/*
 * Copyright (c) 2018 Smaato Inc. All rights reserved.
 */

package com.adapter.ax;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import com.baseLibs.BaseApplication;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

public class MobvistarMopubAdapterInterstitial extends CustomEventInterstitial implements InterstitialVideoListener {
    private static final String TAG = "MOBVISTAR";
    private MTGInterstitialVideoHandler interstitial;
    private CustomEventInterstitialListener customEventInterstitialListener;

    @Override
    protected void loadInterstitial(Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {
        if (!(context instanceof Activity)) {
            Log.e(TAG, "context Not is Activity");
            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.MISSING_AD_UNIT_ID);
                }
            });
            return;
        }

        if (!serverExtras.containsKey("appId") || !serverExtras.containsKey("appKey") || !serverExtras.containsKey("AdUnitId")) {
            Log.e(TAG, "serverExtras Missing data");
            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.MISSING_AD_UNIT_ID);
                }
            });
            return;
        }
        try {
            initSdk(context, serverExtras.get("appId"), serverExtras.get("appKey"));
            this.customEventInterstitialListener = customEventInterstitialListener;

            if (interstitial == null) {
                interstitial = new MTGInterstitialVideoHandler((Activity) context, serverExtras.get("AdUnitId"));
                interstitial.setInterstitialVideoListener(this);
            }
            interstitial.load();
        } catch (Exception e) {
            e.printStackTrace();
            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                }
            });
        }
    }

    //    String appId = "114572";
//    String appKey = "39d7c92521751d4df243cf8cca137ea6";
    private void initSdk(Context context, String appId, String appKey) {
        final MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        if (sdk.getStatus() == MIntegralSDK.PLUGIN_LOAD_STATUS.COMPLETED) {
            Log.d(TAG, "has initSdk:  STATUS.COMPLETED");
            return;
        }
        Log.d(TAG, "initSdk");

        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
        sdk.init(map, context);
    }

    //region event ads
    @Override
    public void onLoadSuccess(String unitId) {
        Log.d(TAG, "onLoadSuccess");
    }

    @Override
    public void onVideoLoadSuccess(String unitId) {
        Log.e(TAG, "onVideoLoadSuccess");
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (customEventInterstitialListener != null)
                    customEventInterstitialListener.onInterstitialLoaded();
            }
        });
    }

    @Override
    public void onVideoLoadFail(String errorMsg) {
        Log.e(TAG, "onVideoLoadFail errorMsg:" + errorMsg);
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (customEventInterstitialListener != null)
                    customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.NO_FILL);
            }
        });
    }

    @Override
    public void onShowFail(String errorMsg) {
        Log.e(TAG, "onShowFail=" + errorMsg);
    }

    @Override
    public void onAdShow() {
        Log.e(TAG, "onAdShow");
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                if (customEventInterstitialListener != null)
                    customEventInterstitialListener.onInterstitialShown();
            }
        });
    }

    @Override
    public void onAdClose(boolean isCompleteView) {
        Log.e(TAG, "onAdClose rewardinfo :" + "isCompleteView：" + isCompleteView);
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                if (customEventInterstitialListener != null)
                    customEventInterstitialListener.onInterstitialDismissed();
            }
        });
    }

    @Override
    public void onVideoAdClicked(String unitId) {
        Log.e(TAG, "onVideoAdClicked");
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                if (customEventInterstitialListener != null)
                    customEventInterstitialListener.onInterstitialClicked();
            }
        });
    }

    @Override
    public void onVideoComplete(String unitId) {
        Log.e(TAG, "onVideoComplete");
    }

    @Override
    public void onEndcardShow(String unitId) {
        Log.e(TAG, "onEndcardShow");
    }
    //endregion

    @Override
    protected void onInvalidate() {
        try {
            if (interstitial != null) {
                interstitial.clearVideoCache();
                interstitial = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void showInterstitial() {
        if (interstitial != null && interstitial.isReady()) {
            interstitial.show();
        }
    }
}