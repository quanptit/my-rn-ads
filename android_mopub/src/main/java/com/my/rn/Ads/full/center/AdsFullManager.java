package com.my.rn.Ads.full.center;

import android.app.Activity;

import androidx.annotation.Nullable;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private MopubFullCenter mopubInterstitialManager;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        mopubInterstitialManager = new MopubFullCenter();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, PromiseSaveObj promiseSaveObj) {
        boolean isShowed;
        isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
        if (!isShowed)
            isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
        return isShowed;
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, boolean isFromStart, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                mopubInterstitialManager.loadCenterAds(activity,isFromStart, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FB:
                fbFullAdsManager.loadCenterAds(activity,isFromStart, iAdLoaderCallback);
                break;
            default:
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
                break;
        }
    }

    @Override protected boolean isCachedCenterExtend() {
        return mopubInterstitialManager.isCachedCenter() ||
                fbFullAdsManager.isCachedCenter();
    }

    @Override protected void destroyExtend() {
        try {
            mopubInterstitialManager.destroy();
            fbFullAdsManager.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
