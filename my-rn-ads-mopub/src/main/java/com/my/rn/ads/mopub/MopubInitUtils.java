package com.my.rn.ads.mopub;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdInitUtils;
import com.my.rn.ads.settings.AdsSetting;

import java.util.ArrayList;

public class MopubInitUtils implements IAdInitUtils {
    private boolean isIniting, isInited;
    private ArrayList<IAdInitCallback> listCallback = new ArrayList<>();

    private String getKey() {
        String keySave = AdsSetting.getCenterKey(AdsSetting.ID_MOPUB);
        if (!TextUtils.isEmpty(keySave))
            return keySave;
        return KeysAds.MOPUB_FULL_CENTER;
    }

    @Override public void initAds(Activity activity, IAdInitCallback callback) {
        if (MoPub.isSdkInitialized() || isInited) {
            if (callback != null)
                callback.didInitialise();
            return;
        }
        String key = getKey();
        if (TextUtils.isEmpty(key)) {
            if (callback != null)
                callback.didFailToInitialise();
            return;
        }
        if (callback != null)
            listCallback.add(callback);
        if (isIniting) return;
        isIniting = true;
        SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(key);
        if (BuildConfig.DEBUG)
            configBuilder.withLogLevel(MoPubLog.LogLevel.DEBUG);
        else
            configBuilder.withLogLevel(MoPubLog.LogLevel.NONE);

        MoPub.initializeSdk(BaseApplication.getAppContext(), configBuilder.build(), new SdkInitializationListener() {
            @Override public void onInitializationFinished() {
                Log.d("MopubInitUtils", "MoPub onInitializationFinished");
                isInited = true;
                isIniting = false;
                if (listCallback != null) {
                    for (IAdInitCallback initializationListener : listCallback) {
                        if (initializationListener != null)
                            initializationListener.didInitialise();
                    }
                    listCallback.clear();
                }
            }
        });
    }

    public static IAdInitUtils getInstance() {
        return BaseApplicationContainAds.getInstance().getIAdInitMopubUtils();
    }
}
