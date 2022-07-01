package com.my.rn.ads.mopub.ad_native;

import android.content.Context;
import android.view.ViewGroup;

import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.INativeManager;

public class MopubNativeManager implements INativeManager {

    @Override public NativeViewUtils createNewAds(Context context, int typeAds, final ViewGroup nativeAdContainer,
                                                   final IAdLoaderCallback loaderCallback) {
        NativeViewUtils nativeViewUtils = new NativeViewUtils();
        nativeViewUtils.setAdsCallback(loaderCallback);
        nativeViewUtils.startLoadAndDisplayAds(typeAds, context, nativeAdContainer);
        return nativeViewUtils;
    }
}

//    private MoPubNative moPubNative;
//    private boolean isLoading = false;
//    private boolean hasLoadAds;
//    private boolean isSkipWaitForComplete = false;
//    private Queue<NativeAd> nativeAds = new LinkedList<>(); // Sẽ chứa NO_ADS_LOAD Ads
//    private static final String TAG = "MOPUB_NATIVE";
//    private @Nullable IAdLoaderCallback iAdLoaderCallback;
//    ManagerLoaderCallback managerLoaderCallback = new ManagerLoaderCallback();
//
//    public void setiAdLoaderCallback(IAdLoaderCallback iAdLoaderCallback) {
//        this.iAdLoaderCallback = iAdLoaderCallback;
//    }
//
//    private String getAdUnitID() {
//        if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_NATIVE != null)
//            return "11a17b188668469fb0412708c3d16813";
//        String saveKey = AdsSetting.getNativeLargeKey(AdsSetting.ID_MOPUB);
//        if (saveKey != null) return saveKey;
//        return KeysAds.MOPUB_NATIVE;
//    }
//    @Override public void loadAds(Activity activity, IAdLoaderCallback loaderCallback) {
//        if (nativeAds.size() > 0) {
//            loaderCallback.onAdsLoaded();
//            return;
//        }
//        managerLoaderCallback.registerCallback(loaderCallback, 12000);
//        checkAndLoadAds(activity);
//    }
//
//    @Override public void checkAndLoadAds(Activity activity) {
//        _checkAndLoadAds(false);
//    }
//
//    private void _checkAndLoadAds(boolean isNotResetCountError) {
//        if (!BaseUtils.isOnline()) {
//            isLoading = false;
//            onNativeFail(NativeErrorCode.CONNECTION_ERROR);
//            return;
//        }
//        if (isLoading || nativeAds.size() >= NO_ADS_LOAD) return;
//        if (MopubInitUtils.getInstance().isInited()) {
//            excuteLoadNativeAds();
//            return;
//        }
//        BaseApplication.getHandler().post(new Runnable() {
//            @Override public void run() {
//                MopubInitUtils.getInstance().initAds(null, new IAdInitCallback() {
//                    @Override public void didInitialise() {
//                        excuteLoadNativeAds();
//                    }
//
//                    @Override public void didFailToInitialise() {
//                        isLoading = false;
//                        isSkipWaitForComplete = true;
//                    }
//                });
//            }
//        });
//    }
//
//    private void excuteLoadNativeAds() {
//        String adUnit = getAdUnitID();
//        if (TextUtils.isEmpty(adUnit)) return;
//        if (moPubNative == null) {
//            moPubNative = new MoPubNative(BaseApplication.getAppContext(), adUnit, this);
//            MopubNativeRenderUtils.initAdRender(moPubNative);
//        }
//
//        try {
//            isLoading = true;
//            final RequestParameters mRequestParameters = new RequestParameters.Builder()
//                    .desiredAssets(desiredAssets)
//                    .build();
//            BaseApplication.getHandler().post(new Runnable() {
//                @Override public void run() {
//                    moPubNative.makeRequest(mRequestParameters);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            isLoading = false;
//        }
//    }
//
//    // region Event callback load Ads
//    @Override public void onNativeLoad(NativeAd nativeAd) {
//        Log.d("MopubNativeManager", "onNativeLoad");
//        hasLoadAds = true;
//        isLoading = false;
//        nativeAds.add(nativeAd);
//        if (iAdLoaderCallback != null)
//            iAdLoaderCallback.onAdsLoaded();
//        managerLoaderCallback.dispatchLoadedCallback();
//        _checkAndLoadAds(false);
//    }
//
//    @Override public void onNativeFail(NativeErrorCode errorCode) {
//        Log.d("MopubNativeManager", "MOPUB Load native ads => onNativeFail: " + errorCode);
//        isSkipWaitForComplete = true;
//        isLoading = false;
//        hasLoadAds = true;
////        if (countLoadError < 3) {
////            _checkAndLoadAds(true);
////        } else
////        managerLoaderCallback.dispatchLoadFailCallback();
//        if (iAdLoaderCallback != null)
//            iAdLoaderCallback.onAdsFailedToLoad();
//    }
//    //endregion
//
//    //region utils
//
//    public static INativeManager getInstance() {
//        return BaseApplicationContainAds.getNativeManagerInstance();
//    }
//
//    private static final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
//            RequestParameters.NativeAdAsset.TITLE,
//            RequestParameters.NativeAdAsset.TEXT,
//            RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
//            RequestParameters.NativeAdAsset.MAIN_IMAGE,
//            RequestParameters.NativeAdAsset.ICON_IMAGE,
//            RequestParameters.NativeAdAsset.STAR_RATING
//    );
//    private static final int NO_ADS_LOAD = 1;
//
//    public boolean isCaching() {
//        return isLoading;
//    }
//
//    public boolean isCached() {
//        try {
//            return !nativeAds.isEmpty();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    //endregion
//}