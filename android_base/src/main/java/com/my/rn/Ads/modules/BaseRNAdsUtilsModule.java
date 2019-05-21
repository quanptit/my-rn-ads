package com.my.rn.Ads.modules;

import android.app.Activity;
import android.util.Log;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.*;
import com.my.rn.Ads.AdsUtils;
import com.my.rn.Ads.BaseApplicationContainAds;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;

public abstract class BaseRNAdsUtilsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "BaseRNAdsModule";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_SIZE_CHANGE = "onSizeChange";

    @ReactMethod
    public abstract void initAds(final String urlAdsSetting, final Promise promise);

    @ReactMethod
    public void loadStartAds(final Promise promise) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                try {
                    BaseShowStartAdsManager baseShowStartAdsManager = BaseShowStartAdsManager.getInstanceNotNull();
                    baseShowStartAdsManager.loadStartAds(getSafeActivity(), promise);
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.resolve(false);
                }
            }
        });
    }

    @ReactMethod
    public void showStartAdsIfCache(final Promise promise) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                BaseShowStartAdsManager baseShowStartAdsManager = BaseShowStartAdsManager.getInstance();
                try {
                    if (baseShowStartAdsManager != null)
                        baseShowStartAdsManager.showStartIfCache(getSafeActivity(), promise);
                    else
                        promise.resolve(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.resolve(false);
                }
            }
        });
    }

    // region Full screen and Reward Ads
    @ReactMethod
    public void canShowFullCenterAds(final Promise promise) {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                promise.resolve(!AdsUtils.isDoNotShowAds() && BaseAdsFullManager.getInstance().isCachedCenter());
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
        BaseApplicationContainAds.getHandler().post(new Runnable() {
            @Override public void run() {
                BaseAdsFullManager.getInstance().showAdsCenter(activity, promise);
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
                BaseAdsFullManager.getInstance().showAdsCenter(activity, true, null);
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
                BaseAdsFullManager.getInstance().cacheAdsCenter(activity, true);
            }
        });
    }

    @ReactMethod
    public void canShowRewardVideoAds(final Promise promise) {
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                promise.resolve(BaseAdsFullManager.getInstance().isCachedCenter());
            }
        });
    }
    //endregion

    @ReactMethod
    public void isPreferShowBanner(int typeAds, final Promise promise) {
        promise.resolve(ManagerTypeAdsShow.isPreferShowBanner(typeAds));
    }

    @ReactMethod
    public abstract void loadNativeAds(int typeAds, final Promise promise);

    @ReactMethod
    public abstract void canShowNativeAds(int typeAds, final Promise promise);

    @ReactMethod
    public abstract void cacheNativeAdsIfNeed(int typeAds);

    /**
     * return "FB" | "ADMOB" | "ADX" | "MOPUB" | null.
     * Nếu return null => show Ads là các app của mình thay thế <TH không có mạng sẽ show cái này>
     */
    @ReactMethod
    public abstract void getTypeShowBanner(String typeAds, int index, final Promise promise);

    //region init & utils
    public BaseRNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    @Override public String getName() {
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
