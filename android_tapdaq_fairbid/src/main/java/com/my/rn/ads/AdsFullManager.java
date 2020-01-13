package com.my.rn.ads;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.my.rn.ads.fairbid.FairbidFullCenter;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.fb.FbCenter;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private FairbidFullCenter fairbidFullCenter;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        fairbidFullCenter = new FairbidFullCenter();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, IAdsCalbackOpen promiseSaveObj) {
        if (fairbidFullCenter.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        return fbFullAdsManager.showAdsCenterIfCache(activity, promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, boolean isFromStart, String typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        //TODO
        fairbidFullCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
//        switch (typeAds) {
//            case AdsSetting.ID_TAPDAQ_VIDEO:
//                fairbidFullCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
//                break;
//            case AdsSetting.ID_TAPDAQ_FULL:
//                tapdaqFullCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
//                break;
//            case AdsSetting.ID_FB:
//                fbFullAdsManager.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
//                break;
//            default:
//                if (iAdLoaderCallback != null)
//                    iAdLoaderCallback.onAdsFailedToLoad();
//                break;
//        }
    }

    @Override protected boolean isCachedCenterExtend(Activity activity) {
        return fairbidFullCenter.isCachedCenter(activity) ||
                fbFullAdsManager.isCachedCenter(activity);
    }

    @Override protected boolean isCachedByMediation(Activity activity) {
        return fairbidFullCenter.isCachedCenter(activity);
    }

    @Override protected void destroyExtend() {
        try {
            fairbidFullCenter.destroy();
            fbFullAdsManager.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void destroyIgnoreMediationExtend() {
        if (fbFullAdsManager != null)
            fbFullAdsManager.destroyAds();
    }
}
