package com.my.rn.ads.mopub;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.appsharelib.KeysAds;
import com.my.rn.ads.BaseRewardedAdsManager;

import java.util.Set;

public class MopubRewardedUtils extends BaseRewardedAdsManager {
    @Override protected void _cacheRewardedAds(Activity activity) {
//        MoPubRewardedVideoListener rewardedVideoListener = new MoPubRewardedVideoListener() {
//            @Override
//            public void onRewardedVideoLoadSuccess(String adUnitId) {
//                MopubRewardedUtils.this.onRewardedVideoLoadSuccess();
//            }
//
//            @Override
//            public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
//                MopubRewardedUtils.this.onRewardedVideoLoadFailure();
//            }
//
//            @Override
//            public void onRewardedVideoStarted(String adUnitId) {
//                MopubRewardedUtils.this.onRewardedVideoStarted();
//            }
//
//            @Override
//            public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
//                MopubRewardedUtils.this.onRewardedVideoPlaybackError();
//            }
//
//            @Override
//            public void onRewardedVideoClicked(@NonNull String adUnitId) {
//                //  Called when a rewarded video is clicked.
//            }
//
//            @Override
//            public void onRewardedVideoClosed(String adUnitId) {
//                MopubRewardedUtils.this.onRewardedVideoClosed();
//            }
//
//            @Override public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
//                MopubRewardedUtils.this.onRewardedVideoCompleted();
//            }
//        };
//        MoPubRewardedVideos.setRewardedVideoListener(rewardedVideoListener);
//        MoPubRewardedVideos.loadRewardedVideo(KeysAds.MOPUB_REWARDED);
    }

    @Override protected void showRewardedAdsIfCache(Activity activity) {
//        MoPubRewardedVideos.showRewardedVideo(KeysAds.MOPUB_REWARDED);
    }

    @Override public boolean isCachedRewardedAds(Activity activity) {
        return false;
//        return MoPubRewardedVideos.hasRewardedVideo(KeysAds.MOPUB_REWARDED);
    }

}
