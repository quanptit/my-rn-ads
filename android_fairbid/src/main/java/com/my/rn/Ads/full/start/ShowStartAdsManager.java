package com.my.rn.Ads.full.start;

import android.app.Activity;
import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.facebook.react.bridge.UiThreadUtil;
import com.mopub.common.SdkInitializationListener;
import com.my.rn.Ads.*;

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
        if (TextUtils.isEmpty(KeysAds.getMOPUB_FULL_START())) {
            UiThreadUtil.runOnUiThread(new Runnable() {
                @Override public void run() {
                    iAdLoaderCallback.onAdsFailedToLoad();
                }
            });
            return;
        }
        ApplicationContainAds.getMopubInitUtils().initMopub(new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                if (mopubStart != null)
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
