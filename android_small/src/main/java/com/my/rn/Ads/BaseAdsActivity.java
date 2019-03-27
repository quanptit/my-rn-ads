package com.my.rn.Ads;

import com.my.rn.Ads.full.start.BaseShowStartAdsManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class BaseAdsActivity extends BasicAdsActivity {
    @Override protected BaseShowStartAdsManager createInstance() {
        return new ShowStartAdsManager();
    }
}
