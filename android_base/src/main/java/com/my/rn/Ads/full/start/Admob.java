package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;

public class Admob extends BaseFullStartAds {
    private InterstitialAd interstitialAds;

    @Override public String getLogTAG() {
        return "ADMOB_START";
    }

    @Override protected boolean isCached() {
        return interstitialAds != null && interstitialAds.isLoaded();
    }

    @Override
    public void destroyAds() {
        interstitialAds = null;
    }

    @Override public String getKeyAds() {
        String keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_ADMOB_START, null);
        if (!TextUtils.isEmpty(keySave))
            return keySave;
        return KeysAds.getAdmod_START();
    }


    @Override protected void showAds() throws Exception {
        if (interstitialAds != null)
            interstitialAds.show();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAds = new InterstitialAd(BaseApplication.getAppContext());
        interstitialAds.setAdUnitId(getKeyAds());
        interstitialAds.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Admob.this.onAdLoaded(iAdLoaderCallback);
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
