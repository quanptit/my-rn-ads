package com.my.rn.Ads.modules;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import javax.annotation.Nullable;
import java.util.Map;

class AdxBannerUI extends ReactViewGroup implements LifecycleEventListener {
    private ReactContext mContext;
    private PublisherAdView mAdView;
    private RCTEventEmitter mEventEmitter;
    private AdSize mSize;

    public AdxBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private void createAdViewIfCan() {
        if (mAdView == null && mSize != null) {
            removeAllViews();
            mAdView = new PublisherAdView(this.getContext());
            mAdView.setAdUnitId(KeysAds.getAdxBanner());
            mAdView.setAdSizes(mSize);
            mAdView.setAdListener(new AdListener() {
                @Override public void onAdFailedToLoad(int i) {
                    L.d("AdxBannerUI onAdFailedToLoad code = : " + i);
                    WritableMap event = Arguments.createMap();
                    event.putInt("errorCode", i);
                    mEventEmitter.receiveEvent(getId(), RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
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
            PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();
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
        mEventEmitter.receiveEvent(getId(), RNAdsUtilsModule.EVENT_SIZE_CHANGE, event);
    }

    public void setSize(AdSize size) {
        mSize = size;
        createAdViewIfCan();
    }

    @Override
    public void onHostDestroy() {
        destroy();
    }

//    @Override protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        destroy();
//    }

    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
            mAdView = null;
        }
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

public class AdxBannerView extends SimpleViewManager<AdxBannerUI> {
    @Override
    protected AdxBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new AdxBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "AdxBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(AdxBannerUI view, String typeAds) {
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
                RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                RNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", RNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
