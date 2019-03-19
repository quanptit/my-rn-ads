package com.my.rn.Ads;

import android.os.Bundle;
import android.util.Log;
import com.epomapps.android.consent.ConsentForm;
import com.epomapps.android.consent.ConsentFormListener;
import com.epomapps.android.consent.ConsentInformationManager;
import com.epomapps.android.consent.OnConsentStatusUpdateListener;
import com.epomapps.android.consent.model.ConsentStatus;
import com.epomapps.android.consent.model.LocationStatus;
import com.mopub.common.MoPub;
import com.my.rn.Ads.full.start.BaseShowStartAdsManager;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class BaseAdsActivity extends BasicAdsActivity {
    private static final String TAG = "BaseAdsActivity";

    @Override protected BaseShowStartAdsManager createInstance() {
        return new ShowStartAdsManager();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoPub.onCreate(this);
        requestConsent(savedInstanceState);
    }

    private void requestConsent(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            final ConsentInformationManager consentInformationManager = ConsentInformationManager.getInstance(this);
            //first activity start
            consentInformationManager.requestConsentStatusUpdate(new OnConsentStatusUpdateListener() {
                @Override
                public void onConsentStateUpdated(ConsentStatus consentStatus, LocationStatus locationStatus) {
                    if (consentStatus == ConsentStatus.UNKNOWN && consentInformationManager.getLocationStatus() == LocationStatus.IN_EEA) {
                        try {
//                            loadConsentForm();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailed(String message) {

                }
            });
        }
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
