package com.my.rn.Ads;

import android.os.Bundle;
import com.baseLibs.BaseReactActivtiy;
import com.mopub.common.MoPub;
import com.my.rn.Ads.full.center.AdsFullManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class BaseAdsActivity extends BaseReactActivtiy {
    @Override protected void breforeSuperOnCreate() {
        super.breforeSuperOnCreate();
        AdsFullManager.setMainActivity(this);
        new ShowStartAdsManager().showStartAds(this);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoPub.onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MoPub.onStart(this);
    }

    protected void onStop() {
        super.onStop();
        MoPub.onStop(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoPub.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MoPub.onResume(this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        MoPub.onRestart(this);
    }

    @Override protected void onDestroy() {
        AdsFullManager.getInstance().destroy();
        MoPub.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MoPub.onBackPressed(this);
    }
}
