package com.my.rn.ads;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.my.rn.ads.fairbid.FairbidFullCenter;
import com.my.rn.ads.fb.FbCenter;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.mopub.MopubFullCenter;
import com.my.rn.ads.settings.AdsSetting;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private MopubFullCenter mopubInterstitialManager;
    private FairbidFullCenter fairbidFullCenter;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        mopubInterstitialManager = new MopubFullCenter();
        fairbidFullCenter = new FairbidFullCenter();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, IAdsCalbackOpen promiseSaveObj) {
        if (fairbidFullCenter.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        if (mopubInterstitialManager.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        return fbFullAdsManager.showAdsCenterIfCache(activity, promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, boolean isFromStart, String typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case AdsSetting.ID_MOPUB:
                mopubInterstitialManager.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                break;
            case AdsSetting.ID_FAIRBID:
                fairbidFullCenter.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
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
        return fairbidFullCenter.isCachedCenter(activity) ||
                mopubInterstitialManager.isCachedCenter(activity) ||
                fbFullAdsManager.isCachedCenter(activity);
    }

    @Override protected boolean isCachedByMediation(Activity activity) {
        return fairbidFullCenter.isCachedCenter(activity)
                || mopubInterstitialManager.isCachedCenter(activity);
    }

    @Override protected void destroyExtend() {
        try {
            fairbidFullCenter.destroy();
            mopubInterstitialManager.destroy();
            fbFullAdsManager.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void destroyIgnoreMediationExtend() {
        if (fbFullAdsManager != null)
            fbFullAdsManager.destroy();
    }
}
