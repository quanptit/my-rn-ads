package com.my.rn.ads.modules;

import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.my.rn.ads.settings.AdsSetting;

import javax.annotation.Nullable;

import java.util.Map;

class AdmobBannerUI extends ReactViewGroup implements LifecycleEventListener {
    private ReactContext mContext;
    private AdView mAdView;
    private RCTEventEmitter mEventEmitter;
    private AdSize mSize;
    private Boolean isNoRefresh;

    public AdmobBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private String getKeyAds() {
        if (KeysAds.IS_DEVELOPMENT && KeysAds.ADMOD_BANER_NO_REFRESH != null)
            return "ca-app-pub-3940256099942544/6300978111";
        if (this.mSize == AdSize.MEDIUM_RECTANGLE) {
            String saveKey = AdsSetting.getBannerRectKey(AdsSetting.ID_ADMOB);
            if (saveKey != null) return saveKey;
            return KeysAds.ADMOD_BANER_NO_REFRESH;
        }

        String saveKey = AdsSetting.getBannerKey(AdsSetting.ID_ADMOB);
        if (saveKey != null) return saveKey;
        return KeysAds.ADMOD_BANER_NO_REFRESH;
    }
    private void sendAdFaildEvent(int errorCode){
        WritableMap event = Arguments.createMap();
        event.putInt("errorCode", errorCode);
        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
    }

    private void createAdViewIfCan() {
        if (mAdView == null && mSize != null && isNoRefresh != null) {
            String adUnitId = getKeyAds();
            if (TextUtils.isEmpty(adUnitId)){
                sendAdFaildEvent(-99);
                return;
            }
            removeAllViews();
            mAdView = new AdView(this.getContext());
            Log.d("AdmobBannerUI", "Create Ad View: " + adUnitId);
            mAdView.setAdUnitId(adUnitId);
            mAdView.setAdSize(mSize);
            mAdView.setAdListener(new AdListener() {
                @Override public void onAdFailedToLoad(int i) {
                    sendAdFaildEvent(i);
                    mAdView = null;
                }

                @Override public void onAdLoaded() {
                    if (mAdView == null) return;
                    AdSize adSize = mAdView.getAdSize();
                    int width = adSize.getWidthInPixels(mContext);
                    int height = adSize.getHeightInPixels(mContext);
                    int left = mAdView.getLeft();
                    int top = mAdView.getTop();
                    mAdView.measure(width, height);
                    mAdView.layout(left, top, left + width, top + height);
                    sendOnSizeChangeEvent(adSize);
                }
            });
            addView(mAdView);

            AdRequest.Builder adRequest = new AdRequest.Builder();
            if (KeysAds.DEVICE_TESTS != null) {
                for (String s : KeysAds.DEVICE_TESTS) {
                    adRequest.addTestDevice(s);
                }
            }
            mAdView.loadAd(adRequest.build());
        }
    }

    private void sendOnSizeChangeEvent(AdSize adSize) {
        int width, height;
        if (adSize == AdSize.SMART_BANNER) {
            width = (int) PixelUtil.toDIPFromPixel(adSize.getWidthInPixels(mContext));
            height = (int) PixelUtil.toDIPFromPixel(adSize.getHeightInPixels(mContext));
        } else {
            width = adSize.getWidth();
            height = adSize.getHeight();
        }
        WritableMap event = Arguments.createMap();
        event.putDouble("width", width);
        event.putDouble("height", height);
        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, event);
    }

    public void setSize(AdSize size) {
        mSize = size;
        createAdViewIfCan();
    }

    public void setIsNoRefresh(boolean isNoRefresh) {
        this.isNoRefresh = isNoRefresh;
        createAdViewIfCan();
    }

    @Override
    public void onHostDestroy() {
        destroy();
    }

    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
            mAdView = null;
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("AdmobBanner", "onDetachedFromWindow");
        destroy();
    }

    //region hide
    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    //endregion
}

public class AdmobBannerView extends SimpleViewManager<AdmobBannerUI> {
    @Override
    protected AdmobBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new AdmobBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "AdmobBannerView";
    }


    @ReactProp(name = "isNoRefresh")
    public void setIsNoRefresh(AdmobBannerUI view, Boolean isNoRefresh) {
        view.setIsNoRefresh(isNoRefresh);
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(AdmobBannerUI view, String typeAds) {
        AdSize adSize = null;
        if (typeAds.equals("BANNER_HEIGHT_50"))
            adSize = AdSize.BANNER;
        else if (typeAds.equals("RECTANGLE_HEIGHT_250"))
            adSize = AdSize.MEDIUM_RECTANGLE;
        else
            adSize = AdSize.SMART_BANNER;
        view.setSize(adSize);
    }

    @Nullable @Override public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
