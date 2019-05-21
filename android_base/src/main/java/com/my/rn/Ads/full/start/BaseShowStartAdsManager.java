package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import com.baseLibs.utils.BaseUtils;
import com.facebook.react.bridge.Promise;
import com.my.rn.Ads.*;
import com.my.rn.Ads.AdsUtils;
import com.my.rn.Ads.full.center.PromiseSaveObj;

public abstract class BaseShowStartAdsManager {
    private static final String TAG = "SHOW_START";
    private Admob admobStart;
    private AdxStart adxStart;

    protected abstract boolean showStartAdsExtend(Activity activity, PromiseSaveObj promiseSaveObj);

    protected abstract void loadStartAdssExtend(Activity activity, int type, IAdLoaderCallback iAdLoaderCallback);

    protected abstract boolean isCachedExtend();

    protected abstract void destroyExtends();

    //region get & set
    public Admob getAdmob() {
        if (admobStart == null) admobStart = new Admob();
        return admobStart;
    }

    public AdxStart getAdx() {
        if (adxStart == null) adxStart = new AdxStart();
        return adxStart;
    }

    public @Nullable static BaseShowStartAdsManager getInstance() {
        return BaseApplicationContainAds.getInstance().getShowStartAdsManager(false);
    }

    public static BaseShowStartAdsManager getInstanceNotNull() {
        return BaseApplicationContainAds.getInstance().getShowStartAdsManager(true);
    }
    //endregion

    // region load
    public void loadStartAds(final Activity activity, Promise promise) {
        if (activity == null || AdsUtils.isDoNotShowAds() || !BaseUtils.isOnline()) {
            Log.d(TAG, "SKIP show: offline, vip ... ");
            promise.resolve(false);
            BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
            return;
        }
        Log.d(TAG, "loadStartAds Start loading");
        final PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        try {
            final IAdLoaderCallback iAdLoaderCallback4 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    promiseSaveObj.resolve(false);
                    BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            final IAdLoaderCallback iAdLoaderCallback3 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    loadStartAds(activity, 3, iAdLoaderCallback4);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };

            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    loadStartAds(activity, 2, iAdLoaderCallback3);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            IAdLoaderCallback iAdLoaderCallback1 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    loadStartAds(activity, 1, iAdLoaderCallback2);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };

            loadStartAds(activity, 0, iAdLoaderCallback1);
        } catch (Exception e) {
            e.printStackTrace();
            promiseSaveObj.resolve(false);
            BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
        } catch (Error error) {
            error.printStackTrace();
            promiseSaveObj.resolve(false);
            BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
        }
    }

    private void loadStartAds(Activity activity, int index, IAdLoaderCallback iAdLoaderCallback) {
        try {
            int type = ManagerTypeAdsShow.getTypeShowFullStart(index);
            switch (type) {
                case ManagerTypeAdsShow.TYPE_ADX:
                    getAdx().loadStartAds(activity, iAdLoaderCallback);
                    break;
                case ManagerTypeAdsShow.TYPE_ADMOB:
                    getAdmob().loadStartAds(activity, iAdLoaderCallback);
                    break;
                default:
                    loadStartAdssExtend(activity, type, iAdLoaderCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            iAdLoaderCallback.onAdsFailedToLoad();
        }
    }

    public boolean isCached() {
        return isCachedExtend()
                || (adxStart != null && adxStart.isCached())
                || (admobStart != null && admobStart.isCached());
    }
    //endregion

    // Sử dụng cho trường hợp gọi ở center app
    public boolean showStartIfCacheAsCenter(final Activity activity, PromiseSaveObj promiseOpenObj) {
        if (!isCached()) return false;
        Log.d(TAG, "showStartIfCacheAsCenter");
        return showAds(activity, promiseOpenObj);
    }

    public void showStartIfCache(final Activity activity, Promise promise) {
        PromiseSaveObj promiseOpenObj = new PromiseSaveObj(promise);
        if (AdsUtils.isDoNotShowAds() || !isCached()) {
            promiseOpenObj.resolve(false);
            return;
        }
        if (showAds(activity, promiseOpenObj)) return;

        promiseOpenObj.resolve(false);
    }

    private boolean showAds(final Activity activity, PromiseSaveObj promiseOpenObj) {
        boolean isShowed = showStartAdsExtend(activity, promiseOpenObj);
        if (isShowed) return true;
        if (admobStart != null) {
            isShowed = admobStart.showAdsIfCache(promiseOpenObj);
            if (isShowed) return true;
        }
        if (adxStart != null) {
            isShowed = adxStart.showAdsIfCache(promiseOpenObj);
            if (isShowed) return true;
        }
        return false;
    }

    public void destroy() {
        try {
            if (admobStart != null) {
                admobStart.destroy();
                admobStart = null;
            }
            if (adxStart != null) {
                adxStart.destroy();
                adxStart = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        destroyExtends();
    }
}
