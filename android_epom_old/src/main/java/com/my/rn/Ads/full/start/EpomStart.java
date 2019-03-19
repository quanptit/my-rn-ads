package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.util.Log;
import com.adclient.android.sdk.listeners.ClientAdListener;
import com.adclient.android.sdk.type.AdType;
import com.adclient.android.sdk.type.ParamsType;
import com.adclient.android.sdk.view.AbstractAdClientView;
import com.adclient.android.sdk.view.AdClientInterstitial;
import com.appsharelib.KeysAds;
import com.my.rn.Ads.IAdLoaderCallback;

import java.util.HashMap;

public class EpomStart extends BaseFullStartAds {
    private AdClientInterstitial interstitial;

    @Override protected String getLogTAG() {
        return "EPOM_START";
    }

    @Override public String getKeyAds() {
        return KeysAds.EPOM_FULL_START;
    }

    @Override protected void adsInitAndLoad(Activity activity, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        interstitial = new AdClientInterstitial(activity);
        HashMap<ParamsType, Object> configuration = new HashMap<ParamsType, Object>();
        configuration.put(ParamsType.AD_PLACEMENT_KEY, getKeyAds());
        configuration.put(ParamsType.ADTYPE, AdType.INTERSTITIAL.toString());
        configuration.put(ParamsType.AD_SERVER_URL, "http://appservestar.com/");
        interstitial.setConfiguration(configuration);
        interstitial.addClientAdListener(new ClientAdListener() {
            @Override
            public void onReceivedAd(AbstractAdClientView adClientView) {
                EpomStart.this.onAdOpened();
            }

            @Override
            public void onFailedToReceiveAd(AbstractAdClientView adClientView) {
                EpomStart.this.onAdFailedToLoad("onFailedToReceiveAd", iAdLoaderCallback);
            }

            @Override
            public void onClickedAd(AbstractAdClientView adClientView) {
                Log.d(getLogTAG(), "--> Ad clicked callback.");
            }

            @Override
            public void onLoadingAd(AbstractAdClientView adClientView, String message) {
                if (interstitial.isAdLoaded()) {
                    EpomStart.this.onAdLoaded();
                    Log.d(getLogTAG(), "message loaded: " + message);
                } else {
                    Log.d(getLogTAG(), "--> Ad not loaded. message: " + message);
                }
            }

            @Override
            public void onClosedAd(AbstractAdClientView adClientView) {
                EpomStart.this.onAdClosed();
            }
        });
        interstitial.load();
    }

    @Override protected void adsShow() throws Exception {
        if (interstitial != null)
            interstitial.show();
    }

    @Override public void destroy() {
        try {
            if (interstitial != null) {
                interstitial.destroy();
                interstitial = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {error.printStackTrace();}
    }


}
