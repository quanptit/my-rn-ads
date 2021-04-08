package com.app;

import android.os.Bundle;

import com.facebook.ads.AudienceNetworkAds;
import com.my.rn.ads.BaseAdsActivity;


public class MainActivity extends BaseAdsActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "App";
    }
}
