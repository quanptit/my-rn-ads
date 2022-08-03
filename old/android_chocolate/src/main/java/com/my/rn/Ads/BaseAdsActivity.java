package com.my.rn.Ads;

import android.os.Bundle;
import com.my.rn.Ads.chocolate.ChocolateFullManager;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class BaseAdsActivity extends BasicAdsActivity {
    private static final String TAG = "BaseAdsActivity";

    @Override protected BaseShowStartAdsManager createInstance() {
        return new ShowStartAdsManager();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override protected void onResume() {
        super.onResume();
        ChocolateFullManager.getInstance().onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        ChocolateFullManager.getInstance().onPause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ChocolateFullManager.getInstance().onDestroy();
    }
}
