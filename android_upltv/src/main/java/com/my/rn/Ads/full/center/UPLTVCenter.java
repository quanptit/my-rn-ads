package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.util.Log;
import com.my.rn.Ads.IAdLoaderCallback;
import com.up.ads.UPAdsSdk;
import com.up.ads.UPInterstitialAd;
import com.up.ads.wrapper.interstitial.UPInterstitialAdListener;
import com.up.ads.wrapper.interstitial.UPInterstitialLoadCallback;

public class UPLTVCenter extends BaseFullCenterAds {
    private UPInterstitialAd interstitialCenter;

    @Override protected String getLogTAG() {
        return "UPLTV_CENTER";
    }

    @Override public boolean isCachedCenter() {
        return interstitialCenter != null && interstitialCenter.isReady();
    }

    @Override protected void showAds() {
        interstitialCenter.show();
    }

    @Override public String getKeyAds() {
        return "full_center";
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (!UPAdsSdk.isInited()) {
            Log.d(getLogTAG(), "UPAdsSdk init");
            UPAdsSdk.init(activity, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
        }

        interstitialCenter = new UPInterstitialAd(activity, getKeyAds());
        interstitialCenter.setUpInterstitialAdListener(new UPInterstitialAdListener() {
            @Override public void onClicked() {
            }

            @Override public void onClosed() {
                UPLTVCenter.this.onAdClosed();
            }

            @Override public void onDisplayed() {
                UPLTVCenter.this.onAdOpened();
            }
        });
        interstitialCenter.load(new UPInterstitialLoadCallback() {
            @Override public void onLoadFailed(String s) {
                UPLTVCenter.this.onAdFailedToLoad(s, iAdLoaderCallback);
            }

            @Override public void onLoadSuccessed(String s) {
                Log.d(getLogTAG(), "onLoadSuccessed: " + s);
                UPLTVCenter.this.onAdLoaded(iAdLoaderCallback);
            }
        });
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
