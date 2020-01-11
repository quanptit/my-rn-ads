package com.my.rn.ads.settings;

import android.util.Log;

import androidx.annotation.Nullable;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.BaseUtils;
import com.baseLibs.utils.PreferenceUtils;
import com.google.gson.JsonSyntaxException;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;

import java.util.ArrayList;

public class AdsSetting {
    private SettingObj settingObj;

    private @Nullable SettingObj getSettingObj() {
        if (this.settingObj != null)
            return this.settingObj;
        try {
            String jsonStrSave = PreferenceUtils.getStringSetting(ADS_SETTING_SAVE);
            if (jsonStrSave == null) return null;
            SettingObj settingObjFromServer = SettingObj.createInstance(jsonStrSave);
            if (settingObjFromServer != null) {
                this.settingObj = settingObjFromServer;
                return this.settingObj;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private @Nullable String getTypeShowAds(int index, AdPlacementSettingObject placementSettingObject) {
        if (placementSettingObject.isNotShow) return null;
        AdUnitSettingObj adUnitSettingObj = placementSettingObject.getAdUnitSettingObj(index);
        if (adUnitSettingObj != null)
            return adUnitSettingObj.id;
        return null;
    }

    public boolean isShowStartAds() {
        SettingObj settingObj = getSettingObj();
        if (settingObj == null || settingObj.start_ads == null)
            return KeysAds.DEFAULT_IS_SHOW_START_ADS;
        return !settingObj.start_ads.isNotShow;
    }

    public @Nullable String getTypeShowFullStart(int index) {
        SettingObj settingObj = getSettingObj();
        if (settingObj == null) {
            if (index > 0) return null;
            return KeysAds.DEFAULT_START;
        }
        return getTypeShowAds(index, settingObj.start_ads);
    }

    public String getTypeShowFullCenter(int index) {
        SettingObj settingObj = getSettingObj();
        if (settingObj == null) {
            if (index > 0) return null;
            return KeysAds.DEFAULT_CENTER;
        }
        return getTypeShowAds(index, settingObj.center_ads);
    }

    //region native
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
    
    public String getTypeShowLargeNative(int index) {
        return null;
    }

    public String getTypeShowSmallNative(int index) {
        return null;
    }
    //endregion

    // region banner
    public static final String RECTANGLE_HEIGHT_250 = "RECTANGLE_HEIGHT_250";

    public String getTypeShowBanner(int index) {
        SettingObj settingObj = getSettingObj();
        if (settingObj == null) {
            if (index > 0) return null;
            return KeysAds.DEFAULT_BANNER;
        }
        return getTypeShowAds(index, settingObj.banner_ads);
    }

    public String getTypeShowBannerRect(int index) {
        SettingObj settingObj = getSettingObj();
        if (settingObj == null) {
            if (index > 0) return null;
            return KeysAds.DEFAULT_MRECT;
        }
        return getTypeShowAds(index, settingObj.banner_rect_ads);
    }
    //endregion

    //region update from server
    private final ArrayList<IAdInitCallback> listCallback = new ArrayList<>();
    private boolean isUpdated;

    public void initAdsSetting(@Nullable IAdInitCallback callback) {
        if (callback == null) return;
        if (settingObj != null) {
            callback.didInitialise();
        } else if (isUpdated)
            callback.didFailToInitialise();
        else
            listCallback.add(callback);
    }

    private void postAllCallback() {
        if (listCallback != null) {
            for (IAdInitCallback callback : listCallback) {
                if (callback != null)
                    if (settingObj != null)
                        callback.didInitialise();
                    else
                        callback.didFailToInitialise();
            }
            listCallback.clear();
        }
    }

    public void updateAdsSetting(String urlAdsSetting) {
        long lasTimeUpdate = PreferenceUtils.getLongSetting(UPDATE_ADS_TIME, 0);
        if (System.currentTimeMillis() - lasTimeUpdate < 48 * 60 * 60 * 1000) {
            if (settingObj == null)
                getSettingObj();
            if (settingObj != null)
                return;
        }

        Log.d(TAG, "Start updateSetting from server: " + urlAdsSetting);
        BaseUtils.executeHttpGet(urlAdsSetting, new BaseUtils.IGetDataResponse<String>() {
            @Override public void onResponse(String jsonSettingStr) {
                try {
                    isUpdated = true;
                    SettingObj settingObjFromServer = SettingObj.createInstance(jsonSettingStr);
                    if (settingObjFromServer != null) {
                        AdsSetting.this.settingObj = settingObjFromServer;
                        PreferenceUtils.saveBooleanSetting("not_s_b_btn", settingObjFromServer.center_ads.not_s_b_btn);
                        PreferenceUtils.saveStringSetting(ADS_SETTING_SAVE, jsonSettingStr);
                        PreferenceUtils.saveLongSetting(UPDATE_ADS_TIME, System.currentTimeMillis());
                        logTest();
                    } else
                        Log.e(TAG, "settingObjFromServer.isVaild() FAIL: " + jsonSettingStr);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                postAllCallback();
            }

            @Override public void onError(String errorStr) {
                isUpdated = true;
                postAllCallback();
                Log.d(TAG, "updateAdsSetting onError: " + errorStr);
            }
        });
    }

    private void logTest() {
        if (!KeysAds.IS_DEVELOPMENT) return;
        Log.d(TAG, "====== getTypeShowFullStart ============");
        for (int i = 0; i <= 6; i++)
            Log.d(TAG, i + ": " + getTypeShowFullStart(i) + " - " + getStartKey(getTypeShowFullStart(i)));

        Log.d(TAG, "====== getTypeShowFullCenter ============");
        for (int i = 0; i <= 6; i++)
            Log.d(TAG, i + ": " + getTypeShowFullCenter(i) + " - " + getCenterKey(getTypeShowFullCenter(i)));
        if (getSettingObj() != null && getSettingObj().center_ads.not_s_b_btn)
            Log.w(TAG, "Không show ads khi nhấn back button");

        Log.d(TAG, "====== getTypeShowBanner ============");
        for (int i = 0; i <= 6; i++)
            Log.d(TAG, i + ": " + getTypeShowBanner(i) + " - " + getBannerKey(getTypeShowBanner(i)));

        Log.d(TAG, "====== MRECT ============");
        for (int i = 0; i <= 6; i++)
            Log.d(TAG, i + ": " + getTypeShowBannerRect(i) + " - " + getBannerRectKey(getTypeShowBannerRect(i)));
    }
    //endregion

    //region get local save key
    private static @Nullable String getAdUnit(String id, AdPlacementSettingObject placementObj) {
        if (placementObj == null || id == null) return null;
        return placementObj.getAdUnitKey(id);
    }

    public static @Nullable String getStartKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.start_ads);
    }

    public static @Nullable String getCenterKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.center_ads);
    }

