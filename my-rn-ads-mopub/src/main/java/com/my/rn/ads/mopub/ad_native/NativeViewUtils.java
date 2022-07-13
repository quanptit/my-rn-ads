package com.my.rn.ads.mopub.ad_native;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.INativeManager;

public class NativeViewUtils implements INativeManager.INativeViewUtils {
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;

    private IAdLoaderCallback loaderCallback;

    @Override public void destroyAds() {
        try {
            if (nativeAd != null)
                nativeAdLoader.destroy(nativeAd);
            if (nativeAdLoader != null)
                nativeAdLoader.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nativeAd = null;
        nativeAdLoader = null;
    }

    @Override public void setAdsCallback(IAdLoaderCallback loaderCallback) {
        this.loaderCallback = loaderCallback;
    }

    public void startLoadAndDisplayAds(int typeAds, Context context, final ViewGroup nativeAdContainer) {
        nativeAdLoader = getMaxNativeAdLoader(typeAds, context);
        if (nativeAdLoader == null) {
            L.d("Native Ad Unit Is NULL: typeAds = " + typeAds);
            if (loaderCallback != null)
                loaderCallback.onAdsFailedToLoad();
            return;
        }
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                L.d("onNativeAdLoaded: " + ad.getNetworkName());
                if (nativeAd != null)
                    nativeAdLoader.destroy(nativeAd);
                nativeAd = ad;
                // Add ad view to view.
                nativeAdContainer.removeAllViews();
                nativeAdContainer.addView(nativeAdView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (loaderCallback != null)
                    loaderCallback.onAdsLoaded();
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                L.d("onNativeAdLoadFailed: " + error.getMessage());
                if (loaderCallback != null)
                    loaderCallback.onAdsFailedToLoad();
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        nativeAdLoader.loadAd();
    }

    private MaxNativeAdLoader getMaxNativeAdLoader(int typeAds, Context context) {
        if (typeAds >= 2) {
            return KeysAds.MAX_NATIVE_LARGE != null
                    ? new MaxNativeAdLoader(KeysAds.MAX_NATIVE_LARGE, context)
                    : null;
        } else {
            return KeysAds.MAX_NATIVE_SMALL != null
                    ? new MaxNativeAdLoader(KeysAds.MAX_NATIVE_SMALL, context)
                    : null;
        }
    }
}

