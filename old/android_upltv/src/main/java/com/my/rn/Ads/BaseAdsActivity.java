package com.my.rn.Ads;

import android.os.Bundle;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;
import com.up.ads.UPAdsSdk;

public class BaseAdsActivity extends BasicAdsActivity {
    private static final String TAG = "BaseAdsActivity";

    @Override protected BaseShowStartAdsManager createInstance() {
        return new ShowStartAdsManager();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UPAdsSdk.init(this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
    }
}
