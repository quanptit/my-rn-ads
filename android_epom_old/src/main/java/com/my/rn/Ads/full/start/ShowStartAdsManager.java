package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.my.rn.Ads.*;
import com.my.rn.Ads.full.center.AdsFullManager;

public class ShowStartAdsManager {
    private static final String TAG = "ShowStartAdsManager";
    private Fb fb;
    private AdxStart adx;
    private EpomStart epomStart;

    public ShowStartAdsManager() {
        fb = new Fb();
        adx = new AdxStart();
        epomStart = new EpomStart();
    }

    /**
     * gọi StartAds và init Mopub. callback sau khi init xong
     */
    public void showStartAds(final Activity activity, boolean isFullScreen) {
        Log.d(TAG, "showStartAds Call");
        SplashActivity.openActivity(activity, isFullScreen);
        try {
            if (activity == null || AdsFullManager.isDoNotShowAds() || !BaseUtils.isOnline()) {
                Log.d(TAG, "ShowStartAdsManager Offline or Vip => Finish activity =============== " + (activity == null));
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override public void run() {
                        SplashActivity.finishActivity();
                    }
                }, 800);
                return;
            }

            AdsFullManager.timeCallShowStart = System.currentTimeMillis();
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

            if (KeysAds.IS_SKIP_START_ADS) {
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override public void run() {
                        SplashActivity.finishActivity();
                    }
                }, 800);
                return;
            }

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
                adx.showStartAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_MOPUB:
                epomStart.showStartAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FB:
                fb.showStartAds(activity, iAdLoaderCallback);
                break;
            default:
                if (iAdLoaderCallback != null)
                    iAdLoaderCallback.onAdsFailedToLoad();
                break;
        }
    }

    public void destroy() {
        try {
            if (adx != null) {
                adx.destroy();
                adx = null;
            }
            if (fb != null) {
                fb.destroy();
                fb = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
