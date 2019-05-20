package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.adapter.ax.FairBidInterstitial;
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
    private MopubFullCenter mopubInterstitialManager;
    private FairBidInterstitial fairBidInterstitial;

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        mopubInterstitialManager = new MopubFullCenter();
        fairBidInterstitial = new FairBidInterstitial();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, PromiseSaveObj promiseSaveObj) throws Exception {
        boolean isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
        if (isShowed) return true;
        isShowed = fairBidInterstitial.showIfCache(activity, promiseSaveObj);
        if (isShowed) return true;
        isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
        if (isShowed) return true;
        return fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) throws Exception {
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                mopubInterstitialManager.loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FB:
                fbFullAdsManager.loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FAIRBID:
                fairBidInterstitial.loadInterstitial(activity);
                break;
            default:
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
                break;
        }
    }

    @Override protected boolean isCachedCenterExtend() {
        return mopubInterstitialManager.isCachedCenter() ||
                fbFullAdsManager.isCachedCenter() ||
                fairBidInterstitial.isCached();
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
