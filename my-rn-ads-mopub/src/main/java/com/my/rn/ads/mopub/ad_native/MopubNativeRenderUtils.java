package com.my.rn.ads.mopub.ad_native;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mopub.nativeads.*;
import com.my.rn.ads.mopub.R;
import com.my.rn.ads.settings.AdsSetting;

class MopubNativeRenderUtils {
    public static void initAdRender(MoPubNative moPubNative) {
        moPubNative.registerAdRenderer(new MoPubVideoNativeAdRenderer(new MediaViewBinder.Builder(R.layout.v_native_video_mopub_ads)
                .mediaLayoutId(R.id.native_ad_video_view)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build()));

        moPubNative.registerAdRenderer(new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.v_native_static_mopub_ads)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build()));

        moPubNative.registerAdRenderer(new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.v_fb_native_ads_banner_large)
                        .textId(R.id.native_ad_body)
                        .mediaViewId(R.id.native_ad_media)
                        .adIconViewId(R.id.native_ad_icon)
                        .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                        .advertiserNameId(R.id.native_ad_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        .callToActionId(R.id.native_ad_call_to_action)
                        .adSocialContextViewId(R.id.native_ad_social_context)
                        .sponsoredNameId(R.id.native_ad_sponsored_label)
                        .build()));

        // Cái này sẽ cần viết lại để giới hạn vùng nhấp chuột chỉ có CTA button
        moPubNative.registerAdRenderer(new GooglePlayServicesAdRenderer(
                new MediaViewBinder.Builder(R.layout.admob_nativead_layout)
                        .titleId(R.id.title_textview)
                        .textId(R.id.body_textview)
                        .callToActionId(R.id.cta_button)
                        .iconImageId(R.id.icon_image_view)
                        .mediaLayoutId(R.id.native_ad_media_layout)
                        .addExtra(GooglePlayServicesAdRenderer.VIEW_BINDER_KEY_AD_CHOICES_ICON_CONTAINER,
                                R.id.adchoices_view)
                        .addExtra(GooglePlayServicesAdRenderer.VIEW_BINDER_KEY_STAR_RATING,
                                R.id.star_rating)
                        .addExtra(GooglePlayServicesAdRenderer.VIEW_BINDER_KEY_STORE,
                                R.id.store_textview)
                        .addExtra(GooglePlayServicesAdRenderer.VIEW_BINDER_KEY_ADVERTISER,
                                R.id.store_textview)
                        .build()));
    }

    public static View createAdView(Context context, NativeAd nativeAd, int typeAds, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View adView = null;
        MoPubAdRenderer render = nativeAd.getMoPubAdRenderer();
        if (typeAds != AdsSetting.TYPE_SUMMARY_LIST3 && typeAds != AdsSetting.TYPE_SUMMARY_LIST2 && typeAds != AdsSetting.TYPE_SUMMARY_LIST1) {
            if (render instanceof FacebookAdRenderer)
                adView = inflater.inflate(R.layout.v_fb_native_ads_voca_sample, parent, false);
            else if (render instanceof GooglePlayServicesAdRenderer)
                adView = ((GooglePlayServicesAdRenderer) render).createAdView(context, parent, R.layout.admob_nativead_layout_voca);
        } else {
            adView = nativeAd.createAdView(context, parent);
        }
        if (adView == null)
            adView = nativeAd.createAdView(context, parent);

        parent.removeAllViews();
        parent.addView(adView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        parent.requestLayout();
        nativeAd.prepare(adView);
        nativeAd.renderAdView(adView);
        return adView;
    }
}
