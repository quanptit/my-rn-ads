package com.adapter.ax;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.tapdaq.AdInitTapdaqUtils;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

import java.util.Map;

public class TapdaqMopubAdapterInterstitial extends CustomEventInterstitial {
    private static final String TAG = "TAPDAQ_MOPUB";
    private Activity activity;
    private CustomEventInterstitialListener customEventInterstitialListener;

    @Override
    protected void loadInterstitial(Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {
        Log.d(TAG, "loadInterstitial");
        if (context instanceof Activity)
            activity = (Activity) context;
        else
            activity = BaseAdsFullManager.getMainActivity();
        if (activity == null) {
            Log.e(TAG, "loadInterstitial activity==null, Please CHECK AGAIN");
            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
            return;
        }
        this.customEventInterstitialListener = customEventInterstitialListener;

        if (Tapdaq.getInstance().isVideoReady(activity, null)) {
            customEventInterstitialListener.onInterstitialLoaded();
            return;
        }

        new Thread(new Runnable() {
            @Override public void run() {
                AdInitTapdaqUtils.getInstance()
                        .initAds(activity, new IAdInitCallback() {
                            @Override public void didInitialise() {
                                Tapdaq.getInstance().loadVideo(activity, new TMAdListener() {
                                    @Override public void didLoad() {
                                        customEventInterstitialListener.onInterstitialLoaded();
                                    }

                                    @Override public void didFailToLoad(TMAdError error) {
                                        Log.d(TAG, "loadInterstitial didFailToLoad: " + error.toString());
                                        customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                                    }
                                });
                            }

                            @Override public void didFailToInitialise() {
                                Log.w(TAG, "loadInterstitial ==> didFailToInitialise");
                                customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
                            }
                        });
            }
        }).start();
    }

    @Override
    protected void onInvalidate() {
        Log.d(TAG, "onInvalidate");
        this.activity = null;
        this.customEventInterstitialListener = null;
    }

    @Override
    protected void showInterstitial() {
        Log.d(TAG, "showInterstitial");
        if (this.activity == null){
            Log.e(TAG, "showInterstitial FAIL BECAUSE activity == null");
            return;
        }
        Tapdaq.getInstance().showVideo(activity, new TMAdListener(){
            @Override public void didClose() {
                customEventInterstitialListener.onInterstitialDismissed();
            }

            @Override public void didDisplay() {
                customEventInterstitialListener.onInterstitialShown();
            }

            @Override public void didClick() {
                customEventInterstitialListener.onInterstitialClicked();
            }
        });
    }
}