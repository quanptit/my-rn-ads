package com.my.rn.ads.modules;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

public class EmptyAdsView extends ReactViewGroup {
    private RCTEventEmitter mEventEmitter;

    public EmptyAdsView(ThemedReactContext context) {
        super(context);
        mEventEmitter = context.getJSModule(RCTEventEmitter.class);
    }

    private void sendAdFailEvent(int errorCode) {
        WritableMap event = Arguments.createMap();
        event.putInt("errorCode", errorCode);
        mEventEmitter.receiveEvent(getId(), BaseRNAdsUtilsModule.EVENT_AD_FAILED_TO_LOAD, event);
    }

    public void setTypeAds(String typeAds) {
        sendAdFailEvent(-999);
    }
}
