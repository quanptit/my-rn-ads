package com.my.rn.Ads.modules;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baseLibs.utils.L;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.my.rn.Ads.mopub.MopubNativeManager;

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
        View nativeAdsView = MopubNativeManager.getInstance().createNewAds(view.getContext(), typeAds, view);
//        if (nativeAdsView != null) {
//            view.addView(nativeAdsView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            view.requestLayout();
//        }
    }

    @Override
    protected View createViewInstance(final ThemedReactContext reactContext) {
        final FrameLayout view = new FrameLayout(reactContext);
//        View nativeAdsView = NativeAdsManager.createNewAds(frameLayout.getContext(), ManagerTypeAdsShow.TYPE_SUMMARY_LIST3);
//        if (nativeAdsView != null){
//            L.d("Display Native Ads");
//            view.addView(nativeAdsView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        }

//        frameLayout.setBackgroundColor(Color.YELLOW);
//        BannerAdsManager.loadBanerAds(reactContext, frameLayout, new ISizeChange() {
//            @Override public void onSizeChange(double widthDpi, double heightDpi) {
//                RNAdsUtilsModule.sendOnSizeChangeEvent(frameLayout, widthDpi, heightDpi);
//            }
//        });
        return view;
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
