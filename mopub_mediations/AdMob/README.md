
 ## Cách cập nhật: ##
1. Replace toàn bộ code
2. Cập nhật cái version trong build.gradle cho đúng mới nhất. 
3. So sánh file sau và merge với local history.


```
GooglePlayServicesInterstitial.java,
GooglePlayServicesRewardedVideo.java, 
GooglePlayServicesNative.java
```

## Chi tiết như sau ##

**Thêm Test Device theo từng account. (Cái khai báo trong AppShareLibs)**

```
//GooglePlayServicesInterstitial.java
//GooglePlayServicesRewardedVideo.java
//GooglePlayServicesNative.java
Comment cái test device gốc của mopub lại.

//        if (!TextUtils.isEmpty(testDeviceId)) { 
//            requestConfigurationBuilder.setTestDeviceIds(Collections.singletonList(testDeviceId)); 
//        } 
        //====== My code 
        requestConfigurationBuilder.setTestDeviceIds(Arrays.asList(KeysAds.DEVICE_TESTS)); 
        //======

```

**GooglePlayServicesAdRenderer**
```
1. Thêm Hàm sau vào
public View createAdView(@NonNull Context context, @Nullable ViewGroup parent, int layoutId) { 
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false); 
        // Create a frame layout and add the inflated view as a child. This will allow us to add 
        // the Google native ad view into the view hierarchy at render time. 
        FrameLayout wrappingView = new FrameLayout(context); 
        wrappingView.setId(ID_WRAPPING_FRAME); 
        wrappingView.addView(view); 
        MoPubLog.log(CUSTOM, ADAPTER_NAME, "Ad view created."); 
        return wrappingView; 
}

2. Thêm code vào bên trên và bên dưới ở trong hàm "updateNativeAdview" cái code cũ của nó như sau:
private void updateNativeAdview(GooglePlayServicesNativeAd staticNativeAd, 
                                    GoogleStaticNativeViewHolder staticNativeViewHolder, 
                                    NativeAdView nativeAdView) {
    ............
    MediaView mediaview = new MediaView
    ......
    nativeAdView.setNativeAd(staticNativeAd.getNativeAd());
}

===> Code mới như sau:
private void updateNativeAdview(GooglePlayServicesNativeAd staticNativeAd, 
                                    GoogleStaticNativeViewHolder staticNativeViewHolder, 
                                    NativeAdView nativeAdView) { 
    ............ 
   
            //====== My code:  Chú ý thêm file RNAdmobMediaView. (chức năng measure vị trí chỉ sử dụng cho react native) 
            MediaView mediaview = new RNAdmobMediaView(nativeAdView.getContext()); 
            final RelativeLayout.LayoutParams fullParent = 
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); 
            fullParent.addRule(RelativeLayout.CENTER_IN_PARENT); 
            mediaview.setLayoutParams(fullParent); 
            //====================

    ............    
    //====== My code 
        if (staticNativeAd.getAdvertiser() == null) { 
            if (staticNativeViewHolder.mAdvertiserTextView != null) 
                staticNativeViewHolder.mAdvertiserTextView.setVisibility(View.GONE); 
        } else if (staticNativeViewHolder.mAdvertiserTextView != null) 
            staticNativeViewHolder.mAdvertiserTextView.setVisibility(View.VISIBLE); 
        if (staticNativeAd.getStore() == null) { 
            if (staticNativeViewHolder.mStoreTextView != null) 
                staticNativeViewHolder.mStoreTextView.setVisibility(View.GONE); 
        } else { 
            staticNativeViewHolder.mStoreTextView.setVisibility(View.VISIBLE); 
            NativeRendererHelper.addTextView( 
                    staticNativeViewHolder.mStoreTextView, staticNativeAd.getStore()); 
            nativeAdView.setStoreView(staticNativeViewHolder.mStoreTextView); 
        } 
        if (staticNativeViewHolder.mStarRatingTextView != null) { 
            if (staticNativeAd.getStarRating() != null && staticNativeAd.getStarRating() > 4) { 
                staticNativeViewHolder.mStarRatingTextView.setRating(staticNativeAd.getStarRating().floatValue()); 
                nativeAdView.setStarRatingView(staticNativeViewHolder.mStarRatingTextView); 
            } else 
                staticNativeViewHolder.mStarRatingTextView.setVisibility(View.GONE); 
        } 
        //=============
    nativeAdView.setNativeAd(staticNativeAd.getNativeAd()); 
    //====== My code: ngăn ko cho click vào những chỗ ko cần 
        if (staticNativeViewHolder.mTitleView != null) 
            staticNativeViewHolder.mTitleView.setOnClickListener(null); 
        if (staticNativeViewHolder.mIconImageView != null) 
            staticNativeViewHolder.mIconImageView.setOnClickListener(null); 
        if (staticNativeViewHolder.mTextView != null) 
            staticNativeViewHolder.mTextView.setOnClickListener(null); 
        if (staticNativeViewHolder.mAdvertiserTextView != null) 
            staticNativeViewHolder.mAdvertiserTextView.setOnClickListener(null); 
        if (staticNativeViewHolder.mStoreTextView != null) 
            staticNativeViewHolder.mStoreTextView.setOnClickListener(null);
        //========
}


// Thay cái mStarRatingTextView thành RatingBar. (Nguyên gốc là TextView)
private static class GoogleStaticNativeViewHolder {
.....
RatingBar mStarRatingTextView; // My Code
```


