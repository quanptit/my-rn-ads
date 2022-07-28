package com.my.rn.ads.startio;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.my.rn.ads.BaseAppOpenAdsManager;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.startapp.sdk.ads.splash.SplashConfig;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.model.AdPreferences;

public class StartIoOpenApp extends BaseAppOpenAdsManager {
    public StartIoOpenApp(){
        StartAppAd.disableSplash();
        StartAppAd.disableAutoInterstitial();
    }

    @Override public void cacheAds(Activity activity, IAdLoaderCallback loaderCallback) {
        loaderCallback.onAdsLoaded();
    }

    @Override public boolean showAdsIfCached(Activity activity, IAdsCalbackOpen adsCalbackOpen) {
        Log.d("aa", "showSplash Start IO");
        SplashConfig splashConfig = new SplashConfig();
        splashConfig.setOrientation(SplashConfig.Orientation.PORTRAIT)
                .setTheme(SplashConfig.Theme.USER_DEFINED)
                .setCustomScreen(R.layout.empty)
        ;
        StartAppAd.showSplash(activity, null, splashConfig, new AdPreferences(),
                () -> {
                    Log.d("aa", "HIDE Splash Start IO");
                    adsCalbackOpen.onAdOpened();
                });


        return true;
    }

    @Override public void destroy() {
    }

}
