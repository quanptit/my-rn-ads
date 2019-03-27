package com.my.rn.Ads.chocolate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.my.rn.Ads.ApplicationContainAds;
import com.vdopia.ads.lw.*;

public class ChocolateFullManager implements LVDOInterstitialListener {
    private static final String TAG = "ChocolateFullManager";
    private LVDOInterstitialAd mInterstitialAd;
    private boolean isCaching;
    private LVDOInterstitialListener listener;

    public static ChocolateFullManager getInstance() {
        if (ApplicationContainAds.getInstance().chocoFullManager == null)
            ApplicationContainAds.getInstance().chocoFullManager = new ChocolateFullManager();
        return ApplicationContainAds.getInstance().chocoFullManager;
    }

    private LVDOAdRequest mAdRequest;

    private LVDOAdRequest getLvdoAdRequest(Activity context) {
        if (mAdRequest == null) {
            mAdRequest = new LVDOAdRequest(context);
            String packageName = BaseApplication.getAppContext().getPackageName();
            String storeUrl = "https://play.google.com/store/apps/details?id=" + packageName;
            mAdRequest.setAppStoreUrl(storeUrl);
            mAdRequest.setRequester(KeysAds.CHOCO_REQUESTER);
            mAdRequest.setAppDomain(packageName);
            mAdRequest.setAppName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
            mAdRequest.setCategory("IAB5");
            mAdRequest.setPublisherDomain(storeUrl);
        }
        return mAdRequest;
    }
//
//
//    publisherdomain=na. (pass this as app’s site domain)

    public void setListenner(LVDOInterstitialListener listener) {
        this.listener = listener;
    }

    public void loadAds(final Activity activity) {
        ChocolateInitUtils.getInstance().initChocolate(activity, new InitCallback() {
            @Override public void onSuccess() {
                if (mInterstitialAd != null && (isCaching || mInterstitialAd.isReady())) return;
                if (mInterstitialAd == null)
                    mInterstitialAd = new LVDOInterstitialAd(activity, KeysAds.CHOCO_APP_KEY, ChocolateFullManager.this);
                isCaching = true;
                mInterstitialAd.loadAd(getLvdoAdRequest(activity));
            }

            @Override public void onError(String s) {
            }
        });
    }

    public void showAdsIfCache() {
        if (isCache()) {
            Log.d(TAG, "mInterstitialAd.getWinningPartnerName(): " + mInterstitialAd.getWinningPartnerName());
            mInterstitialAd.show();
        }
    }

    public boolean isCache() {
        return mInterstitialAd != null && mInterstitialAd.isReady();
    }

    @Override public void onInterstitialLoaded(LVDOInterstitialAd lvdoInterstitialAd) {
        isCaching = false;
        if (this.listener != null)
            this.listener.onInterstitialLoaded(lvdoInterstitialAd);
    }

    @Override public void onInterstitialFailed(LVDOInterstitialAd lvdoInterstitialAd, LVDOConstants.LVDOErrorCode lvdoErrorCode) {
        isCaching = false;
        if (this.listener != null)
            this.listener.onInterstitialFailed(lvdoInterstitialAd, lvdoErrorCode);
    }

    @Override public void onInterstitialShown(LVDOInterstitialAd lvdoInterstitialAd) {
        if (this.listener != null)
            this.listener.onInterstitialShown(lvdoInterstitialAd);
    }

    @Override public void onInterstitialClicked(LVDOInterstitialAd lvdoInterstitialAd) {
        if (this.listener != null)
            this.listener.onInterstitialClicked(lvdoInterstitialAd);
    }

    @Override public void onInterstitialDismissed(LVDOInterstitialAd lvdoInterstitialAd) {
        if (this.listener != null)
            this.listener.onInterstitialDismissed(lvdoInterstitialAd);
    }

    public void onResume() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.resume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd.destroyView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
