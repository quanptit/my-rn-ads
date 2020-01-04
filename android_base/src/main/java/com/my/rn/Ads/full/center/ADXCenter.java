package com.my.rn.ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.settings.AdsSetting;

public class ADXCenter extends BaseFullCenterAds {
    private PublisherInterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "ADX_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null && interstitialCenter.isLoaded());
    }

    @Override protected void showAds(Activity activity) {
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        if (KeysAds.IS_DEVELOPMENT)
            return "ca-app-pub-3940256099942544/1033173712";
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_ADX);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.ADX_FULL_START;
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_ADX);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.ADX_FULL_CENTER;
        }
    }


    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
        Log.d(getLogTAG(), "adsInitAndLoad: "+keyAds);
        interstitialCenter = new PublisherInterstitialAd(BaseApplication.getAppContext());
        interstitialCenter.setAdUnitId(keyAds);
        interstitialCenter.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                ADXCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                ADXCenter.this.onAdFailedToLoad("errorCode: " + errorCode, iAdLoaderCallback);
            }

            @Override
            public void onAdClosed() {
                ADXCenter.this.onAdClosed();
            }

            @Override public void onAdOpened() {
                ADXCenter.this.onAdOpened();
            }
        });
        PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
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
