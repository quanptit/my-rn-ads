package com.my.rn.Ads.full.center;

import android.app.Activity;

import android.support.annotation.Nullable;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.react.bridge.Promise;
import com.google.android.gms.ads.MobileAds;
import com.my.rn.Ads.ApplicationContainAds;
import com.my.rn.Ads.IAdLoaderCallback;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;

import java.lang.ref.WeakReference;

public class AdsFullManager {
    public static final boolean IS_DEVELOPER = false;
    public AdmobCenter admobCenter;
    public FbCenter fbFullAdsManager;
    private ADXCenter adxCenter;
    private MopubFullCenter mopubInterstitialManager;

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

    public static long timeCallShowStart;
    private WeakReference<Activity> mainActivityRef = null;


    public static AdsFullManager getInstance() {
        return ApplicationContainAds.getAdsFullManager();
    }

    public AdsFullManager() {
        fbFullAdsManager = new FbCenter();
        mopubInterstitialManager = new MopubFullCenter();
        try {
            if (com.appsharelib.KeysAds.getADMOB_APP_ID() != null)
                MobileAds.initialize(BaseApplication.getAppContext(), KeysAds.getADMOB_APP_ID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // region center =============
    private void cacheAdsCenter(Activity activity, int index, @Nullable IAdLoaderCallback iAdLoaderCallback) {
        int typeAds = ManagerTypeAdsShow.getTypeShowFullCenter(index);
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                mopubInterstitialManager.loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FB:
                fbFullAdsManager.loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_ADMOB:
                getAdmobCenter().loadCenterAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_ADX:
                getADXCenter().loadCenterAds(activity, iAdLoaderCallback);
                break;
            default:
                mopubInterstitialManager.loadCenterAds(activity, iAdLoaderCallback);
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
            if (!skipCheck && (isDoNotShowAds() || !canShowAdsCenter()))
                return;
            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    cacheAdsCenter(activity, 2, null);
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
        if (!skipCheck && (isDoNotShowAds() || !canShowAdsCenter())) {
            promiseSaveObj.resolve(false);
            return false;
        }
        boolean isShowed;
        try {
            isShowed = mopubInterstitialManager.showAdsCenterIfCache(promiseSaveObj);
            if (!isShowed)
                isShowed = fbFullAdsManager.showAdsCenterIfCache(promiseSaveObj);
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
        mopubInterstitialManager.destroy();
    }

    private static boolean canShowAdsCenter() {
//        if (true)
//            return true; //TOD

        long lastTimeShowAds = PreferenceUtils.getLongSetting(KeysAds.LAST_TIME_SHOW_ADS, 0);
        if (System.currentTimeMillis() - lastTimeShowAds > 5 * 60 * 1000)  // 1.5phut
            return true;
        return false;
    }

    public static boolean isDoNotShowAds() {
        if (IS_DEVELOPER) return true;
        if (PreferenceUtils.getBooleanSetting(KeysAds.REMOVE_ADS, false)) return true;
        return false;
    }

    public boolean isCachedCenter() {
        return mopubInterstitialManager.isCachedCenter() ||
                fbFullAdsManager.isCachedCenter()
                || (admobCenter != null && admobCenter.isCachedCenter())
                || (adxCenter != null && adxCenter.isCachedCenter());
    }

    public static void setMainActivity(Activity activity) {
        getInstance().mainActivityRef = new WeakReference<>(activity);
    }

    public static Activity getMainActivity() {
        if (getInstance().mainActivityRef == null) return null;
        return getInstance().mainActivityRef.get();
    }
    //endregion
}
