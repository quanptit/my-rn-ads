package com.my.rn.ads.mopub.ad_native_banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.rn.ads.mopub.R;
import com.my.rn.ads.settings.AdsSetting;

class MopubNativeRenderBannerUtils {
//    public static void initAdRender(MoPubNative moPubNative) {
////        moPubNative.registerAdRenderer(new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.v_native_banner)
////                .iconImageId(R.id.native_icon_image)
////                .titleId(R.id.native_title)
////                .textId(R.id.native_text)
////                .callToActionId(R.id.native_cta)
////                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
////                .build()));
////
////        moPubNative.registerAdRenderer(new FacebookAdRenderer(
////                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.v_native_banner_fb)
////                        .advertiserNameId(R.id.native_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
////                        .textId(R.id.native_text)
//////                        .adSocialContextViewId(R.id.native_ad_social_context)
////                        .adIconViewId(R.id.native_icon_image)
////                        .adChoicesRelativeLayoutId(R.id.ad_choices_container)
////                        .callToActionId(R.id.native_cta)
////                        .build()));
////        moPubNative.registerAdRenderer(new GooglePlayServicesAdRenderer(
////                new GooglePlayServicesViewBinder.Builder(R.layout.v_native_banner_admob)
////                        .titleId(R.id.native_title)
////                        .textId(R.id.native_text)
////                        .callToActionId(R.id.native_cta)
////                        .iconImageId(R.id.native_icon_image)
////                        .build()));
//    }
//
//    public static View createAdView(Context context, NativeAd nativeAd, int typeAds, ViewGroup parent) {
//        View adView = nativeAd.createAdView(context, parent);
//        parent.removeAllViews();
//        parent.addView(adView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        parent.requestLayout();
//        nativeAd.prepare(adView);
//        nativeAd.renderAdView(adView);
//        return adView;
//    }
}
