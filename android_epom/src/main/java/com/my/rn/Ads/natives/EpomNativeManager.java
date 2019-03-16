package com.my.rn.Ads.natives;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.baseLibs.BaseApplication;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.R;
import com.my.rn.Ads.modules.NativeAdsView;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EpomNativeManager {
    private static final String TAG = "EPOM_NATIVE";
    private EpomCacheUtils epomCacheUtils_DETAIL_LIST3;

    public EpomCacheUtils getEpomCacheUtils_DETAIL_LIST3() {
        if (epomCacheUtils_DETAIL_LIST3 == null)
            epomCacheUtils_DETAIL_LIST3 = new EpomCacheUtils(BaseApplication.getAppContext(), R.layout.v_fb_native_ads_voca_sample);
        return epomCacheUtils_DETAIL_LIST3;
    }

    private EpomCacheUtils getEpomCacheUtils(int typeAds) {
        if (typeAds == ManagerTypeAdsShow.TYPE_DETAIL_LIST3)
            return getEpomCacheUtils_DETAIL_LIST3();
        else
            return getEpomCacheUtils_DETAIL_LIST3();
    }

    public void cacheNative(int typeAds) {
        EpomCacheUtils epomCacheUtils = getEpomCacheUtils(typeAds);
        epomCacheUtils.cache();
    }

    public boolean isCached(int typeAds) {
        EpomCacheUtils epomCacheUtils = getEpomCacheUtils(typeAds);
        return epomCacheUtils.isCached();
    }

    public void showNewNativeAds(int typeAds, NativeAdsView nativeAdsView, ViewGroup parent) {
        EpomCacheUtils epomCacheUtils = getEpomCacheUtils(typeAds);
        epomCacheUtils.showNewNativeAds(nativeAdsView.getActivity(), nativeAdsView, parent);
    }

    public boolean canShowNativeAds(int typeAds) {
        return isCached(typeAds);
    }
//
//    public void cacheNativeAndWaitForComplete() throws Exception {
//        if (!nativeAds.isEmpty()) return;
//        Log.d(TAG, "cacheNativeAndWaitForComplete");
//        long startTimeLoad = System.currentTimeMillis();
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                _checkAndLoadAds(false);
//            }
//        });
//        while (true) {
//            Thread.sleep(100);
//            if (!nativeAds.isEmpty()) return;
//            if (System.currentTimeMillis() - startTimeLoad > 3000) {
//                Log.d(TAG, "cacheAndWaitForComplete Fail Time out");
//                return;
//            }
//        }
//    }

    public static EpomNativeManager getInstance() {
        if (ApplicationContainAds.getInstance().epomNativeManager == null)
            ApplicationContainAds.getInstance().epomNativeManager = new EpomNativeManager();
        return ApplicationContainAds.getInstance().epomNativeManager;
    }
}
