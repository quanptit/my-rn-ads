package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.Promise;
import com.google.android.gms.ads.MobileAds;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

import java.lang.ref.WeakReference;

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
        BaseShowStartAdsManager showStartAdsManager = ShowStartAdsManager.getInstance();
        if (showStartAdsManager != null) {
            isShowed = showStartAdsManager.showStartIfCacheAsCenter(activity, promiseSaveObj);
            if (isShowed) return true;
        }

        isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
        if (!isShowed)
            isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
        return isShowed;
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        BaseShowStartAdsManager showStartAdsManager = ShowStartAdsManager.getInstance();
        if (showStartAdsManager != null) {
            if (showStartAdsManager.isCached()) {
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsLoaded();
                return;
            }
        }

        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                mopubInterstitialManager.loadCenterAds(activity, iAdLoaderCallback);
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
        BaseShowStartAdsManager showStartAdsManager = ShowStartAdsManager.getInstance();
        if (showStartAdsManager != null && showStartAdsManager.isCached()) {
            return true;
        }

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
