package com.my.rn.ads.mopub;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.my.rn.ads.BaseApplicationContainAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdInitUtils;
import com.my.rn.ads.settings.AdsSetting;

import java.util.ArrayList;

public class MopubInitUtils implements IAdInitUtils {
    private boolean isIniting, isInited;
    private ArrayList<IAdInitCallback> listCallback = new ArrayList<>();


    @Override public boolean isInited() {
        return isInited;
    }

    @Override public void initAds(Activity activity, IAdInitCallback callback) {
        if (isInited()) {
            if (callback != null)
                callback.didInitialise();
            return;
        }
        if (callback != null)
            listCallback.add(callback);
        if (isIniting) return;
        isIniting = true;

        Context context = activity == null ? BaseApplication.getAppContext() : activity;
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.getInstance(context).getSettings().setVerboseLogging(false);
        AppLovinSdk.initializeSdk(context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                Log.d("MopubInitUtils", "MAX onSdkInitialized");
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
