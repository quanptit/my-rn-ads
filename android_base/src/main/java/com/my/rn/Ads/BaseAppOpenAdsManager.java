package com.my.rn.ads;

import android.app.Activity;

public abstract class BaseAppOpenAdsManager {
    public static BaseAppOpenAdsManager getInstance() {
        return BaseApplicationContainAds.getInstance().getAppOpenAdsManager();
    }

    public abstract void cacheAds(Activity activity, IAdLoaderCallback loaderCallback);


    public abstract boolean showAdsIfCached(Activity activity, IAdsCalbackOpen adsCalbackOpen);

    public abstract void destroy();
}
