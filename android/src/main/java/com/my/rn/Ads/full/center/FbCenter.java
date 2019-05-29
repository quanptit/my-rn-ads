package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.baseLibs.utils.PreferenceUtils;
import com.baseLibs.BaseApplication;

public class FbCenter extends BaseFullCenterAds {
    private InterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "FB_CENTER";
    }

    @Override public boolean isCachedCenter() {
        return interstitialCenter != null && interstitialCenter.isAdLoaded();
    }

    @Override protected void showAds() {
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_FB_START, null);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.FB_FULL_START;
        } else {
            keySave = PreferenceUtils.getStringSetting(ManagerTypeAdsShow.KEY_FB_CENTER, null);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.FB_FULL_ADS;
        }
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialCenter = new InterstitialAd(BaseApplication.getAppContext(), keyAds);
        interstitialCenter.setAdListener(new InterstitialAdListener() {
            @Override public void onAdLoaded(Ad ad) {
                FbCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override public void onError(Ad ad, AdError adError) {
                onAdFailedToLoad(adError.getErrorMessage(), iAdLoaderCallback);
            }

            @Override public void onInterstitialDisplayed(Ad ad) {
                onAdOpened();
            }

            @Override public void onInterstitialDismissed(Ad ad) {
                onAdClosed();
            }

            @Override public void onAdClicked(Ad ad) {

            }

            @Override public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialCenter.loadAd();
    }

    @Override protected void destroyAds() {
        try {
            if (interstitialCenter != null) {
                interstitialCenter.destroy();
                interstitialCenter = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e1) {e1.printStackTrace();}
    }

}
