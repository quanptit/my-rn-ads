package com.my.rn.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.my.rn.ads.BaseAppOpenAdsManager;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;

public class AdmobAppOpenManager extends BaseAppOpenAdsManager {
    private static final String LOG_TAG = "AdmobAppOpenManager";
    private AppOpenAd appOpenAd = null;

    AdmobAppOpenManager() {
//        MobileAds.initialize(
//                BaseApplication.getAppContext(),
//                initializationStatus -> {
//                });
    }

//    public String getKeyAds() {
//        return KeysAds.Admod_START;
//    }


    @Override public void cacheAds(Activity activity, IAdLoaderCallback loaderCallback) {
        loaderCallback.onAdsFailedToLoad();
//        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
//            @Override
//            public void onAdLoaded(AppOpenAd ad) {
//                AdmobAppOpenManager.this.appOpenAd = ad;
//                Log.d(LOG_TAG, "onAdLoaded");
//                loaderCallback.onAdsLoaded();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                loaderCallback.onAdsFailedToLoad();
//                destroy();
//            }
//
//        };
//        AdRequest request = new AdRequest.Builder().build();
//        AppOpenAd.load(BaseApplication.getAppContext(), getKeyAds(), request,
//                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    @Override public boolean showAdsIfCached(Activity activity, IAdsCalbackOpen adsCalbackOpen) {
        return false;
//        if (appOpenAd == null)
//            return false;
//
//        FullScreenContentCallback fullScreenContentCallback =
//                new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent");
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent");
//                        adsCalbackOpen.noAdsCallback();
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        adsCalbackOpen.onAdOpened();
//
//                        long lastTimeShowAds = PreferenceUtils.getLongSetting(KeysAds.LAST_TIME_SHOW_ADS, 0);
//                        // Trước đó khá lâu chưa show full ads => Set time là trước đó 4 phút đã show ad. 5 phút là sẽ show lại
//                        if (System.currentTimeMillis() - lastTimeShowAds > 210000)
//                            PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS,
//                                    System.currentTimeMillis() - 210000);
//                        Log.d(LOG_TAG, "onAdShowedFullScreenContent");
//                    }
//                };
//        appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
//        appOpenAd.show(activity);
//        return true;
    }


    @Override public void destroy() {
        appOpenAd = null;
    }
}
