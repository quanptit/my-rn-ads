package com.my.rn.ads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.baseLibs.utils.L;

public abstract class BaseNativeView extends FrameLayout {
    protected boolean isDestroyed = false;
    protected IAdLoaderCallback loaderCallback;
    private final int typeAds;

    public BaseNativeView(@NonNull Context context, int typeAds, IAdLoaderCallback loaderCallback) {
        super(context);
        this.loaderCallback = loaderCallback;
        this.typeAds = typeAds;
    }

    protected void onAdsLoaded() {
        if (loaderCallback != null)
            loaderCallback.onAdsLoaded();
    }

    protected void onAdsFailedToLoad() {
        if (loaderCallback != null)
            loaderCallback.onAdsFailedToLoad();
    }

    public void loadAds() {
        if (isDestroyed) return;
        String adUnit = getKeyAds(typeAds);
        if (TextUtils.isEmpty(adUnit)) {
            L.d(getClass().getName() + ": BaseNativeView onAdsFailedToLoad because isEmpty adUnit");
            onAdsFailedToLoad();
            return;
        }
        loadAds(typeAds, adUnit);
    }

    @Override protected void onDetachedFromWindow() {
        Log.d("BaseNativeView", "onDetachedFromWindow => destroyAds");
        isDestroyed = true;
        destroyAds();
        super.onDetachedFromWindow();
    }

    abstract protected void loadAds(int typeAds, String adUnit);

    abstract protected void destroyAds();

    abstract protected String getKeyAds(int typeAds);
}
