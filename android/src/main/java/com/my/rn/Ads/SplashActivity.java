package com.my.rn.Ads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.L;
import com.baseLibs.utils.PreferenceUtils;
import com.my.rn.Ads.full.start.ShowStartAdsManager;

public class SplashActivity extends FragmentActivity {
    private static SplashActivity instance;
    private Runnable finshSplash = new Runnable() {
        @Override public void run() {
            finishActivity();
        }
    };
    private static final String TAG = "SPLASH_ACTIVITY";
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

        setContentView(R.layout.activity_splash);
        instance = this;

        String appTitle = null;
        try {
            appTitle = getApplicationInfo().loadLabel(getPackageManager()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appTitle != null) {
            TextView tvAppName = findViewById(R.id.tvAppName);
            tvAppName.setText(appTitle);
        }

        boolean containSetting = PreferenceUtils.contains(ManagerTypeAdsShow.full_start);
        int time = containSetting ? 9000 : 12000;
        startIncreasePercent(containSetting ? 5000 : 8000);
        Log.d(TAG, "onCreate Start delay finish time = " + time);
        BaseApplication.getHandler().postDelayed(finshSplash, KeysAds.IS_DEVELOPMENT ? 30000 : time);
    }

    private void startIncreasePercent(final int timeMiliseconds) {
        final TextView tvLoading = findViewById(R.id.tvLoading);
        this.timer = new CountDownTimer(timeMiliseconds, timeMiliseconds / 99) {
            public void onTick(long millisUntilFinished) {
                int percent = (int) (((timeMiliseconds - millisUntilFinished) / (float) timeMiliseconds) * 100);
                if (tvLoading != null)
                    tvLoading.setText(percent + "%");
            }

            public void onFinish() {
                if (tvLoading != null)
                    tvLoading.setText("99%");
            }
        }.start();
    }

    public static void finishActivity() {
        try {
            if (instance != null) {
                Log.d(TAG, "finishActivity");
                instance.finish();
                instance = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance = null;
    }

    public static boolean isRunning() {
        return instance != null;
    }

    @Override public void onBackPressed() {
    }


    @Override
    protected void onDestroy() {
        instance = null;
        try {
            BaseApplication.getHandler().removeCallbacks(finshSplash);
            if (timer != null)
                timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public static void openActivity(Activity activity) {
        if (instance != null) return;
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }
}