package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;

public class AdxStart extends BaseFullStartAds {
    private PublisherInterstitialAd interstitialAdsStart;

    @Override protected String getLogTAG() {
        return "ADX-START";
    }

    @Override public String getKeyAds() {
        String keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_ADX_START, null);
        if (!TextUtils.isEmpty(keySave))
            return keySave;
        return KeysAds.getAdxFullStart();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAdsStart = new PublisherInterstitialAd(BaseApplication.getAppContext());
        interstitialAdsStart.setAdUnitId(getKeyAds());
        interstitialAdsStart.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdxStart.this.onAdLoaded(iAdLoaderCallback);
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

    @Override protected boolean isCached() {
        return interstitialAdsStart != null && interstitialAdsStart.isLoaded();
    }

    @Override protected void showAds() throws Exception {
        if (interstitialAdsStart != null)
            interstitialAdsStart.show();
    }

    @Override public void destroyAds() {
        interstitialAdsStart = null;
    }
}
