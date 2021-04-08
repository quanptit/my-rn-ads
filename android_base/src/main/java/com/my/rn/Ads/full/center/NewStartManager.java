package com.my.rn.ads.full.center;

import android.app.Activity;
import android.content.Context;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.my.rn.ads.settings.AdsSetting;

// Sẽ tự động tải File setting, xác định xem có show Ads không. Có mới show Admob lúc start.
public class NewStartManager {
    public static void showStartAds(final Activity activity, final ISplashScreenActionCallback callback) {
        String packageName = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
        AdsSetting.getInstance().updateAdsSetting(KeysAds.ROOT + "ads_new/" + packageName);

        L.d("Load Admob Start Ads");
        BaseAdsFullManager.getInstance().cacheAdmobStartAds(activity, new IAdLoaderCallback() {
            @Override public void onAdsFailedToLoad() {
                L.d("Admob Start onAdsFailedToLoad");
                callback.requestHide();
            }

            @Override public void onAdsLoaded() {
                if (!callback.isShowing()) {
                    L.d("Admob Start onAdsLoaded, But SKIP show because hiden SplashScreen");
                    return;
                }

                L.d("Admob Start onAdsLoaded ==> check can show start in setting and Showing");
                AdsSetting.getInstance().initAdsSetting(new IAdInitCallback() {
                    @Override public void didInitialise() {
                        if (AdsSetting.getInstance().isShowStartAds()) {
                            showAdmobAfterCached(activity, callback);
                        } else {
                            L.d("Start Ads Setting Not Show => Skip");
                            callback.hide();
                        }
                    }

                    @Override public void didFailToInitialise() {
                        if (AdsSetting.getInstance().isShowStartAds()) {
                            showAdmobAfterCached(activity, callback);
                        } else {
                            L.d("Start Ads Setting Not Show => Skip");
                            callback.hide();
                        }
                    }
                });
            }
        });
    }

    private static void showAdmobAfterCached(Activity activity, final ISplashScreenActionCallback callback) {
        BaseAdsFullManager.getInstance().getAdmobCenter().showAdsCenterIfCache(activity,
                new IAdsCalbackOpen() {
                    @Override public void onAdOpened() {
                        callback.hide();
                    }

                    @Override public void noAdsCallback() {
                        callback.hide();
                    }
                });
    }


    public interface ISplashScreenActionCallback {
        void requestHide();

        boolean isShowing();

        void hide();
    }
}
