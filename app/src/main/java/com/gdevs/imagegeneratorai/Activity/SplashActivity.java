package com.gdevs.imagegeneratorai.Activity;

import static com.gdevs.imagegeneratorai.Config.ADMOB_APP_OPEN_AD_ID;
import static com.gdevs.imagegeneratorai.Config.ADMOB_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.ADMOB_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.ADMOB_NATIVE_ID;
import static com.gdevs.imagegeneratorai.Config.AD_NETWORK;
import static com.gdevs.imagegeneratorai.Config.AD_STATUS;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_APP_OPEN_AP_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_BANNER_MREC_ZONE_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_BANNER_ZONE_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_INTERSTITIAL_ZONE_ID;
import static com.gdevs.imagegeneratorai.Config.APPLOVIN_NATIVE_MANUAL_ID;
import static com.gdevs.imagegeneratorai.Config.BACKUP_AD_NETWORK;
import static com.gdevs.imagegeneratorai.Config.FAN_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.FAN_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.FAN_NATIVE_ID;
import static com.gdevs.imagegeneratorai.Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID;
import static com.gdevs.imagegeneratorai.Config.GOOGLE_AD_MANAGER_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.GOOGLE_AD_MANAGER_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.GOOGLE_AD_MANAGER_NATIVE_ID;
import static com.gdevs.imagegeneratorai.Config.INTERSTITIAL_AD_INTERVAL;
import static com.gdevs.imagegeneratorai.Config.IRONSOURCE_APP_KEY;
import static com.gdevs.imagegeneratorai.Config.IRONSOURCE_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.IRONSOURCE_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.NATIVE_AD_INDEX;
import static com.gdevs.imagegeneratorai.Config.NATIVE_STYLE;
import static com.gdevs.imagegeneratorai.Config.STARTAPP_APP_ID;
import static com.gdevs.imagegeneratorai.Config.STYLE_NEWS;
import static com.gdevs.imagegeneratorai.Config.STYLE_RADIO;
import static com.gdevs.imagegeneratorai.Config.UNITY_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.UNITY_GAME_ID;
import static com.gdevs.imagegeneratorai.Config.UNITY_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.WORTISE_APP_ID;
import static com.gdevs.imagegeneratorai.Config.WORTISE_APP_OPEN_AD_ID;
import static com.gdevs.imagegeneratorai.Config.WORTISE_BANNER_ID;
import static com.gdevs.imagegeneratorai.Config.WORTISE_INTERSTITIAL_ID;
import static com.gdevs.imagegeneratorai.Config.WORTISE_NATIVE_ID;
import static com.ymg.ads.sdk.util.Constant.ADMOB;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.ymg.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.ymg.ads.sdk.util.Constant.WORTISE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.gdevs.imagegeneratorai.Config;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.AdsPref;
import com.gdevs.imagegeneratorai.Utils.Anims;
import com.gdevs.imagegeneratorai.Utils.Constant;


import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private TextView developers;
    private RelativeLayout parentLayout;
    private AppCompatImageView logo;
    private AdsPref adsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        developers = findViewById(R.id.developers);
        parentLayout = findViewById(R.id.parentLayout);


        logo = findViewById(R.id.logo);
        Anims aVar = new Anims(this.getResources().getDrawable(R.drawable.logo));
        aVar.m14932a(true);
        logo.setImageDrawable(aVar);
        logo.setVisibility(View.VISIBLE);

        adsPref = new AdsPref(this);

        adsPref.saveAds(
                AD_STATUS,
                AD_NETWORK,
                BACKUP_AD_NETWORK,
                "",
                "",
                ADMOB_BANNER_ID,
                ADMOB_INTERSTITIAL_ID,
                ADMOB_NATIVE_ID,
                ADMOB_APP_OPEN_AD_ID,
                GOOGLE_AD_MANAGER_BANNER_ID,
                GOOGLE_AD_MANAGER_INTERSTITIAL_ID,
                GOOGLE_AD_MANAGER_NATIVE_ID,
                GOOGLE_AD_MANAGER_APP_OPEN_AD_ID,
                FAN_BANNER_ID,
                FAN_INTERSTITIAL_ID,
                FAN_NATIVE_ID,
                STARTAPP_APP_ID,
                UNITY_GAME_ID,
                UNITY_BANNER_ID,
                UNITY_INTERSTITIAL_ID,
                APPLOVIN_BANNER_ID,
                APPLOVIN_INTERSTITIAL_ID,
                APPLOVIN_NATIVE_MANUAL_ID,
                APPLOVIN_APP_OPEN_AP_ID,
                APPLOVIN_BANNER_ZONE_ID,
                APPLOVIN_BANNER_MREC_ZONE_ID,
                APPLOVIN_INTERSTITIAL_ZONE_ID,
                IRONSOURCE_APP_KEY,
                IRONSOURCE_BANNER_ID,
                IRONSOURCE_INTERSTITIAL_ID,
                WORTISE_APP_ID,
                WORTISE_BANNER_ID,
                WORTISE_INTERSTITIAL_ID,
                WORTISE_NATIVE_ID,
                WORTISE_APP_OPEN_AD_ID,
                INTERSTITIAL_AD_INTERVAL,
                NATIVE_AD_INDEX,
                NATIVE_AD_INDEX
        );

        dataIsReadys();
    }

    private void dataIsReadys() {
        goToNextActivity();
    }

    private void goToNextActivity() {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    }
                });
            }
        }, 3000);
    }
}
