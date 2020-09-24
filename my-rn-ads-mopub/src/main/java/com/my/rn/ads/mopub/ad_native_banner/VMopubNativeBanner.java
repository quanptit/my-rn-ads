package com.my.rn.ads.mopub.ad_native_banner;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.BaseUtils;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdLoaderCallback;
import com.my.rn.ads.mopub.MopubInitUtils;
import com.my.rn.ads.settings.AdsSetting;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class VMopubNativeBanner extends FrameLayout implements MoPubNative.MoPubNativeNetworkListener {
    private MoPubNative moPubNative;
    private int typeAds = AdsSetting.TYPE_NATIVE_BANNER;
    private NativeAd nativeAd;
    private IAdLoaderCallback iAdLoaderCallback;

    private String getAdUnitID() {
        if (KeysAds.IS_DEVELOPMENT && KeysAds.MOPUB_NATIVE_BANNER != null)
            return "11a17b188668469fb0412708c3d16813";
//        String saveKey = AdsSetting.getNativeLargeKey(AdsSetting.ID_MOPUB);
//        if (saveKey != null) return saveKey;
        return KeysAds.MOPUB_NATIVE_BANNER;
    }

    public void setDisplayAds(int typeAds, IAdLoaderCallback iAdLoaderCallback) {
        this.typeAds = typeAds;
        this.iAdLoaderCallback = iAdLoaderCallback;
        if (!BaseUtils.isOnline()) {
            onNativeFail(NativeErrorCode.CONNECTION_ERROR);
            return;
        }
        displayLoading();

        MopubInitUtils.getInstance().initAds(null, new IAdInitCallback() {
            @Override public void didInitialise() {
                excuteLoadNativeAds();
            }

            @Override public void didFailToInitialise() {
                onNativeFail(NativeErrorCode.UNSPECIFIED);
            }
        });
    }

    private void excuteLoadNativeAds() {
        String adUnit = getAdUnitID();
        if (TextUtils.isEmpty(adUnit)) return;
        if (moPubNative == null) {
            moPubNative = new MoPubNative(BaseApplication.getAppContext(), adUnit, this);
            Map<String, Object> localExtras = new HashMap<>();
            localExtras.put("native_banner", true);
            moPubNative.setLocalExtras(localExtras);

            MopubNativeRenderBannerUtils.initAdRender(moPubNative);
        }
        try {
            RequestParameters mRequestParameters = new RequestParameters.Builder()
                    .desiredAssets(desiredAssets)
                    .build();
            moPubNative.makeRequest(mRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
            onNativeFail(NativeErrorCode.UNSPECIFIED);
        }
    }

    //region callback native ads loaded
    @Override public void onNativeLoad(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
        MopubNativeRenderBannerUtils.createAdView(getContext(), nativeAd, this.typeAds, this);
        if (this.iAdLoaderCallback != null)
            this.iAdLoaderCallback.onAdsLoaded();
    }

    @Override public void onNativeFail(NativeErrorCode errorCode) {
        Log.d(TAG, "onNativeFail: " + errorCode.toString());
        if (this.iAdLoaderCallback != null)
            this.iAdLoaderCallback.onAdsFailedToLoad();
        else
            Log.d(TAG, "this.iAdLoaderCallback == NULL => FAIL ");
    }
    //endregion

    //region utils

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            this.iAdLoaderCallback = null;
            if (this.nativeAd != null)
                this.nativeAd.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
            RequestParameters.NativeAdAsset.TITLE,
            RequestParameters.NativeAdAsset.TEXT,
            RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
            RequestParameters.NativeAdAsset.ICON_IMAGE
    );
    private static final String TAG = "MopubNativeBanner";

    private void init(Context context) {

    }

    public VMopubNativeBanner(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VMopubNativeBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VMopubNativeBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void displayLoading() {
        this.removeAllViews();
        TextView tvLoading = new TextView(getContext());
        tvLoading.setText("Loading...");
        tvLoading.setGravity(Gravity.CENTER);
        this.addView(tvLoading, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, Gravity.CENTER));
    }
    //endregion
}
