package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.IAdLoaderCallback;

public class MopubFullCenter extends BaseFullCenterAds {
    private MoPubInterstitial interstitialCenter;

    @Override protected String getLogTAG() {
        return "MOPUB_CENTER";
    }

    @Override public String getKeyAds(boolean isFromStart) {
        Log.d(getLogTAG(), "getKeyAds isFromStart: " + isFromStart);
        if (isFromStart)
            return KeysAds.getMOPUB_FULL_START();
        return KeysAds.getMOPUB_FULL_CENTER();
    }

    @Override public boolean isCachedCenter() {
        return (interstitialCenter != null && interstitialCenter.isReady());
    }

    @Override protected void showAds() {
        interstitialCenter.show();
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
        ApplicationContainAds.getMopubInitUtils().initMopub(new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                interstitialCenter = new MoPubInterstitial(activity, keyAds);
                interstitialCenter.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                    @Override public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                        onAdLoaded(iAdLoaderCallback);
                    }

                    @Override public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                        onAdFailedToLoad(errorCode.toString(), iAdLoaderCallback);
                    }

                    @Override public void onInterstitialShown(MoPubInterstitial interstitial) {
                        onAdOpened();
                    }

                    @Override public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                        onAdClosed();
                    }

                    //region hide
                    @Override public void onInterstitialClicked(MoPubInterstitial interstitial) {

                    }
                    //endregion
                });
                interstitialCenter.load();
            }
        });
    }
}
