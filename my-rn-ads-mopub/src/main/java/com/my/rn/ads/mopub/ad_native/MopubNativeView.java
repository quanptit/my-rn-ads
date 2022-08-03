package com.my.rn.ads.mopub.ad_native;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.my.rn.ads.BaseNativeView;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.mopub.R;
import com.my.rn.ads.settings.AdsSetting;

@SuppressLint("ViewConstructor") public class MopubNativeView extends BaseNativeView {
    private static final String TAG = "MopubNativeView";
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;

    public MopubNativeView(Context context, int typeAds, IAdLoaderCallback loaderCallback) {
        super(context, typeAds, loaderCallback);
    }

    @Override
    protected void loadAds(int typeAds, String adUnit) {
        destroyAds();
        Context context = getContext();


        nativeAdLoader = new MaxNativeAdLoader(adUnit, context);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                L.d("onNativeAdLoaded: " + ad.getNetworkName() + ", isDestroyed = " + isDestroyed);
                if (isDestroyed) return;
                if (nativeAd != null)
                    nativeAdLoader.destroy(nativeAd);
                nativeAd = ad;
                try {
                    int native_title_color = getResources().getColor(R.color.native_title);
                    int native_body_color = getResources().getColor(R.color.native_body);
                    nativeAdView.getTitleTextView().setTextColor(native_title_color);
                    nativeAdView.getBodyTextView().setTextColor(native_body_color);
                    nativeAdView.getAdvertiserTextView().setTextColor(native_body_color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Add ad view to view.
                removeAllViews();
                addView(nativeAdView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                MopubNativeView.this.onAdsLoaded();
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                L.d("onNativeAdLoadFailed: " + error.getMessage());
                MopubNativeView.this.onAdsFailedToLoad();
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
            }
        });
        nativeAdLoader.loadAd();
    }

    @Override
    protected String getKeyAds(int typeAds) {
        // Test thấy lỗi tùm lum cái ni. nên bỏ qua. sử dụng FB và admob
        return AdsSetting.isNativeBanner(typeAds) ? null : KeysAds.MAX_NATIVE_LARGE;
    }

    @Override protected void destroyAds() {
        try {
            if (nativeAd != null)
                nativeAdLoader.destroy(nativeAd);
            if (nativeAdLoader != null)
                nativeAdLoader.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nativeAd = null;
        nativeAdLoader = null;
    }

    public static ColorStateList getSystemAttrColor(Context context,
                                                    int attr) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attr});
        ColorStateList color = a.getColorStateList(a.getIndex(0));
        a.recycle();
        return color;
    }
}
