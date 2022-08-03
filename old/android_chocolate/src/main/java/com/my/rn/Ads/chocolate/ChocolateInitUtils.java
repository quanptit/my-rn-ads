package com.my.rn.Ads.chocolate;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.my.rn.Ads.ApplicationContainAds;
import com.vdopia.ads.lw.*;

import java.util.ArrayList;

public class ChocolateInitUtils {
    private static final String TAG = "ChocolateInit";
    private boolean chocolateIniting;
    private ArrayList<InitCallback> listMopubInitializationListener = new ArrayList<>();

    public static ChocolateInitUtils getInstance() {
        if (ApplicationContainAds.getInstance().chocolateInitUtils == null)
            ApplicationContainAds.getInstance().chocolateInitUtils = new ChocolateInitUtils();
        return ApplicationContainAds.getInstance().chocolateInitUtils;
    }

    public void initChocolate(Activity activity, final InitCallback listener) {
        if (Chocolate.isInitialized()) {
            if (listener != null)
                listener.onSuccess();
            return;
        }
        if (listener != null) listMopubInitializationListener.add(listener);
        if (chocolateIniting) return;
        chocolateIniting = true;
        Chocolate.init(activity, KeysAds.CHOCO_APP_KEY, new InitCallback() {
            @Override
            public void onSuccess() {
                initComplete();
                    /*
                    If you like, here is a good time to make an ad request OR make a pre-fetch request.
                    But, do NOT do both at same time; too resource-intensive.

                    //Depending on your needs, pre-fetch one of the ad unit types.
                    //LVDOInterstitialAd.prefetch(MainActivity.this, API_KEY, adRequest);
                                 //OR
                    //LVDORewardedAd.prefetch(MainActivity.this, API_KEY, adRequest);

                    //OR make a normal ad request.  For example:
                    //rewardedAd.loadAd(adRequest);
                    */
            }

            @Override
            public void onError(String message) {
                //It's ok.  Our mediation sdk will still work!  Network must be down.
                initComplete();
            }
        });
    }

    private void initComplete() {
        Log.d(TAG, "initComplete");
        chocolateIniting = false;
        if (listMopubInitializationListener != null) {
            for (InitCallback initializationListener : listMopubInitializationListener) {
                initializationListener.onSuccess();
            }
            listMopubInitializationListener.clear();
        }
    }
}
