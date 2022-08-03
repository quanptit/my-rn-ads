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

import java.lang.ref.WeakReference;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private EpomCenter epomCenter;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        epomCenter = new EpomCenter();
    }

    @Override protected boolean showAdsCenterIfCache(PromiseSaveObj promiseSaveObj) {
        boolean isShowed = epomCenter.showAdsCenterIfCache(promiseSaveObj);
        if (!isShowed)
            isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
        return isShowed;
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                epomCenter.loadCenterAds(activity, iAdLoaderCallback);
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
        return epomCenter.isCachedCenter() ||
                fbFullAdsManager.isCachedCenter();
    }

    @Override protected void destroyExtend() {
        try {
            epomCenter.destroy();
            fbFullAdsManager.destroyAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
