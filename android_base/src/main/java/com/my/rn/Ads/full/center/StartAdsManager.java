package com.my.rn.ads.full.center;

import android.app.Activity;
import android.util.Log;

import com.baseLibs.BaseApplication;
import com.my.rn.ads.BaseAppOpenAdsManager;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.my.rn.ads.settings.AdsSetting;

public class StartAdsManager {
    public static class WrapAdLoaderCallback {
        IAdLoaderCallback loaderCallback;

        public WrapAdLoaderCallback(IAdLoaderCallback loaderCallback) {
            this.loaderCallback = loaderCallback;
        }

        public void onAdsFailedToLoad() {
            if (loaderCallback != null)
                loaderCallback.onAdsFailedToLoad();
            loaderCallback = null;
        }

        public void onAdsLoaded() {
            if (loaderCallback != null)
                loaderCallback.onAdsLoaded();
            loaderCallback = null;
        }
    }

    // Callback chỉ được gọi khi đã có setting
    public static void loadStartAds(final Activity activity, IAdLoaderCallback loaderCallback) {
        if (!AdsSetting.getInstance().isShowStartAds()){
            Log.d("StartAdsManager", "Skip show start ads by setting");
            loaderCallback.onAdsFailedToLoad();
            return;
        }
        final WrapAdLoaderCallback wrapAdLoaderCallback = new WrapAdLoaderCallback(loaderCallback);
        // 1. Thực hiện tải setting online. Nếu tải được và thiết lập ko show start Ads ==> call onAdsFailedToLoad
        AdsSetting.getInstance().initAdsSetting(new IAdInitCallback() {
            @Override public void didInitialise() {
                if (!AdsSetting.getInstance().isShowStartAds())
                    wrapAdLoaderCallback.onAdsFailedToLoad();
            }

            @Override public void didFailToInitialise() {
                if (!AdsSetting.getInstance().isShowStartAds())
                    wrapAdLoaderCallback.onAdsFailedToLoad();
            }
        });

        // 2. Thực hiện tải quảng cáo. Khi thực hiện xong sẽ được promise chuyển xuống code js
        BaseAppOpenAdsManager.getInstance().cacheAds(activity, new IAdLoaderCallback() {
            @Override public void onAdsFailedToLoad() {
                wrapAdLoaderCallback.onAdsFailedToLoad();
            }

            @Override public void onAdsLoaded() {
                if (wrapAdLoaderCallback.loaderCallback == null) {
                    destroyStartAdsIfNeed(activity);
                    return;
                }
                wrapAdLoaderCallback.onAdsLoaded();
            }
        });
    }

    // return true khi có ads được cached và show nó ra
    public static void showStartAdsIfCached(Activity activity, IAdsCalbackOpen adsCalbackOpen) {
        if (AdsSetting.getInstance().isShowStartAds())
            if (BaseAppOpenAdsManager.getInstance().showAdsIfCached(activity,  adsCalbackOpen))
                return;
        adsCalbackOpen.noAdsCallback();
        destroyStartAdsIfNeed(activity);
    }

    // Hàm này sẽ gọi khi load ads nhưng bị timeout không được show. Và chỉ destroy nếu start không phải là mediation
    public static void destroyStartAdsIfNeed(final Activity activity) {
        Log.d(TAG, "======= destroyStartAdsIfNeed =========");
        BaseAppOpenAdsManager.getInstance().destroy();
        BaseApplication.getHandler().postDelayed(() -> BaseAdsFullManager.getInstance().cacheAdsCenter(activity), 6000);
    }

    ///////////////////////
    private static final String TAG = "StartAdsManager";
}
