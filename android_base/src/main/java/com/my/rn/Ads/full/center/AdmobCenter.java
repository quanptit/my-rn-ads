package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import com.baseLibs.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.my.rn.Ads.ManagerTypeAdsShow;

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

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_ADMOB_START, null);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.getAdmod_START();
        } else {
            keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_ADMOB_CENTER, null);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.getAdmod_FULL();
        }
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
        interstitialCenter = new InterstitialAd(BaseApplication.getAppContext());
        interstitialCenter.setAdUnitId(keyAds);
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
