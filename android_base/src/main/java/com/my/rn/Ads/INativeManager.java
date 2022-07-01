package com.my.rn.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface INativeManager {
    INativeViewUtils createNewAds(Context context, int typeAds, ViewGroup parent, IAdLoaderCallback loaderCallback);

    interface INativeViewUtils {
        void destroyAds();

        void setAdsCallback(IAdLoaderCallback loaderCallback);
    }
}
