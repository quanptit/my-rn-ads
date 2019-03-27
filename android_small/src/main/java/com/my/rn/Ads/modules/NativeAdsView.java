package com.my.rn.Ads.modules;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class NativeAdsView extends SimpleViewManager<View> {
    public static final String REACT_CLASS = "NativeAdsView";

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
        return new FrameLayout(reactContext);
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
