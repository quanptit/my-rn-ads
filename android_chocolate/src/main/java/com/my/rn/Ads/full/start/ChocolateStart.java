package com.my.rn.Ads.full.start;

import android.app.Activity;
import com.appsharelib.KeysAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.chocolate.ChocolateFullManager;
import com.vdopia.ads.lw.LVDOConstants;
import com.vdopia.ads.lw.LVDOInterstitialAd;
import com.vdopia.ads.lw.LVDOInterstitialListener;

public class ChocolateStart extends BaseFullStartAds{

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        ChocolateFullManager.getInstance().setListenner(new LVDOInterstitialListener() {
            @Override public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateStart.this.onAdLoaded();
            }

            @Override public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
                ChocolateStart.this.onAdFailedToLoad(lvdoErrorCode.toString(), iAdLoaderCallback);
            }

            @Override public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateStart.this.onAdOpened();
            }

            @Override public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {

            }

            @Override public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {
                ChocolateStart.this.onAdClosed();
            }
        });
        ChocolateFullManager.getInstance().loadAds(activity);
    }

    @Override protected void adsShow() throws Exception {
        ChocolateFullManager.getInstance().showAdsIfCache();
    }

    @Override public void destroy() {
    }

    @Override public String getKeyAds() {
        return KeysAds.CHOCO_APP_KEY;
    }

    @Override protected String getLogTAG() {
        return "CHOCO_START";
    }
}
