package com.my.rn.Ads.modules;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.adclient.android.sdk.nativeads.AdClientNativeAd;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.my.rn.Ads.full.center.BaseAdsFullManager;

import java.util.Map;

/**
 * TODO: nếu ecpm tốt hơn banner. Nếu chưa cache => sẽ là loading.
 * - Chỉ cache khi có một request được yêu cầu. Kiểu đã dùng loại nào => mới cache loại đó
 * - Mỗi loại chỉ cache một cái
 * - Cache trước cái Exit.
 */
public class NativeAdsView extends SimpleViewManager<View> {
    public static final String REACT_CLASS = "NativeAdsView";
    public AdClientNativeAd adClientNativeAd;
    private ThemedReactContext reactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(FrameLayout view, Integer typeAds) {
        if (view.getChildCount() > 0)
            view.removeAllViews();
        //TODO
//        MopubNativeManager.getInstance().createNewAds(view.getContext(), typeAds, view);
    }

    @Override
    protected View createViewInstance(final ThemedReactContext reactContext) {
        this.reactContext = reactContext;
        return new FrameLayout(reactContext);
    }

    @Nullable public Activity getActivity() {
        Activity activity;
        if (this.reactContext == null)
            activity = null;
        else
            activity = this.reactContext.getCurrentActivity();
        return activity != null ? activity : BaseAdsFullManager.getMainActivity();
    }

    @Override public void onDropViewInstance(View view) {
        super.onDropViewInstance(view);
        try {
            if (adClientNativeAd != null) {
                adClientNativeAd.destroy();
                adClientNativeAd = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                RNAdsUtilsModule.EVENT_SIZE_CHANGE
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }
}
