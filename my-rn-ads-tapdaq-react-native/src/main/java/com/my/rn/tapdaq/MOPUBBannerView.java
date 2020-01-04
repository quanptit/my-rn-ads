package com.my.rn.tapdaq;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.widget.FrameLayout;

import com.baseLibs.utils.BaseUtils;
import com.baseLibs.utils.L;
import com.facebook.ads.AdSize;
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
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.modules.BaseRNAdsUtilsModule;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;

import java.util.Map;

import javax.annotation.Nullable;

class MOPUBBannerUI extends ReactViewGroup implements LifecycleEventListener {
    private static final String TAG = "MOPUBBannerUI";
    private ReactContext mContext;
    private RCTEventEmitter mEventEmitter;
    private FrameLayout frameLayoutAds;
    private TMBannerAdView myAdView;
    private AdSize mSize;

    public MOPUBBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private void removeAllChild() {
        removeAllViews();
        myAdView = null;
        frameLayoutAds = null;
    }

    private void createAdViewIfCan() {
        if (myAdView == null && mSize != null) {
            BaseApplicationContainAds.getIAdInitUtilsInstance().initAds(getSafeActivity(), new IAdInitCallback() {
                @Override public void didInitialise() {
                    createAdViewIfCanAfterInit();
                }

                @Override public void didFailToInitialise() {
                }
            });
        }
    }

    private void createAdViewIfCanAfterInit() {
        removeAllChild();
        this.frameLayoutAds = new FrameLayout(mContext);
        addView(frameLayoutAds, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        myAdView = new TMBannerAdView(this.getContext());

        Resources r = mContext.getResources();
        int pxW;
        int widthDpi = mSize.getWidth();
        if (widthDpi < 0) {
            if (mSize == AdSize.RECTANGLE_HEIGHT_250)
                pxW = BaseUtils.convertDpToPixel(320);
            else
                pxW = r.getDisplayMetrics().widthPixels;
        } else
            pxW = BaseUtils.convertDpToPixel(widthDpi);
        int pxH = BaseUtils.convertDpToPixel(mSize.getHeight());
        frameLayoutAds.addView(myAdView, new FrameLayout.LayoutParams(pxW, pxH));
        if (mSize == AdSize.RECTANGLE_HEIGHT_250) {
            myAdView.load(getSafeActivity(), TMBannerAdSizes.MEDIUM_RECT, tmAdListener);
        } else
            myAdView.load(getSafeActivity(), TMBannerAdSizes.STANDARD, tmAdListener);
    }

    private TMAdListener tmAdListener = new TMAdListener() {
        @Override
        public void didLoad() {
            // First banner loaded into view
            L.d("didLoad");
            this.updateSize();
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            // No banners available. View will stop refreshing
            L.d("MOPUBBannerUI onBannerFailed errorCode: " + error.toString());
            WritableMap event = Arguments.createMap();
            event.putInt("errorCode", error.getErrorCode());
            mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
        }

        @Override
        public void didRefresh() {
            L.d("didRefresh");
            // Subequent banner loaded, this view will refresh every 30 seconds
            this.updateSize();
        }

        private void updateSize() {
            if (frameLayoutAds == null) return;
            Resources r = mContext.getResources();
            int pxW;
            int widthDpi = mSize.getWidth();
            if (widthDpi < 0) {
                if (mSize == AdSize.RECTANGLE_HEIGHT_250)
                    pxW = BaseUtils.convertDpToPixel(320);
                else
                    pxW = r.getDisplayMetrics().widthPixels;
            } else
                pxW = BaseUtils.convertDpToPixel(widthDpi);

            int pxH = BaseUtils.convertDpToPixel(mSize.getHeight());
            frameLayoutAds.measure(pxW, pxH);
            frameLayoutAds.layout(0, 0, pxW, pxH);
        }
    };

    public void setSize(AdSize size) {
        mSize = size;
        createAdViewIfCan();
    }

    private Activity getSafeActivity() {
        if (getContext() != null && getContext() instanceof Activity)
            return (Activity) getContext();
        return BaseAdsFullManager.getMainActivity();
    }

    //region hide
    @Override public void onHostResume() {

    }

    @Override public void onHostPause() {

    }
    //endregion

    @Override
    public void onHostDestroy() {
        destroy();
    }

    public void destroy() {
        if (myAdView != null && getContext() != null) {
            myAdView.destroy(getContext());
            myAdView = null;
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        destroy();
    }
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
                BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD),
                BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE, MapBuilder.of("registrationName", BaseRNAdsUtilsModule.EVENT_SIZE_CHANGE)
        );
    }
}
