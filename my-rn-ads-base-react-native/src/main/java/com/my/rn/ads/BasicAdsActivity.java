package com.my.rn.ads;

import com.baseLibs.BaseReactActivtiy;
import com.my.rn.ads.full.center.BaseAdsFullManager;

public abstract class BasicAdsActivity extends BaseReactActivtiy {

    @Override protected void breforeSuperOnCreate() {
        super.breforeSuperOnCreate();
        BaseAdsFullManager.setMainActivity(this);
    }

    @Override protected void onDestroy() {
        BaseAdsFullManager.getInstance().destroy();
        super.onDestroy();
    }

}