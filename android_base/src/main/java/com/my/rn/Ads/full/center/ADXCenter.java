package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.settings.AdsSetting;

public class ADXCenter extends BaseFullCenterAds {
    private PublisherInterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "ADX_CENTER";
    }

    @Override protected boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null && interstitialCenter.isLoaded());
    }

    @Override protected void showAds(Activity activity) {
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_ADX);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.getAdxFullStart();
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_ADX);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.getAdxFullCenter();
        }
    }


    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
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
