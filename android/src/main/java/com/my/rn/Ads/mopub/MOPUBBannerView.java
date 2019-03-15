package com.my.rn.Ads.mopub;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.my.rn.Ads.modules.RNAdsUtilsModule;

import javax.annotation.Nullable;
import java.util.Map;

class MOPUBBannerUI extends MoPubView implements MoPubView.BannerAdListener, LifecycleEventListener {
    private ReactContext mContext;
    private RCTEventEmitter mEventEmitter;
    private String typeAds;
    private boolean isAddAds;

    public MOPUBBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
        this.setBannerAdListener(this);
    }

    public void setTypeAds(String typeAds) {
        if (!TextUtils.isEmpty(this.typeAds)) return;
        this.typeAds = typeAds;
        if (typeAds.equals("RECTANGLE_HEIGHT_250"))
            this.setAdUnitId(KeysAds.getMOPUB_BANNER_LARGE());
        else
            this.setAdUnitId(KeysAds.getMOPUB_BANNER());
        Log.d("MOPUBBannerUI", "setTypeAds and load ads");
        this.loadAd();
    }

    @Override
    public void onViewAdded(View child) {
        //React-Native cannot autosize RNWebViews, so you have to manually specify style.height
        //or do some trickery
        //Turns out this is also true for all other WebViews, which are added internally inside this banner
        //So we just size them manually
        //Width and Height must be set in RN style
        super.onViewAdded(child);
        int withDpi = getAdWidth();
        int heightDpi = getAdHeight();
        Resources r = mContext.getResources();
        int width = withDpi > 0 ? Dips.asIntPixels(withDpi, getContext()) : r.getDisplayMetrics().widthPixels;
        int height = Dips.asIntPixels(heightDpi, getContext());
        isAddAds = true;

        child.measure(width, height);
        child.layout(0, 0, width, height);
        child.requestLayout();
        sendOnSizeChangeEvent(withDpi, heightDpi);
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        int withDpi = banner.getAdWidth();
        int heightDpi = banner.getAdHeight();
        int width = Dips.asIntPixels(withDpi, this.getContext());
        int height = Dips.asIntPixels(heightDpi, this.getContext());
        int left = banner.getLeft();
        int top = banner.getTop();
        banner.measure(width, height);
        banner.layout(left, top, left + width, top + height);
        if (isAddAds)
            sendOnSizeChangeEvent(withDpi, heightDpi);
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        L.d("MOPUBBannerUI onBannerFailed errorCode: " + errorCode.toString());
        WritableMap event = Arguments.createMap();
        event.putInt("errorCode", errorCode.getIntCode());
        mEventEmitter.receiveEvent(getId(), RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {
    }


    private void sendOnSizeChangeEvent(int widthDpi, int heightDpi) {
        WritableMap event = Arguments.createMap();
        event.putDouble("width", widthDpi);
        event.putDouble("height", heightDpi);
        mEventEmitter.receiveEvent(getId(), RNAdsUtilsModule.EVENT_SIZE_CHANGE, event);
    }

    @Override
    public void onHostDestroy() {
        this.destroy();
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

public class MOPUBBannerView extends SimpleViewManager<MOPUBBannerUI> {
    @Override
    protected MOPUBBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new MOPUBBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "MOPUBBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(MOPUBBannerUI view, String typeAds) {
        view.setTypeAds(typeAds);
    }

    @Nullable @Override public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                RNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", RNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
