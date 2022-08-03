package com.adapter.ax;

import android.content.Context;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.mopub.common.util.Views;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

public class ADXMopubAdapterBanner extends CustomEventBanner {
    private static final String TAG = "ADX_MOPUB_BANNER";
    private CustomEventBannerListener mBannerListener;
    private static final String AD_UNIT_ID_KEY = "AdUnitId";

    private PublisherAdView mGoogleAdView;

    @Override
    protected void loadBanner(
            final Context context,
            final CustomEventBannerListener customEventBannerListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mBannerListener = customEventBannerListener;

        String adUnitId;
        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        } else {
            Log.w(TAG, "Error Extract data");
            mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            return;
        }

        mGoogleAdView = new PublisherAdView(context);
        mGoogleAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mBannerListener != null) {
                    mBannerListener.onBannerFailed(getMoPubErrorCode(errorCode));
                }
            }

            @Override
            public void onAdLeftApplication() {
//                if (mBannerListener != null) {
//                    mBannerListener.onLeaveApplication();
//                }
            }

            @Override
            public void onAdLoaded() {
                if (mBannerListener != null) {
                    mBannerListener.onBannerLoaded(mGoogleAdView);
                }
            }

            @Override public void onAdClicked() {
                if (mBannerListener != null) {
                    mBannerListener.onBannerClicked();
                }
            }

            @Override
            public void onAdOpened() {
                if (mBannerListener != null) {
                    mBannerListener.onBannerImpression();
                }
            }
        });
        mGoogleAdView.setAdUnitId(adUnitId);

        mGoogleAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

        PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        adRequest.setRequestAgent("MoPub");

        try {
            mGoogleAdView.loadAd(adRequest.build());
            Log.d(TAG, "Start loadBanner: " + adUnitId);
        } catch (Exception e) {
            mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
        }
    }

    @Override
    protected void onInvalidate() {
        Views.removeFromParent(mGoogleAdView);

        if (mGoogleAdView != null) {
            mGoogleAdView.setAdListener(null);
            mGoogleAdView.destroy();
        }
    }

    //region utils

    /**
     * Converts a given Google Mobile Ads SDK error code into {@link MoPubErrorCode}.
     *
     * @param error Google Mobile Ads SDK error code.
     * @return an equivalent MoPub SDK error code for the given Google Mobile Ads SDK error
     * code.
     */
    private MoPubErrorCode getMoPubErrorCode(int error) {
        MoPubErrorCode errorCode;
        switch (error) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorCode = MoPubErrorCode.INTERNAL_ERROR;
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorCode = MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorCode = MoPubErrorCode.NO_CONNECTION;
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorCode = MoPubErrorCode.NO_FILL;
                break;
            default:
                errorCode = MoPubErrorCode.UNSPECIFIED;
        }
        return errorCode;
    }
    //endregion
}