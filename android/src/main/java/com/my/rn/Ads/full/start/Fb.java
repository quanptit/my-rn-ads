package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.my.rn.Ads.IAdLoaderCallback;

public class Fb extends BaseFullStartAds {
    private InterstitialAd interstitialAdsStart;

    @Override protected String getLogTAG() {
        return "FB_START";
    }

    @Override protected boolean isSkipThisAds() {
        return TextUtils.isEmpty(KeysAds.FB_FULL_START);
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitialAdsStart = new InterstitialAd(BaseApplication.getAppContext(), KeysAds.FB_FULL_START);
        interstitialAdsStart.setAdListener(new InterstitialAdListener() {
            @Override public void onAdLoaded(Ad ad) {
                Fb.this.onAdLoaded();
            }

            @Override public void onInterstitialDisplayed(Ad ad) {
                Fb.this.onAdOpened();
            }

            @Override public void onInterstitialDismissed(Ad ad) {
                Fb.this.onAdClosed();
            }

            @Override public void onError(Ad ad, AdError adError) {
                Fb.this.onAdFailedToLoad(adError.getErrorMessage(), iAdLoaderCallback);
            }

            //region hide
            @Override public void onAdClicked(Ad ad) {
            }

            @Override public void onLoggingImpression(Ad ad) {
            }
            //endregion
        });
        interstitialAdsStart.loadAd();
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
