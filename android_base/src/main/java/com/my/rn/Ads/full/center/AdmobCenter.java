package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.baseLibs.utils.L;
import com.baseLibs.utils.PreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.my.rn.Ads.settings.AdsSetting;

public class AdmobCenter extends BaseFullCenterAds {
    private InterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "ADMOB_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null && interstitialCenter.isLoaded());
    }

    @Override protected void showAds(Activity activity) {
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_ADMOB);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.Admod_START;
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_ADMOB);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.Admod_FULL;
        }
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
        Log.d(getLogTAG(), "adsInitAndLoad: " + keyAds);
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
