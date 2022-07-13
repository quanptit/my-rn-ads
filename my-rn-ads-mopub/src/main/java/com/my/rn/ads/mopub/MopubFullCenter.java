package com.my.rn.ads.mopub;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.appsharelib.KeysAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.my.rn.ads.settings.AdsSetting;

public class MopubFullCenter extends BaseFullCenterAds {
    private MaxInterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "MAX_CENTER";
    }

    @Override public String getKeyAds(boolean isFromStart) {
        Log.d(getLogTAG(), "getKeyAds isFromStart: " + isFromStart);
        String keySave;
        keySave = AdsSetting.getCenterKey(AdsSetting.ID_MOPUB);
        if (!TextUtils.isEmpty(keySave))
            return keySave;
        return KeysAds.MAX_FULL;
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null && interstitialCenter.isReady());
    }

    @Override protected void showAds(Activity activity) {
        interstitialCenter.showAd();
    }

    @Override public void destroyAds() {
        try {
            if (interstitialCenter != null) {
                interstitialCenter.destroy();
                interstitialCenter = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void adsInitAndLoad(final Activity activity, final String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        MopubInitUtils.getInstance().initAds(activity, new IAdInitCallback() {
            @Override public void didInitialise() {
                interstitialCenter = new MaxInterstitialAd(keyAds, activity);
                interstitialCenter.setListener(new MaxAdListener() {
                    @Override public void onAdLoaded(MaxAd ad) {
                        MopubFullCenter.this.onAdLoaded(iAdLoaderCallback);
                    }

                    @Override public void onAdDisplayed(MaxAd ad) {
                        onAdOpened();
                    }

                    @Override public void onAdHidden(MaxAd ad) {
                        onAdClosed();
                    }

                    @Override public void onAdClicked(MaxAd ad) {

                    }

                    @Override public void onAdLoadFailed(String adUnitId, MaxError error) {
                        String errorMesg = error!=null?error.getMessage():null;
                        onAdFailedToLoad(errorMesg, iAdLoaderCallback);
                    }

                    @Override public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
                interstitialCenter.loadAd();
            }
            @Override public void didFailToInitialise() {
            }
        });

//        AppLovinSdk.getInstance(activity).showMediationDebugger();
    }
}
