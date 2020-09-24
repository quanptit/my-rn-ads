package com.my.rn.ads.mopub.ad_native;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.mopub.nativeads.*;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.INativeManager;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.settings.AdsSetting;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;

public class MopubNativeManager implements MoPubNative.MoPubNativeNetworkListener, INativeManager {
    private MoPubNative moPubNative;
    private boolean isLoading = false;
    private boolean hasLoadAds;
    private boolean isSkipWaitForComplete = false;
    private Queue<NativeAd> nativeAds = new LinkedList<>(); // Sẽ chứa NO_ADS_LOAD Ads
    private static final String TAG = "MOPUB_NATIVE";
    private @Nullable IAdLoaderCallback iAdLoaderCallback;

    public void setiAdLoaderCallback(IAdLoaderCallback iAdLoaderCallback) {
        this.iAdLoaderCallback = iAdLoaderCallback;
    }

    private String getAdUnitID() {
        if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_NATIVE != null)
            return "11a17b188668469fb0412708c3d16813";
        String saveKey = AdsSetting.getNativeLargeKey(AdsSetting.ID_MOPUB);
        if (saveKey != null) return saveKey;
        return KeysAds.MOPUB_NATIVE;
    }

    @Override public void cacheNativeAndWaitForComplete(Activity activity) throws Exception {
        if (!nativeAds.isEmpty()) return;
        isSkipWaitForComplete = false;
        Log.d(TAG, "cacheNativeAndWaitForComplete");
        long startTimeLoad = System.currentTimeMillis();
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                _checkAndLoadAds(false);
            }
        });
        while (true) {
            if (isSkipWaitForComplete) return;
            Thread.sleep(100);
            if (!nativeAds.isEmpty()) return;
            if (System.currentTimeMillis() - startTimeLoad > 6000) {
                Log.d(TAG, "cacheAndWaitForComplete Fail Time out");
                return;
            }
        }
    }

    @Override public boolean canShowNativeAds(int typeAds) {
        return isCached();
    }

    @Override public boolean firstCacheAndCheckCanShowNativeAds(Activity activity, int typeAds) throws Exception {
        boolean isCached = isCached();
        if (isCached || hasLoadAds()) return isCached;
        // chưa thực hiện load ads lần nào => load
        cacheNativeAndWaitForComplete(activity);

        return isCached();
    }

    @Override public boolean hasLoadAds() {
        return hasLoadAds;
    }

    @Override public @Nullable NativeViewResult createNewAds(Context context, int typeAds, ViewGroup parent) {
        NativeAd nativeAd = nativeAds.poll();
        if (nativeAd == null) return null;
        View adsView = MopubNativeRenderUtils.createAdView(context, nativeAd, typeAds, parent);
        return new NativeViewResult(adsView, nativeAd);
    }

    @Override public @Nullable View createNewAds(Context context, Object nativeAdObj, int typeAds, ViewGroup parent) {
        if (nativeAdObj instanceof NativeAd) {
            return MopubNativeRenderUtils.createAdView(context, (NativeAd) nativeAdObj, typeAds, parent);
        }
        return null;
    }
    public void clearRegisterView(NativeAd nativeAd, View view){
        nativeAd.clear(view);
    }

    @Override public void checkAndLoadAds(Activity activity) {
        _checkAndLoadAds(false);
    }

    private void _checkAndLoadAds(boolean isNotResetCountError) {
        if (!BaseUtils.isOnline()){
            isLoading = false;
            onNativeFail(NativeErrorCode.CONNECTION_ERROR);
            return;
        }
        if (isLoading || nativeAds.size() >= NO_ADS_LOAD) return;
        MopubInitUtils.getInstance().initAds(null, new IAdInitCallback() {
            @Override public void didInitialise() {
                excuteLoadNativeAds();
            }

            @Override public void didFailToInitialise() {
                isLoading = false;
                isSkipWaitForComplete = true;
            }
        });
    }

    private void excuteLoadNativeAds() {
        String adUnit = getAdUnitID();
        if (TextUtils.isEmpty(adUnit)) return;
        if (moPubNative == null) {
            moPubNative = new MoPubNative(BaseApplication.getAppContext(), adUnit, this);
            MopubNativeRenderUtils.initAdRender(moPubNative);
        }

        try {
            isLoading = true;
            RequestParameters mRequestParameters = new RequestParameters.Builder()
                    .desiredAssets(desiredAssets)
                    .build();
            moPubNative.makeRequest(mRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
            isLoading = false;
        }
    }

    // region Event callback load Ads
    @Override public void onNativeLoad(NativeAd nativeAd) {
        Log.d("MopubNativeManager", "onNativeLoad");
        hasLoadAds = true;
        isLoading = false;
        nativeAds.add(nativeAd);
        if (iAdLoaderCallback != null)
            iAdLoaderCallback.onAdsLoaded();
        _checkAndLoadAds(false);
    }

    @Override public void onNativeFail(NativeErrorCode errorCode) {
        Log.d("MopubNativeManager", "MOPUB Load native ads => onNativeFail: " + errorCode);
        isSkipWaitForComplete = true;
        isLoading = false;
        hasLoadAds = true;
//        if (countLoadError < 3) {
//            _checkAndLoadAds(true);
//        } else
            if (iAdLoaderCallback != null)
            iAdLoaderCallback.onAdsFailedToLoad();
    }
    //endregion

    //region utils

    public static INativeManager getInstance() {
        return BaseApplicationContainAds.getNativeManagerInstance();
    }

    private static final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
            RequestParameters.NativeAdAsset.TITLE,
            RequestParameters.NativeAdAsset.TEXT,
            RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
            RequestParameters.NativeAdAsset.MAIN_IMAGE,
            RequestParameters.NativeAdAsset.ICON_IMAGE,
            RequestParameters.NativeAdAsset.STAR_RATING
    );
    private static final int NO_ADS_LOAD = 1;

    public boolean isCaching(){
        return isLoading;
    }
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
