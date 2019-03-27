package com.my.rn.Ads.nativeads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.appsharelib.KeysAds;
import com.facebook.ads.NativeAdBase;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.full.AdsFullManager;
import com.my.rn.Ads.nativeads.fb.FbNativeAdsManager;
import com.my.rn.Ads.nativeads.fb.FbNativeBannerAdsManager;
import com.my.rn.Ads.nativeads.fb.FbNativeViewUtils;

public class NativeAdsManager {
    public static void cacheNativeAds(int typeAds) {
        if (AdsFullManager.isDoNotShowAds()) return;
        if (typeAds == ManagerTypeAdsShow.TYPE_SUMMARY_LIST3)
            getInstance().fbNativeAdsManagerSummary.checkAndLoadAds();
        else
            getInstance().fbNativeBannerAdsManager.checkAndLoadAds();
        getInstance().fbNativeAdsManager.checkAndLoadAds();
    }
    public static void cacheNativeAds() {
        getInstance().fbNativeAdsManager.checkAndLoadAds();
        getInstance().fbNativeAdsManagerExit.checkAndLoadAds();
    }

    public static void cacheNativeBannerAdsAndWaitForComplete(boolean cacheCenterAds) {
        if (AdsFullManager.isDoNotShowAds()) return;
        try {
            if (ManagerTypeAdsShow.getPreferTypeAdsShowInListSummary() == ManagerTypeAdsShow.TYPE_SUMMARY_LIST3)
                getInstance().fbNativeAdsManagerSummary.cacheAndWaitForComplete(cacheCenterAds);
            else
                getInstance().fbNativeBannerAdsManager.cacheAndWaitForComplete(cacheCenterAds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void cacheNativeBannerAdsAndWaitForComplete() {
//        if (AdsFullManager.isDoNotShowAds()) return;
//        try {
//            if (ManagerTypeAdsShow.getPreferTypeAdsShowInListSummary() == ManagerTypeAdsShow.TYPE_SUMMARY_LIST3)
//                getInstance().fbNativeAdsManagerSummary.cacheAndWaitForComplete();
//            else
//                getInstance().fbNativeBannerAdsManager.cacheAndWaitForComplete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static View createNewAdsLikeBanner(Context activity) {
        return createNewAds(activity, ManagerTypeAdsShow.getTypeAdsNativeLikeBanner());
    }

    public static View createNewAds(Context activity, int typeAds) {
        try {
            return getInstance().createAdsFullOptions(activity, typeAds);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean displayExitAds(FrameLayout layoutAds) {
        return getInstance()._displayExitAds(layoutAds);
    }

    public static NativeAdsManager getInstance() {
        return ApplicationContainAds.getNativeAdsManager();
    }

    // region Hien thi cho list
    public FbNativeAdsManager fbNativeAdsManager = new FbNativeAdsManager(2, KeysAds.FB_NATIVE, NativeAdBase.MediaCacheFlag.ALL);
    public FbNativeAdsManager fbNativeAdsManagerSummary = new FbNativeAdsManager(1, KeysAds.FB_NATIVE_BANNER_LARGE, NativeAdBase.MediaCacheFlag.NONE);
    public FbNativeBannerAdsManager fbNativeBannerAdsManager = new FbNativeBannerAdsManager(NativeAdBase.MediaCacheFlag.NONE);

    public View createAdsFullOptions(final Context context, int typeAds) {
        View view = FbNativeViewUtils.createNativeAdsView(context, getNextAds(typeAds), typeAds);
        if (view != null)
            return view;
        return null;
    }

    private NativeAdBase getNextAds(int typeAds) {
        NativeAdBase nativeAdBase;
        if (ManagerTypeAdsShow.isNativeBanner(typeAds))
            nativeAdBase = fbNativeBannerAdsManager.getNextAds();
        else {
            if (typeAds == ManagerTypeAdsShow.TYPE_SUMMARY_LIST3) {
                nativeAdBase = fbNativeAdsManagerSummary.getNextAds();
                if (nativeAdBase == null)
                    nativeAdBase = fbNativeAdsManager.getNextAds();
                if (nativeAdBase == null) // Backup nếu large chưa kịp load thì lấy cái small
                    nativeAdBase = fbNativeBannerAdsManager.getNextAds();
            } else {
                nativeAdBase = fbNativeAdsManager.getNextAds();
                if (nativeAdBase == null)
                    nativeAdBase = fbNativeAdsManagerSummary.getNextAds();
            }
        }
        return nativeAdBase;
    }

    public boolean canShowNativeAds(int typeAds) {
        if (ManagerTypeAdsShow.isNativeBanner(typeAds))
            return fbNativeBannerAdsManager.isCached();
        return fbNativeAdsManagerSummary.isCached() || fbNativeAdsManager.isCached();
    }

    //endregion

    //region For exit Ads
    public FbNativeAdsManager fbNativeAdsManagerExit = new FbNativeAdsManager(1, KeysAds.FB_NATIVE_EXIT, NativeAdBase.MediaCacheFlag.ALL);

    public boolean _displayExitAds(final FrameLayout layoutAds) {
        try {
            NativeAdBase nativeAdBase = fbNativeAdsManagerExit.getNextAds();
            if (nativeAdBase != null) {
                View ads = FbNativeViewUtils.createNativeAdsView(layoutAds.getContext(), nativeAdBase, ManagerTypeAdsShow.getPreferTypeAdsShowExit());
                ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutAds.removeAllViews();
                layoutAds.addView(ads, layoutParams);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //endregion
}
