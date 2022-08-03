package com.my.rn.Ads.modules;

import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.full.center.AdsFullManager;

public class RNAdsUtilsModule extends BaseRNAdsUtilsModule {
    private static final String TAG = "RN_ADS_MODULE";

    public RNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    @Override public void initAds(final String urlAdsSetting, final Promise promise) {
        Log.d(TAG, "initAds Call");
        promise.resolve(true);
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(urlAdsSetting))
                    ManagerTypeAdsShow.updateAdsSetting(urlAdsSetting);
                AdsFullManager.getInstance().cacheAdsCenter(getSafeActivity());
            }
        }, KeysAds.IS_SKIP_START_ADS ? 3000 : 20000);
    }

    @ReactMethod
    @Override public void loadNativeAds(final int typeAds, final Promise promise) {
        promise.resolve(1);
//        new Thread(new Runnable() {
//            @Override public void run() {
//                try {
//                    EpomNativeManager.getInstance().cacheNativeAndWaitForComplete(typeAds);
//                    promise.resolve(1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    promise.reject("0", e.getMessage());
//                }
//            }
//        }).start();
    }

    @ReactMethod
    @Override public void canShowNativeAds(int typeAds, Promise promise) {
        promise.resolve(false);
//        promise.resolve(EpomNativeManager.getInstance().canShowNativeAds(typeAds));
    }

    @ReactMethod
    @Override public void cacheNativeAdsIfNeed(int typeAds) {
//        EpomNativeManager.getInstance().cacheNative(typeAds);
    }

    /**
     * return "FB" | "ADMOB" | "ADX" | "MOPUB" | null.
     * Nếu return null => show Ads là các app của mình thay thế <TH không có mạng sẽ show cái này>
     */
    @ReactMethod
    @Override public void getTypeShowBanner(String typeAds, int index, final Promise promise) {
        int type = ManagerTypeAdsShow.getTypeShowBaner(typeAds, index);
        switch (type) {
            case ManagerTypeAdsShow.TYPE_FB:
                promise.resolve("FB");
                return;
            case ManagerTypeAdsShow.TYPE_ADX:
                promise.resolve("ADX");
                return;
            case -1:
                promise.resolve(null);
                return;
            default:
                promise.resolve("ADX");
                break;
        }
    }

    //region not change. but need override with @ReactMethod

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

    @ReactMethod @Override public void showRewardVideoAds() {
        super.showRewardVideoAds();
    }

    @ReactMethod @Override public void loadRewardVideoAds() {
        super.loadRewardVideoAds();
    }

    @ReactMethod @Override public void canShowRewardVideoAds(Promise promise) {
        super.canShowRewardVideoAds(promise);
    }
    //endregion
}
