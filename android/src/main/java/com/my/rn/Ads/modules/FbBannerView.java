package com.my.rn.Ads.modules;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.BaseUtils;
import com.baseLibs.utils.L;
import com.facebook.ads.*;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.mopub.common.util.Dips;

import javax.annotation.Nullable;
import java.util.Map;

class FbBannerUI extends ReactViewGroup implements AdListener, LifecycleEventListener {
    private ReactContext mContext;
    private AdView myAdView;
    private RCTEventEmitter mEventEmitter;
    private AdSize mSize;

    public FbBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private void createAdViewIfCan() {
        if (myAdView == null && mSize != null) {
            myAdView = new AdView(this.getContext(), getPlacementID(), mSize);
            myAdView.setAdListener(this);
            myAdView.loadAd();
        }
    }

    public void setSize(AdSize size) {
        mSize = size;
        createAdViewIfCan();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        WritableMap event = Arguments.createMap();

        event.putInt("errorCode", adError.getErrorCode());
        event.putString("errorMessage", adError.getErrorMessage());
        L.d("FbBannerUI onError: " + adError.getErrorMessage());
        mEventEmitter.receiveEvent(getId(), RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);

        myAdView = null;
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (myAdView == null) return;
        this.removeAllViews();
        Resources r = mContext.getResources();
        int pxW;
        int widthDpi = mSize.getWidth();
        if (widthDpi<0){
            if (mSize == AdSize.RECTANGLE_HEIGHT_250)
                pxW = BaseUtils.convertDpToPixel(320);
            else
                pxW = r.getDisplayMetrics().widthPixels;
        }else
            pxW = BaseUtils.convertDpToPixel(widthDpi);

        int pxH = BaseUtils.convertDpToPixel(mSize.getHeight());
//        L.d("FB BANNER: onAdLoaded: pxW =" + pxW + ", mSize.getHeight(): " + mSize.getHeight()); //TOD
//        myAdView.measure(pxW, pxH);
//        myAdView.layout(0, 0, pxW, pxH);
//        addView(myAdView);

        FrameLayout frameLayout = new FrameLayout(mContext);
        addView(frameLayout);
        frameLayout.addView(myAdView, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        frameLayout.measure(pxW, pxH);
        frameLayout.layout(0, 0, pxW, pxH);
    }

    @Override
    public void onHostDestroy() {
        destroy();
    }

    public void destroy() {
        if (myAdView != null) {
            myAdView.destroy();
            myAdView = null;
        }
    }

    //region utils
    private String getPlacementID() {
        if (mSize == AdSize.RECTANGLE_HEIGHT_250)
            return KeysAds.FB_BANNER_RECT;
        return KeysAds.FB_BANNER;
    }

    private int getWidthAds(AdSize adSize) {
        if (adSize.getWidth() > 0) return adSize.getWidth();
        if (adSize.equals(AdSize.RECTANGLE_HEIGHT_250))
            return 320;
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    //endregion
    //region hide
    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onAdClicked(Ad ad) {
    }

    @Override
    public void onLoggingImpression(Ad ad) {
    }
    //endregion
}

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
                RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", RNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD)
        );
    }
}
