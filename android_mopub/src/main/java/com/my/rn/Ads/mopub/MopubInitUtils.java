package com.my.rn.Ads.mopub;

import android.util.Log;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.my.rn.Ads.BuildConfig;

import java.util.ArrayList;

public class MopubInitUtils {
    private boolean moPubIniting;
    private ArrayList<SdkInitializationListener> listMopubInitializationListener = new ArrayList<>();

    public void initMopub(final SdkInitializationListener listener) {
        if (MoPub.isSdkInitialized()) {
            listener.onInitializationFinished();
            return;
        }
        if (listener != null) listMopubInitializationListener.add(listener);
        if (moPubIniting) return;
        moPubIniting = true;
        SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(KeysAds.getMOPUB_FULL_START());
        if (BuildConfig.DEBUG)
            configBuilder.withLogLevel(MoPubLog.LogLevel.INFO);
        else
            configBuilder.withLogLevel(MoPubLog.LogLevel.NONE);
        MoPub.initializeSdk(BaseApplication.getAppContext(), configBuilder.build(), new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                Log.d("MopubInitUtils", "MoPub onInitializationFinished");
                if (listMopubInitializationListener != null) {
                    for (SdkInitializationListener initializationListener : listMopubInitializationListener) {
                        if (initializationListener != null)
                            initializationListener.onInitializationFinished();
                    }
                    listMopubInitializationListener.clear();
                }
                moPubIniting = false;
            }
        });
    }
}
