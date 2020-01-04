package com.my.rn.ads.modules;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.my.rn.ads.BaseApplicationContainAds;

public class RNAdsUtilsModule extends BaseRNAdsUtilsModule {
    private static final String TAG = "RN_ADS_MODULE";

    public RNAdsUtilsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    //region not change. but need override with @ReactMethod
    @ReactMethod
    @Override public void initAds(final String urlAdsSetting, final Promise promise) {
        super.initAds(urlAdsSetting, promise);
    }

    @ReactMethod @Override public void showRewardVideoAds() {
        super.showRewardVideoAds();
    }

    @ReactMethod @Override public void loadRewardVideoAds() {
        super.loadRewardVideoAds();
    }

    @ReactMethod @Override public void canShowRewardVideoAds(final Promise promise) {
        super.canShowRewardVideoAds(promise);
    }

    @ReactMethod
    @Override public void loadNativeAds(int typeAds, final Promise promise) {
        super.loadNativeAds(typeAds, promise);
    }

    @ReactMethod
    @Override public void getTypeShowBanner(String typeAds, int index, final Promise promise) {
        super.getTypeShowBanner(typeAds, index, promise);
    }

    @ReactMethod
    public void canShowFullCenterOnBackBtn(final Promise promise) {
        super.canShowFullCenterOnBackBtn(promise);
    }

    @ReactMethod
    public void destroyStartAdsIfNeed() {super.destroyStartAdsIfNeed();}

    @ReactMethod
    @Override public void canShowNativeAds(int typeAds, Promise promise) {
        promise.resolve(BaseApplicationContainAds.getNativeManagerInstance().canShowNativeAds(typeAds));
    }

    @ReactMethod
    @Override public void cacheNativeAdsIfNeed(int typeAds) {
        BaseApplicationContainAds.getNativeManagerInstance().checkAndLoadAds(getSafeActivity());
    }

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
