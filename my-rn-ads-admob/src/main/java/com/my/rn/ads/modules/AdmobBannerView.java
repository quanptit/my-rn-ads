package com.my.rn.ads.modules;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.ads.AdSize;

import javax.annotation.Nullable;

import java.util.Map;

public class AdmobBannerView extends SimpleViewManager<AdmobBannerUI> {
    @Override
    protected AdmobBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new AdmobBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "AdmobBannerView";
    }


    @ReactProp(name = "isNoRefresh")
    public void setIsNoRefresh(AdmobBannerUI view, Boolean isNoRefresh) {
        view.setIsNoRefresh(isNoRefresh);
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(AdmobBannerUI view, String typeAds) {
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
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
