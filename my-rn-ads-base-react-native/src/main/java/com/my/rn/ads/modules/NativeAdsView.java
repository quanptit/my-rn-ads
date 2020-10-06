package com.my.rn.ads.modules;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.my.rn.ads.BaseApplicationContainAds;

import java.util.Map;

public class NativeAdsView extends SimpleViewManager<View> {
    public static final String REACT_CLASS = "NativeAdsView";
//    private View adsView;
//    private String color;

    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @ReactProp(name = "typeAds")
    public void setTypeAds(FrameLayout view, Integer typeAds) {
        if (view.getChildCount() > 0)
            view.removeAllViews();
//        INativeManager.NativeViewResult result =
        BaseApplicationContainAds.getNativeManagerInstance()
                .createNewAds(view.getContext(), typeAds, view);
//        if (result != null)
//            this.adsView = result.adsView;
    }

//    @ReactProp(name = "color")
//    public void setColor(FrameLayout view, String color) {
//        this.color = color;
//        L.d("setColor: " + color);
//    }

    @Override
    protected View createViewInstance(final ThemedReactContext reactContext) {
        return new FrameLayout(reactContext);
    }


    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }
}
