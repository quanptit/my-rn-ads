package com.my.rn.Ads.nativeads.fb;

import android.text.TextUtils;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.baseLibs.utils.PreferenceUtils;
import com.facebook.ads.*;


import android.util.Log;
import com.my.rn.Ads.full.AdsFullManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

abstract class BaseFbNativeAdsManager<T extends NativeAdBase> implements NativeAdListener {
    private int NO_ADS_LOAD; // Number ads sẽ tải trước
    private boolean isLargeAds;
    private int countLoadError = 0;
    private int countReturnNullBecauseSameTitle = 0;
    public boolean isCaching;
    protected String TAG = "aa";
    private NativeAdBase.MediaCacheFlag mediaCacheFlag;

    private T nativeAdLoading = null;
    private HashMap<String, Integer> titlesCount;
    private Queue<T> nativeAds; // Sẽ chứa 3 Ads

    public BaseFbNativeAdsManager(boolean isLargeAds, int NO_ADS_LOAD, NativeAdBase.MediaCacheFlag mediaCacheFlag) {
        this.isLargeAds = isLargeAds;
        this.NO_ADS_LOAD = NO_ADS_LOAD;
        this.mediaCacheFlag = mediaCacheFlag;
        titlesCount = new HashMap<>();
        nativeAds = new LinkedList<>();
    }

    public boolean isCached() {
        return !nativeAds.isEmpty();
    }

    abstract T createNativeAdsOj();

    public void checkAndLoadAds() {
        _checkAndLoadAds(false);
    }

    private void _checkAndLoadAds(boolean isNotResetCountError) {
        if (nativeAdLoading != null || nativeAds.size() >= NO_ADS_LOAD) return;
        this.isCaching = nativeAds.isEmpty();
        if (!isNotResetCountError)
            countLoadError = 0;
        try {
            nativeAdLoading = createNativeAdsOj();
            nativeAdLoading.setAdListener(this);
            nativeAdLoading.loadAd(mediaCacheFlag);
        } catch (Exception e) {
            e.printStackTrace();
            nativeAdLoading = null;
        }
    }


    /*Cần chạy function ở background thread. TIMEOUT = 5000*/
    public void cacheAndWaitForComplete(final boolean cacheCenterAds) throws InterruptedException {
        if (!nativeAds.isEmpty() && !cacheCenterAds) return;
        long startTimeLoad = System.currentTimeMillis();
        BaseApplication.getHandler().post(new Runnable() {
            @Override public void run() {
                _checkAndLoadAds(false);
                if (cacheCenterAds)
                    AdsFullManager.getInstance().cacheAdsCenter();
            }
        });
        while (true) {
            Thread.sleep(100);
            boolean isSuccess = true;
            if (nativeAds.isEmpty())
                isSuccess = false;
            //if (cacheCenterAds && !AdsFullManager.getInstance().isCachedCenter())
            //    isSuccess = false;
            if (isSuccess) return;
            if (System.currentTimeMillis() - startTimeLoad > 3000) {
                L.d("cacheAndWaitForComplete Fail Time out");
                return;
            }
        }
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        Log.d(TAG, "FB Load native ads => onAdError" + adError.getErrorMessage());
        countLoadError++;
        nativeAdLoading = null;
        if (countLoadError < 3) {
            this.isCaching = false;
            _checkAndLoadAds(true);
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (nativeAdLoading != null) {
            nativeAds.add(nativeAdLoading);
            nativeAdLoading = null;
        }
        this.isCaching = false;
        _checkAndLoadAds(false);
    }


    public T getNextAds() {
//        if (true) {
//            _checkAndLoadAds(false);
//            return nativeAds.peek();
//        }
        T firstAds = nativeAds.peek();
        T ads = null;
        try {
            ads = getNextAds(false);
            if (ads==null) ads = firstAds;
            if (ads != null) {
                L.d("getNextAds: Return value: " + ads.getAdvertiserName());
                if (mediaCacheFlag == NativeAdBase.MediaCacheFlag.NONE)
                    ads.downloadMedia();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _checkAndLoadAds(false);
        return ads;
    }

    private T getNextAds(boolean isFromDeQui) {
        T adsNext = nativeAds.poll();
        int trongSo = isLargeAds ? 2 : 1;
        if (adsNext == null) {
            if (isFromDeQui) {
                countReturnNullBecauseSameTitle++;
                Log.d(TAG, "Không có Native Ads nào vì giống title quá nhiều lần: " + countReturnNullBecauseSameTitle);
                if (countReturnNullBecauseSameTitle > getSettingNoBackupAdsShow() / trongSo) {
                    L.d("Reset lại số lần hiển thị backup ads");
                    countReturnNullBecauseSameTitle = 0;
                    titlesCount.clear();
                }
                return null;
            } else {
                Log.d(TAG, "Không có Native Ads nào đã được load => Load cái mới");
                return null;
            }
        }
        String adsTitle = adsNext.getAdvertiserName();
        if (TextUtils.isEmpty(adsTitle))
            return adsNext;

        int noShowed;
        if (titlesCount.containsKey(adsTitle)) {
            noShowed = titlesCount.get(adsTitle);
        } else
            noShowed = 0;

        if (noShowed > getSettingNoAllowSameTitle()) {
            return getNextAds(true); // Ads nay ko duoc show, load Next
        } else {
            titlesCount.put(adsTitle, noShowed + trongSo);
            return adsNext;
        }
    }

    //region utils

    /**
     * Nếu ko muốn check same title thì đơn giản tăng giá trị này lên cao
     */
    private static int getSettingNoAllowSameTitle() {
        return PreferenceUtils.getIntSetting(KeysAds.NO_ALLOW_SAME_TITLE, 6);
    }

    private static int getSettingNoBackupAdsShow() {
        return PreferenceUtils.getIntSetting(KeysAds.NO_BACKUP_ADS_SHOW, 6);
    }

    @Override public void onMediaDownloaded(Ad ad) {
    }

    //endregion

    //region hide
    @Override
    public void onAdClicked(Ad ad) {
    }

    @Override
    public void onLoggingImpression(Ad ad) {
    }
    //endregion
}