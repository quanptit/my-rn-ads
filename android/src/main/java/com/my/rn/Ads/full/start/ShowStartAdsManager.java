package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.mopub.common.SdkInitializationListener;
import com.my.rn.Ads.*;
import com.my.rn.Ads.full.center.AdsFullManager;

public class ShowStartAdsManager extends BaseShowStartAdsManager {
    private Fb fb;
    private MopubStart mopubStart;

    public ShowStartAdsManager() {
        fb = new Fb();
        mopubStart = new MopubStart();
    }

    @Override protected void showStartAdsExtend(Activity activity, int type, IAdLoaderCallback iAdLoaderCallback) {
        switch (type) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                showMopubStart(activity, iAdLoaderCallback);
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

    private synchronized void showMopubStart(final Activity activity, final IAdLoaderCallback iAdLoaderCallback) {
        ApplicationContainAds.getMopubInitUtils().initMopub(new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                mopubStart.showStartAds(activity, iAdLoaderCallback);
            }
        });
    }

    @Override protected void destroyExtends() {
        try {
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
