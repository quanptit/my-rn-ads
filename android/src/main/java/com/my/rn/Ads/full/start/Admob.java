package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.my.rn.Ads.BuildConfig;
import com.my.rn.Ads.IAdLoaderCallback;

public class Admob extends BaseFullStartAds {
    private InterstitialAd interstitialAds;

    @Override public String getLogTAG() {
        return "ADMOB_START";
    }

    @Override
    public void destroy() {
        interstitialAds = null;
    }

    @Override protected boolean isSkipThisAds() {
        return TextUtils.isEmpty(KeysAds.getAdmod_START());
    }

    @Override protected void adsShow() throws Exception {
        if (interstitialAds != null)
            interstitialAds.show();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAds = new InterstitialAd(BaseApplication.getAppContext());
        interstitialAds.setAdUnitId(KeysAds.getAdmod_START());
        interstitialAds.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Admob.this.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Admob.this.onAdFailedToLoad("errorCode: " + errorCode, iAdLoaderCallback);
            }

            @Override public void onAdOpened() {
                Admob.this.onAdOpened();
            }

            @Override public void onAdClosed() {
                Admob.this.onAdClosed();
            }
        });
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        interstitialAds.loadAd(adRequest.build());
    }
}
