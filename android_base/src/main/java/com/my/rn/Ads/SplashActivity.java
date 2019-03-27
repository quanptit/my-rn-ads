package com.my.rn.Ads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.appsharelib.KeysAds;
import com.baseLibs.BaseApplication;
import com.baseLibs.utils.PreferenceUtils;
import com.my.rn.Ads.base.R;

public class SplashActivity extends FragmentActivity {
    private static SplashActivity instance;
    private Runnable finshSplash = new Runnable() {
        @Override public void run() {
            BaseApplicationContainAds.getInstance().destroyBaseShowStartAdsManager();
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
        if (getIntent().getBooleanExtra("FULL_SCREEN", false)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
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

//        boolean containSetting = PreferenceUtils.contains(ManagerTypeAdsShow.full_start);
//        int time = PreferenceUtils.contains(ManagerTypeAdsShow.full_start) ? 9000 : 12000;
//        startIncreasePercent(containSetting ? 5000 : 8000);

        int time = KeysAds.SPLASH_MAX_TIME;
        startIncreasePercent(time < 8000 ? time : 8000);
        Log.d(TAG, "onCreate Start delay finish time = " + time);
        BaseApplication.getHandler().postDelayed(finshSplash, time);
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


    public static void openActivity(Activity activity, boolean isFullScreen) {
        if (instance != null) return;
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra("FULL_SCREEN", isFullScreen);
        activity.startActivity(intent);
    }
}
