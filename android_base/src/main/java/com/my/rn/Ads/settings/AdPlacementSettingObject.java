package com.my.rn.ads.settings;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdPlacementSettingObject {
    @SerializedName("isNotShow") public boolean isNotShow; // true => sẽ không show ads ứng với placement này
    @SerializedName("not_s_b_btn") public boolean not_s_b_btn; // not show from back button
    @SerializedName("setting") public ArrayList<AdUnitSettingObj> setting;

    public AdUnitSettingObj getAdUnitSettingObj(int index) {
        if (setting == null) return null;
        if (setting.size() <= index) return null;
        AdUnitSettingObj adUnitSettingObj = setting.get(index);
        if (adUnitSettingObj.ecpm < 0) return null;
        return adUnitSettingObj;
    }

    public @Nullable String getAdUnitKey(String id) {
        if (setting == null) return null;
        for (AdUnitSettingObj adUnitSettingObj : setting) {
            if (id.equals(adUnitSettingObj.id))
                return adUnitSettingObj.adUnit;
        }
        return null;
    }

    public void sortByEcpm() {
        if (setting == null) return;
        Collections.sort(setting, new Comparator<AdUnitSettingObj>() {
            @Override public int compare(AdUnitSettingObj lhs, AdUnitSettingObj rhs) {
                // Return < 0 ==> lhs đứng phía trước
                return Float.compare(rhs.ecpm, lhs.ecpm);
            }
        });
    }
}
