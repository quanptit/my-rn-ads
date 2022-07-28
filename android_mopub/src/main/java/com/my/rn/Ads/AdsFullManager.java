package com.my.rn.ads;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.my.rn.ads.fb.FbCenter;
import com.my.rn.ads.full.center.AdmobCenter;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.mopub.MopubFullCenter;
import com.my.rn.ads.settings.AdsSetting;

public class AdsFullManager extends BaseAdsFullManager {
    private FbCenter fbFullAdsManager;
    private MopubFullCenter mopubInterstitialManager;
    private AdmobCenter admobCenter;

    //region get & set

    public AdmobCenter getAdmobCenter() {
        if (admobCenter == null)
            admobCenter = new AdmobCenter();
        return admobCenter;
    }

    //endregion

    public AdsFullManager() {
        super();
        fbFullAdsManager = new FbCenter();
        mopubInterstitialManager = new MopubFullCenter();
    }

    @Override protected boolean showAdsCenterIfCache(Activity activity, IAdsCalbackOpen promiseSaveObj) {
        if (admobCenter != null &&
                admobCenter.showAdsCenterIfCache(activity, promiseSaveObj))
            return true;
        if (mopubInterstitialManager.showAdsCenterIfCache(activity, promiseSaveObj)) return true;
        return fbFullAdsManager.showAdsCenterIfCache(activity, promiseSaveObj);
    }

    @Override protected void cacheAdsCenterExtend(Activity activity, boolean isFromStart, String typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        switch (typeAds) {
            case AdsSetting.ID_ADMOB:
            case AdsSetting.ID_ADX:
                getAdmobCenter().loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                break;
            case AdsSetting.ID_MOPUB:
            case AdsSetting.ID_TAPDAQ:
            case AdsSetting.ID_TAPDAQ_FULL:
            case AdsSetting.ID_FAIRBID:
            case AdsSetting.ID_TAPDAQ_VIDEO:
                mopubInterstitialManager.loadCenterAds(activity, isFromStart, iAdLoaderCallback);
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
        return mopubInterstitialManager.isCachedCenter(activity) ||
                fbFullAdsManager.isCachedCenter(activity);
    }

    @Override protected void destroyExtend() {
        try {
            mopubInterstitialManager.destroy();
            fbFullAdsManager.destroyAds();
            if (admobCenter != null)
                admobCenter.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
