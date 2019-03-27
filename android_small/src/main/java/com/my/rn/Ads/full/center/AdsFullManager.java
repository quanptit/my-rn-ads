package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
    }

    @Override protected boolean showAdsCenterIfCache(PromiseSaveObj promiseSaveObj) {
        return fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_FB:
                fbFullAdsManager.loadCenterAds(activity, iAdLoaderCallback);
                break;
            default:
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
                break;
        }
    }

    @Override protected boolean isCachedCenterExtend() {
        return fbFullAdsManager.isCachedCenter();
    }

    @Override protected void destroyExtend() {
        try {
            fbFullAdsManager.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
