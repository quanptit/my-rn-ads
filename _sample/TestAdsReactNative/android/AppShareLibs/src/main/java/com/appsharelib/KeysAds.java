package com.appsharelib;

import com.baseLibs.utils.DeviceTestID;

/**
 * Setting Ads:
 * 0: Fb => Admob
 * 1: FB => AdX
 * 2: Admob => FB
 * 3: AdX => FB
 */
public class KeysAds {
    private static final int SETTING_NATIVE_TEST = 1; //1: FAN, 2: ADMOB_VIDEO,4: ADMOB_IMAGE, 3: Moupub only
    private static final int setting_full_test = 3; // 1: FAN, 2: ADMOB, 3: MOPUB

    public static final boolean IS_DEVELOPMENT = false;
    public static final String[] DEVICE_TESTS = DeviceTestID.DEVICE_TESTS_A_Z;
    /////////// TEST MOPUB: App với tên là test
    public static final String MOPUB_NATIVE_FB_ONLY = "d36ffcd8d47b48efaa5d387365b3bec7"; // APP Luyện nghe tiếng anh
    public static final String MOPUB_NATIVE_ADMOB_VIDEO_ONLY = "fb108680572d453987ebd930caf1421f";
    public static final String MOPUB_NATIVE_ADMOB_IMAGE_ONLY = "2b252e4d34a0453cb075508c0c4ddfe7";
    public static final String MOPUB_NATIVE_MOPUB_ONLY = "11a17b188668469fb0412708c3d16813";

    private static final String MOPUB_FULL = "24935ea6499f4217b1a4e319dcca52d3"; // Adunit name: Full-open-biding
    private static final String FB_FULL = "844050995928629_844545205879208";
    private static final String Admod_FULL_ = "ca-app-pub-3940256099942544/8691691433";
    ////////////////


    public static final String MOPUB_NATIVE = SETTING_NATIVE_TEST == 1 ? MOPUB_NATIVE_FB_ONLY :
            (SETTING_NATIVE_TEST == 2 ? MOPUB_NATIVE_ADMOB_VIDEO_ONLY :
                    (SETTING_NATIVE_TEST == 3 ? MOPUB_NATIVE_ADMOB_IMAGE_ONLY : MOPUB_NATIVE_MOPUB_ONLY));
    public static final String MOPUB_FULL_CENTER = setting_full_test == 3 ? MOPUB_FULL : null;
    public static final String FB_FULL_ADS = setting_full_test == 1 ? FB_FULL : null;
    public static final String Admod_FULL = setting_full_test == 2 ? Admod_FULL_ : null;

    //MOPUB, FB, ADMOB, ADX, TAPDAQ_VIDEO, TAPDAQ_FULL, TAPDAQ
    public static final String DEFAULT_START = "ADMOB";
    public static final boolean DEFAULT_IS_SHOW_START_ADS = false;
    public static final String[] DEFAULT_CENTER = new String[]{"MOPUB", "FB", "ADMOB"};
    public static final String[] DEFAULT_BANNER = new String[]{"MOPUB", "FB", "ADMOB"};
    public static final String[] DEFAULT_MRECT = DEFAULT_BANNER;

    public static final String TAPDAQ_APP_ID = "5f714061c313df32267ee4ea";
    public static final String TAPDAQ_CLIENT_KEY = "33f32a4d-a450-456b-9e4a-946119eea2c9";

    public static final String ADX_FULL_START = null;
    public static final String ADX_FULL_CENTER = null;
    public static final String ADX_BANNER = null;


    public static final String FB_BANNER_RECT = null;
    public static final String FB_FULL_START = null;
    public static final String FB_BANNER = null;

    public static final String MOPUB_FULL_START = null;
    public static final String MOPUB_BANNER_LARGE = "39ca9abf40654b93a913cc59cdc8703b";
    public static final String MOPUB_BANNER = null;
    public static final String MOPUB_REWARDED = null;
    public static final String MOPUB_NATIVE_BANNER = null;

    public static final String ADMOD_BANER_NO_REFRESH = null;
    public static final String Admod_START = "ca-app-pub-3940256099942544/1033173712";

    public static final String ROOT = "http://learnlanguage.xyz/";
    //region ========= KeysAds.========
    public static final String LAST_TIME_SHOW_ADS = "LAST_TIME_SHOW_ADS";
    public static final String REMOVE_ADS = "REMOVE_ADS";
    //endregion

}


