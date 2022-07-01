package com.my.rn.ads.modules;

import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
//import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.my.rn.ads.settings.AdsSetting;

public class AdxBannerUI extends ReactViewGroup implements LifecycleEventListener {
    private ReactContext mContext;
//    private PublisherAdView mAdView;
    private RCTEventEmitter mEventEmitter;
    private AdSize mSize;

    public AdxBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private String getKeyAds() {
        if (KeysAds.IS_DEVELOPMENT && KeysAds.ADX_BANNER != null)
            return "ca-app-pub-3940256099942544/6300978111";
        if (this.mSize == AdSize.MEDIUM_RECTANGLE) {
            String saveKey = AdsSetting.getBannerRectKey(AdsSetting.ID_ADX);
            if (saveKey != null) return saveKey;
            return KeysAds.ADX_BANNER;
        }

        String saveKey = AdsSetting.getBannerKey(AdsSetting.ID_ADX);
        if (saveKey != null) return saveKey;
        return KeysAds.ADX_BANNER;
    }

    private void sendAdFaildEvent(int errorCode) {
        WritableMap event = Arguments.createMap();
        event.putInt("errorCode", errorCode);
        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
    }

    private void createAdViewIfCan() {
//        if (mAdView == null && mSize != null) {
//            String adUnitId = getKeyAds();
//            Log.d("ADXBannerUI", "Create Ad View: " + adUnitId);
//            if (TextUtils.isEmpty(adUnitId)) {
//                sendAdFaildEvent(-99);
//                return;
//            }
//            removeAllViews();
//            mAdView = new PublisherAdView(this.getContext());
//            mAdView.setAdUnitId(adUnitId);
//            mAdView.setAdSizes(mSize);
//            mAdView.setAdListener(new AdListener() {
//                @Override public void onAdFailedToLoad(int i) {
//                    L.d("AdxBannerUI onAdFailedToLoad code = : " + i);
//                    sendAdFaildEvent(i);
//                    mAdView = null;
//                }
//
//                @Override public void onAdLoaded() {
//                    if (mAdView == null) return;
//                    AdSize adSize = mAdView.getAdSize();
//                    int width = adSize.getWidthInPixels(mContext);
//                    int height = adSize.getHeightInPixels(mContext);
//                    int left = mAdView.getLeft();
//                    int top = mAdView.getTop();
//                    mAdView.measure(width, height);
//                    mAdView.layout(left, top, left + width, top + height);
//                    sendOnSizeChangeEvent(adSize);
//                }
//            });
//            addView(mAdView);
//            PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
//            if (KeysAds.DEVICE_TESTS != null) {
//                for (String s : KeysAds.DEVICE_TESTS) {
//                    adRequest.addTestDevice(s);
//                }
//            }
//            mAdView.loadAd(adRequest.build());
//        }
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

    @Override
    public void onHostDestroy() {
        destroy();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("ADX_BANNER", "onDetachedFromWindow");
        destroy();
    }

    public void destroy() {
//        if (mAdView != null) {
//            mAdView.destroy();
//            mAdView = null;
//        }
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
