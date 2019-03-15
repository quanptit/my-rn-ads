package com.my.rn.Ads.modules;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.*;
import com.mopub.common.SdkInitializationListener;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.mopub.MopubNativeManager;

public class RNAdsUtilsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_SIZE_CHANGE = "onSizeChange";
    private static String TAG = "RN_ADS_MODULE";

    @ReactMethod
    public void initAds(final String urlAdsSetting, final Promise promise) {
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
                            }, 10000);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject("0", e.getMessage(), e);
                }
            }
        });
    }

    // region Full screen and Reward Ads
    @ReactMethod
    public void canShowFullCenterAds(final Promise promise) {
        promise.resolve(!AdsFullManager.isDoNotShowAds() && AdsFullManager.getInstance().isCachedCenter());
    }

    @ReactMethod
    public void showFullCenterAds(final Promise promise) {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("showFullCenterAds Fail: getSafeActivity NULL ===================");
            return;
        }
        ApplicationContainAds.getHandler().post(new Runnable() {
            @Override public void run() {
                ApplicationContainAds.getAdsFullManager().showAdsCenter(activity, promise);
            }
        });
    }

    @ReactMethod
    public void showRewardVideoAds() {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("showRewardVideoAds Fail: getSafeActivity NULL ===================");
            return;
        }
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                ApplicationContainAds.getAdsFullManager().showAdsCenter(activity, true, null);
            }
        });
    }

    @ReactMethod
    public void loadRewardVideoAds() {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("loadRewardVideoAds Fail: getSafeActivity NULL ===================");
            return;
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                ApplicationContainAds.getAdsFullManager().cacheAdsCenter(activity, true);
            }
        });
    }

    @ReactMethod
    public void canShowRewardVideoAds(final Promise promise) {
        ApplicationContainAds.getHandler().post(new Runnable() {
            @Override public void run() {
                promise.resolve(ApplicationContainAds.getAdsFullManager().isCachedCenter());
            }
        });
    }
    //endregion

    @ReactMethod
    public void isPreferShowBanner(int typeAds, final Promise promise) {
        promise.resolve(ManagerTypeAdsShow.isPreferShowBanner(typeAds));
    }

    @ReactMethod
    public void loadNativeAds(final Promise promise) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    MopubNativeManager.getInstance().cacheNativeAndWaitForComplete();
                    promise.resolve(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    promise.reject(e.getMessage());
                }
            }
        }).start();
    }

    @ReactMethod
    public void canShowNativeAds(int typeAds, final Promise promise) {
        promise.resolve(MopubNativeManager.getInstance().canShowNativeAds(typeAds));
    }

    @ReactMethod
    public void cacheNativeAdsIfNeed(int typeAds) {
        MopubNativeManager.getInstance().checkAndLoadAds();
    }

    /**
     * return "FB" | "ADMOB" | "ADX" | "MOPUB" | null.
     * Nếu return null => show Ads là các app của mình thay thế <TH không có mạng sẽ show cái này>
     */
    @ReactMethod
    public void getTypeShowBanner(String typeAds, int index, final Promise promise) {
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
                promise.resolve("MOPUB");
                break;
        }
    }

    //region init & utils
    public RNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    @Override public String getName() {
        return "RNAdsUtils";
    }

    private Activity getSafeActivity() {
        Activity activity = getCurrentActivity();
        if (activity == null)
            return AdsFullManager.getMainActivity();
        return activity;
    }

    @Override public void onHostResume() {

    }

    @Override public void onHostPause() {

    }

    @Override public void onHostDestroy() {
        AdsFullManager.getInstance().destroy();
    }
    //endregion
}
