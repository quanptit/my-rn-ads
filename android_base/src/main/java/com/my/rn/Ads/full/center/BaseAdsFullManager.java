package com.my.rn.ads.full.center;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.baseLibs.utils.L;
import com.my.rn.ads.AdsUtils;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.IAdsCalbackOpen;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;
import com.my.rn.ads.settings.AdsSetting;

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

    //endregion

    /**
     * isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
     * if (!isShowed)
     * isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
     */
    protected abstract boolean showAdsCenterIfCache(final Activity activity, IAdsCalbackOpen iAdsCalbackOpen) throws Exception;

    protected abstract void cacheAdsCenterExtend(Activity activity, boolean isFromStart, String idAds, @Nullable IAdLoaderCallback iAdLoaderCallback) throws Exception;

    // region center =============
    private void cacheAdsCenter(Activity activity, boolean isFromStart, int index, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        String typeAds = isFromStart ? AdsSetting.getInstance().getTypeShowFullStart(index)
                : AdsSetting.getInstance().getTypeShowFullCenter(index);
        if (typeAds == null) {
            if (iAdLoaderCallback != null)
                iAdLoaderCallback.onAdsFailedToLoad();
            return;
        }
        try {
            switch (typeAds) {
                case AdsSetting.ID_ADMOB:
                    getAdmobCenter().loadCenterAds(activity, isFromStart, iAdLoaderCallback);
                    break;
                case AdsSetting.ID_ADX:
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

    public void cacheAdsCenter(Activity activity) {
        cacheAdsCenter(activity, false, false, null);
    }

    public void cacheAdsCenterSkipCheck(Activity activity) {
        cacheAdsCenter(activity, false, true, null);
    }

    public void cacheAdsCenter(final Activity activity, final boolean isFromStart, boolean skipCheckCanShowAds,
                               @Nullable final IAdLoaderCallback loaderCallback) {
        if (activity == null) {
            L.e("cacheAdsCenter Error: activity NULL");
            if (loaderCallback != null) loaderCallback.onAdsFailedToLoad();
            return;
        }
        boolean isShowAds;
        if (skipCheckCanShowAds)
            isShowAds = true;
        else
            isShowAds = !AdsUtils.isDoNotShowAds() && (canShowAdsCenter(true) || isFromStart);
        if (!isShowAds) {
            if (loaderCallback != null) loaderCallback.onAdsFailedToLoad();
            return;
        }

        try {
            final IAdLoaderCallback iAdLoaderCallback5 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() { if (loaderCallback != null) loaderCallback.onAdsFailedToLoad(); }

                @Override public void onAdsLoaded() {
                    if (loaderCallback != null) loaderCallback.onAdsLoaded();
                }
            };
            final IAdLoaderCallback iAdLoaderCallback4 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 4, iAdLoaderCallback5);
                }

                @Override public void onAdsLoaded() {
                    if (loaderCallback != null) loaderCallback.onAdsLoaded();
                }
            };
            final IAdLoaderCallback iAdLoaderCallback3 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 3, iAdLoaderCallback4);
                }

                @Override public void onAdsLoaded() {
                    if (loaderCallback != null) loaderCallback.onAdsLoaded();
                }
            };
            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 2, iAdLoaderCallback3);
                }

                @Override public void onAdsLoaded() {
                    if (loaderCallback != null) loaderCallback.onAdsLoaded();
                }
            };
            IAdLoaderCallback iAdLoaderCallback1 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, isFromStart, 1, iAdLoaderCallback2);
                }

                @Override public void onAdsLoaded() {
                    if (loaderCallback != null) loaderCallback.onAdsLoaded();
                }
            };

            cacheAdsCenter(activity, isFromStart, 0, iAdLoaderCallback1);
        } catch (Exception e) {
            e.printStackTrace();
            if (loaderCallback != null) loaderCallback.onAdsFailedToLoad();
        } catch (Error error) {
            error.printStackTrace();
            if (loaderCallback != null) loaderCallback.onAdsFailedToLoad();
        }
    }

    public boolean showStartAds(Activity activity, final IAdsCalbackOpen promise) {
        return showAdsCenter(activity, true, false, promise);
    }

    public boolean showAdsCenter(Activity activity, final IAdsCalbackOpen promise) {
        return showAdsCenter(activity, false, promise);
    }

    public boolean showAdsCenter(final Activity activity, final boolean skipCheck, final IAdsCalbackOpen promise) {
        return showAdsCenter(activity, false, skipCheck, promise);
    }

    public boolean showAdsCenter(final Activity activity, final boolean isFromStart, final boolean skipCheck,
                                 final IAdsCalbackOpen promise) {
        boolean isShowAds;
        if (skipCheck)
            isShowAds = true;
        else
            isShowAds = !AdsUtils.isDoNotShowAds() && (canShowAdsCenter(false) || isFromStart);
        if (!isShowAds) {
            promise.noAdsCallback();
            return false;
        }

        boolean isShowed;
        try {
            isShowed = showAdsCenterIfCache(activity, promise);
            if (!isShowed && admobCenter != null)
                isShowed = admobCenter.showAdsCenterIfCache(activity, promise);
            if (!isShowed && adxCenter != null)
                isShowed = adxCenter.showAdsCenterIfCache(activity, promise);

            if (!isShowed) { // Không có quảng cáo để show
                promise.noAdsCallback();
                cacheAdsCenter(activity, false, skipCheck, null);
            }
            return isShowed;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }
        promise.noAdsCallback();
        return false;
    }

    //endregion

    //region utils
    public void destroy() {
        if (mainActivityRef != null) {
            mainActivityRef.clear();
            mainActivityRef = null;
        }
        if (admobCenter != null)
            admobCenter.destroy();
        if (adxCenter != null)
            adxCenter.destroy();
        destroyExtend();
    }

    public void destroyIgnoreMediation() {
        if (admobCenter != null)
            admobCenter.destroy();
        if (adxCenter != null)
            adxCenter.destroy();
        destroyIgnoreMediationExtend();
    }

    protected abstract void destroyIgnoreMediationExtend();

    protected abstract void destroyExtend();

    //checkForCache: kiểm tra điều kiện để cache ads. nếu false => Kiểm tra điều kiện để show ads
    private static boolean canShowAdsCenter(boolean checkForCache) {
//        if (true)
//            return true; //TODOs

        long lastTimeShowAds = PreferenceUtils.getLongSetting(KeysAds.LAST_TIME_SHOW_ADS, 0);
        long time = checkForCache ? 3 * 60 * 1000 : 5 * 60 * 1000;
        if (System.currentTimeMillis() - lastTimeShowAds > time)
            return true;
        return false;
    }

    public boolean isCachedCenter(Activity activity) {
        return isCachedCenterExtend(activity)
                || (admobCenter != null && admobCenter.isCachedCenter(activity))
                || (adxCenter != null && adxCenter.isCachedCenter(activity));
    }

    protected abstract boolean isCachedCenterExtend(Activity activity);

    protected abstract boolean isCachedByMediation(Activity activity);
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
