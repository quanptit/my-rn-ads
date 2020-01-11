package com.my.rn.ads.mopub;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.my.rn.ads.settings.AdsSetting;

public class MopubFullCenter extends BaseFullCenterAds {
    private MoPubInterstitial interstitialCenter;

    @Override protected String getLogTAG() {
        return "MOPUB_CENTER";
    }

    @Override public String getKeyAds(boolean isFromStart) {
        Log.d(getLogTAG(), "getKeyAds isFromStart: " + isFromStart);
        if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_FULL_CENTER != null)
            return "24534e1901884e398f1253216226017e";
        String keySave;
        if (isFromStart) {
            keySave = AdsSetting.getStartKey(AdsSetting.ID_MOPUB);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.MOPUB_FULL_START;
        } else {
            keySave = AdsSetting.getCenterKey(AdsSetting.ID_MOPUB);
            if (!TextUtils.isEmpty(keySave))
                return keySave;
            return KeysAds.MOPUB_FULL_CENTER;
        }
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return (interstitialCenter != null && interstitialCenter.isReady());
    }

    @Override protected void showAds(Activity activity) {
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
        MopubInitUtils.getInstance().initAds(activity, new IAdInitCallback() {
            @Override public void didInitialise() {
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

            @Override public void didFailToInitialise() {

            }
        });
    }
}
