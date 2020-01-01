package com.my.rn.Ads;

import android.os.Bundle;

import com.tapdaq.sdk.Tapdaq;

public class BaseAdsActivity extends BasicAdsActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override protected void onDestroy() {
        Tapdaq.getInstance().onTerminate(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
