package com.my.rn.ads.fb;

import android.app.Activity;
import android.text.TextUtils;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.DeviceTestID;
import com.facebook.ads.*;

import com.my.rn.ads.IAdLoaderCallback;
import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.my.rn.ads.settings.AdsSetting;

import java.util.Arrays;

public class FbCenter extends BaseFullCenterAds {
    public static boolean IS_LOADER = false;
    private InterstitialAd interstitialCenter;

    public FbCenter() {
    }

    @Override protected String getLogTAG() {
        return "FB_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return interstitialCenter != null && interstitialCenter.isAdLoaded();
    }

    @Override protected void showAds(Activity activity) {
        IS_LOADER = false;
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialCenter.isAdInvalidated()) {
            return;
        }
        interstitialCenter.show();
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_FB);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.FB_FULL_ADS;
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
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
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
        };
        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialCenter.loadAd(
                interstitialCenter.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
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
