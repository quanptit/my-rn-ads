package com.my.rn.Ads.full.start;

import android.app.Activity;
import com.mopub.common.SdkInitializationListener;
import com.my.rn.Ads.*;

public class ShowStartAdsManager extends BaseShowStartAdsManager {
    private Fb fb;
    private EpomStart epomStart;

    public ShowStartAdsManager() {
        fb = new Fb();
        epomStart = new EpomStart();
    }

    @Override protected void showStartAdsExtend(Activity activity, int type, IAdLoaderCallback iAdLoaderCallback) {
        switch (type) {
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


    @Override protected void destroyExtends() {
        try {
            if (epomStart != null) {
                epomStart.destroy();
                epomStart = null;
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
