package com.my.rn.ads.utils;

import com.facebook.react.bridge.Promise;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdLoaderCallback;

public class ManagerLoadNativeAds {

    public void loadNativeAds(int typeAds, final Promise promise) {
        new Thread(new Runnable() {
            @Override public void run() {
                new IAdLoaderCallback() {
                    @Override public void onAdsLoaded() {

                    }

                    @Override public void onAdsFailedToLoad() {

                    }
                };
                try {

                    promise.resolve(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject("0", e.getMessage());
                }
            }
        }).start();
    }

    //** Đảm bảo */
    public static void registerCallback(IAdLoaderCallback loaderCallback) {

    }

    public static void dispatchACallbackNativeLoad(boolean isFailToLoad) {

    }
}
