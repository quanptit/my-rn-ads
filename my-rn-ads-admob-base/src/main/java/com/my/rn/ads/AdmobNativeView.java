package com.my.rn.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.L;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.my.rn.ads.admob.R;
import com.my.rn.ads.settings.AdsSetting;

@SuppressLint("ViewConstructor") public class AdmobNativeView extends BaseNativeView {
    private NativeAd nativeAd;

    public AdmobNativeView(Context context, int typeAds, IAdLoaderCallback loaderCallback) {
        super(context, typeAds, loaderCallback);
    }

    @Override
    protected void loadAds(int typeAds, String adUnit) {
        Context context = getContext();
        MobileAds.initialize(context);
        AdLoader.Builder builder = new AdLoader.Builder(context, adUnit);
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions)
                .setRequestCustomMuteThisAd(true)
                .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                .build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder
                .forNativeAd(nativeAd -> {
                    if (isDestroyed) return;
                    if (AdmobNativeView.this.nativeAd != null)
                        AdmobNativeView.this.nativeAd.destroy();
                    AdmobNativeView.this.nativeAd = nativeAd;
                    int layoutId = AdsSetting.isNativeBanner(typeAds) ? R.layout.admob_native_banner : R.layout.admob_native_medium;
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(context)
                            .inflate(layoutId, AdmobNativeView.this, false);
                    populateNativeAdView(nativeAd, adView);
                    AdmobNativeView.this.removeAllViews();
                    AdmobNativeView.this.addView(adView);
                }).withAdListener(new AdListener() {
                    @Override public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        L.d("Admob Native onAdFailedToLoad: " + loadAdError.getMessage());
                        AdmobNativeView.this.onAdsFailedToLoad();
                    }

                    @Override public void onAdLoaded() {
                        AdmobNativeView.this.onAdsLoaded();
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override protected void destroyAds() {
        if (AdmobNativeView.this.nativeAd != null)
            AdmobNativeView.this.nativeAd.destroy();
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        View layoutRate = adView.findViewById(R.id.layoutRate);
        TextView adPriceTextView = adView.findViewById(R.id.ad_price);
        TextView adStoreTextView = adView.findViewById(R.id.ad_store);
        RatingBar adStarsView = adView.findViewById(R.id.ad_stars);
        TextView adBodyTextView = adView.findViewById(R.id.ad_body);
        TextView adHeadlineTextView = adView.findViewById(R.id.ad_headline);
        TextView adCallActionTextView = adView.findViewById(R.id.ad_call_to_action);
        ImageView adIconView = adView.findViewById(R.id.ad_app_icon);
        MediaView adMediaView = adView.findViewById(R.id.ad_media);
        TextView adAdvertiserView = adView.findViewById(R.id.ad_advertiser);

        adView.setPriceView(adPriceTextView);
        adView.setStoreView(adStoreTextView);
        adView.setStarRatingView(adStarsView);
        adView.setBodyView(adBodyTextView);
        adView.setHeadlineView(adHeadlineTextView);
        adView.setCallToActionView(adCallActionTextView);
        adView.setIconView(adIconView);
        adView.setMediaView(adMediaView);
        adView.setAdvertiserView(adAdvertiserView);

        // Title
        adHeadlineTextView.setText(nativeAd.getHeadline());

        //body
        if (layoutRate==null){ // Native banner
            adBodyTextView.setText(nativeAd.getBody());
        }else{
            if (nativeAd.getBody() == null) {
                adBodyTextView.setVisibility(View.GONE);
                if (layoutRate != null)
                    layoutRate.setVisibility(VISIBLE);
                int countElementShow = 0;
                if (setTextOrGONE(adAdvertiserView, nativeAd.getAdvertiser()))
                    countElementShow++;
                if (adStarsView != null) {
                    if (nativeAd.getStarRating() == null) {
                        adStarsView.setVisibility(View.GONE);
                    } else {
                        adStarsView.setRating(nativeAd.getStarRating().floatValue());
                        adStarsView.setVisibility(View.VISIBLE);
                        countElementShow++;
                    }
                }
                if (countElementShow < 2 && setTextOrGONE(adStoreTextView, nativeAd.getStore()))
                    countElementShow++;

                if (countElementShow < 2 && setTextOrGONE(adPriceTextView, nativeAd.getPrice()))
                    countElementShow++;
            } else {
                adBodyTextView.setVisibility(View.VISIBLE);
                if (layoutRate != null)
                    layoutRate.setVisibility(GONE);

                SpannableString adAtt = new SpannableString("Ad");
                BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.parseColor("#FFCC66"));
                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.WHITE);
                adAtt.setSpan(backgroundSpan, 0, adAtt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                adAtt.setSpan(foregroundSpan, 0, adAtt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                adAtt.setSpan(new StyleSpan(Typeface.BOLD), 0, adAtt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(adAtt);
                builder.append("  ");
                builder.append(nativeAd.getBody());
                adBodyTextView.setText(builder);
            }
        }

        if (adMediaView != null) {
            adMediaView.setImageScaleType(ImageView.ScaleType.CENTER);
            if (nativeAd.getMediaContent() != null)
                adMediaView.setMediaContent(nativeAd.getMediaContent());
        }
        if (nativeAd.getCallToAction() == null) {
            adCallActionTextView.setVisibility(View.GONE);
        } else {
            adCallActionTextView.setVisibility(View.VISIBLE);
            adCallActionTextView.setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adIconView.setVisibility(View.GONE);
        } else {
            adIconView.setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        adView.setNativeAd(nativeAd);

        disableClick(adView.getBodyView());
        disableClick(adView.getAdvertiserView());
        if (nativeAd.getCallToAction() != null) {
            disableClick(adView.getHeadlineView());
            disableClick(adView.getIconView());
            disableClick(adView.getMediaView());
        }
    }

    protected String getKeyAds(int typeAds) {
        String key = typeAds > 2 ? KeysAds.Admod_NATIVE : KeysAds.Admod_NATIVE_SMALL;
        if (TextUtils.isEmpty(key)) return null;
        if (KeysAds.isNeedShowTestAds()) return "ca-app-pub-3940256099942544/2247696110";
        return key;
    }

    static boolean setTextOrGONE(@Nullable TextView textView, @Nullable String str) {
        if (textView == null) return false;
        if (TextUtils.isEmpty(str)) {
            textView.setVisibility(GONE);
            return false;
        }
        textView.setText(str);
        return true;
    }

    static void disableClick(@Nullable View v) {
        if (v != null)
            v.setOnClickListener(null);
    }
}
