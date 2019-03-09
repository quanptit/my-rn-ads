package com.my.rn.Ads;

import android.text.TextUtils;
import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.utils.BaseUtils;
import com.baseLibs.utils.L;
import com.baseLibs.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * 0: Fb => Admob
 * 1: FB => AdX
 * 2: Admob => FB
 * 3: AdX => FB
 */
public class ManagerTypeAdsShow {
    public static final int TYPE_MOPUB = 0;
    public static final int TYPE_ADMOB = 1;
    public static final int TYPE_FB = 2;
    //    public static final int TYPE_START_APP = 3;
    //    public static final int TYPE_INMOBI = 4;
    public static final int TYPE_ADX = 5;

    //region type ads setting order ======
    private static final int Mopub_FB_Admob_ADX = 0;
    private static final int FB_Mopub_Admob_ADX = 1;
    private static final int Admob_ADX_FB_Mopub = 2;
    private static final int Mopub_ADX_FB_Admob = 3;

    /**
     * Thứ tự show theo thứ tự của mảng. 0 sẽ là đầu tiên
     */
    private static int[] getOrderShow(int typeOrderShowInSetting) {
        switch (typeOrderShowInSetting) {
            case Mopub_FB_Admob_ADX:
                return new int[]{TYPE_MOPUB, TYPE_FB, TYPE_ADMOB, TYPE_ADX};
            case Admob_ADX_FB_Mopub:
                return new int[]{TYPE_ADMOB, TYPE_ADX, TYPE_FB, TYPE_MOPUB};
            case FB_Mopub_Admob_ADX:
                return new int[]{TYPE_FB, TYPE_MOPUB, TYPE_ADMOB, TYPE_ADX};
            case Mopub_ADX_FB_Admob:
                return new int[]{TYPE_MOPUB, TYPE_ADX, TYPE_FB, TYPE_ADMOB};
            default:
                return new int[]{TYPE_MOPUB, TYPE_ADMOB, TYPE_ADX, TYPE_FB};
        }
    }
    //endregion

    //region type native ========
    /**
     * Trạng thái sử dụng cái Native Banner Default do bọn facebook cung câp
     */
    public static final int TYPE_SUMMARY_LIST1 = 0;
    /**
     * Sử dụng cái Native Small Do mình thiết kế có kiểu giống RowSummary
     */
    public static final int TYPE_SUMMARY_LIST2 = 1;
    /**
     * Sử dụng cái Native dạng lớn. Có icon giống như facebook messaenger
     */
    public static final int TYPE_SUMMARY_LIST3 = 3;

    /**
     * Trạng thái sử dụng cái Native Large Default do bọn facebook cung cấp
     */
    public static final int TYPE_DETAIL_LIST1 = 10;
    /**
     * Trạng thái sử dụng cái Native Large mà mình hay sử dụng trước giờ
     */
    public static final int TYPE_DETAIL_LIST2 = 11;
    /**
     * Gần giống cái row vocabulary
     */
    public static final int TYPE_DETAIL_LIST3 = 12;
    //endregion

