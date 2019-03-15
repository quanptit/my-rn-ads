package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.mopub.common.SdkInitializationListener;
import com.my.rn.Ads.*;
import com.my.rn.Ads.full.center.AdsFullManager;

public class ShowStartAdsManager {
    private static final String TAG = "ShowStartAdsManager";
    private Admob admob;
    private Fb fb;
    private AdxStart adx;
    private MopubStart mopubStart;

    public ShowStartAdsManager() {
        admob = new Admob();
        fb = new Fb();
        adx = new AdxStart();
        mopubStart = new MopubStart();
    }

    /**
     * gọi StartAds và init Mopub. callback sau khi init xong
     */
    public void showStartAds(final Activity activity) {
        Log.d(TAG, "showStartAds Call");
        SplashActivity.openActivity(activity);
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
                showMopubStart(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_FB:
                fb.showStartAds(activity, iAdLoaderCallback);
                break;
            case ManagerTypeAdsShow.TYPE_ADMOB:
                admob.showStartAds(activity, iAdLoaderCallback);
                break;
            default:
                showMopubStart(activity, iAdLoaderCallback);
                break;
        }
    }

    private synchronized void showMopubStart(final Activity activity, final IAdLoaderCallback iAdLoaderCallback) {
        ApplicationContainAds.getMopubInitUtils().initMopub(new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                mopubStart.showStartAds(activity, iAdLoaderCallback);
            }
        });
    }

    public void destroy() {
        try {
            if (admob != null) {
                admob.destroy();
                admob = null;
            }
            if (adx != null) {
                adx.destroy();
                adx = null;
            }
            if (fb != null) {
                fb.destroy();
                fb = null;
            }
            if (mopubStart != null) {
                mopubStart.destroy();
                mopubStart = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
