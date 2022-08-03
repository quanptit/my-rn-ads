package com.my.rn.Ads.natives;

import android.content.Context;
import com.adclient.android.sdk.nativeads.AdClientNativeAdBinder;
import com.adclient.android.sdk.nativeads.AdClientNativeAdRenderer;
import com.my.rn.Ads.ManagerTypeAdsShow;
import com.my.rn.Ads.R;

import java.util.ArrayList;
import java.util.List;

public class EpomNativeRenderUtils {

//    public static AdClientNativeAdRenderer createNativeAdRenderer(Context context, int typeAds) {
//        return createNativeAdRendererForLarge(context, typeAds);
//    }

    public static AdClientNativeAdRenderer createNativeAdRendererForLarge(Context context, int layoutId) {
        AdClientNativeAdBinder binder = new AdClientNativeAdBinder(layoutId);
        binder.bindView(AdClientNativeAdBinder.ViewType.TITLE_TEXT_VIEW, R.id.native_ad_title);
        binder.bindView(AdClientNativeAdBinder.ViewType.DESCRIPTION_TEXT_VIEW, R.id.native_ad_body);

        binder.bindView(AdClientNativeAdBinder.ViewType.ICON_VIEW, R.id.native_ad_icon);
        binder.bindView(AdClientNativeAdBinder.ViewType.MEDIA_VIEW, R.id.native_ad_media);
        binder.bindView(AdClientNativeAdBinder.ViewType.CALL_TO_ACTION_VIEW, R.id.native_ad_call_to_action);

        binder.bindView(AdClientNativeAdBinder.ViewType.PRIVACY_ICON_VIEW, R.id.native_ad_privacy_information_icon);
        binder.bindView(AdClientNativeAdBinder.ViewType.SPONSORED_TEXT_VIEW, R.id.native_ad_sponsored_label);

        binder.bindView(AdClientNativeAdBinder.ViewType.RATING_VIEW, R.id.ratingBar);


        final List<Integer> clickItems = new ArrayList<>();
        clickItems.add(R.id.native_ad_call_to_action);
        binder.setClickableItems(clickItems);

        return new AdClientNativeAdRenderer(context, binder);
    }
}
