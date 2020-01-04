package com.my.rn.ads.tapdaq;

import android.app.Activity;
import android.util.Log;

import com.appsharelib.KeysAds;
import com.my.rn.ads.IAdInitCallback;
import com.my.rn.ads.IAdInitUtils;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.adnetworks.TMMediationNetworks;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AdInitUtils implements IAdInitUtils {
    private boolean isIniting;
    private ArrayList<IAdInitCallback> listCallback = new ArrayList<>();

    @Override public void initAds(Activity activity, IAdInitCallback callback) {
        if (Tapdaq.getInstance().IsInitialised()) {
            callback.didInitialise();
            return;
        }
        if (callback != null) listCallback.add(callback);
        if (isIniting) return;
        isIniting = true;
        TapdaqConfig config = new TapdaqConfig();
        config.registerTestDevices(TMMediationNetworks.AD_MOB, Arrays.asList(KeysAds.DEVICE_TESTS));
        if (KeysAds.IS_DEVELOPMENT)
            TLog.setLoggingLevel(TLogLevel.DEBUG);
        else
            TLog.setLoggingLevel(TLogLevel.WARNING);

        config.setAutoReloadAds(false);
        Tapdaq.getInstance().initialize(activity, KeysAds.TAPDAQ_APP_ID, KeysAds.TAPDAQ_CLIENT_KEY, config, new TMInitListener() {
            @Override
            public void didInitialise() {
                super.didInitialise();
                if (listCallback != null) {
                    for (IAdInitCallback initializationListener : listCallback) {
                        if (initializationListener != null)
                            initializationListener.didInitialise();
                    }
                    listCallback.clear();
                }
                isIniting = false;
            }

            @Override
            public void didFailToInitialise(TMAdError error) {
                super.didFailToInitialise(error);
                logError(error);
                if (listCallback != null) {
                    for (IAdInitCallback initializationListener : listCallback) {
                        if (initializationListener != null)
                            initializationListener.didFailToInitialise();
                    }
                    listCallback.clear();
                }
                isIniting = false;
            }
        });
    }

    //region utils
    private static final String TAG = "TAPDAQ";

    private void logError(TMAdError error) {
        try {
            String str = String.format(Locale.ENGLISH, "didFailToInitialise: %d - %s", error.getErrorCode(), error.getErrorMessage());
            for (String key : error.getSubErrors().keySet()) {
                try {
                    for (TMAdError value : error.getSubErrors().get(key)) {
                        String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key, value.getErrorCode(), value.getErrorMessage());
                        str = str.concat("\n ");
                        str = str.concat(subError);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
