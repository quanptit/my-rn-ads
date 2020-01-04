package com.my.rn.ads;

public abstract class IAdLoaderCallback {
    public abstract void onAdsFailedToLoad();

    public void onAdsLoaded() {}
}
