package com.adapter.ax.inmobiNativeAd;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.ERROR;

public class StaticNativeViewHolder {
    @Nullable public View mainView;
    @Nullable public TextView titleView;
    @Nullable public TextView textView;
    @Nullable public TextView callToActionView;
    @Nullable public ImageView mainImageView;
    @Nullable public ImageView iconImageView;
    @Nullable public ViewGroup privacyContainer;

    @VisibleForTesting
    static final StaticNativeViewHolder EMPTY_VIEW_HOLDER = new StaticNativeViewHolder();

    // Use fromViewBinder instead of a constructor
    private StaticNativeViewHolder() {}

    @NonNull
    public static StaticNativeViewHolder fromViewBinder(@NonNull final View view,
                                                        @NonNull final ViewBinder viewBinder) {
        final StaticNativeViewHolder staticNativeViewHolder = new StaticNativeViewHolder();
        staticNativeViewHolder.mainView = view;
        try {
            staticNativeViewHolder.titleView = (TextView) view.findViewById(viewBinder.titleId);
            staticNativeViewHolder.textView = (TextView) view.findViewById(viewBinder.textId);
            staticNativeViewHolder.callToActionView =
                    (TextView) view.findViewById(viewBinder.callToActionId);
            staticNativeViewHolder.mainImageView = view.findViewById(viewBinder.mainImageId);
            staticNativeViewHolder.iconImageView =
                    (ImageView) view.findViewById(viewBinder.iconImageId);
            staticNativeViewHolder.privacyContainer = view.findViewById(viewBinder.privacyContainer);
            return staticNativeViewHolder;
        } catch (ClassCastException exception) {
            exception.printStackTrace();
            MoPubLog.log(ERROR, "Could not cast from id in ViewBinder to expected View type", exception);
            return EMPTY_VIEW_HOLDER;
        }
    }
}
