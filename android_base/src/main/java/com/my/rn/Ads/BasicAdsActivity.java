package com.my.rn.Ads;

import com.baseLibs.BaseReactActivtiy;
import com.my.rn.Ads.full.center.BaseAdsFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;

public abstract class BasicAdsActivity extends BaseReactActivtiy {

    protected abstract BaseShowStartAdsManager createInstance();

    @Override protected void breforeSuperOnCreate() {
        super.breforeSuperOnCreate();
        BaseAdsFullManager.setMainActivity(this);
        BaseShowStartAdsManager showStartAdsManager = createInstance();
        BaseApplicationContainAds.getInstance().showStartAdsManager = showStartAdsManager;
        showStartAdsManager.showStartAds(this, isShowFullScreen());
    }

    @Override protected void onDestroy() {
        BaseAdsFullManager.getInstance().destroy();
        BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
        super.onDestroy();
    }

}
