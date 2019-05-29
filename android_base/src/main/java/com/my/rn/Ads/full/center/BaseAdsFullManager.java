package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.Promise;
import com.google.android.gms.ads.MobileAds;
import com.my.rn.Ads.AdsUtils;
import com.my.rn.Ads.BaseApplicationContainAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;

import java.lang.ref.WeakReference;

public abstract class BaseAdsFullManager {
    private AdmobCenter admobCenter;
    private ADXCenter adxCenter;

    //region get & set
    public ADXCenter getADXCenter() {
        if (adxCenter == null)
            adxCenter = new ADXCenter();
        return adxCenter;
    }

    public AdmobCenter getAdmobCenter() {
        if (admobCenter == null)
            admobCenter = new AdmobCenter();
        return admobCenter;
    }

    public BaseAdsFullManager() {
        try {
            if (com.appsharelib.KeysAds.getADMOB_APP_ID() != null)
                MobileAds.initialize(BaseApplication.getAppContext(), KeysAds.getADMOB_APP_ID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    /**
     * isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
     * if (!isShowed)
     * isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
     */
    protected abstract boolean showAdsCenterIfCache(final Activity activity, PromiseSaveObj promiseSaveObj) throws Exception;

    protected abstract void cacheAdsCenterExtend(Activity activity, boolean isFromStart, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback) throws Exception;

    // region center =============
    private void cacheAdsCenter(Activity activity, boolean isFromStart, int index, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        int typeAds = isFromStart ? ManagerTypeAdsShow.getTypeShowFullStart(index)
                : ManagerTypeAdsShow.getTypeShowFullCenter(index);
        try {
            switch (typeAds) {
                case ManagerTypeAdsShow.TYPE_ADMOB:
                    getAdmobCenter().loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                    break;
                case ManagerTypeAdsShow.TYPE_ADX:
                    getADXCenter().loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                    break;
                default:
                    cacheAdsCenterExtend(activity, isFromStart, typeAds, iAdLoaderCallback);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cacheAdsCenterFromStart(Activity activity, final Promise promise) {
        cacheAdsCenter(activity, true, false, promise);
    }

    public void cacheAdsCenter(Activity activity) {
        cacheAdsCenter(activity, false, false, null);
    }

    public void cacheAdsCenterSkipCheck(Activity activity) {
        cacheAdsCenter(activity, false, true, null);
    }

    public void cacheAdsCenter(final Activity activity, final boolean isFromStart, boolean skipCheck, @Nullable Promise promise) {
        final PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        if (activity == null) {
            L.e("cacheAdsCenter Error: activity NULL");
            promiseSaveObj.resolve(false);
            return;
        }
        boolean isShowAds;
        if (skipCheck)
            isShowAds = true;
        else
            isShowAds = !AdsUtils.isDoNotShowAds() && (canShowAdsCenter(true) || isFromStart);
        if (!isShowAds) {
            promiseSaveObj.resolve(false);
            return;
        }

        try {
            final IAdLoaderCallback iAdLoaderCallback5 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    promiseSaveObj.resolve(false);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            final IAdLoaderCallback iAdLoaderCallback4 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 4, iAdLoaderCallback5);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            final IAdLoaderCallback iAdLoaderCallback3 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 3, iAdLoaderCallback4);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 2, iAdLoaderCallback3);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };
            IAdLoaderCallback iAdLoaderCallback1 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 1, iAdLoaderCallback2);
                }

                @Override public void onAdsLoaded() {
                    promiseSaveObj.resolve(true);
                }
            };

            cacheAdsCenter(activity, isFromStart, 0, iAdLoaderCallback1);
        } catch (Exception e) {
            e.printStackTrace();
            promiseSaveObj.resolve(false);
        } catch (Error error) {
            error.printStackTrace();
            promiseSaveObj.resolve(false);
        }
    }

    public boolean showStartAds(Activity activity, final Promise promise) {
        return showAdsCenter(activity, true, false, promise);
    }

    public boolean showAdsCenter(Activity activity, final Promise promise) {
        return showAdsCenter(activity, false, promise);
    }

    public boolean showAdsCenter(final Activity activity, final boolean skipCheck, final Promise promise) {
        return showAdsCenter(activity, false, skipCheck, promise);
    }

    public boolean showAdsCenter(final Activity activity, final boolean isFromStart, final boolean skipCheck, final Promise promise) {
        PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);

        boolean isShowAds;
        if (skipCheck)
            isShowAds = true;
        else
            isShowAds = !AdsUtils.isDoNotShowAds() && (canShowAdsCenter(false) || isFromStart);
        if (!isShowAds) {
            promiseSaveObj.resolve(false);
            return false;
        }

        boolean isShowed;
        try {
            isShowed = showAdsCenterIfCache(activity, promiseSaveObj);
            if (!isShowed && admobCenter != null)
                isShowed = admobCenter.showAdsCenterIfCache(promiseSaveObj);
            if (!isShowed && adxCenter != null)
                isShowed = adxCenter.showAdsCenterIfCache(promiseSaveObj);

            if (!isShowed) { // Không có quảng cáo để show
                promiseSaveObj.resolve(false);
                cacheAdsCenter(activity, false, skipCheck, null);
            }
            return isShowed;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }
        promiseSaveObj.resolve(false);
        return false;
    }

    //endregion


    //region utils
    public void destroy() {
        if (mainActivityRef != null) {
            mainActivityRef.clear();
            mainActivityRef = null;
        }
        destroyExtend();
    }

    protected abstract void destroyExtend();

    private static boolean canShowAdsCenter(boolean checkForCache) {
//        if (true)
//            return true; //TODOs

        long lastTimeShowAds = PreferenceUtils.getLongSetting(KeysAds.LAST_TIME_SHOW_ADS, 0);
        long time = checkForCache ? 3 * 60 * 1000 : 5 * 60 * 1000;
        if (System.currentTimeMillis() - lastTimeShowAds > time)
            return true;
        return false;
    }


    public boolean isCachedCenter() {
        return isCachedCenterExtend()
                || (admobCenter != null && admobCenter.isCachedCenter())
                || (adxCenter != null && adxCenter.isCachedCenter());
    }

    protected abstract boolean isCachedCenterExtend();
    //endregion

    //region save activity ref
    public static BaseAdsFullManager getInstance() {
        return BaseApplicationContainAds.getInstance().getAdsFullManager();
    }

    private WeakReference<Activity> mainActivityRef = null;

    public static void setMainActivity(Activity activity) {
        getInstance().mainActivityRef = new WeakReference<>(activity);
    }

    public static Activity getMainActivity() {
        if (getInstance().mainActivityRef == null) return null;
        return getInstance().mainActivityRef.get();
    }
    //endregion
}
