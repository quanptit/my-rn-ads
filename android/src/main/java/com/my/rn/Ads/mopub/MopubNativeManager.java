package com.my.rn.Ads.mopub;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.mopub.nativeads.*;
import com.my.rn.Ads.ApplicationContainAds;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;

public class MopubNativeManager implements MoPubNative.MoPubNativeNetworkListener {
    private MoPubNative moPubNative;
    private boolean isLoading = false;
    private Queue<NativeAd> nativeAds = new LinkedList<>(); // Sẽ chứa NO_ADS_LOAD Ads
    private static final String TAG = "MOPUB_NATIVE";

    public MopubNativeManager(Context context) {
        if (KeysAds.getMOPUB_NATIVE()==null) return;
        moPubNative = new MoPubNative(context, KeysAds.getMOPUB_NATIVE(), this);
        MopubNativeRenderUtils.initAdRender(moPubNative);
    }

    public void cacheNativeAndWaitForComplete() throws Exception {
        if (!nativeAds.isEmpty()) return;
        Log.d(TAG, "cacheNativeAndWaitForComplete");
        long startTimeLoad = System.currentTimeMillis();
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                _checkAndLoadAds(false);
            }
        });
        while (true) {
            Thread.sleep(100);
            if (!nativeAds.isEmpty()) return;
            if (System.currentTimeMillis() - startTimeLoad > 3000) {
                Log.d(TAG, "cacheAndWaitForComplete Fail Time out");
                return;
            }
        }
    }
    public boolean canShowNativeAds(int typeAds) {
        return isCached();
    }

    public @Nullable View createNewAds(Context context, int typeAds, ViewGroup parent) {
        NativeAd nativeAd = nativeAds.poll();
        if (nativeAd == null) return null;
        return MopubNativeRenderUtils.createAdView(context, nativeAd, typeAds, parent);
    }

    public void checkAndLoadAds() {
        _checkAndLoadAds(false);
    }

    private void _checkAndLoadAds(boolean isNotResetCountError) {
        if (isLoading || nativeAds.size() >= NO_ADS_LOAD) return;
        if (!isNotResetCountError)
            countLoadError = 0;
        try {
            RequestParameters mRequestParameters = new RequestParameters.Builder()
                    .desiredAssets(desiredAssets)
                    .build();
            moPubNative.makeRequest(mRequestParameters);
            isLoading = true;
        } catch (Exception e) {
            e.printStackTrace();
            isLoading = false;
        }
    }

    // region Event callback load Ads
    @Override public void onNativeLoad(NativeAd nativeAd) {
        isLoading = false;
        nativeAds.add(nativeAd);
        _checkAndLoadAds(false);
    }

    @Override public void onNativeFail(NativeErrorCode errorCode) {
        Log.d("MopubNativeManager", "MOPUB Load native ads => onNativeFail: " + errorCode);
        countLoadError++;
        isLoading = false;
        if (countLoadError < 3) {
            _checkAndLoadAds(true);
        }
    }
    //endregion

    //region utils

    public static MopubNativeManager getInstance() {
        return ApplicationContainAds.getMopubNativeManager();
    }
    private static final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
            RequestParameters.NativeAdAsset.TITLE,
            RequestParameters.NativeAdAsset.TEXT,
            RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
            RequestParameters.NativeAdAsset.MAIN_IMAGE,
            RequestParameters.NativeAdAsset.ICON_IMAGE,
            RequestParameters.NativeAdAsset.STAR_RATING
    );
    private static final int NO_ADS_LOAD = 2;
    private int countLoadError = 0;

    public boolean isCached() {
        try {
            return !nativeAds.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion
}
