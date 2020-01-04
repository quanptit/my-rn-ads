package com.my.rn.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public interface INativeManager {
    void checkAndLoadAds(Activity activity);
    void cacheNativeAndWaitForComplete(final Activity activity) throws Exception;

    boolean canShowNativeAds(int typeAds);

    boolean isCached();

    @Nullable View createNewAds(Context context, int typeAds, ViewGroup parent);
}
