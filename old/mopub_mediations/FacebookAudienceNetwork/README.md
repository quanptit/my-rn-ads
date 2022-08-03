
### Cách cập nhật:
1. Replace toàn bộ code
2. Thay thế cái version trong file build.gradle 
3. So sánh file FacebookAdRenderer.java, 
FacebookNative.java, FacebookInterstitial.java và merge với local history.
    
### Các thay đổi so với bản gốc     

các thay đổi đều nằm trong block của file:
  
**FacebookNative.java**
```java
1. Add method
    // My Code ============
    private static void registerChildViewsForInteraction(final View view, final NativeAdBase nativeAdBase,
                                                         final MediaView mediaView, final MediaView adIconView, List<View> clickableViews) {
        if (nativeAdBase == null) {
            return;
        }
        if (nativeAdBase instanceof NativeAd && mediaView != null) {
            NativeAd nativeAd = (NativeAd) nativeAdBase;
            nativeAd.registerViewForInteraction(view, mediaView, adIconView, clickableViews);
        } else if (nativeAdBase instanceof NativeBannerAd) {
            NativeBannerAd nativeBannerAd = (NativeBannerAd) nativeAdBase;
            nativeBannerAd.registerViewForInteraction(view, adIconView, clickableViews);
        }
    }
    //==========

2.

        /**
         * Returns the Sponsored Label associated with this ad.
         */
        @Nullable final public String getSponsoredName() {
            //====== My code
            String result = mNativeAd.getSponsoredTranslation();
            if (result == null) result = "Sponsored";
            return result;
            //======
            //return mNativeAd instanceof NativeBannerAd ? mNativeAd.getSponsoredTranslation() : null;
        }
```


**FacebookAdRenderer.java**
```java

// 1.

private void update(final FacebookNativeViewHolder facebookNativeViewHolder,
                        final FacebookNative.FacebookNativeAd nativeAd) {
...........
        NativeRendererHelper.addTextView(facebookNativeViewHolder.getSponsoredLabelView(),
                nativeAd.getSponsoredName());
        //====== My code
        NativeRendererHelper.addTextView(facebookNativeViewHolder.getAdSocialContextView(), nativeAd.getFacebookNativeAd().getAdSocialContext());
        List<View> clickableViews = new ArrayList<>();
        if (facebookNativeViewHolder.getCallToActionView() != null)
            clickableViews.add(facebookNativeViewHolder.getCallToActionView());
        if (clickableViews.size() > 0)
            nativeAd.registerChildViewsForInteraction(facebookNativeViewHolder.getMainView(),
                    facebookNativeViewHolder.getMediaView(), facebookNativeViewHolder.getAdIconView(), clickableViews);
        else
            nativeAd.registerChildViewsForInteraction(facebookNativeViewHolder.getMainView(),
                    facebookNativeViewHolder.getMediaView(), facebookNativeViewHolder.getAdIconView());
        //============
....
}


// 2. 

static class FacebookNativeViewHolder {
..........
		@Nullable
        private TextView sponsoredLabelView;

        // ========== My code
        @Nullable private TextView adSocialContextView;

        @Nullable
        public TextView getAdSocialContextView() {
            return adSocialContextView;
        }
        // ==============

        // Use fromViewBinder instead of a constructor
        private FacebookNativeViewHolder() {
        }
.......


//3.
static FacebookNativeViewHolder fromViewBinder(@Nullable final View view, ........
.............
			// ========== My code
            viewHolder.adSocialContextView = view.findViewById(facebookViewBinder.adSocialContextViewId);
            //===============

            return viewHolder;
        }

//4. 
    public static class FacebookViewBinder {
	..............
	    final int sponsoredLabelId;

        // ========== My code
        final int adSocialContextViewId;
        //==========

        private FacebookViewBinder(@NonNull final Builder builder) {
            Preconditions.checkNotNull(builder);
.................
            this.sponsoredLabelId = builder.sponsoredLabelId;
            // ========== My code
            this.adSocialContextViewId = builder.adSocialContextViewId;
            //==============
        }

        public static class Builder {

          ......................
			private int advertiserNameId;
            private int sponsoredLabelId;
            //======== My Code =======
            private int adSocialContextViewId;

            @NonNull
            public final Builder adSocialContextViewId(final int adSocialContextViewId) {
                this.adSocialContextViewId = adSocialContextViewId;
                return this;
            }
            //========================
```



