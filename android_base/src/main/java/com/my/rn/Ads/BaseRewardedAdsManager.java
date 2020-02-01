package com.my.rn.ads;

import android.app.Activity;

public abstract class BaseRewardedAdsManager {
    public abstract void cacheRewardedAds(Activity activity);

    public abstract void showRewardedAds(Activity activity);

    public abstract boolean isCachedRewardedAds(Activity activity);
}
