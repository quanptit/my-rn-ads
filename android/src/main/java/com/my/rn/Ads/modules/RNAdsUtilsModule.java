package com.my.rn.Ads.modules;

import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.mopub.MopubNativeManager;

public class RNAdsUtilsModule extends BaseRNAdsUtilsModule {
    private static final String TAG = "RN_ADS_MODULE";

    public RNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    @Override public void initAds(final String urlAdsSetting, final Promise promise) {
        Log.d(TAG, "initAds Call");
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                try {
                    ApplicationContainAds.getMopubInitUtils().initMopub(new SdkInitializationListener() {
                        @Override public void onInitializationFinished() {
                            promise.resolve(1);
                            BaseApplication.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!TextUtils.isEmpty(urlAdsSetting))
                                        ManagerTypeAdsShow.updateAdsSetting(urlAdsSetting);
                                    AdsFullManager.getInstance().cacheAdsCenter(getSafeActivity());
                                }
                            }, 12000);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject("0", e.getMessage(), e);
                }
            }
        });
    }

    @ReactMethod
    @Override public void loadNativeAds(int typeAds, final Promise promise) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    MopubNativeManager.getInstance().cacheNativeAndWaitForComplete();
                    promise.resolve(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject("0", e.getMessage());
                }
            }
        }).start();
    }

    @ReactMethod
    @Override public void canShowNativeAds(int typeAds, Promise promise) {
        promise.resolve(MopubNativeManager.getInstance().canShowNativeAds(typeAds));
    }

    @ReactMethod
    @Override public void cacheNativeAdsIfNeed(int typeAds) {
        MopubNativeManager.getInstance().checkAndLoadAds();
    }

    /**
     * return "FB" | "ADMOB" | "ADX" | "MOPUB" | null.
     * Nếu return null => show Ads là các app của mình thay thế <TH không có mạng sẽ show cái này>
     */
    @ReactMethod
    @Override public void getTypeShowBanner(String typeAds, int index, final Promise promise) {
        int type = ManagerTypeAdsShow.getTypeShowBaner(typeAds, index);
        switch (type) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                promise.resolve("MOPUB");
                return;
            case ManagerTypeAdsShow.TYPE_FB:
                promise.resolve("FB");
                return;
            case ManagerTypeAdsShow.TYPE_ADMOB:
                promise.resolve("ADMOB");
                return;
            case ManagerTypeAdsShow.TYPE_ADX:
                promise.resolve("ADX");
                return;
            case -1:
                promise.resolve(null);
                return;
            default:
                promise.resolve(null);
                break;
        }
    }

    //region RewardVideoAds
    @ReactMethod @Override public void showRewardVideoAds() {
        if (TextUtils.isEmpty(KeysAds.MOPUB_REWARDED_VIDEOS)) {
            super.showRewardVideoAds();
            return;
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                Log.d(TAG, "MoPubRewardedVideos.showRewardedVideo ");
                MoPubRewardedVideos.showRewardedVideo(KeysAds.MOPUB_REWARDED_VIDEOS);
            }
        });
    }

    @ReactMethod @Override public void loadRewardVideoAds() {
        if (TextUtils.isEmpty(KeysAds.MOPUB_REWARDED_VIDEOS)) {
            super.loadRewardVideoAds();
            return;
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                Log.d(TAG, "MoPubRewardedVideos.loadRewardedVideo ");
                MoPubRewardedVideos.loadRewardedVideo(KeysAds.MOPUB_REWARDED_VIDEOS);
            }
        });
    }

    @ReactMethod @Override public void canShowRewardVideoAds(final Promise promise) {
        if (TextUtils.isEmpty(KeysAds.MOPUB_REWARDED_VIDEOS)) {
            super.canShowRewardVideoAds(promise);
            return;
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                promise.resolve(MoPubRewardedVideos.hasRewardedVideo(KeysAds.MOPUB_REWARDED_VIDEOS));
            }
        });
    }
    //endregion

    //region not change. but need override with @ReactMethod

    @ReactMethod @Override public void loadStartAds(Promise promise) {
        super.loadStartAds(promise);
    }

    @ReactMethod @Override public void showStartAdsIfCache(Promise promise) {
        super.showStartAdsIfCache(promise);
    }

    @ReactMethod @Override public void isPreferShowBanner(int typeAds, Promise promise) {
        super.isPreferShowBanner(typeAds, promise);
    }

    @ReactMethod @Override public void canShowFullCenterAds(Promise promise) {
        super.canShowFullCenterAds(promise);
    }

    @ReactMethod @Override public void cacheAdsCenter() {
        super.cacheAdsCenter();
    }

    @ReactMethod @Override public void showFullCenterAds(Promise promise) {
        super.showFullCenterAds(promise);
    }
    //endregion
}
