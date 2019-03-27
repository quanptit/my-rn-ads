package com.my.rn.Ads;


public class GDPRUtils {
//    private static final String TAG = "GDPRUtils";
//    private Activity activity;
//
//    public GDPRUtils(Activity activity) {
//        this.activity = activity;
//    }
//
//    /**
//     * Chú ý, chỉ gọi hàm này nếu là EU user
//     */
//    public void showFormRequestUpdateGDPR() {
//        Log.i(TAG, "showFormRequestUpdateGDPR");
//        UPAdsSdk.isEuropeanUnionUser(activity, new UPAdsSdk.UPEuropeanUnionUserCheckCallBack() {
//                    @Override public void isEuropeanUnionUser(boolean isEuropean) {
//                        Log.d(TAG, "isEuropeanUnionUser: " + isEuropean);
//                        if (isEuropean) {
//                            UPAdsSdk.notifyAccessPrivacyInfoStatus(activity, new UPAdsSdk.UPAccessPrivacyInfoStatusCallBack() {
//                                @Override
//                                public void onAccessPrivacyInfoAccepted() {
//                                    Log.i(TAG, "onAccessPrivacyInfoAccepted");
//                                    UPAdsSdk.updateAccessPrivacyInfoStatus(activity, UPAccessPrivacyInfoStatusEnum.UPAccessPrivacyInfoStatusAccepted);
////                UPAdsSdk.init(activity, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
//                                }
//
//                                @Override
//                                public void onAccessPrivacyInfoDefined() {
//                                    Log.i(TAG, "onAccessPrivacyInfoDefined");
//                                    UPAdsSdk.updateAccessPrivacyInfoStatus(activity, UPAccessPrivacyInfoStatusEnum.UPAccessPrivacyInfoStatusDefined);
////                UPAdsSdk.init(MainActivity.this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
//                                }
//                            });
//                        }
//                    }
//                }
//        );
//    }
//
////    UPAdsSdk.UPEuropeanUnionUserCheckCallBack isEuropeanUserCallback = new UPAdsSdk.UPEuropeanUnionUserCheckCallBack() {
////        @Override
////        public void isEuropeanUnionUser(boolean result) {
////            if (result) {
////                // result: true means that client in the EU
////                // ......
////                // ......
////
////                //pop up Dialog to ask authorizing
////
////            } else {
////                // if client is not in the EU,just initializing upsdk
////                UPAdsSdk.init(MainActivity.this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
////            }
////        }
////    };
//
//    private void requestConsent(Context context, Bundle savedInstanceState) {
////        UPAdsSdk.isEuropeanUnionUser(this, );
//
//
//        // new code for GDPR
////        AccessPrivacyInfoManager.UPAccessPrivacyInfoStatusEnum result = UPAdsSdk.getAccessPrivacyInfoStatus(context);
////        if (result == AccessPrivacyInfoManager.UPAccessPrivacyInfoStatusEnum.UPAccessPrivacyInfoStatusUnkown) {
////            //nếu trạng thái xác nhận là chưa biết => Kiểm tra xem là user EU hay không
////            UPAdsSdk.isEuropeanUnionUser(context, new UPAdsSdk.UPEuropeanUnionUserCheckCallBack() {
////                @Override
////                public void isEuropeanUnionUser(boolean isEuropeanUnionUser) {
////                    if (isEuropeanUnionUser) {
////                        UPAdsSdk.updateAccessPrivacyInfoStatus(MainActivity.this,
////                                AccessPrivacyInfoManager.UPAccessPrivacyInfoStatusEnum.UPAccessPrivacyInfoStatusAccepted);
////
////                        UPAdsSdk.init(MainActivity.this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
////                    } else {
////                        UPAdsSdk.init(MainActivity.this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
////                    }
////                }
////            });
////        } else {
////            UPAdsSdk.init(context, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
////        }
//
//
////        //region EEA
////        if (savedInstanceState == null) {
////            final ConsentInformationManager consentInformationManager = ConsentInformationManager.getInstance(this);
////            //first activity start
////            consentInformationManager.requestConsentStatusUpdate(new OnConsentStatusUpdateListener() {
////
////                @Override
////                public void onConsentStateUpdated(ConsentStatus consentStatus, LocationStatus locationStatus) {
////                    if (consentStatus == ConsentStatus.UNKNOWN && consentInformationManager.getLocationStatus() == LocationStatus.IN_EEA) {
////                        try {
////                            loadConsentForm();
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////
////                @Override
////                public void onFailed(String message) {
////
////                }
////            });
////        }
////        //endregion
//    }
//
////    private ConsentForm consentForm;
////
////    private void loadConsentForm() {
////        consentForm = ConsentForm.createBuilder(this)
////                .withListener(new ConsentFormListener() {
////                    @Override
////                    public void onConsentFormLoaded() {
////                        Log.d(TAG, "SampleApp onConsentFormLoaded ", null);
////                        if (consentForm != null)
////                            consentForm.show();
////                    }
////
////                    @Override
////                    public void onConsentFormError(String reason) {
////                        Log.d(TAG, "SampleApp onConsentFormError: " + reason, null);
////                    }
////
////                    @Override
////                    public void onConsentFormOpened() {
////                        Log.d(TAG, "SampleApp onConsentFormOpened ", null);
////                    }
////
////                    @Override
////                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
////                        Log.d(TAG, "SampleApp onConsentFormClosed ", null);
////
////                    }
////                })
////                .withPayOption()
////                .build();
////
////        consentForm.load();
////    }
}
