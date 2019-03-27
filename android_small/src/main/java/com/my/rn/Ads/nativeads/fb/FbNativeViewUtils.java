package com.my.rn.Ads.nativeads.fb;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.facebook.ads.*;
import com.my.rn.Ads.R;
import com.my.rn.Ads.ManagerTypeAdsShow;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class FbNativeViewUtils {
    /**
     * typeAds: AdsNativeObj.TYPE_...
     */
    public static View createNativeAdsView(Context context, NativeAdBase nativeAdBase, int typeAds) {
        if (nativeAdBase == null) return null;
        switch (typeAds) {
            case ManagerTypeAdsShow.TYPE_SUMMARY_LIST1:
                if (nativeAdBase instanceof NativeBannerAd) {
                    return displayTYPE_SUMMARY_LIST1(context, (NativeBannerAd) nativeAdBase);
                }
                break;
            case ManagerTypeAdsShow.TYPE_SUMMARY_LIST2:
                if (nativeAdBase instanceof NativeBannerAd) {
                    return displayTYPE_SUMMARY_LIST2(context, (NativeBannerAd) nativeAdBase);
                }
                break;
            case ManagerTypeAdsShow.TYPE_SUMMARY_LIST3:
                if (nativeAdBase instanceof NativeAd) {
                    return displayTYPE_SUMMARY_LIST3(context, (NativeAd) nativeAdBase);
                } else if (nativeAdBase instanceof NativeBannerAd) {
                    return displayTYPE_SUMMARY_LIST2(context, (NativeBannerAd) nativeAdBase);
                }
                break;
            case ManagerTypeAdsShow.TYPE_DETAIL_LIST1:
                if (nativeAdBase instanceof NativeAd)
                    return displayTYPE_DETAIL_LIST1(context, (NativeAd) nativeAdBase);
            case ManagerTypeAdsShow.TYPE_DETAIL_LIST2:
                if (nativeAdBase instanceof NativeAd) {
                    return displayTYPE_DETAIL_LIST2(context, (NativeAd) nativeAdBase);
                }
                break;
            case ManagerTypeAdsShow.TYPE_DETAIL_LIST3:
                if (nativeAdBase instanceof NativeAd) {
                    return displayTYPE_DETAIL_LIST3(context, (NativeAd) nativeAdBase);
                }
                break;
            default:
                break;
        }
        L.e("========= displayNativeAds ERROR typeAds Không phù hợp: " + typeAds + ", nativeAdBase instanceof NativeAd: " + (nativeAdBase instanceof NativeAd));
        return null;
    }

    //region === TYPE_SUMMARY_LIST ====
    private static View displayTYPE_SUMMARY_LIST1(Context context, NativeBannerAd nativeBannerAd) {
        nativeBannerAd.unregisterView();

        return NativeBannerAdView.render(context, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_100);
    }

    private static View displayTYPE_SUMMARY_LIST2(Context context, NativeBannerAd nativeBannerAd) {
        nativeBannerAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(BaseApplication.getAppContext());
        View adView = inflater.inflate(R.layout.v_fb_native_ads_small, null, false);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(context, nativeBannerAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());
        nativeAdBody.setText(nativeBannerAd.getAdBodyText());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
        return adView;
    }

    private static View displayTYPE_SUMMARY_LIST3(Context context, NativeAd nativeAd) {
        return createLargeNativeAds(context, nativeAd, R.layout.v_fb_native_ads_banner_large);
    }
    //endregion

    private static View createLargeNativeAds(Context context, NativeAd nativeAd, int layoutId) {
        nativeAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(BaseApplication.getAppContext());
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        View adView = inflater.inflate(layoutId, null, false);
        nativeAdLayout.addView(adView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        AdIconView adIconView = adView.findViewById(R.id.native_ad_icon);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        nativeAdMedia.requestLayout();

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        nativeAd.registerViewForInteraction(adView, nativeAdMedia, adIconView, clickableViews);
        return nativeAdLayout;
    }

    // region =========== TYPE_DETAIL_LIST ========
    private static View displayTYPE_DETAIL_LIST1(Context context, NativeAd nativeAd) {
        nativeAd.unregisterView();
        View adView = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_300);
        return adView;
    }

    private static View displayTYPE_DETAIL_LIST2(Context context, NativeAd nativeAd) {
        return createLargeNativeAds(context, nativeAd, R.layout.v_fb_native_ads);
    }

    /**
     * Giống cái Row voca
     */
    private static View displayTYPE_DETAIL_LIST3(Context context, NativeAd nativeAd) {
        return createLargeNativeAds(context, nativeAd, R.layout.v_fb_native_ads_voca_sample);
    }
    //endregion
}
