package com.adapter.ax;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mopub.common.LifecycleListener;
import com.mopub.mobileads.AdData;
import com.mopub.mobileads.BaseAd;
import com.mopub.mobileads.MoPubErrorCode;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.tapdaq.AdInitTapdaqUtils;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

public class TapdaqMopubAdapterInterstitial extends BaseAd {
    private static final String TAG = "TAPDAQ_MOPUB";


    @Nullable @Override protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @NonNull @Override protected String getAdNetworkId() {
        return "";
    }

    @Override protected void load(@NonNull Context context, @NonNull AdData adData) throws Exception {
        Log.d(TAG, "loadInterstitial");
        if (context instanceof Activity)
            activity = (Activity) context;
        else
            activity = BaseAdsFullManager.getMainActivity();


        if (activity == null) {
            Log.e(TAG, "loadInterstitial activity==null, Please CHECK AGAIN");
            mLoadListener.onAdLoadFailed(MoPubErrorCode.INTERNAL_ERROR);
            return;
        }
        if (Tapdaq.getInstance().isVideoReady(activity, null)) {
            mLoadListener.onAdLoaded();
            return;
        }

        new Thread(new Runnable() {
            @Override public void run() {
                AdInitTapdaqUtils.getInstance()
                        .initAds(activity, new IAdInitCallback() {
                            @Override public void didInitialise() {
                                Tapdaq.getInstance().loadVideo(activity, new TMAdListener() {
                                    @Override public void didLoad() {
                                        mLoadListener.onAdLoaded();
                                    }

                                    @Override public void didFailToLoad(TMAdError error) {
                                        Log.d(TAG, "loadInterstitial didFailToLoad: " + error.toString());
                                        mLoadListener.onAdLoadFailed(MoPubErrorCode.FULLSCREEN_LOAD_ERROR);
                                    }
                                });
                            }

                            @Override public void didFailToInitialise() {
                                Log.w(TAG, "loadInterstitial ==> didFailToInitialise");
                                mLoadListener.onAdLoadFailed(MoPubErrorCode.INTERNAL_ERROR);
                            }
                        });
            }
        }).start();
    }

    //#region utils
    @Override protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull AdData adData) throws Exception {
        return false;
    }
    //#endregion


    private Activity activity;


    @Override
    protected void onInvalidate() {
        Log.d(TAG, "onInvalidate");
        this.activity = null;
    }

    @Override
    protected void show() {
        Log.d(TAG, "showInterstitial");
        if (this.activity == null) {
            Log.e(TAG, "showInterstitial FAIL BECAUSE activity == null");
            if (mInteractionListener != null)
                mInteractionListener.onAdFailed(MoPubErrorCode.INTERNAL_ERROR);
            return;
        }
        if (!Tapdaq.getInstance().isVideoReady(activity, null)) {
            if (mInteractionListener != null)
                mInteractionListener.onAdFailed(MoPubErrorCode.NETWORK_NO_FILL);
        }
        Tapdaq.getInstance().showVideo(activity, new TMAdListener() {
            @Override public void didClose() {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdDismissed();
                }
            }

            @Override public void didDisplay() {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdShown();
                    mInteractionListener.onAdImpression();
                }
            }

            @Override public void didClick() {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdClicked();
                }
            }
        });
    }
}