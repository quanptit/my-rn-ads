package com.adapter.ax;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.mopub.common.LifecycleListener;
import com.mopub.mobileads.AdData;
import com.mopub.mobileads.BaseAd;
import com.mopub.mobileads.MoPubErrorCode;
import com.google.android.gms.ads.AdListener;

/**
 * //TODO: Cần test ở app sử dụng
 * Setting Trong mopub như sau:
 * CUSTOM EVENT CLASS: com.adapter.ax.ADXMopubAdapterInterstitial
 * CUSTOM EVENT CLASS DATA: {"AdUnitId": "This is AD_UNIT_ID for full screen"};  ex test: {"AdUnitId": "/21617015150/47909961/21720491886"}
 */
public class ADXMopubAdapterInterstitial extends BaseAd {
    private static final String TAG = "ADX-MOPUB-FULL";
    private PublisherInterstitialAd interstitial;
    @Nullable private String mAdUnitId;

    @NonNull @Override protected String getAdNetworkId() {
        return mAdUnitId != null ? mAdUnitId : "";
    }


    @Override protected void load(@NonNull Context context, @NonNull AdData adData) throws Exception {
        Log.d(TAG, "loadInterstitial");
        if (interstitial == null) {
            interstitial = new PublisherInterstitialAd(BaseApplication.getAppContext());
            mAdUnitId = adData.getExtras().get("AdUnitId");
            if (TextUtils.isEmpty(mAdUnitId)) {
                if (mLoadListener != null)
                    mLoadListener.onAdLoadFailed(MoPubErrorCode.NETWORK_NO_FILL);
                return;
            }
            interstitial.setAdUnitId(mAdUnitId);
            interstitial.setAdListener(new InterstitialAdListener());
        }

        PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        interstitial.loadAd(adRequest.build());
    }

    private class InterstitialAdListener extends AdListener {
        /*
         * Google Play Services AdListener implementation
         */
        @Override
        public void onAdClosed() {
            if (mInteractionListener != null) {
                mInteractionListener.onAdDismissed();
            }
        }

        @Override
        public void onAdFailedToLoad(LoadAdError loadAdError) {
            Log.d(TAG, "onAdFailedToLoad: " + loadAdError.toString());
            if (mLoadListener != null) {
                mLoadListener.onAdLoadFailed(getMoPubErrorCode(loadAdError.getCode()));
            }
        }

        @Override
        public void onAdLeftApplication() {
            Log.d(TAG, "onAdLeftApplication");
            if (mInteractionListener != null) {
                mInteractionListener.onAdClicked();
            }
        }

        @Override
        public void onAdLoaded() {
            Log.d(TAG, "onAdLoaded");
            if (mLoadListener != null) {
                mLoadListener.onAdLoaded();
            }
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "onAdOpened");
            if (mInteractionListener != null) {
                mInteractionListener.onAdShown();
                mInteractionListener.onAdImpression();
            }
        }

        /**
         * Converts a given Google Mobile Ads SDK error code into {@link MoPubErrorCode}.
         *
         * @param error Google Mobile Ads SDK error code.
         * @return an equivalent MoPub SDK error code for the given Google Mobile Ads SDK error
         * code.
         */
        private MoPubErrorCode getMoPubErrorCode(int error) {
            switch (error) {
                case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                    return MoPubErrorCode.INTERNAL_ERROR;
                case AdRequest.ERROR_CODE_INVALID_REQUEST:
                    return MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                case AdRequest.ERROR_CODE_NETWORK_ERROR:
                    return MoPubErrorCode.NO_CONNECTION;
                case AdRequest.ERROR_CODE_NO_FILL:
                    return MoPubErrorCode.NO_FILL;
                default:
                    return MoPubErrorCode.UNSPECIFIED;
            }
        }
    }

    //#region utils
    @Nullable @Override protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull AdData adData) throws Exception {
        return false;
    }
    //#endregion

    public void destroy() {
        try {
            if (interstitial != null) {
                interstitial.setAdListener(null);
                interstitial = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInvalidate() {
        destroy();
    }

    @Override
    protected void show() {
        if (interstitial != null && interstitial.isLoaded())
            interstitial.show();
        else if (mInteractionListener != null) {
            mInteractionListener.onAdFailed(MoPubErrorCode.NETWORK_NO_FILL);
        }
    }
}