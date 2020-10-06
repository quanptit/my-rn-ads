package com.my.rn.ads.modules;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.baseLibs.utils.PreferenceUtils;
import com.facebook.react.bridge.*;
import com.my.rn.ads.AdsUtils;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.my.rn.ads.PromiseSaveObj;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.full.center.StartAdsManager;
import com.my.rn.ads.settings.AdsSetting;

public class BaseRNAdsUtilsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "BaseRNAdsModule";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_SIZE_CHANGE = "onSizeChange";

    //#region Start Ads Only Admob

    //#endregion

    //region start ads
    @ReactMethod
    public void loadStartAds(final Promise promise) {
        Log.d(TAG, "Call loadStartAds");
        final PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                StartAdsManager.loadStartAds(getSafeActivity(), new IAdLoaderCallback() {
                    @Override public void onAdsFailedToLoad() {
                        Log.d(TAG, "loadStartAds onAdsFailedToLoad");
                        promiseSaveObj.resolve(false);
                    }

                    @Override public void onAdsLoaded() {
                        Log.d(TAG, "loadStartAds onAdsLoaded");
                        promiseSaveObj.resolve(true);
                    }
                });
            }
        });
    }

    @ReactMethod
    public void showStartAdsIfCache(final Promise promise) {
        Log.d(TAG, "Call showStartAdsIfCache");
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("showFullCenterAds Fail: getSafeActivity NULL ===================");
            promise.resolve(false);
            return;
        }
        final PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        BaseApplicationContainAds.getHandler().post(new Runnable() {
            @Override public void run() {
                StartAdsManager.showStartAdsIfCached(activity,
                        new IAdsCalbackOpen() {
                            @Override public void onAdOpened() {
                                Log.d(TAG, "showStartAdsIfCache onAdOpened");
                                promiseSaveObj.resolve(true);
                            }

                            @Override public void noAdsCallback() {
                                Log.d(TAG, "showStartAdsIfCache noAdsCallback");
                                promiseSaveObj.resolve(false);
                            }
                        });
            }
        });
    }

    @ReactMethod
    public void destroyStartAdsIfNeed() {
        StartAdsManager.destroyStartAdsIfNeed(getSafeActivity());
    }
    //endregion

    // region Full screen and Reward Ads
    @ReactMethod
    public void canShowFullCenterAds(final Promise promise) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                boolean result = !AdsUtils.isDoNotShowAds()
                        && BaseAdsFullManager.getInstance().isCachedCenter(getSafeActivity());
                promise.resolve(result);
            }
        });
    }

    @ReactMethod
    public void canShowFullCenterOnBackBtn(final Promise promise) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                boolean isNotShow = PreferenceUtils.getBooleanSetting("not_s_b_btn");
                promise.resolve(!isNotShow);
            }
        });
    }

    @ReactMethod
    public void cacheAdsCenter() {
        Log.d(TAG, "Call cacheAdsCenter");
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                BaseAdsFullManager.getInstance().cacheAdsCenter(getSafeActivity());
            }
        });
    }

    @ReactMethod
    public void showFullCenterAds(final Promise promise) {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("showFullCenterAds Fail: getSafeActivity NULL ===================");
            promise.resolve(false);
            return;
        }
        final PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        BaseApplicationContainAds.getHandler().post(new Runnable() {
            @Override public void run() {
                BaseAdsFullManager.getInstance().showAdsCenter(activity, new IAdsCalbackOpen() {
                    @Override public void onAdOpened() {
                        promiseSaveObj.resolve(true);
                    }

                    @Override public void noAdsCallback() {
                        promiseSaveObj.resolve(false);
                    }
                });
            }
        });
    }
    //endregion

    // region Reward ads
    @ReactMethod
    public void showRewardVideoAds() {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("showRewardVideoAds Fail: getSafeActivity NULL ===================");
            return;
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                BaseApplicationContainAds.getInstance().getRewardedAdsManager()
                        .showRewardedAds(activity, true, null);
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
                BaseApplicationContainAds.getInstance().getRewardedAdsManager()
                        .cacheRewardedAds(activity);
            }
        });
    }

    @ReactMethod
    public void canShowRewardVideoAds(final Promise promise) {
        final Activity activity = getSafeActivity();
        if (activity == null) {
            L.e("loadRewardVideoAds Fail: getSafeActivity NULL ===================");
            promise.resolve(false);
            return;
        }

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                promise.resolve(BaseApplicationContainAds.getInstance().getRewardedAdsManager()
                        .isCachedRewardedAds(activity));
            }
        });
    }
    //endregion

    //region banner

    /**
     * @param typeAds: "RECTANGLE_HEIGHT_250" | "BANNER_50" | "SMART_BANNER"
     *                 return "FB" | "ADMOB" | "ADX" | "MOPUB" | null.
     *                 Nếu return null => show Ads là các app của mình thay thế <TH không có mạng sẽ show cái này>
     */
    @ReactMethod
    public void getTypeShowBanner(String typeAds, int index, final Promise promise) {
        Log.d(TAG, "getTypeShowBanner: " + typeAds + ", index: " + index);
        if (typeAds.equals("RECTANGLE_HEIGHT_250"))
            promise.resolve(AdsSetting.getInstance().getTypeShowBannerRect(index));
        else
            promise.resolve(AdsSetting.getInstance().getTypeShowBanner(index));
    }

    // @typeAds theo định nghĩa mấy cái trong NativeAdsView: TYPE_DETAIL_VOCA ...
    @ReactMethod
    public void isPreferShowBanner(int typeAds, final Promise promise) {
        // hiện mặc định là fail
        promise.resolve(false);
    }
    //endregion

    //region native ads
    @ReactMethod
    public void canShowNativeAds(int typeAds, Promise promise) {
        promise.resolve(BaseApplicationContainAds.getNativeManagerInstance().canShowNativeAds(typeAds));
    }

    // Nếu chưa cache lần nào sẽ cache, true nếu xác định có ads để show
    @ReactMethod
    public void firstCacheAndCheckCanShowNativeAds(final int typeAds, final Promise promise) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Activity activity = getSafeActivity();
                    if (activity == null) {
                        promise.reject("0", "activity null");
                        return;
                    }
                    promise.resolve(BaseApplicationContainAds
                            .getNativeManagerInstance().firstCacheAndCheckCanShowNativeAds(activity, typeAds));
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject("0", e.getMessage());
                }
            }
        }).start();
    }

    @ReactMethod
    public void hasLoadNativeAds(Promise promise) {
        promise.resolve(BaseApplicationContainAds.getNativeManagerInstance().hasLoadAds());
    }

    @ReactMethod
    public void cacheNativeAdsIfNeed(int typeAds) {
        BaseApplicationContainAds.getNativeManagerInstance().checkAndLoadAds(getSafeActivity());
    }

    @ReactMethod
    public void loadNativeAds(int typeAds, final Promise promise) {
        new Thread(new Runnable() {
            PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
            @Override public void run() {
                try {
                    BaseApplicationContainAds.getNativeManagerInstance().loadAds(getSafeActivity(), new IAdLoaderCallback() {
                        @Override public void onAdsFailedToLoad() {
                            promiseSaveObj.reject("0", "onAdsFailedToLoad");
                        }

                        @Override public void onAdsLoaded() {
                            promiseSaveObj.resolve(1);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    promiseSaveObj.reject("0", e.getMessage());
                }
            }
        }).start();
    }
    //endregion

    @ReactMethod
    public void initAds(final String urlAdsSetting, final Promise promise) {
        Log.d(TAG, "initAds Call");
        AdsSetting.getInstance().updateAdsSetting(urlAdsSetting);
        promise.resolve(1);
    }

    //region init & utils
    public BaseRNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    @Override @NonNull public String getName() {
        return "RNAdsUtils";
    }

    protected Activity getSafeActivity() {
        Activity activity = getCurrentActivity();
        if (activity == null)
            return BaseAdsFullManager.getMainActivity();
        return activity;
    }

    @Override public void onHostResume() {

    }

    @Override public void onHostPause() {

    }

    @Override public void onHostDestroy() {
        BaseAdsFullManager.getInstance().destroy();
    }
    //endregion
}
