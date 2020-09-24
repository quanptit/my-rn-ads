package com.my.rn.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;


public class RewardedAdsManager implements IRewardedAdsManager {
    private static final String TAG = "REWARDED_ADS_MANAGER";
    private boolean isLoading;

    @Override public void showRewardedAds(Activity activity, boolean showLoaddingIfNotCache, @Nullable IAdsCalbackOpen adsCalbackOpen) {

    }

    @Override public void cacheRewardedAds(Activity activity) {
        if (isLoading) return;
        isLoading = true;
//                MoPubRewardedVideoListener rewardedVideoListener = new MoPubRewardedVideoListener() {
//                    @Override
//                    public void onRewardedVideoLoadSuccess(String adUnitId) {
//                        // Called when the video for the given adUnitId has loaded. At this point you should be able to call MoPubRewardedVideos.showRewardedVideo(String) to show the video.
//                        Log.d(TAG, "onRewardedVideoLoadSuccess");
//                    }
//
//                    @Override
//                    public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
//                        // Called when a video fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
//                        Log.d(TAG, "onRewardedVideoLoadFailure");
//                    }
//
//                    @Override
//                    public void onRewardedVideoStarted(String adUnitId) {
//                        // Called when a rewarded video starts playing.
//                        Log.d(TAG, "onRewardedVideoStarted");
//                    }
//
//                    @Override
//                    public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
//                        //  Called when there is an error during video playback.
//                        Log.d(TAG, "onRewardedVideoPlaybackError");
//                    }
//
//                    @Override
//                    public void onRewardedVideoClicked(@NonNull String adUnitId) {
//                        //  Called when a rewarded video is clicked.
//                    }
//
//                    @Override
//                    public void onRewardedVideoClosed(String adUnitId) {
//                        // Called when a rewarded video is closed. At this point your application should resume.
//                    }
//
//                    @Override public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
//
//                    }
//                };
//                MoPubRewardedVideos.setRewardedVideoListener(rewardedVideoListener);
//        MoPubRewardedVideos.loadRewardedVideo(KeysAds.MOPUB_REWARDED);
        Log.d(TAG, "cacheRewardedAds");
    }

//    @Override public void showRewardedAds(Activity activity) {
//        Log.d(TAG, "showRewardedAds");
////        boolean isReady = isCachedRewardedAds(activity);
////        if (isReady)
////            MoPubRewardedVideos.showRewardedVideo(KeysAds.MOPUB_REWARDED);
//    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return false;
//        return MoPubRewardedVideos.hasRewardedVideo(KeysAds.MOPUB_REWARDED);
    }
}
