package com.my.rn.Ads;

import android.os.Bundle;
import com.mopub.common.MoPub;

public class BaseAdsActivity extends BasicAdsActivity {
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
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MoPub.onPause(this);
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
        try {
            MoPub.onResume(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        MoPub.onRestart(this);
    }

    @Override protected void onDestroy() {
        MoPub.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MoPub.onBackPressed(this);
    }
}
