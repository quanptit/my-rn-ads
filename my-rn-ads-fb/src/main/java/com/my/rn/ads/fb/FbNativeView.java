package com.my.rn.ads.fb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.my.rn.ads.BaseNativeView;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.settings.AdsSetting;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor") public class FbNativeView extends BaseNativeView {

    private static final String TAG = "FbNativeView";
    private NativeAdBase nativeAd;

    public FbNativeView(Context context, int typeAds, IAdLoaderCallback loaderCallback) {
        super(context, typeAds, loaderCallback);
    }

    @Override
    protected void loadAds(int typeAds, String adUnit) {
        L.d("FbNativeView Load ads");
        boolean isInitialized = AudienceNetworkAds.isInitialized(BaseApplication.getAppContext());
        if (!isInitialized)
            AudienceNetworkAds.initialize(BaseApplication.getAppContext());
        destroyAds();

        Context context = getContext();
        if (AdsSetting.isNativeBanner(typeAds))
            nativeAd = new NativeBannerAd(context, adUnit);
        else
            nativeAd = new NativeAd(context, adUnit);

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                FbNativeView.this.onAdsFailedToLoad();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (isDestroyed) return;
                FbNativeView.this.onAdsLoaded();
                NativeAdLayout adView;
                if (ad instanceof NativeBannerAd)
                    adView = (NativeAdLayout) LayoutInflater.from(context).inflate(R.layout.fb_native_banner,
                            FbNativeView.this, false);
                else
                    adView = (NativeAdLayout) LayoutInflater.from(context).inflate(R.layout.fb_native_medium,
                            FbNativeView.this, false);
                populateNativeAdView(nativeAd, adView);
                FbNativeView.this.removeAllViews();
                FbNativeView.this.addView(adView);
            }

            //region hide unuse
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
            //endregion
        };
        // Request an ad
        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener)
                .build());
    }

    private void populateNativeAdView(NativeAdBase nativeAd, NativeAdLayout nativeAdLayout) {
        nativeAd.unregisterView();

        // Add the AdOptionsView
        RelativeLayout adChoicesContainer = nativeAdLayout.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(getContext(), nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = nativeAdLayout.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = nativeAdLayout.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = nativeAdLayout.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = nativeAdLayout.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdCallToAction = nativeAdLayout.findViewById(R.id.native_ad_call_to_action);

        MediaView nativeAdMedia = nativeAdLayout.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = nativeAdLayout.findViewById(R.id.native_ad_social_context);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());

        if (nativeAdSocialContext != null) {
            String s = nativeAd.getAdSocialContext();
            if (TextUtils.isEmpty(s)) {
                nativeAdLayout.findViewById(R.id.native_ad_sponsored_separate).setVisibility(View.GONE);
                nativeAdSocialContext.setVisibility(GONE);
            } else {
                nativeAdSocialContext.setText(s);
            }
        }

        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdCallToAction);

        if (nativeAd instanceof NativeAd) {
            ((NativeAd) nativeAd).registerViewForInteraction(nativeAdLayout, nativeAdMedia, nativeAdIcon, clickableViews);
        } else
            ((NativeBannerAd) nativeAd).registerViewForInteraction(nativeAdLayout, nativeAdIcon, clickableViews);
    }

    @Override
    protected String getKeyAds(int typeAds) {
        return AdsSetting.isNativeBanner(typeAds) ? KeysAds.FB_NATIVE_SMALL : KeysAds.FB_NATIVE;
    }

    @Override protected void destroyAds() {
        try {
            if (nativeAd != null)
                nativeAd.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nativeAd = null;
    }

}
