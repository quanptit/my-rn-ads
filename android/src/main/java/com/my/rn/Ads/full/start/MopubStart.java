package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.my.rn.Ads.IAdLoaderCallback;

public class MopubStart extends BaseFullStartAds {
    private MoPubInterstitial interstitialAdsStart;

    @Override protected String getLogTAG() {
        return "MOPUB_START";
    }

    @Override public String getKeyAds() {
        return KeysAds.getMOPUB_FULL_START();
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAdsStart = new MoPubInterstitial(activity, getKeyAds());
        interstitialAdsStart.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {

            @Override public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                MopubStart.this.onAdLoaded();
            }

            @Override public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                MopubStart.this.onAdFailedToLoad(errorCode.toString(), iAdLoaderCallback);
            }

            @Override public void onInterstitialShown(MoPubInterstitial interstitial) {
                MopubStart.this.onAdOpened();
            }

            @Override public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                MopubStart.this.onAdClosed();
            }

            //region Hide
            @Override public void onInterstitialClicked(MoPubInterstitial interstitial) {

            }
            //endregion
        });

        interstitialAdsStart.load();
    }

    @Override protected void adsShow() throws Exception {
        if (interstitialAdsStart != null)
            interstitialAdsStart.show();
    }

    @Override public void destroy() {
        try {
            if (interstitialAdsStart != null) {
                interstitialAdsStart.destroy();
                interstitialAdsStart = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {error.printStackTrace();}
    }


}
