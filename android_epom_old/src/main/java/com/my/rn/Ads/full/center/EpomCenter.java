package com.my.rn.Ads.full.center;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.adclient.android.sdk.listeners.ClientAdListener;
import com.adclient.android.sdk.type.AdType;
import com.adclient.android.sdk.type.ParamsType;
import com.adclient.android.sdk.view.AbstractAdClientView;
import com.adclient.android.sdk.view.AdClientInterstitial;
import com.appsharelib.KeysAds;
import com.my.rn.Ads.IAdLoaderCallback;

import java.util.HashMap;

public class EpomCenter extends BaseFullCenterAds {
    private AdClientInterstitial interstitial;

    @Override protected String getLogTAG() {
        return "EPOM_CENTER";
    }

    @Override public boolean isCachedCenter() {
        return interstitial != null && interstitial.isAdLoaded();
    }

    @Override protected void showAds() {
        interstitial.show();
    }

    @Override protected boolean isSkipThisAds() {
        return TextUtils.isEmpty(KeysAds.EPOM_FULL_ADS);
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitial = new AdClientInterstitial(activity);
        HashMap<ParamsType, Object> configuration = new HashMap<ParamsType, Object>();
        configuration.put(ParamsType.AD_PLACEMENT_KEY, KeysAds.EPOM_FULL_ADS);
        configuration.put(ParamsType.ADTYPE, AdType.INTERSTITIAL.toString());
        configuration.put(ParamsType.AD_SERVER_URL, "http://appservestar.com/");
        interstitial.setConfiguration(configuration);
        interstitial.addClientAdListener(new ClientAdListener() {
            @Override
            public void onReceivedAd(AbstractAdClientView adClientView) {
                EpomCenter.this.onAdOpened();
            }

            @Override
            public void onFailedToReceiveAd(AbstractAdClientView adClientView) {
                EpomCenter.this.onAdFailedToLoad("onFailedToReceiveAd", iAdLoaderCallback);
            }

            @Override
            public void onClickedAd(AbstractAdClientView adClientView) {
                Log.d(getLogTAG(), "--> Ad clicked callback.");
            }

            @Override
            public void onLoadingAd(AbstractAdClientView adClientView, String message) {
                if (interstitial.isAdLoaded()) {
                    EpomCenter.this.onAdLoaded(iAdLoaderCallback);
                    Log.d(getLogTAG(), "message loaded: " + message);
                } else {
                    Log.d(getLogTAG(), "--> Ad not loaded. message: " + message);
                }
            }

            @Override
            public void onClosedAd(AbstractAdClientView adClientView) {
                EpomCenter.this.onAdClosed();
            }
        });
        interstitial.load();
    }

    @Override protected void destroyAds() {
        try {
            if (interstitial != null) {
                interstitial.destroy();
                interstitial = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e1) {e1.printStackTrace();}
    }

}
