package com.my.rn.Ads.natives;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.baseLibs.BaseApplication;
import com.facebook.react.bridge.UiThreadUtil;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.R;
import com.my.rn.Ads.modules.NativeAdsView;


public class EpomNativeManager {
    private static final String TAG = "EPOM_NATIVE";
    private EpomCacheUtils epomCacheUtils_DETAIL_LIST3;

    public EpomCacheUtils getEpomCacheUtils_DETAIL_LIST3() {
        if (epomCacheUtils_DETAIL_LIST3 == null)
            epomCacheUtils_DETAIL_LIST3 = new EpomCacheUtils(BaseApplication.getAppContext(),
                    R.layout.v_fb_native_ads_voca_sample);
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

    public void showNewNativeAds(int typeAds, NativeAdsView nativeAdsView, FrameLayout parent) {
        EpomCacheUtils epomCacheUtils = getEpomCacheUtils(typeAds);
        epomCacheUtils.showNewNativeAds(nativeAdsView.getActivity(), nativeAdsView, parent);
    }

    public boolean canShowNativeAds(int typeAds) {
        return isCached(typeAds);
    }

    public void cacheNativeAndWaitForComplete(int typeAds) throws Exception {
        Log.d(TAG, "cacheNativeAndWaitForComplete typeAds: " + typeAds);
        final EpomCacheUtils epomCacheUtils = getEpomCacheUtils(typeAds);
        long startTimeLoad = System.currentTimeMillis();
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                epomCacheUtils.cache();
            }
        });
        while (true) {
            Thread.sleep(100);
            if (epomCacheUtils.isCached()) return;
            if (System.currentTimeMillis() - startTimeLoad > 1000) {
                if (!epomCacheUtils.isCaching()) return;

                if (System.currentTimeMillis() - startTimeLoad > 5000) {
                    Log.d(TAG, "cacheAndWaitForComplete Fail Time out");
                    return;
                }
            }
        }
    }

    public static EpomNativeManager getInstance() {
        if (ApplicationContainAds.getInstance().epomNativeManager == null)
            ApplicationContainAds.getInstance().epomNativeManager = new EpomNativeManager();
        return ApplicationContainAds.getInstance().epomNativeManager;
    }
}
