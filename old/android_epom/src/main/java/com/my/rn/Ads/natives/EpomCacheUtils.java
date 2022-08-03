package com.my.rn.Ads.natives;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.adclient.android.sdk.nativeads.*;
import com.adclient.android.sdk.type.AdType;
import com.adclient.android.sdk.type.ParamsType;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.BaseUtils;
import com.facebook.react.bridge.UiThreadUtil;
import com.my.rn.Ads.modules.NativeAdsView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class EpomCacheUtils implements ClientNativeAdListener {
    private static final String TAG = "EPOM_NA_CACHE";
    private Context context;
    private AdClientNativeAd adClientNativeAd;
    //    private AdClientNativeAdRenderer adRenderer;
    private int layoutId;

    public EpomCacheUtils(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
    }

    public void showNewNativeAds(Activity activityParam, final NativeAdsView nativeAdsView, final FrameLayout parent) {
        if (!isCached()) {
            Log.e(TAG, "showNewNativeAds Fail not cache");
            return;
        }
        final Activity activity = activityParam != null ? activityParam : BaseUtils.getActivity(parent.getContext());
        if (activity == null) {
            Log.e(TAG, "showNewNativeAds Fail:  activity NULL ==========");
            return;
        }
        View view = adClientNativeAd.getView(activity);
        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
        adClientNativeAd.renderView(view);
        adClientNativeAd.registerImpressionsAndClicks(view);
        View rating = adClientNativeAd.getRenderer().getViewByType(view, AdClientNativeAdBinder.ViewType.RATING_VIEW);
        if (rating != null && rating.getVisibility() == View.INVISIBLE)
            rating.setVisibility(View.GONE);
        parent.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        parent.requestLayout();
        nativeAdsView.adClientNativeAd = adClientNativeAd;
        EpomCacheUtils.this.adClientNativeAd = null;

        // Cache New ads
        cache();
    }

    public boolean isCaching() {
        return adClientNativeAd != null && adClientNativeAd.isLoadingInProgress();
    }

    public boolean isCached() {
        return adClientNativeAd != null && adClientNativeAd.isAdLoaded();
    }

    public void cache() {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override public void run() {
                if (adClientNativeAd != null) {
                    if ((adClientNativeAd.isLoadingInProgress() || adClientNativeAd.isAdLoaded()))
                        return;
                    else
                        destroy();
                }
                AdClientNativeAdRenderer adRenderer = EpomNativeRenderUtils.createNativeAdRendererForLarge(context, layoutId);

                HashMap<ParamsType, Object> configuration = new HashMap<>();
                configuration.put(ParamsType.AD_PLACEMENT_KEY, KeysAds.EPOM_NATIVE);
                configuration.put(ParamsType.ADTYPE, AdType.NATIVE_AD.toString());
                configuration.put(ParamsType.AD_SERVER_URL, "https://appservestar.com/");
                configuration.put(ParamsType.REFRESH_INTERVAL, 0);
                adClientNativeAd = new AdClientNativeAd(context, configuration, adRenderer);
                adClientNativeAd.setClientNativeAdListener(EpomCacheUtils.this);
                adClientNativeAd.load();
            }
        });
    }

    @Override public void onFailedToReceiveAd(AdClientNativeAd adClientNativeAd, String message, boolean b) {
        Log.d(TAG, "onFailedToReceiveAd message = " + message);
        destroy();
    }

    @Override public void onImpressionAd(AdClientNativeAd adClientNativeAd, boolean b) {
        Log.d(TAG, "onImpressionAd");
    }

    @Override public void onClickedAd(AdClientNativeAd adClientNativeAd, boolean b) {
        Log.d(TAG, "onClickedAd");
    }

    @Override public void onLoadingAd(AdClientNativeAd adClientNativeAd, boolean isLoaded, String message, boolean callbackFromUIThread) {
        Log.d(TAG, "onLoadingAd isLoaded = " + isLoaded + ", message = " + message);
    }

    @Override public void onRefreshedAd(AdClientNativeAd adClientNativeAd, RefreshType refreshType, String message, boolean callbackFromUIThread) {
        Log.d(TAG, "onRefreshedAd message = " + message);
    }

    public void destroy() {
        try {
            if (adClientNativeAd != null) {
                adClientNativeAd.destroy();
                adClientNativeAd = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            adClientNativeAd = null;
        }
    }

}
