package com.my.rn.ads.modules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.baseLibs.utils.L;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.BaseNativeViewUtils;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.INativeManager;

import java.util.Map;

public class NativeAdsView extends SimpleViewManager<NativeAdsView.NativeAdsViewUI> {
    public static final String REACT_CLASS = "NativeAdsView";
    public static final String EVENT_UNKNOWN_ERROR = "onUnknownError";
    public static final String EVENT_AD_LOADER = "onAdLoaded";
    public static final String EVENT_AD_LOAD_FAILED = "onAdFailed";
//    private View adsView;
//    private String color;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(NativeAdsViewUI view, Integer typeAds) {
        view.setTypeAds(typeAds);
    }

//    @ReactProp(name = "color")
//    public void setColor(FrameLayout view, String color) {
//        this.color = color;
//        L.d("setColor: " + color);
//    }

    @NonNull @Override
    protected NativeAdsViewUI createViewInstance(@NonNull final ThemedReactContext reactContext) {
        return new NativeAdsViewUI(reactContext);
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                EVENT_UNKNOWN_ERROR, EVENT_AD_LOADER, EVENT_AD_LOAD_FAILED
        };
        for (String event : events) {
            builder.put(event, MapBuilder.of("registrationName", event));
        }
        return builder.build();
    }

    @SuppressLint("ViewConstructor") public static class NativeAdsViewUI extends FrameLayout {
        private ReactContext mContext;
        private RCTEventEmitter mEventEmitter;
        private BaseNativeViewUtils nativeViewUtils;

        public NativeAdsViewUI(ThemedReactContext context) {
            super(context);
            mContext = context;
            mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
        }

        public void setTypeAds(Integer typeAds) {
            if (getChildCount() > 0)
                removeAllViews();
            if (nativeViewUtils != null)
                nativeViewUtils.destroy();
            IAdLoaderCallback loadCallback = new IAdLoaderCallback() {
                @Override public void onAdsLoaded() {
                    mEventEmitter.receiveEvent(getId(), NativeAdsView.EVENT_AD_LOADER, Arguments.createMap());
                }

                @Override public void onAdsFailedToLoad() {
                    mEventEmitter.receiveEvent(getId(), NativeAdsView.EVENT_AD_LOAD_FAILED, Arguments.createMap());
                }
            };

            nativeViewUtils = BaseApplicationContainAds.getInstance().createNativeViewUtilsInstance(loadCallback);
            nativeViewUtils.startLoadAndDisplayAds(typeAds, this);
//            nativeViewResult = BaseApplicationContainAds.getNativeManagerInstance()
//                    .createNewAds(mContext, typeAds, this, loadCallback);
        }

        @Override protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            L.d("NativeAdsViewUI onDetachedFromWindow");
            if (nativeViewUtils != null)
                nativeViewUtils.destroy();
        }
    }
}
