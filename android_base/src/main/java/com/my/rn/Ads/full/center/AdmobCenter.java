package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;

public class AdmobCenter extends BaseFullCenterAds {
    private InterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "ADMOB_CENTER";
    }

    @Override public boolean isCachedCenter() {
        return (interstitialCenter != null && interstitialCenter.isLoaded());
    }

    @Override protected void showAds() {
        interstitialCenter.show();
    }

    @Override public String getKeyAds() {
        return KeysAds.getAdmod_FULL();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
        interstitialCenter = new InterstitialAd(BaseApplication.getAppContext());
        interstitialCenter.setAdUnitId(getKeyAds());
        interstitialCenter.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdmobCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                AdmobCenter.this.onAdFailedToLoad("errorCode: " + errorCode, iAdLoaderCallback);
            }

            @Override
            public void onAdClosed() {
                AdmobCenter.this.onAdClosed();
            }

            @Override public void onAdOpened() {
                AdmobCenter.this.onAdOpened();
            }
        });
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        interstitialCenter.loadAd(adRequest.build());
    }

    @Override protected void destroyAds() {
        interstitialCenter = null;
    }
}
