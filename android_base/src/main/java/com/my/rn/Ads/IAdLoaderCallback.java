package com.my.rn.Ads;

public abstract class IAdLoaderCallback {
    public abstract void onAdsFailedToLoad();

    public void onAdsLoaded() {}
}
