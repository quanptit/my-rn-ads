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
    protected abstract boolean showAdsCenterIfCache(PromiseSaveObj promiseSaveObj);

    protected abstract void cacheAdsCenterExtend(Activity activity, int typeAds, @Nullable IAdLoaderCallback iAdLoaderCallback);

    // region center =============
    private void cacheAdsCenter(Activity activity, int index, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        int typeAds = ManagerTypeAdsShow.getTypeShowFullCenter(index);
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_ADMOB:
                getAdmobCenter().loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_ADX:
                getADXCenter().loadCenterAds(activity, iAdLoaderCallback);
                break;
            default:
                cacheAdsCenterExtend(activity, typeAds, iAdLoaderCallback);
                break;
        }
    }

    public void cacheAdsCenter(Activity activity) {
        cacheAdsCenter(activity, false);
    }

    public void cacheAdsCenter(final Activity activity, boolean skipCheck) {
        if (activity == null) {
            L.e("cacheAdsCenter Error: activity NULL");
            return;
        }
        try {
            if (!skipCheck && (AdsUtils.isDoNotShowAds() || !canShowAdsCenter()))
                return;
            final IAdLoaderCallback iAdLoaderCallback3 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, 3, null);
                }
            };
            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, 2, iAdLoaderCallback3);
                }
            };
            IAdLoaderCallback iAdLoaderCallback1 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, 1, iAdLoaderCallback2);
                }
            };

            cacheAdsCenter(activity, 0, iAdLoaderCallback1);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {error.printStackTrace();}
    }

    public boolean showAdsCenter(Activity activity, final Promise promise) {
        return showAdsCenter(activity, false, promise);
    }

    public boolean showAdsCenter(final Activity activity, final boolean skipCheck, final Promise promise) {
        PromiseSaveObj promiseSaveObj = new PromiseSaveObj(promise);
        if (!skipCheck && (AdsUtils.isDoNotShowAds() || !canShowAdsCenter())) {
            promiseSaveObj.resolve(false);
            return false;
        }
        boolean isShowed;
        try {
            isShowed = showAdsCenterIfCache(promiseSaveObj);
            if (!isShowed && admobCenter != null)
                isShowed = admobCenter.showAdsCenterIfCache(promiseSaveObj);
            if (!isShowed && adxCenter != null)
                isShowed = adxCenter.showAdsCenterIfCache(promiseSaveObj);

            if (!isShowed) { // Không có quảng cáo để show
                promiseSaveObj.resolve(false);
                cacheAdsCenter(activity, skipCheck);
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

    private static boolean canShowAdsCenter() {
//        if (true)
//            return true; //TODOs

        long lastTimeShowAds = PreferenceUtils.getLongSetting(KeysAds.LAST_TIME_SHOW_ADS, 0);
        if (System.currentTimeMillis() - lastTimeShowAds > 5 * 60 * 1000)  // 1.5phut
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
