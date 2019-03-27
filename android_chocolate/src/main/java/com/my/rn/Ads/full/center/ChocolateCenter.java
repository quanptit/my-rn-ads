package com.my.rn.Ads.full.center;

import android.app.Activity;
import com.appsharelib.KeysAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.chocolate.ChocolateFullManager;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.LVDOInterstitialAd;
import com.vdopia.ads.lw.LVDOInterstitialListener;

public class ChocolateCenter extends BaseFullCenterAds {
    @Override protected String getLogTAG() {
        return "CHOCO_CENTER";
    }

    @Override public boolean isCachedCenter() {
        return ChocolateFullManager.getInstance().isCache();
    }

    @Override protected void showAds() {
        ChocolateFullManager.getInstance().showAdsIfCache();
    }

    @Override public String getKeyAds() {
        return KeysAds.CHOCO_APP_KEY;
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        ChocolateFullManager.getInstance().setListenner(new LVDOInterstitialListener() {
            @Override public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
                ChocolateCenter.this.onAdFailedToLoad(lvdoErrorCode.toString(), iAdLoaderCallback);
            }

            @Override public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateCenter.this.onAdOpened();
            }

            @Override public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {

            }

            @Override public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateCenter.this.onAdClosed();
            }
        });
        ChocolateFullManager.getInstance().loadAds(activity);
    }

    @Override protected void destroyAds() {
    }

}
