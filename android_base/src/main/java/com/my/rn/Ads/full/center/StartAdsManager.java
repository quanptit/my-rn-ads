package com.my.rn.ads.full.center;

import android.app.Activity;
import android.util.Log;

import com.baseLibs.BaseApplication;
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
            loaderCallback.onAdsFailedToLoad();
            return;
        }
        final WrapAdLoaderCallback wrapAdLoaderCallback = new WrapAdLoaderCallback(loaderCallback);
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

        IAdLoaderCallback loaderCallbackAndWaitHasSetting = new IAdLoaderCallback() {
            @Override public void onAdsFailedToLoad() {
                wrapAdLoaderCallback.onAdsFailedToLoad();
            }

            @Override public void onAdsLoaded() {
                if (wrapAdLoaderCallback.loaderCallback == null) {
                    destroyStartAdsIfNeed(activity);
                    return;
                }
                AdsSetting.getInstance().initAdsSetting(new IAdInitCallback() {
                    @Override public void didInitialise() {
                        wrapAdLoaderCallback.onAdsLoaded();
                    }

                    @Override public void didFailToInitialise() {
                        wrapAdLoaderCallback.onAdsLoaded();
                    }
                });
            }
        };
        BaseAdsFullManager.getInstance().cacheAdsCenter(activity, true, true, loaderCallbackAndWaitHasSetting);
    }

    // return true khi có ads được cached và show nó ra
    public static void showStartAdsIfCached(Activity activity, IAdsCalbackOpen adsCalbackOpen) {
        if (AdsSetting.getInstance().isShowStartAds())
            if (BaseAdsFullManager.getInstance().showAdsCenter(activity, true, false, adsCalbackOpen))
                return;
        adsCalbackOpen.noAdsCallback();
        destroyStartAdsIfNeed(activity);
    }

    // Hàm này sẽ gọi khi load ads nhưng bị timeout không được show. Và chỉ destroy nếu start không phải là mediation
    public static void destroyStartAdsIfNeed(final Activity activity) {
        Log.d(TAG, "======= destroyStartAdsIfNeed =========");
        BaseAdsFullManager.getInstance().destroyIgnoreMediation();
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override public void run() {
                BaseAdsFullManager.getInstance().cacheAdsCenter(activity);
            }
        }, 6000);
    }

    ///////////////////////
    private static final String TAG = "StartAdsManager";
}
