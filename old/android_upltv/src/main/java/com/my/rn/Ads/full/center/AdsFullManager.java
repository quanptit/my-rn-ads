package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private UPLTVCenter upltvCenter;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        upltvCenter = new UPLTVCenter();
    }

    @Override protected boolean showAdsCenterIfCache(PromiseSaveObj promiseSaveObj) {
        boolean isShowed = upltvCenter.showAdsCenterIfCache(promiseSaveObj);
        if (!isShowed)
            isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
        return isShowed;
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                upltvCenter.loadCenterAds(activity, iAdLoaderCallback);
                break;
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
        return upltvCenter.isCachedCenter() ||
                fbFullAdsManager.isCachedCenter();
    }

    @Override protected void destroyExtend() {
        try {
            upltvCenter.destroy();
            fbFullAdsManager.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
