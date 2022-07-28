package com.my.rn.ads.fb;

import android.app.Activity;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;

import com.my.rn.ads.IAdLoaderCallback;
import com.baseLibs.BaseApplication;
import com.my.rn.ads.full.center.BaseFullCenterAds;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class FbCenter extends BaseFullCenterAds {
    public static boolean IS_LOADER = false;

    public FbCenter() {
    }

    @Override protected String getLogTAG() {
        return "UNITY_CENTER";
    }

    @Override public boolean isCachedCenter(Activity activity) {
        return IS_LOADER;
    }

    @Override protected void showAds(Activity activity) {
        IS_LOADER = false;
        UnityAds.show(activity, getKeyAds(false), new UnityAdsShowOptions(),
                new IUnityAdsShowListener() {
                    @Override public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        L.d("onUnityAdsShowFailure");
                        onAdClosed();
                    }

                    @Override public void onUnityAdsShowStart(String placementId) {
                        L.d("onUnityAdsShowStart");
                        IS_LOADER = false;
                        onAdOpened();
                    }

                    @Override public void onUnityAdsShowClick(String placementId) {

                    }

                    @Override public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        L.d("onUnityAdsShowComplete: "+state.name());
                        onAdClosed();
                    }
                });
    }

    @Override public String getKeyAds(boolean isFromStart) {
        return KeysAds.UNITY_FULL;
    }

    private void _loadAds(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) {
        UnityAds.load(keyAds, new IUnityAdsLoadListener() {
            @Override public void onUnityAdsAdLoaded(String placementId) {
                IS_LOADER = true;
                FbCenter.this.onAdLoaded(iAdLoaderCallback);
            }

            @Override public void onUnityAdsFailedToLoad(String placementId,
                                                         UnityAds.UnityAdsLoadError error, String message) {
                IS_LOADER = false;
                onAdFailedToLoad(message, iAdLoaderCallback);
            }
        });
    }

    @Override protected void adsInitAndLoad(Activity activity, String keyAds, final IAdLoaderCallback iAdLoaderCallback) throws Exception {
        if (!UnityAds.isInitialized()) {
            UnityAds.initialize(BaseApplication.getAppContext(), KeysAds.UNITY_GAME_ID,
                    KeysAds.isNeedShowTestAds(), new IUnityAdsInitializationListener() {
                        @Override public void onInitializationComplete() {
                            _loadAds(activity, keyAds, iAdLoaderCallback);
                        }

                        @Override public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                            L.d("UnityAds onInitializationFailed: " + message);
                            iAdLoaderCallback.onAdsFailedToLoad();
                        }
                    });
        }
    }

    @Override public void destroyAds() {
    }

}
