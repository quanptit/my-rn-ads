package com.my.rn.ads;

import com.appsharelib.KeysAds;
import com.baseLibs.utils.PreferenceUtils;

public class AdsUtils {
    public static boolean isDoNotShowAds() {
        return PreferenceUtils.getBooleanSetting(KeysAds.REMOVE_ADS, false)
                || PreferenceUtils.getBooleanSetting("IS_VIP", false);
    }
}