    public static @Nullable String getNativeLargeKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.large_native_ads);
    }

    public static @Nullable String getNativeSmallKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.small_native_ads);
    }

    public static @Nullable String getBannerKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.banner_ads);
    }

    public static @Nullable String getBannerRectKey(String id) {
        SettingObj settingObj = getInstance().getSettingObj();
        if (settingObj == null) return null;
        return getAdUnit(id, settingObj.banner_rect_ads);
    }
    //endregion

    public static AdsSetting getInstance() {
        return BaseApplicationContainAds.getAdsSetting();
    }

    //region constant
    public static final String ID_ADMOB = "ADMOB";
    public static final String ID_FB = "FB";
    public static final String ID_MOPUB = "MOPUB";
    public static final String ID_ADX = "ADX";
    public static final String ID_TAPDAQ_VIDEO = "TAPDAQ_VIDEO";
    public static final String ID_TAPDAQ_FULL = "TAPDAQ_FULL";
    public static final String ID_TAPDAQ = "TAPDAQ";
//    public static final String ID_ = "";
//    public static final String ID_ = "";
//    public static final String ID_ = "";

    private static final String UPDATE_ADS_TIME = "U_A_T_N";
    private static final String ADS_SETTING_SAVE = "ADS_SETTING_SAVE";
    private static final String TAG = "ADS_SETTING";

    //endregion
}
