package com.my.rn.ads.fb.rn;

import com.facebook.ads.*;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.my.rn.ads.modules.BaseRNAdsUtilsModule;

import javax.annotation.Nullable;

import java.util.Map;


public class FbBannerView extends SimpleViewManager<FbBannerUI> {
    @Override
    protected FbBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new FbBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "FbBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(FbBannerUI view, String typeAds) {
        AdSize adSize = null;
        if (typeAds.equals("BANNER_HEIGHT_50"))
            adSize = AdSize.BANNER_HEIGHT_50;
        else if (typeAds.equals("RECTANGLE_HEIGHT_250"))
            adSize = AdSize.RECTANGLE_HEIGHT_250;
        else if (typeAds.equals("BANNER_HEIGHT_90"))
            adSize = AdSize.BANNER_HEIGHT_90;
        else
            adSize = AdSize.BANNER_HEIGHT_50;
        view.setSize(adSize);
    }

    @Nullable @Override public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD)
        );
    }
}
