package com.my.rn.ads.settings;

import com.google.gson.annotations.SerializedName;

public class AdUnitSettingObj {
    @SerializedName("id") public String id;
    @SerializedName("ecpm") public float ecpm;
    @SerializedName("adUnit") public String adUnit;
}
