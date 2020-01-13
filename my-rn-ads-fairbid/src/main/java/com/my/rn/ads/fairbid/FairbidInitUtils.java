package com.my.rn.ads.fairbid;

import android.app.Activity;
import android.os.AsyncTask;

import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.fyber.FairBid;
import com.fyber.fairbid.user.UserInfo;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public class FairbidInitUtils {
    public static void initFairBid(Activity activity) {
        if (FairBid.hasStarted())
            return;
        FairBid config = FairBid.configureForAppId(KeysAds.FAIRBID_APP_ID);
        if (BuildConfig.DEBUG)
            config.enableLogs();

        config.disableAutoRequesting()  // disables auto request of ads
                .start(activity);
        setUserId();
    }

    private static void setUserId() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(BaseApplication.getAppContext());
                    String myId = adInfo != null ? adInfo.getId() : null;
                    UserInfo.setUserId(myId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
