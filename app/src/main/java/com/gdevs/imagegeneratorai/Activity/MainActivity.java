package com.gdevs.imagegeneratorai.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdevs.imagegeneratorai.Adapter.ItemAdapter;
import com.gdevs.imagegeneratorai.Adapter.RatioAdapter;
import com.gdevs.imagegeneratorai.Config;
import com.gdevs.imagegeneratorai.Model.Item;
import com.gdevs.imagegeneratorai.Model.RatioModel;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.AdsManager;
import com.gdevs.imagegeneratorai.Utils.AdsPref;
import com.gdevs.imagegeneratorai.Utils.PrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity<NavController> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private EditText editTextPrompt, editTextNegative;
    private Button buttonGenerate;
    private ImageView imageViewGenerated;
    private TextView explorePrompt, btnPro;
    private PrefManager prefManager;
    String[] permissionsList = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AdsPref adsPref;
    private AdsManager adsManager;
    private static final String ONESIGNAL_APP_ID = "f8bf3c7f-fb54-4b9e-9be9-db9c149b57f2";

    private LinearLayout ll_1_1 , ll_9_16, ll_16_9 , ll_3_4 , ll_4_5;
    private TextView tv_1_1 , tv_9_16, tv_16_9 , tv_3_4 , tv_4_5;
    private ImageView iv_1_1 , iv_9_16, iv_16_9 , iv_3_4 , iv_4_5;

    private RecyclerView recyclerView;
    private RatioAdapter ratioAdapter;
    private List<RatioModel> ratioList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        prefManager = new PrefManager(this);
        prefManager.setString("prompt","");

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        // OneSignal Initialization
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();


        //adsManager.loadBannerAd(true);
        adsManager.loadInterstitialAd(true,adsPref.getInterstitialAdInterval());
        adsManager.loadNativeAd(true, "default");


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_action_action);

        editTextPrompt = findViewById(R.id.editTextPrompt);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        imageViewGenerated = findViewById(R.id.imageViewGenerated);
        explorePrompt = findViewById(R.id.explorePrompt);
        editTextNegative = findViewById(R.id.editTextNegative);
        btnPro = findViewById(R.id.btnPro);

        editTextPrompt.setText(prefManager.getString("prompt"));

        Gson gson = new Gson();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(getAssets().open("items.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Item[] items = gson.fromJson(reader, Item[].class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(Arrays.asList(items),this);
        recyclerView.setAdapter(itemAdapter);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
            if (!checkPermissions(this, permissionsList)) {
                ActivityCompat.requestPermissions(this, permissionsList, 21);
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Populate the ratioList with your data
        ratioList = new ArrayList<>();
        ratioList.add(new RatioModel(R.drawable.ratio_1_1, "1:1"));
        ratioList.add(new RatioModel(R.drawable.ratio_9_16, "9:16"));
        ratioList.add(new RatioModel(R.drawable.ratio_16_9, "16:9"));
        ratioList.add(new RatioModel(R.drawable.ratio_3_4, "3:4"));
        ratioList.add(new RatioModel(R.drawable.ratio_4_5, "4:5"));

        ratioAdapter = new RatioAdapter(ratioList, this);
        recyclerView.setAdapter(ratioAdapter);


        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (containsAdultWord(editTextPrompt.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Contains inappropriate word", Toast.LENGTH_SHORT).show();
                }else {
                    prefManager.setString("prompt",editTextPrompt.getText().toString().trim());
                    Intent favorites = new Intent(MainActivity.this,
                            GenerateActivity.class);
                    favorites.putExtra("prompt", editTextPrompt.getText().toString().trim());
                    favorites.putExtra("type", prefManager.getString("type"));
                    favorites.putExtra("size", prefManager.getString("SIZE"));
                    favorites.putExtra("negative", editTextNegative.getText().toString().trim());
                    startActivity(favorites);
                    showInterstitialAd();
                }
            }
        });

        explorePrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PromptActivity.class));
            }
        });

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PrimeActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextPrompt.setText(prefManager.getString("prompt"));
    }



    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_about);

        Button dialog_btn = dialog.findViewById(R.id.btn_done);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showExitDialog();
    }

    private void showExitDialog() {
        final Dialog dialogs = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogs.setContentView(R.layout.dialog_exit);

        LinearLayout mbtnYes = dialogs.findViewById(R.id.mbtnYes);
        LinearLayout mbtnNo = dialogs.findViewById(R.id.mbtnNo);
        mbtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
                finish();
            }
        });
        mbtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.nav_premium){
            startActivity(new Intent(MainActivity.this, PrimeActivity.class));
        }
        if (menuItem.getItemId() == R.id.nav_galler){
            showInterstitialAd();
            startActivity(new Intent(MainActivity.this, GalleryActivity.class));
        }
        if (menuItem.getItemId() == R.id.nav_rate){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
            }catch (ActivityNotFoundException ex){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
            }
        }
        if (menuItem.getItemId() == R.id.nav_share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store/apps/details?id="+getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT,shareBodyText);
            startActivity(Intent.createChooser(intent,"share via"));
        }
        if (menuItem.getItemId() == R.id.nav_contact){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getResources().getString(R.string.nav_header_subtitle)});
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.nav_header_desc));
            i.putExtra(Intent.EXTRA_TEXT   , getResources().getString(R.string.nav_header_desc));
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        if (menuItem.getItemId() == R.id.nav_about){
            showAboutDialog();
        }
        if (menuItem.getItemId() == R.id.nav_settings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        if (menuItem.getItemId() == R.id.nav_insta){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.instagram.com/"+ Config.INSTAGRAM));
            startActivity(browserIntent);
        }
        return false;
    }

    private void showInterstitialAd() {
        if (adsPref.getInterstitialAdCounter() >= adsPref.getInterstitialAdInterval()) {
            adsPref.updateInterstitialAdCounter(1);
            adsManager.showInterstitialAd();
        } else {
            adsPref.updateInterstitialAdCounter(adsPref.getInterstitialAdCounter() + 1);
        }
    }

    private boolean containsAdultWord(String input) {
        //String[] adultWords = {"nude", "adult", "sexy","porn", "sex", "boob","boobs", "breast", "bikini","nudes", "fuck", "fucking"};
        String[] adultWords = {"nude"};

        for (String word : adultWords) {
            if (input.contains(word)) {
                return true;
            }
        }
        return false;
    }
}