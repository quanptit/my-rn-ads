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
        MoPub.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MoPub.onBackPressed(this);
    }
}
