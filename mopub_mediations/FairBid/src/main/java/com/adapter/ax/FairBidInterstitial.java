package com.adapter.ax;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.InterstitialAd;
import com.my.rn.Ads.full.center.PromiseSaveObj;

public class FairBidInterstitial {
    private static final String TAG = "FAIRBID_FULL";
    private PromiseSaveObj promiseSaveObj;

    public void loadInterstitial(Activity activity) throws Exception {
        if (!HeyzapAds.hasStarted()){
            HeyzapAds.start(KeysAds.FAIR_BID, activity, HeyzapAds.DISABLE_AUTOMATIC_FETCH);
            HeyzapAds.setThirdPartyVerboseLogging(true);
        }
        if (InterstitialAd.isAvailable()) {
            return;
        }
        Log.d(TAG, "FairBidInterstitial fetch");
        InterstitialAd.setOnStatusListener(defaultListenner);
        InterstitialAd.fetch();
    }

    public boolean isCached() {
        return InterstitialAd.isAvailable();
    }

    public boolean showIfCache(Activity activity, PromiseSaveObj promiseSaveObj) throws Exception {
        if (InterstitialAd.isAvailable()) {
            this.promiseSaveObj = promiseSaveObj;
            InterstitialAd.display(activity);
            return true;
        }
        return false;
    }

    //region utils
    private final HeyzapAds.OnStatusListener defaultListenner = new HeyzapAds.OnStatusListener() {
        @Override
        public void onShow(String tag) {
            try {
                if (promiseSaveObj != null) {
                    promiseSaveObj.resolve(true);
                    promiseSaveObj = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS, System.currentTimeMillis());
            Log.d(TAG, "onShow: " + tag);
        }

        @Override
        public void onClick(String tag) {
            Log.d(TAG, "onClick: " + tag);
        }

        @Override
        public void onHide(String tag) {
            Log.d(TAG, "onHide: " + tag);
        }

        @Override
        public void onFailedToShow(String tag) {
            Log.d(TAG, "onFailedToShow: " + tag);
        }

        @Override
        public void onAvailable(String tag) {
            Log.d(TAG, "onAvailable: " + tag);
        }

        @Override
        public void onFailedToFetch(String tag) {
            Log.d(TAG, "onFailedToFetch: " + tag);
        }

        @Override
        public void onAudioStarted() {
            // The ad about to be shown will require audio. Any background audio should be muted.
        }

        @Override
        public void onAudioFinished() {
            // The ad being shown no longer requires audio. Any background audio can be resumed.
        }
    };
    //endregion
}
