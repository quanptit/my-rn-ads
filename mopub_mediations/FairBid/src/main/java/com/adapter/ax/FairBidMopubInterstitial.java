//package com.adapter.ax;
//
//import android.app.Activity;
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import com.appsharelib.KeysAds;
//import com.baseLibs.BaseApplication;
//import com.baseLibs.utils.PreferenceUtils;
//import com.heyzap.sdk.ads.HeyzapAds;
//import com.heyzap.sdk.ads.InterstitialAd;
//import com.mopub.mobileads.CustomEventInterstitial;
//import com.mopub.mobileads.MoPubErrorCode;
//
//import java.util.Map;
//
///**
// * Setting Trong mopub như sau:
// * CUSTOM EVENT CLASS: com.adapter.ax.ADXMopubAdapterInterstitial
// * CUSTOM EVENT CLASS DATA: {"AdUnitId": "This is AD_UNIT_ID for full screen"};  ex test: {"AdUnitId": "/21617015150/47909961/21720491886"}
// */
//public class FairBidMopubInterstitial extends CustomEventInterstitial implements HeyzapAds.OnStatusListener {
//    private static final String TAG = "M_FAIRBID-FULL";
//    private Activity activity;
//    private CustomEventInterstitialListener listener;
//
//    @Override
//    protected void loadInterstitial(Context context,
//                                    final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
//                                    final Map<String, String> serverExtras) {
//        if (!(context instanceof Activity)) {
//            Log.d(TAG, "context not instanceof Activity");
//            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
//            return;
//        }
//        this.activity = (Activity) context;
//        this.listener = customEventInterstitialListener;
//
//        Log.d(TAG, "loadInterstitial");
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                try {
//                    FairBidInterstitial.loadInterstitial(activity, FairBidMopubInterstitial.this);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onInvalidate() {
//        Log.d(TAG, "onInvalidate");
//        FairBidInterstitial.setDefaultListenner();
//        this.activity = null;
//        this.listener = null;
//
//    }
//
//    @Override
//    protected void showInterstitial() {
//        Log.d(TAG, "showInterstitial");
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                try {
//                    if (activity != null)
//                        FairBidInterstitial.showIfCache(activity);
//                    else{
//                        FairBidInterstitial.setDefaultListenner();
//                        if (listener != null){
//                            listener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
//                            listener = null;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    //region Ads Event
//    @Override public void onShow(String s) {
//        Log.d(TAG, "onShow");
//        PreferenceUtils.saveLongSetting(KeysAds.LAST_TIME_SHOW_ADS, System.currentTimeMillis());
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null)
//                    listener.onInterstitialShown();
//            }
//        });
//    }
//
//    @Override public void onClick(String s) {
//        Log.d(TAG, "On click");
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null)
//                    listener.onInterstitialClicked();
//            }
//        });
//    }
//
//    @Override public void onHide(String s) {
//        Log.d(TAG, "onHide");
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null) {
//                    listener.onInterstitialDismissed();
//                    listener = null;
//                    FairBidInterstitial.setDefaultListenner();
//                }
//            }
//        });
//    }
//
//    @Override public void onFailedToShow(String s) {
//        Log.d(TAG, "onFailedToShow");
//        FairBidInterstitial.setDefaultListenner();
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null){
//                    listener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
//                    listener = null;
//                }
//            }
//        });
//    }
//
//    @Override public void onAvailable(String s) {
//        Log.d(TAG, "onAvailable");
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null)
//                    listener.onInterstitialLoaded();
//            }
//        });
//    }
//
//    @Override public void onFailedToFetch(String s) {
//        Log.d(TAG, "onFailedToFetch");
//        FairBidInterstitial.setDefaultListenner();
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                if (listener != null){
//                    listener.onInterstitialFailed(MoPubErrorCode.NO_FILL);
//                    listener = null;
//                }
//            }
//        });
//    }
//
//    @Override public void onAudioStarted() {
//
//    }
//
//    @Override public void onAudioFinished() {
//
//    }
//    //endregion
//}