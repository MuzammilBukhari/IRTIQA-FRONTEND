package com.gdevs.imagegeneratorai.Activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdevs.imagegeneratorai.Adapter.DownloadAdapter;
import com.gdevs.imagegeneratorai.Model.DataModel;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.AdsManager;
import com.gdevs.imagegeneratorai.Utils.AdsPref;

//import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryActivity extends AppCompatActivity {

    File file;
    ArrayList<DataModel> downloadImageList = new ArrayList<>();
    ArrayList<DataModel> downloadVideoList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    LinearLayout isEmptyList;
    DownloadAdapter mAdapter;
    TextView txt;
    private AdsPref adsPref;
    private AdsManager adsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Downloaded Art");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadBannerAd(true);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        isEmptyList = findViewById(R.id.isEmptyList);
        txt = findViewById(R.id.txt);


        loadMedia();

    }

    public void loadMedia() {
        File DOWNLOAD_DIR = new File(Environment.getExternalStorageDirectory() + "/Pictures/"+getString(R.string.app_name));
        file = DOWNLOAD_DIR;
        if (!file.exists()){
            file.mkdir();
        }
        downloadImageList.clear();
        downloadVideoList.clear();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            displayVideoFiles(file, mRecyclerView);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                displayVideoFiles(file, mRecyclerView);
            }
        }
    }

    void displayVideoFiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        ArrayList<DataModel> videoList = new ArrayList<>();

        if (listfilemedia.length != 0) {
            isEmptyList.setVisibility(View.GONE);
        } else {
            isEmptyList.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < listfilemedia.length; i++) {
            File mediaFile = listfilemedia[i];
            if (isVideoFile(mediaFile)) {
                videoList.add(new DataModel(mediaFile.getAbsolutePath(), mediaFile.getName()));
            }
        }

        if (videoList.size() > 0) {
            isEmptyList.setVisibility(View.GONE);
        } else {
            isEmptyList.setVisibility(View.VISIBLE);
        }

        mAdapter = new DownloadAdapter(this, videoList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    boolean isVideoFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }
        File[] sortedByDate = folder.listFiles();
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
//        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

        return sortedByDate;
    }
}