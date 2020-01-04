package com.my.rn.ads.tapdaq;

import android.app.Activity;

import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

public class TapdaqFullCenter extends BaseFullCenterAds {
    private IAdLoaderCallback iAdLoaderCallback;

    @Override protected String getLogTAG() {
        return "TAPDAQ_FULL_CENTER";
    }

    private TMAdListener tmAdListenerFinal = new TMAdListener() {
        @Override public void didLoad() {
            onAdLoaded(iAdLoaderCallback);
            iAdLoaderCallback = null;
        }

        @Override public void didFailToLoad(TMAdError error) {
            onAdFailedToLoad(error.toString(), iAdLoaderCallback);
            iAdLoaderCallback = null;
        }

        @Override public void didClose() {
            onAdClosed();
        }

        @Override public void didDisplay() {
            onAdOpened();
        }
    };

    @Override public String getKeyAds(boolean isFromStart) {
        return "default";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return Tapdaq.getInstance().isInterstitialReady(activity, getKeyAds(false));
    }

    @Override protected void showAds(Activity activity) {
        if (Tapdaq.getInstance().isInterstitialReady(activity, getKeyAds(false)))
            Tapdaq.getInstance().showInterstitial(activity, tmAdListenerFinal);
    }

    @Override public void destroyAds() {
    }


    @Override protected void adsInitAndLoad(final Activity activity, final String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        this.iAdLoaderCallback = iAdLoaderCallback;
        BaseApplicationContainAds.getInstance().getIAdInitUtils()
                .initAds(activity, new IAdInitCallback() {
                    @Override public void didInitialise() {
                        if (!Tapdaq.getInstance().isInterstitialReady(activity, getKeyAds(false)))
                            Tapdaq.getInstance().loadInterstitial(activity, tmAdListenerFinal);
                    }

                    @Override public void didFailToInitialise() {
                        onAdFailedToLoad("didFailToInitialise", iAdLoaderCallback);
                    }
                });
    }
}
