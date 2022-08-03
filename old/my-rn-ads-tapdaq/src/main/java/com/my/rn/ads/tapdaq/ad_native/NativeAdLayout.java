package com.my.rn.ads.tapdaq.ad_native;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.baseLibs.utils.L;
import com.my.rn.ads.tapdaq.R;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdImage;
import com.tapdaq.sdk.network.HttpClientBase;
import com.tapdaq.sdk.network.TClient;

public class NativeAdLayout extends RelativeLayout {
    private LayoutInflater mInflater;

    private FrameLayout mAdview;
    private TextView mTitleView;
    private TextView mBodyTextView;
    private TextView tvRating;
    private ImageView mImageView;
    private Button mButton;
    private FrameLayout mAdChoicesView;
    private FrameLayout mIconView;
    private TextView mStoreTextView;
    private RatingBar mStarRating;
    private RelativeLayout mMediaView;
    private TDMediatedNativeAd currentRenderAd;

    public NativeAdLayout(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public NativeAdLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public NativeAdLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View v = mInflater.inflate(R.layout.tapdaq_nativead_layout, this, true);
        mTitleView = v.findViewById(R.id.title_textview);
        mBodyTextView = v.findViewById(R.id.body_textview);
        tvRating = v.findViewById(R.id.tvRating);
        mButton = v.findViewById(R.id.cta_button);
        mAdChoicesView = v.findViewById(R.id.adchoices_view);
        mIconView = v.findViewById(R.id.icon_image_view);
        mStoreTextView = v.findViewById(R.id.store_textview);
        mStarRating = v.findViewById(R.id.star_rating_textview);
        mMediaView = v.findViewById(R.id.media_view);
        mAdview = v.findViewById(R.id.ad_view);
    }

    public void clear() {
        mTitleView.setText("");
        mBodyTextView.setText("");
//        mCaptionTextView.setText("");
        mButton.setText("");
        mStoreTextView.setText("");
//        mStarRating.setText("");

        mAdview.removeAllViews();
        mMediaView.removeAllViews();
        mAdChoicesView.removeAllViews();
        if (mImageView != null)
            mImageView.setImageBitmap(null);
        mIconView.removeAllViews();
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    //region render ads
    private void renderImageOrMedia(TDMediatedNativeAd ad) {
        mMediaView.removeAllViews();
        if (ad.getMediaView() != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            mMediaView.addView(ad.getMediaView(), params);
            if (ad.getVideoController() != null && ad.getVideoController().hasVideoContent())
                return;
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setBackgroundColor(Color.TRANSPARENT);
            mMediaView.addView(frameLayout, params);
            frameLayout.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    L.d("Click on image media");
                }
            });
            return;
        }
        if (ad.getImages() != null) {
            RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(CENTER_IN_PARENT);
            if (mImageView == null) {
                mImageView = new ImageView(getContext());
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            mMediaView.addView(mImageView, params);
            TDMediatedNativeAdImage image = ad.getImages().get(0);
            if (image.getDrawable() != null) {
                mImageView.setImageDrawable(ad.getImages().get(0).getDrawable());
            } else if (image.getUrl() != null) {
                new TClient().executeImageGET(getContext(), image.getUrl(), 0, 0, new HttpClientBase.ResponseImageHandler() {
                    @Override
                    public void onSuccess(Bitmap response) {
                        mImageView.setImageBitmap(response);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            return;
        }
    }
    //endregion

    public void populate(TDMediatedNativeAd ad) {
        if (this.currentRenderAd != null && this.currentRenderAd != ad)
            this.currentRenderAd.destroy();
        this.currentRenderAd = ad;
        clear();
        if (ad == null) return;

        if (ad.getAdView() != null)
            mAdview.addView(ad.getAdView());
        if (ad.getAdChoiceView() != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mAdChoicesView.addView(ad.getAdChoiceView(), params);
        }

        renderImageOrMedia(ad);

        mTitleView.setText(ad.getTitle());
        if (ad.getStarRating() > 4.2) {
            mStarRating.setVisibility(VISIBLE);
            tvRating.setVisibility(VISIBLE);
            mStarRating.setRating((float) ad.getStarRating());
            tvRating.setText(String.valueOf(ad.getStarRating()));
            LayerDrawable stars = (LayerDrawable) mStarRating.getProgressDrawable();
            setRatingStarColor(stars.getDrawable(2), Color.parseColor("#A05500"));
        } else {
            mStarRating.setVisibility(GONE);
            tvRating.setVisibility(GONE);
        }

        mBodyTextView.setText(ad.getBody());
        mButton.setText(ad.getCallToAction());
        if (ad.getStore() != null) {
            mStoreTextView.setVisibility(VISIBLE);
            mStoreTextView.setText(ad.getStore());
        } else
            mStoreTextView.setVisibility(GONE);

        if (ad.getAppIconView() != null)
            mIconView.addView(ad.getAppIconView());
        else
            mIconView.setVisibility(GONE);

        ad.registerView(mButton);
        if (ad.getVideoController() != null && ad.getVideoController().hasVideoContent()) {
            ad.getVideoController().play();
        }

        ad.trackImpression(getContext());
    }
}
