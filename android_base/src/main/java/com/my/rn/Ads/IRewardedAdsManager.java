package com.my.rn.ads;

import android.app.Activity;

import androidx.annotation.Nullable;

public interface IRewardedAdsManager {

    void showRewardedAds(Activity activity, boolean showLoaddingIfNotCache, @Nullable IAdsCalbackOpen adsCalbackOpen);

    void cacheRewardedAds(Activity activity);

    boolean isCachedRewardedAds(Activity activity);
}
