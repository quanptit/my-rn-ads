package com.my.rn.Ads.full.center;

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

public class ADXCenter extends BaseFullCenterAds {
    private PublisherInterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "ADX_CENTER";
    }

    public boolean isCachedCenter() {
        return (interstitialCenter != null && interstitialCenter.isLoaded());
    }

    @Override protected void showAds() {
        interstitialCenter.show();
    }

    @Override public String getKeyAds() {
        String keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_ADX_CENTER, null);
        if (!TextUtils.isEmpty(keySave))
            return keySave;
        return KeysAds.getAdxFullCenter();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null && interstitialCenter.isLoading()) return;
        interstitialCenter = new PublisherInterstitialAd(BaseApplication.getAppContext());
        interstitialCenter.setAdUnitId(KeysAds.getAdxFullCenter());
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