    //region update Setting ===================
    public static void updateAdsSetting(String urlAdsSetting) {
        long lasTimeUpdate = PreferenceUtils.getLongSetting(UPDATE_ADS_TIME, 0);
        if (System.currentTimeMillis() - lasTimeUpdate < 48 * 60 * 60 * 1000) return;
        Log.d(TAG, "Start updateSetting");

        BaseUtils.executeHttpGet(urlAdsSetting, new BaseUtils.IGetDataResponse<String>() {
            @Override public void onResponse(String jsonSettingStr) {
                try {
                    JsonObject settingsJsonObj = new Gson().fromJson(jsonSettingStr, JsonObject.class);
                    saveIntSettingIfExit(settingsJsonObj, full_start);
                    saveIntSettingIfExit(settingsJsonObj, full_center);
                    saveIntSettingIfExit(settingsJsonObj, banner);
                    saveIntSettingIfExit(settingsJsonObj, large_native);
                    saveIntSettingIfExit(settingsJsonObj, small_native);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                PreferenceUtils.saveLongSetting(UPDATE_ADS_TIME, System.currentTimeMillis());
                Log.d(TAG, "updateSetting Complete: " + jsonSettingStr);
            }

            @Override public void onError(String errorStr) {
                Log.d(TAG, "updateAdsSetting onError: " + errorStr);
            }
        });
    }

    private static void saveIntSettingIfExit(JsonObject settingsJsonObj, String key) {
        saveIntSettingIfExit(settingsJsonObj, key, key);
    }

    private static void saveIntSettingIfExit(JsonObject settingsJsonObj, String prefKey, String jsonKey) {
        if (settingsJsonObj.has(jsonKey))
            PreferenceUtils.saveIntSetting(prefKey, settingsJsonObj.get(jsonKey).getAsInt());
    }
    //endregion

    // region get type show banner

    /**
     * @param index thể hiện lần load thứ .... VD load lần 0 fail => load lần 1 ...
     */
    public static int getTypeShowBaner(int index) {
        if (index > 3 || !BaseUtils.isOnline()) return -1;
        int typeOrderShowInSetting = PreferenceUtils.getIntSetting(banner, Mopub_FB_Admob_ADX);
        int typeShowBanner = getTypeShow(index, typeOrderShowInSetting);
        if (canShowBannerType(typeShowBanner)) return typeShowBanner;
        return getTypeShowBaner(index + 1);
    }

    private static boolean canShowBannerType(int type) {
        if (type == TYPE_ADMOB) return !TextUtils.isEmpty(KeysAds.getADMOD_BANER_NO_REFRESH());
        if (type == TYPE_ADX) return !TextUtils.isEmpty(KeysAds.getAdxBanner());
        if (type == TYPE_MOPUB) return !TextUtils.isEmpty(KeysAds.getMOPUB_BANNER());
        if (type == TYPE_FB) return !TextUtils.isEmpty(KeysAds.FB_BANNER);
        return true;
    }
    //endregion

    public static int getTypeShowFullStart(int index) {
        int typeOrderShowInSetting = PreferenceUtils.getIntSetting(full_start, Mopub_ADX_FB_Admob);
//        typeOrderShowInSetting = Mopub_ADX_FB_Admob;
        return getTypeShow(index, typeOrderShowInSetting);
    }

    public static int getTypeShowFullCenter(int index) {
        int typeOrderShowInSetting = PreferenceUtils.getIntSetting(full_center, Mopub_FB_Admob_ADX);
//        typeOrderShowInSetting = FB_Mopub_Admob_ADX;// TOD
        return getTypeShow(index, typeOrderShowInSetting);
    }


    private static int getTypeShow(int index, int typeOrderShowInSetting) {
        int[] orederShowArray = getOrderShow(typeOrderShowInSetting);
        if (index >= orederShowArray.length) return orederShowArray[0];
        return orederShowArray[index];
    }

    //region Type Show Native Ads
    //return AdsNativeObj.TYPE...
    public static int getPreferTypeAdsShowInListSummary() {
        return PreferenceUtils.getIntSetting(KeysAds.TYPE_ADS_LIST_SUMMARY, TYPE_SUMMARY_LIST3);
    }

    public static int getPreferTypeAdsShowInListDetail() {
        return PreferenceUtils.getIntSetting(KeysAds.TYPE_ADS_LIST_DETAIL, TYPE_DETAIL_LIST2);
    }

    public static int getPreferTypeAdsShowInListDetailVoca() {
        return PreferenceUtils.getIntSetting(KeysAds.TYPE_ADS_LIST_DETAIL_VOCA, TYPE_DETAIL_LIST3);
    }

    public static int getPreferTypeAdsShowExit() {
        return PreferenceUtils.getIntSetting(KeysAds.TYPE_ADS_EXIT, TYPE_DETAIL_LIST3);
    }

    public static int getTypeAdsNativeLikeBanner() {
        return PreferenceUtils.getIntSetting(KeysAds.TYPE_ADS_LIKE_BANNER, TYPE_SUMMARY_LIST1);
    }
    //endregion

    public static boolean isNativeBanner(int typeAds) {
        return typeAds == TYPE_SUMMARY_LIST1 || typeAds == TYPE_SUMMARY_LIST2;
    }

    public static boolean isLargeForBackupAds(int typeAds) {
        return !isNativeBanner(typeAds) && typeAds != TYPE_SUMMARY_LIST3;
    }

    //region hide const
    // Tham khao file "VPS Ads File Setting" trong evernote de biet
    public static final String full_start = "full_start";
    private static final String full_center = "full_center";
    private static final String banner = "banner";
    private static final String large_native = "large_native";
    private static final String small_native = "small_native";

    public static boolean isPreferShowBanner(int typeAds) {
        if (typeAds == TYPE_SUMMARY_LIST1 || typeAds == TYPE_SUMMARY_LIST2)
            return PreferenceUtils.getIntSetting(small_native, 0) != 0;
        return PreferenceUtils.getIntSetting(large_native, 0) != 0;
    }

    private static final String TAG = "TYPE_ADS_SHOW";
    private static final String UPDATE_ADS_TIME = "UPDATE_ADS_TIME";
    //endregion
}
