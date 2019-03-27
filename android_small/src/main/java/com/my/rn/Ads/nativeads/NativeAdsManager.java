package com.my.rn.Ads.nativeads;

import android.content.Context;
import android.view.View;

import com.appsharelib.KeysAds;
import com.facebook.ads.NativeAdBase;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.nativeads.fb.FbNativeAdsManager;
import com.my.rn.Ads.nativeads.fb.FbNativeViewUtils;

public class NativeAdsManager {

    public static void cacheNativeAds() {
        getInstance().fbNativeAdsManager.checkAndLoadAds();
    }

    public static void cacheNativAndWaitForComplete() {
        try {
            getInstance().fbNativeAdsManager.cacheAndWaitForComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View createNewAds(Context activity, int typeAds) {
        try {
            return getInstance().createAdsFullOptions(activity, typeAds);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NativeAdsManager getInstance() {
        return ApplicationContainAds.getInstance().getNativeAdsManager();
    }

    // region Hien thi cho list
    public FbNativeAdsManager fbNativeAdsManager = new FbNativeAdsManager(2, KeysAds.FB_NATIVE, NativeAdBase.MediaCacheFlag.ALL);

    public View createAdsFullOptions(final Context context, int typeAds) {
        View view = FbNativeViewUtils.createNativeAdsView(context, getNextAds(typeAds), typeAds);
        if (view != null)
            return view;
        return null;
    }

    private NativeAdBase getNextAds(int typeAds) {
        return fbNativeAdsManager.getNextAds();
    }

    public boolean canShowNativeAds(int typeAds) {
        return fbNativeAdsManager.isCached();
    }

    //endregion
}
