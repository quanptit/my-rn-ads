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
    boolean firstCacheAndCheckCanShowNativeAds(Activity activity, int typeAds) throws Exception;

    boolean isCached();

    // chỉ ra rằng đã từng load native ads, và đã trả về kết quả là có ads hoặc không
    boolean hasLoadAds();

    @Nullable View createNewAds(Context context, int typeAds, ViewGroup parent);
}
