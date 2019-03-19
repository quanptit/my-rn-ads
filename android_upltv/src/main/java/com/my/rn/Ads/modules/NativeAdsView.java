package com.my.rn.Ads.modules;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baseLibs.utils.L;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import java.util.Map;

/**
 * Can lam: nếu ecpm tốt hơn banner. Nếu chưa cache => sẽ là loading.
 * - Chỉ cache khi có một request được yêu cầu. Kiểu đã dùng loại nào => mới cache loại đó
 * - Mỗi loại chỉ cache một cái
 * - Cache trước cái Exit.
 */
public class NativeAdsView extends SimpleViewManager<FrameLayout> {
    public static final String REACT_CLASS = "NativeAdsView";
    private static final String TAG = REACT_CLASS;
    private ThemedReactContext reactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(final FrameLayout parent, Integer typeAds) {
        if (parent.getChildCount() > 0)
            parent.removeAllViews();
//        EpomNativeManager.getInstance().showNewNativeAds(typeAds, this, parent);
    }

    @Override
    protected FrameLayout createViewInstance(final ThemedReactContext reactContext) {
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

    @Override public void onDropViewInstance(FrameLayout view) {
        super.onDropViewInstance(view);
        L.d("onDropViewInstance");
        try {
//            if (adClientNativeAd != null) {
//                adClientNativeAd.destroy();
//                adClientNativeAd = null;
//            }
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
