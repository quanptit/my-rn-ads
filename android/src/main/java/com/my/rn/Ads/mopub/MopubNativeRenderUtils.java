package com.my.rn.Ads.mopub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.nativeads.*;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.R;

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
                        // Binding to new layouts from Facebook 4.99.0+
                        .mediaViewId(R.id.native_ad_media)
                        .adIconViewId(R.id.native_ad_icon)
                        .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                        .advertiserNameId(R.id.native_ad_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        // End of binding to new layouts
                        .callToActionId(R.id.native_ad_call_to_action)
                        .adSocialContextViewId(R.id.native_ad_social_context)
                        .adSponsoredLabelViewId(R.id.native_ad_sponsored_label)
                        .build()));
    }

    public static View createAdView(Context context, NativeAd nativeAd, int typeAds, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View adView;
        MoPubAdRenderer render = nativeAd.getMoPubAdRenderer();
        if (render instanceof FacebookAdRenderer) {
            if (typeAds == ManagerTypeAdsShow.TYPE_SUMMARY_LIST3)
                adView = inflater.inflate(R.layout.v_fb_native_ads_banner_large, parent, false);
            else
                adView = inflater.inflate(R.layout.v_fb_native_ads_voca_sample, parent, false);
        }else{
            adView = nativeAd.createAdView(context, parent);
        }

        parent.removeAllViews();
        if (adView != null) {
            parent.addView(adView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parent.requestLayout();
            nativeAd.prepare(adView);
            nativeAd.renderAdView(adView);
        }
        return adView;
    }
}
