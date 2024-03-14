package com.gdevs.imagegeneratorai.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gdevs.imagegeneratorai.Adapter.ItemAdapter;
import com.gdevs.imagegeneratorai.Adapter.PromptAdapter;
import com.gdevs.imagegeneratorai.Model.Item;
import com.gdevs.imagegeneratorai.Model.Prompt;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.AdsManager;
import com.gdevs.imagegeneratorai.Utils.AdsPref;
import com.gdevs.imagegeneratorai.Utils.PrefManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class PromptActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private AdsPref adsPref;
    private AdsManager adsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Prompts List");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefManager = new PrefManager(this);
        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadBannerAd(true);

        Gson gson = new Gson();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(getAssets().open("prompt.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Prompt[] items = gson.fromJson(reader, Prompt[].class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PromptAdapter itemAdapter = new PromptAdapter(Arrays.asList(items),this);
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.setOnItemClickListener(new PromptAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Prompt obj, int position) {
                prefManager.setString("prompt",obj.getPrompts());
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}