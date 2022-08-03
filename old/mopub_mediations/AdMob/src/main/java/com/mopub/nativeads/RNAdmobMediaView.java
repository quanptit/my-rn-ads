package com.mopub.nativeads;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;


import javax.annotation.Nullable;

public class RNAdmobMediaView extends MediaView {

    Context mContext;
    VideoController vc;

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };


    public void setVideoController(VideoController videoController) {
        vc = videoController;

    }

    public void getCurrentProgress() {
        if (vc == null) return;
        WritableMap progress = Arguments.createMap();
        progress.putString("currentTime", String.valueOf(vc.getVideoCurrentTime()));
        progress.putString("duration", String.valueOf(vc.getVideoDuration()));
        Log.d("RNGADMediaView", "PROGRESS UPDATE");
    }


    public RNAdmobMediaView(Context context) {
        super(context);
        mContext = context;
        requestLayout();


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestLayout();
    }


    public void setPause(boolean pause) {
        if (vc == null) return;
        if (pause) {
            vc.pause();
        } else {
            vc.play();
        }
    }

    public void setMuted(boolean muted) {
        if (vc == null) return;
        vc.mute(muted);

    }


    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }


    public void sendEvent(String name, @Nullable WritableMap event) {

        ReactContext reactContext = (ReactContext) mContext;
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }

}