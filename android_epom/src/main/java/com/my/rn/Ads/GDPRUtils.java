package com.my.rn.Ads;

public class GDPRUtils {
    //TODO

//    private void requestConsent(Bundle savedInstanceState) {
//        //region EEA
//        if (savedInstanceState == null) {
//            final ConsentInformationManager consentInformationManager = ConsentInformationManager.getInstance(this);
//            //first activity start
//            consentInformationManager.requestConsentStatusUpdate(new OnConsentStatusUpdateListener() {
//
//                @Override
//                public void onConsentStateUpdated(ConsentStatus consentStatus, LocationStatus locationStatus) {
//                    if (consentStatus == ConsentStatus.UNKNOWN && consentInformationManager.getLocationStatus() == LocationStatus.IN_EEA) {
//                        try {
//                            loadConsentForm();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailed(String message) {
//
//                }
//            });
//        }
//        //endregion
//    }

//    private ConsentForm consentForm;
//
//    private void loadConsentForm() {
//        consentForm = ConsentForm.createBuilder(this)
//                .withListener(new ConsentFormListener() {
//                    @Override
//                    public void onConsentFormLoaded() {
//                        Log.d(TAG, "SampleApp onConsentFormLoaded ", null);
//                        if (consentForm != null)
//                            consentForm.show();
//                    }
//
//                    @Override
//                    public void onConsentFormError(String reason) {
//                        Log.d(TAG, "SampleApp onConsentFormError: " + reason, null);
//                    }
//
//                    @Override
//                    public void onConsentFormOpened() {
//                        Log.d(TAG, "SampleApp onConsentFormOpened ", null);
//                    }
//
//                    @Override
//                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
//                        Log.d(TAG, "SampleApp onConsentFormClosed ", null);
//
//                    }
//                })
//                .withPayOption()
//                .build();
//
//        consentForm.load();
//    }
}
