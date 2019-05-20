package com.my.rn.Ads;

import com.baseLibs.BaseReactActivtiy;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;

public abstract class BasicAdsActivity extends BaseReactActivtiy {

    @Override protected void breforeSuperOnCreate() {
        super.breforeSuperOnCreate();
        BaseAdsFullManager.setMainActivity(this);
    }

    @Override protected void onDestroy() {
        BaseAdsFullManager.getInstance().destroy();
        BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
        super.onDestroy();
    }

}
