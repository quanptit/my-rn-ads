package com.my.rn.ads.modules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class MOPUBBannerViewEmpty extends SimpleViewManager<EmptyAdsView> {
    @Override
    @NonNull protected EmptyAdsView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new EmptyAdsView(reactContext);
    }

    @Override
    @NonNull public String getName() {
        return "MOPUBBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(EmptyAdsView view, String typeAds) {
        view.setTypeAds(typeAds);
    }

    @Nullable @Override public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD)
        );
    }
}
