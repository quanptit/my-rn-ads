package com.my.rn.ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsharelib.KeysAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.settings.AdsSetting;

import java.util.Arrays;
import java.util.List;

public class AdmobCenter extends BaseFullCenterAds {
    private static final String TAG = "AdmobCenter";
    boolean isInited = false;
    private InterstitialAd interstitialCenter;

    public AdmobCenter(){
        List<String> testDeviceIds = Arrays.asList(KeysAds.DEVICE_TESTS);
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    @Override protected String getLogTAG() {
        return "ADMOB_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null);
    }

    @Override protected void showAds(Activity activity) {
        interstitialCenter.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                AdmobCenter.this.onAdClosed();
                interstitialCenter = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                interstitialCenter = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
                interstitialCenter = null;
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
                AdmobCenter.this.onAdOpened();
                interstitialCenter = null;
            }
        });
        interstitialCenter.show(activity);
    }

    @Override public String getKeyAds(boolean isFromStart) {
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_ADMOB);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.Admod_FULL();
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_ADMOB);
            if (!TextUtils.isEmpty(keySave)) return keySave;
            return KeysAds.Admod_FULL();
        }
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (interstitialCenter != null) return;
        if (!isInited) {
            MobileAds.initialize(activity);
            isInited = true;
        }
        InterstitialAd.load(activity, keyAds, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitialCenter = interstitialAd;
                AdmobCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                AdmobCenter.this.onAdFailedToLoad("errorCode: " + loadAdError.getMessage(), iAdLoaderCallback);
            }
        });
    }

    @Override protected void destroyAds() {
        interstitialCenter = null;
    }
}
