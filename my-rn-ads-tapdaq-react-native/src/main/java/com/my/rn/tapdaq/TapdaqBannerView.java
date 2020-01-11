package com.my.rn.tapdaq;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
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
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.full.center.BaseAdsFullManager;
import com.my.rn.ads.modules.BaseRNAdsUtilsModule;
import com.my.rn.ads.tapdaq.AdInitTapdaqUtils;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;

import java.util.Map;

import javax.annotation.Nullable;

class TapdaqBannerUI extends ReactViewGroup implements LifecycleEventListener {
    private static final String TAG = "TAPDAQ_BANNER";
    private ReactContext mContext;
    private RCTEventEmitter mEventEmitter;
    private FrameLayout frameLayoutAds;
    private TMBannerAdView myAdView;
    private AdSize mSize;

    public TapdaqBannerUI(ThemedReactContext context) {
        super(context);
        mContext = context;
        mContext.addLifecycleEventListener(this);
        mEventEmitter = mContext.getJSModule(RCTEventEmitter.class);
    }

    private void createAdViewIfCan() {
        if (myAdView == null && mSize != null) {
            AdInitTapdaqUtils.getInstance().initAds(getSafeActivity(), new IAdInitCallback() {
                @Override public void didInitialise() {
                    createAdViewIfCanAfterInit();
                }

                @Override public void didFailToInitialise() {
                    sendAdFailEvent(-999);
                }
            });
        }
    }

    private void createAdViewIfCanAfterInit() {
        removeAllChild();
        this.frameLayoutAds = new FrameLayout(mContext);
        addView(frameLayoutAds, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        myAdView = new TMBannerAdView(this.getContext());
        frameLayoutAds.addView(myAdView,
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
        this.updateSize();
        if (mSize == AdSize.RECTANGLE_HEIGHT_250) {
            myAdView.load(getSafeActivity(), TMBannerAdSizes.MEDIUM_RECT, tmAdListener);
        } else
            myAdView.load(getSafeActivity(), TMBannerAdSizes.STANDARD, tmAdListener);
    }

    private void sendAdFailEvent(int errorCode) {
        WritableMap event = Arguments.createMap();
        event.putInt("errorCode", errorCode);
        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
    }

    private TMAdListener tmAdListener = new TMAdListener() {
        @Override
        public void didLoad() {
            // First banner loaded into view
            Log.d(TAG, "didLoad");
            updateSize();
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            // No banners available. View will stop refreshing
            L.d("MOPUBBannerUI onBannerFailed errorCode: " + error.toString());
            sendAdFailEvent(error.getErrorCode());
        }

        @Override
        public void didRefresh() {
            L.d("didRefresh");
            // Subequent banner loaded, this view will refresh every 30 seconds
            updateSize();
        }
    };

    private void updateSize() {
        if (frameLayoutAds == null || myAdView == null) return;
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
    private void removeAllChild() {
        try {
            if (myAdView != null)
                myAdView.destroy(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeAllViews();
        myAdView = null;
        frameLayoutAds = null;
    }

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

public class TapdaqBannerView extends SimpleViewManager<TapdaqBannerUI> {
    @Override
    protected TapdaqBannerUI createViewInstance(ThemedReactContext reactContext) {
        return new TapdaqBannerUI(reactContext);
    }

    @Override
    public String getName() {
        return "TapdaqBannerView";
    }

    @ReactProp(name = "typeAds")
    public void setTypeAds(TapdaqBannerUI view, String typeAds) {
        AdSize adSize = null;
        if (typeAds.equals("RECTANGLE_HEIGHT_250"))
            adSize = AdSize.RECTANGLE_HEIGHT_250;
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
