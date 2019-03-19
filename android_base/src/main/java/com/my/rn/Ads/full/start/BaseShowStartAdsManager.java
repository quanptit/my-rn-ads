package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.my.rn.Ads.*;
import com.my.rn.Ads.AdsUtils;

public abstract class BaseShowStartAdsManager {
    private static final String TAG = "ShowStartAdsManager";
    public static long timeCallShowStart;
    private Admob admobStart;
    private AdxStart adxStart;

    protected abstract void showStartAdsExtend(Activity activity, int type, IAdLoaderCallback iAdLoaderCallback);

    //region get & set
    public Admob getAdmob() {
        if (admobStart == null) admobStart = new Admob();
        return admobStart;
    }

    public AdxStart getAdx() {
        if (adxStart == null) adxStart = new AdxStart();
        return adxStart;
    }
    //endregion

    /**
     * gọi StartAds và init Mopub. callback sau khi init xong
     */
    public void showStartAds(final Activity activity, boolean isFullScreen) {
        Log.d(TAG, "showStartAds Call");
        if (KeysAds.IS_SKIP_START_ADS) {
            return;
        }

        SplashActivity.openActivity(activity, isFullScreen);
        try {
            if (activity == null || AdsUtils.isDoNotShowAds() || !BaseUtils.isOnline()) {
                Log.d(TAG, "ShowStartAdsManager Offline or Vip => Finish activity =============== " + (activity == null));
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override public void run() {
                        SplashActivity.finishActivity();
                    }
                }, 800);
                return;
            }

            timeCallShowStart = System.currentTimeMillis();
            final IAdLoaderCallback iAdLoaderCallback4 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    SplashActivity.finishActivity();
                    destroy();
                }
            };
            final IAdLoaderCallback iAdLoaderCallback3 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    showStartAds(activity, 3, iAdLoaderCallback4);
                }
            };

            final IAdLoaderCallback iAdLoaderCallback2 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    showStartAds(activity, 2, iAdLoaderCallback3);
                }
            };
            IAdLoaderCallback iAdLoaderCallback1 = new IAdLoaderCallback() {
                @Override public void onAdsFailedToLoad() {
                    showStartAds(activity, 1, iAdLoaderCallback2);
                }
            };

            showStartAds(activity, 0, iAdLoaderCallback1);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    private void showStartAds(Activity activity, int index, IAdLoaderCallback iAdLoaderCallback) {
        int type = ManagerTypeAdsShow.getTypeShowFullStart(index);
        switch (type) {
            case ManagerTypeAdsShow.TYPE_ADX:
                getAdx().showStartAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_ADMOB:
                getAdmob().showStartAds(activity, iAdLoaderCallback);
                break;
            default:
                showStartAdsExtend(activity, type, iAdLoaderCallback);
        }
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
    protected abstract void destroyExtends();
}
