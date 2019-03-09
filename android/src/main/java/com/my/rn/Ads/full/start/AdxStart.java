package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.my.rn.Ads.BuildConfig;
import com.my.rn.Ads.IAdLoaderCallback;

public class AdxStart extends BaseFullStartAds {
    private PublisherInterstitialAd interstitialAdsStart;

    @Override protected String getLogTAG() {
        return "ADX-START";
    }

    @Override protected boolean isSkipThisAds() {
        return TextUtils.isEmpty(KeysAds.getAdxFullStart());
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAdsStart = new PublisherInterstitialAd(BaseApplication.getAppContext());
        interstitialAdsStart.setAdUnitId(KeysAds.getAdxFullStart());
        interstitialAdsStart.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdxStart.this.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                AdxStart.this.onAdFailedToLoad("errorCode: " + errorCode, iAdLoaderCallback);
            }

            @Override public void onAdOpened() {
                AdxStart.this.onAdOpened();
            }

            @Override public void onAdClosed() {
                AdxStart.this.onAdClosed();
            }
        });
        PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        interstitialAdsStart.loadAd(adRequest.build());
    }

    @Override protected void adsShow() throws Exception {
        if (interstitialAdsStart != null)
            interstitialAdsStart.show();
    }

    @Override public void destroy() {
        interstitialAdsStart = null;
    }
}
