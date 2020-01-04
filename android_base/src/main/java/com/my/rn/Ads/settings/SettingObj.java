package com.my.rn.ads.settings;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SettingObj {
    @SerializedName("start_ads") public AdPlacementSettingObject start_ads;
    @SerializedName("center_ads") public AdPlacementSettingObject center_ads;
    @SerializedName("large_native_ads") public AdPlacementSettingObject large_native_ads;
    @SerializedName("small_native_ads") public AdPlacementSettingObject small_native_ads;
    @SerializedName("banner_ads") public AdPlacementSettingObject banner_ads;
    @SerializedName("banner_rect_ads") public AdPlacementSettingObject banner_rect_ads;

    public static @Nullable SettingObj createInstance(String jsonSettingStr) {
        SettingObj settingObj = new Gson().fromJson(jsonSettingStr, SettingObj.class);
        if (settingObj.isVaild()) {
            sortByEcpm(settingObj.start_ads);
            sortByEcpm(settingObj.center_ads);
            sortByEcpm(settingObj.large_native_ads);
            sortByEcpm(settingObj.small_native_ads);
            sortByEcpm(settingObj.banner_ads);
            sortByEcpm(settingObj.banner_rect_ads);
            return settingObj;
        }
        return null;
    }

    private static void sortByEcpm(AdPlacementSettingObject placementSettingObject) {
        if (placementSettingObject != null)
            placementSettingObject.sortByEcpm();
    }

    private boolean isVaild() {
        return start_ads != null && center_ads != null;
    }
}
