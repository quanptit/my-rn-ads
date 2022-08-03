package com.my.rn.ads;

import android.content.Context;
import android.view.ViewGroup;

public abstract class BaseNativeViewUtils {
    protected final Context context;
    BaseNativeView nativeView;
    IAdLoaderCallback loaderCallback;
    int index;
    int typeAds;
    ViewGroup nativeAdContainer;

    public BaseNativeViewUtils(Context context, IAdLoaderCallback loaderCallback) {
        this.loaderCallback = loaderCallback;
        this.context = context;
    }

    private final IAdLoaderCallback localIAdLoaderCallback = new IAdLoaderCallback() {
        @Override
        public void onAdsLoaded() {
            if (isDestroyed) return;
            if (loaderCallback != null)
                loaderCallback.onAdsLoaded();
            if (nativeAdContainer == null) return;
            nativeAdContainer.removeAllViews();
            if (nativeView != null)
                nativeAdContainer.addView(nativeView);
        }

        public void onAdsFailedToLoad() {
            if (isDestroyed) return;
            index++;
            startLoadAndDisplayAds(typeAds, nativeAdContainer);
        }
    };


    public void startLoadAndDisplayAds(int typeAds, final ViewGroup nativeAdContainer) {
        if (isDestroyed) return;
        destroyAds();
        if (index == 0) {
            this.typeAds = typeAds;
            this.nativeAdContainer = nativeAdContainer;
        }
        nativeView = getBaseNativeView(index, typeAds, localIAdLoaderCallback);
        if (nativeView == null) {
            if (loaderCallback != null)
                loaderCallback.onAdsFailedToLoad();
        } else
            nativeView.loadAds();
    }

    private void destroyAds() {
        try {
            if (nativeView != null)
                nativeView.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nativeView = null;
    }

    protected boolean isDestroyed = false;

    public void destroy() {
        isDestroyed = true;
        destroyAds();
        this.nativeAdContainer = null;
    }

    protected abstract BaseNativeView getBaseNativeView(int indexOrder, int typeAds, IAdLoaderCallback loaderCallback);
}
