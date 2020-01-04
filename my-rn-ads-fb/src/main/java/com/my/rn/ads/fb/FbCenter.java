package com.my.rn.ads.fb;

import android.app.Activity;
import android.text.TextUtils;

import com.appsharelib.KeysAds;
import com.facebook.ads.*;

import com.my.rn.ads.IAdLoaderCallback;
import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.my.rn.ads.settings.AdsSetting;

public class FbCenter extends BaseFullCenterAds {
    public static boolean IS_LOADER = false;
    private InterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "FB_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return interstitialCenter != null && interstitialCenter.isAdLoaded();
    }

    @Override protected void showAds(Activity activity) {
        IS_LOADER = false;
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_FB);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.FB_FULL_START;
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_FB);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.FB_FULL_ADS;
        }
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        boolean isInitialized = AudienceNetworkAds.isInitialized(BaseApplication.getAppContext());
        if (!isInitialized)
            AudienceNetworkAds.initialize(BaseApplication.getAppContext());

        interstitialCenter = new InterstitialAd(BaseApplication.getAppContext(), keyAds);
        interstitialCenter.setAdListener(new InterstitialAdListener() {
            @Override public void onAdLoaded(Ad ad) {
                IS_LOADER = true;
                FbCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override public void onError(Ad ad, AdError adError) {
                IS_LOADER = false;
                onAdFailedToLoad(adError.getErrorMessage(), iAdLoaderCallback);
            }

            @Override public void onInterstitialDisplayed(Ad ad) {
                IS_LOADER = false;
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

    @Override public void destroyAds() {
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
