package com.my.rn.Ads.full.start;

import android.app.Activity;
import com.my.rn.Ads.*;

public class ShowStartAdsManager extends BaseShowStartAdsManager {
    private Fb fb;
    private ChocolateStart chocolateStart;

    public ShowStartAdsManager() {
        fb = new Fb();
        chocolateStart = new ChocolateStart();
    }

    @Override protected void showStartAdsExtend(Activity activity, int type, IAdLoaderCallback iAdLoaderCallback) {
        switch (type) {
            case ManagerTypeAdsShow.TYPE_MOPUB:
                chocolateStart.showStartAds(activity, iAdLoaderCallback);
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
            if (fb != null) {
                fb.destroy();
                fb = null;
            }
            if (chocolateStart != null) {
                chocolateStart.destroy();
                chocolateStart = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
