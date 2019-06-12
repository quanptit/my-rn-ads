package com.adapter.ax;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.ads.*;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.FacebookInterstitial;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.*;

/**
 * Setting Trong mopub như sau:
 * CUSTOM EVENT CLASS: com.adapter.ax.FBStartMopubAdapterInterstitial
 * CUSTOM EVENT CLASS DATA: {"AdUnitId": "This is AD_UNIT_ID for full screen"};  ex test: {"AdUnitId": "/21617015150/47909961/21720491886"}
 */
public class FBStartMopubAdapterInterstitial extends CustomEventInterstitial implements InterstitialAdListener {
    public static boolean IS_LOADER = false;
    private static final String TAG = "FB_START";
    private static final String ADAPTER_NAME = "FB_START";

    private InterstitialAd interstitial;
    private CustomEventInterstitialListener mInterstitialListener;

    public void destroy() {
        IS_LOADER = false;
        try {
            interstitial.destroy();
            interstitial = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadInterstitial(Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {
        Log.d(TAG, "loadInterstitial");
        IS_LOADER = false;

        boolean isInitialized = AudienceNetworkAds.isInitialized(context)
                || FacebookInterstitial.sIsInitialized !=null && FacebookInterstitial.sIsInitialized.getAndSet(true);
        if (!isInitialized)
            AudienceNetworkAds.initialize(context);

        setAutomaticImpressionAndClickTracking(false);
        mInterstitialListener = customEventInterstitialListener;

        final String placementId = serverExtras.get("AdUnitId");
        if (TextUtils.isEmpty(placementId)) {
            if (customEventInterstitialListener != null) {
                customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
                Log.w(TAG, "PLACEMENT_ID_KEY Not Corect");
            }
            return;
        }

        if (interstitial == null) {
            interstitial = new InterstitialAd(context, placementId);
            interstitial.setAdListener(this);
        }
        interstitial.loadAd();
    }

    //region Ads listenner
    @Override
    public void onAdLoaded(final Ad ad) {
        IS_LOADER = true;
        Log.d(TAG, "onAdLoaded");
        if (mInterstitialListener != null) {
            mInterstitialListener.onInterstitialLoaded();
        }
    }

    @Override
    public void onError(final Ad ad, final AdError error) {
        IS_LOADER = false;
        Log.d(TAG, "onError " + error.getErrorMessage());
        if (mInterstitialListener != null) {
            if (error == AdError.NO_FILL) {
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
                MoPubLog.log(LOAD_FAILED, ADAPTER_NAME, MoPubErrorCode.NETWORK_NO_FILL.getIntCode(), MoPubErrorCode.NETWORK_NO_FILL);
            } else if (error == AdError.INTERNAL_ERROR) {
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                MoPubLog.log(LOAD_FAILED, ADAPTER_NAME, MoPubErrorCode.NETWORK_INVALID_STATE.getIntCode(), MoPubErrorCode.NETWORK_INVALID_STATE);
            } else {
                mInterstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
                MoPubLog.log(LOAD_FAILED, ADAPTER_NAME, MoPubErrorCode.UNSPECIFIED.getIntCode(), MoPubErrorCode.UNSPECIFIED);
            }
        }
    }

    @Override
    public void onInterstitialDisplayed(final Ad ad) {
        IS_LOADER = false;
        Log.d(TAG, "onInterstitialDisplayed " );
        MoPubLog.log(SHOW_SUCCESS, ADAPTER_NAME);
        if (mInterstitialListener != null) {
            mInterstitialListener.onInterstitialShown();
        }
    }

    @Override
    public void onAdClicked(final Ad ad) {
        MoPubLog.log(CLICKED, ADAPTER_NAME);
        if (mInterstitialListener != null) {
            mInterstitialListener.onInterstitialClicked();
        }
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        MoPubLog.log(CUSTOM, ADAPTER_NAME, "Facebook interstitial ad logged impression.");
        if (mInterstitialListener != null) {
            mInterstitialListener.onInterstitialImpression();
        }
    }

    @Override
    public void onInterstitialDismissed(final Ad ad) {
        if (mInterstitialListener != null) {
            mInterstitialListener.onInterstitialDismissed();
        }
    }
    //endregion

    @Override
    protected void onInvalidate() {
        destroy();
    }

    @Override
    protected void showInterstitial() {
        IS_LOADER = false;
        try {
            MoPubLog.log(SHOW_ATTEMPTED, ADAPTER_NAME);
            if (interstitial != null && interstitial.isAdLoaded()) {
                interstitial.show();
            } else {
                MoPubLog.log(SHOW_FAILED, ADAPTER_NAME, MoPubErrorCode.NETWORK_NO_FILL.getIntCode(), MoPubErrorCode.NETWORK_NO_FILL);
                MoPubLog.log(CUSTOM, ADAPTER_NAME, "Tried to show a Facebook interstitial ad when it's not ready. Please try again.");
                if (mInterstitialListener != null) {
                    Log.w(TAG, "call showInterstitial but not cached");
                    onError(interstitial, AdError.INTERNAL_ERROR);
                } else {
                    MoPubLog.log(CUSTOM, ADAPTER_NAME, "Interstitial listener not instantiated. Please load interstitial again.");
                }
            }
        } catch (Exception e) {
            onError(interstitial, AdError.INTERNAL_ERROR);
            e.printStackTrace();
        }
    }
}