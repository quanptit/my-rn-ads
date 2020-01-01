package com.my.rn.Ads.full.center;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.IAdsCalbackOpen;
import com.my.rn.Ads.settings.AdsSetting;
import com.my.rn.ads.tapdaq.TapdaqFullCenter;
import com.my.rn.ads.tapdaq.TapdaqVideoCenter;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private TapdaqVideoCenter tapdaqVideoCenter;
    private TapdaqFullCenter tapdaqFullCenter;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        tapdaqVideoCenter = new TapdaqVideoCenter();
        tapdaqFullCenter = new TapdaqFullCenter();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, IAdsCalbackOpen promiseSaveObj) {
        if (tapdaqVideoCenter.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        if (tapdaqFullCenter.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        return fbFullAdsManager.showAdsCenterIfCache(activity, promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, boolean isFromStart, String typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case AdsSetting.ID_TAPDAQ_VIDEO:
                tapdaqVideoCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                break;
            case AdsSetting.ID_TAPDAQ_FULL:
                tapdaqFullCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                break;
            case AdsSetting.ID_FB:
                fbFullAdsManager.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                break;
            default:
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
                break;
        }
    }

    @Override protected boolean isCachedCenterExtend(Activity activity) {
        return tapdaqVideoCenter.isCachedCenter(activity) ||
                tapdaqFullCenter.isCachedCenter(activity) ||
                fbFullAdsManager.isCachedCenter(activity);
    }

    @Override protected boolean isCachedByMediation(Activity activity) {
        return tapdaqVideoCenter.isCachedCenter(activity) ||
                tapdaqFullCenter.isCachedCenter(activity);
    }

    @Override protected void destroyExtend() {
        try {
            tapdaqVideoCenter.destroy();
            tapdaqFullCenter.destroy();
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
