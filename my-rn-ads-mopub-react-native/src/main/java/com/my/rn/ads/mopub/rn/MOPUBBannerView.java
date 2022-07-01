package com.my.rn.ads.mopub.rn;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.appsharelib.KeysAds;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.modules.BaseRNAdsUtilsModule;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.settings.AdsSetting;

import javax.annotation.Nullable;

import java.util.Map;

class MOPUBBannerUI extends View {
    private static final String TAG = "MOPUBBannerUI";
    private RCTEventEmitter mEventEmitter;
    private String typeAds;
    private boolean isAddAds;

    public MOPUBBannerUI(ThemedReactContext context) {
        super(context);
//        context.addLifecycleEventListener(this);
//        mEventEmitter = context.getJSModule(RCTEventEmitter.class);
//        this.setBannerAdListener(this);
    }
//
//    private void sendAdFailEvent(int errorCode) {
//        WritableMap event = Arguments.createMap();
//        event.putInt("errorCode", errorCode);
//        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
//    }
//
//    private String getAdUnitID() {
//        if (AdsSetting.RECTANGLE_HEIGHT_250.equals(this.typeAds)) {
//            if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_BANNER_LARGE != null)
//                return "252412d5e9364a05ab77d9396346d73d";
//            String saveKey = AdsSetting.getBannerRectKey(AdsSetting.ID_MOPUB);
//            if (saveKey != null) return saveKey;
//            return KeysAds.MOPUB_BANNER_LARGE;
//        }
//        if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_BANNER != null)
//            return "b195f8dd8ded45fe847ad89ed1d016da";
//        String saveKey = AdsSetting.getBannerKey(AdsSetting.ID_MOPUB);
//        if (saveKey != null) return saveKey;
//        return KeysAds.MOPUB_BANNER;
//    }
//
//    private void createAdView() {
//        MopubInitUtils.getInstance().initAds(getSafeActivity(), new IAdInitCallback() {
//            @Override public void didInitialise() {
//                createAdViewAfterInit();
//            }
//
//            @Override public void didFailToInitialise() {
//                sendAdFailEvent(-999);
//            }
//        });
//    }
//
//    private void createAdViewAfterInit() {
//        if (getAdUnitID() == null) return;
//        this.setAdUnitId(getAdUnitID());
//        if (AdsSetting.RECTANGLE_HEIGHT_250.equals(typeAds)) {
//            this.setAdSize(MoPubView.MoPubAdSize.HEIGHT_250);
//            this.setAutorefreshEnabled(false);
//        } else {
//            this.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
//        }
//        this.loadAd();
//    }

//    @Override
//    public void onViewAdded(View child) {
//        //React-Native cannot autosize RNWebViews, so you have to manually specify style.height
//        //or do some trickery
//        //Turns out this is also true for all other WebViews, which are added internally inside this banner
//        //So we just size them manually
//        //Width and Height must be set in RN style
//        super.onViewAdded(child);
//        int withDpi = getWidthDpi(this);
//        int heightDpi = getAdHeight();
//        int width = Dips.asIntPixels(withDpi, getContext());
//        int height = Dips.asIntPixels(heightDpi, getContext());
//        isAddAds = true;
//
//        Log.d(TAG, "onViewAdded withDpi = " + withDpi);
//        child.measure(width, height);
//        child.layout(0, 0, width, height);
//        child.requestLayout();
//        sendOnSizeChangeEvent(withDpi, heightDpi);
//    }
//
//    public void setTypeAds(String typeAds) {
//        if (!TextUtils.isEmpty(this.typeAds)) return;
//        this.typeAds = typeAds;
//        Log.d("MOPUBBannerUI", "setTypeAds and load ads");
//        createAdView();
//    }
//
//    @Override
//    public void onBannerLoaded(MoPubView banner) {
//        int widthDpi = getWidthDpi(banner);
//        int heightDpi = getHeightDpi(banner);
//        int width = Dips.asIntPixels(widthDpi, this.getContext());
//        int height = Dips.asIntPixels(heightDpi, this.getContext());
//        int left = banner.getLeft();
//        int top = banner.getTop();
//        Log.d(TAG, "onBannerLoaded widthDpi: " + widthDpi + ", left: " + left + ", top: " + top);
//        banner.measure(width, height);
//        banner.layout(left, top, left + width, top + height);
//        if (isAddAds)
//            sendOnSizeChangeEvent(widthDpi, heightDpi);
//    }
//
//    private int getWidthDpi(MoPubView banner) {
//        int withDpi = banner.getAdWidth();
//        if (withDpi < 200)
//            withDpi = 320;
//        return withDpi;
//    }
//
//    private int getHeightDpi(MoPubView banner) {
//        int heightDpi = banner.getAdHeight();
//        if (heightDpi < 45) {
//            if (AdsSetting.RECTANGLE_HEIGHT_250.equals(typeAds)) {
//                heightDpi = 250;
//            } else
//                heightDpi = 50;
//        }
//        return heightDpi;
//    }
//
//    @Override
//    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
//        Log.d(TAG, "onBannerFailed errorCode: " + errorCode.toString());
//        sendAdFailEvent(errorCode.getIntCode());
//    }
//
//    @Override
//    public void onBannerClicked(MoPubView banner) {
//    }
//
//    @Override
//    public void onBannerExpanded(MoPubView banner) {
//    }
//
//    @Override
//    public void onBannerCollapsed(MoPubView banner) {
//    }
//
//    private void sendOnSizeChangeEvent(int widthDpi, int heightDpi) {
//        WritableMap event = Arguments.createMap();
//        event.putDouble("width", widthDpi);
//        event.putDouble("height", heightDpi);
//        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, event);
//    }
//
//    @Override
//    public void onHostDestroy() {
//        this.destroy();
//    }
//
//    //region hide
//    private Activity getSafeActivity() {
//        if (getContext() != null && getContext() instanceof Activity)
//            return (Activity) getContext();
//        return BaseAdsFullManager.getMainActivity();
//    }
//
//    @Override
//    public void onHostResume() {
//    }
//
//    @Override
//    public void onHostPause() {
//
//    }
//    //endregion
//
//    @Override protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        Log.d(TAG, "onDetachedFromWindow");
//        this.destroy();
//    }
}

public class MOPUBBannerView extends SimpleViewManager<View> {
    @Override
    @NonNull protected MOPUBBannerUI createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new MOPUBBannerUI(reactContext);
    }

    @Override
    @NonNull public String getName() {
        return "MOPUBBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(MOPUBBannerUI view, String typeAds) {
//        view.setTypeAds(typeAds);
    }

    @Nullable @Override public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
