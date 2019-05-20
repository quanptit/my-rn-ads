package com.adapter.ax;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;
import com.google.android.gms.ads.AdListener;

import java.util.Map;

/**
 * Setting Trong mopub như sau:
 * CUSTOM EVENT CLASS: com.adapter.ax.ADXMopubAdapterInterstitial
 * CUSTOM EVENT CLASS DATA: {"AdUnitId": "This is AD_UNIT_ID for full screen"};  ex test: {"AdUnitId": "/21617015150/47909961/21720491886"}
 */
public class ADXMopubAdapterInterstitial extends CustomEventInterstitial {

    private static final String TAG = "ADX-MOPUB-FULL";
    private PublisherInterstitialAd interstitial;

    public void destroy() {
        interstitial = null;
    }


    @Override
    protected void loadInterstitial(Context context,
                                    final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras,
                                    final Map<String, String> serverExtras) {
        Log.d(TAG, "loadInterstitial");
        if (interstitial == null) {
            interstitial = new PublisherInterstitialAd(BaseApplication.getAppContext());
            String adUnitId = serverExtras.get("AdUnitId");
            if (TextUtils.isEmpty(adUnitId)) {
                customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.MISSING_AD_UNIT_ID);
                return;
            }
            interstitial.setAdUnitId(adUnitId);
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.d(TAG, "onAdFailedToLoad: " + errorCode);
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override public void run() {
                            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.NO_FILL);
                        }
                    });
                    destroy();
                }

                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "onAdLoaded");
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            customEventInterstitialListener.onInterstitialLoaded();
                        }
                    });
                }

                @Override public void onAdClosed() {
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            customEventInterstitialListener.onInterstitialDismissed();
                        }
                    });
                    destroy();
                }

                @Override public void onAdOpened() {
                    Log.d(TAG, "onAdOpened");
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            customEventInterstitialListener.onInterstitialShown();
                        }
                    });
                }

                public void onAdClicked() {
                    Log.d(TAG, "onAdClicked");
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            customEventInterstitialListener.onInterstitialClicked();
                        }
                    });
                }

                public void onAdImpression() {
                    Log.d(TAG, "onAdImpression");
                    BaseApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            customEventInterstitialListener.onInterstitialImpression();
                        }
                    });
                }
            });
        }
        PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
        if (KeysAds.DEVICE_TESTS != null) {
            for (String s : KeysAds.DEVICE_TESTS) {
                adRequest.addTestDevice(s);
            }
        }
        interstitial.loadAd(adRequest.build());
    }

    @Override
    protected void onInvalidate() {
        destroy();
    }

    @Override
    protected void showInterstitial() {
        try {
            BaseApplication.getHandler().post(new Runnable() {
                @Override public void run() {
                    if (interstitial != null && interstitial.isLoaded())
                        interstitial.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}