package com.my.rn.ads.fairbid;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.fyber.FairBid;
import com.fyber.fairbid.ads.ImpressionData;
import com.fyber.fairbid.ads.Interstitial;
import com.fyber.fairbid.ads.interstitial.InterstitialListener;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.full.center.BaseFullCenterAds;

public class FairbidFullCenter extends BaseFullCenterAds implements InterstitialListener {
    private IAdLoaderCallback iAdLoaderCallback;

    @Override protected String getLogTAG() {
        return "FAIRBID_FULL_CENTER";
    }

    @Override public String getKeyAds(boolean isFromStart) {
        return KeysAds.FAIRBID_FULL_CENTER;
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return Interstitial.isAvailable(KeysAds.FAIRBID_FULL_CENTER);
    }

    @Override protected void showAds(Activity activity) {
        Interstitial.show(KeysAds.FAIRBID_FULL_CENTER, activity);
    }

    @Override public void destroyAds() {
    }


    @Override protected void adsInitAndLoad(final Activity activity, final String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        this.iAdLoaderCallback = iAdLoaderCallback;
        if (!FairBid.hasStarted()) {
            FairbidInitUtils.initFairBid(activity);
            BaseApplication.getHandler().postDelayed(new Runnable() {
                @Override public void run() {
                    loadAdsAfterInit(activity, keyAds);
                }
            }, 3000);
        }else
            loadAdsAfterInit(activity, keyAds);
    }

    private void loadAdsAfterInit(Activity activity, String keyAds) {
//        if (true){ //TODOs
//            FairBid.showTestSuite(activity);
//            return;
//        }

        Interstitial.setInterstitialListener(this);
        Interstitial.request(keyAds);
    }

    @Override public void onAvailable(@NonNull String s) {
        onAdLoaded(iAdLoaderCallback);
    }

    @Override public void onUnavailable(@NonNull String s) {
        onAdFailedToLoad(s, iAdLoaderCallback);
        iAdLoaderCallback = null;
    }

    @Override public void onShow(@NonNull String s, @NonNull ImpressionData impressionData) {
        onAdOpened();
    }

    @Override public void onClick(@NonNull String s) {

    }

    @Override public void onHide(@NonNull String s) {
        onAdClosed();
    }

    @Override public void onShowFailure(@NonNull String s, @NonNull ImpressionData impressionData) {
        Log.d(getLogTAG(), "onShowFailure: " + s);
    }

    @Override public void onRequestStart(@NonNull String s) {
        Log.d(getLogTAG(), "onRequestStart: " + s);
    }
}
