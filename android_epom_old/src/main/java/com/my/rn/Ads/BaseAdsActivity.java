package com.my.rn.Ads;

import com.baseLibs.BaseReactActivtiy;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class BaseAdsActivity extends BaseReactActivtiy {
    @Override protected void breforeSuperOnCreate() {
        super.breforeSuperOnCreate();
        AdsFullManager.setMainActivity(this);
        new ShowStartAdsManager().showStartAds(this, isShowFullScreen());
    }

//    @Override protected void onPause() {
//        super.onPause();
//    }

    @Override protected void onDestroy() {
        AdsFullManager.getInstance().destroy();
        super.onDestroy();
    }
}
