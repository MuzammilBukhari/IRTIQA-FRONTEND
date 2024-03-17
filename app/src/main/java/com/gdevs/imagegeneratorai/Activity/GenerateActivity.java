package com.gdevs.imagegeneratorai.Activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static com.gdevs.imagegeneratorai.Utils.Constant.URL1;
import static com.gdevs.imagegeneratorai.Utils.Constant.URL2;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.AdsManager;
import com.gdevs.imagegeneratorai.Utils.AdsPref;
import com.gdevs.imagegeneratorai.Utils.Constant;
import com.gdevs.imagegeneratorai.Utils.MyJsonFetcher;
import com.gdevs.mycipher.AndroidCipher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;


public class GenerateActivity extends AppCompatActivity {

    RelativeLayout loadingView;
    ImageView generatedImage;
    AppCompatButton btnDownload;
    AppCompatButton btnRegenerate;
    private String prompt, type, negative, size;
    private String generateGet;
    private List<String> words;
    private AdsPref adsPref;
    private AdsManager adsManager;
    private static final int MY_TIMEOUT_MS = 10000; // 10 seconds
    private static final int MY_MAX_RETRIES = 3;

    int width = 1024;
    int height = 700;
    String extraPrompt;
    private TextView responseText;

    CardView generateCardView;
    private VideoView generatedVideo;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        prompt = intent.getStringExtra("prompt");
        type = intent.getStringExtra("type");
        negative = intent.getStringExtra("negative");
        size = intent.getStringExtra("size");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Generated Art");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadInterstitialAd(true, adsPref.getInterstitialAdInterval());
        adsManager.loadBannerAd(true);
        //adsManager.loadNativeAd(adsPref.getIsNativeExitDialog(), adsPref.getNativeAdStyleExitDialog());

        loadingView = findViewById(R.id.loadingView);
        generatedImage = findViewById(R.id.generatedImage);
        btnDownload = findViewById(R.id.btnDownload);
        btnRegenerate = findViewById(R.id.btnRegenerate);
        generatedVideo = findViewById(R.id.generatedVideo);

        String apiUrl;
        try {
            apiUrl = "http://127.0.0.1:8000/search/videos?query=" + URLEncoder.encode(prompt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        btnRegenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCardView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.VISIBLE);
                generatedVideo.setVisibility(View.GONE);
            }
        });

        generateCardView = findViewById(R.id.generateCardView);


        btnRegenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCardView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.VISIBLE);
                generatedVideo.setVisibility(View.GONE);
                new FetchVideoUrlTask(GenerateActivity.this).execute(generateGet);
                //imageCardView.setVisibility(View.GONE);

//                getGeneratedStart();
//                showInterstitialAd();
            }
        });

//        getGeneratedStart();
    }


    private void getGeneratedStart() {
        isBtnEnabled(false);
        getGeneratedPrompt(prompt);
    }

    private void getGeneratedPrompt(String prompt) {
        char randomLetter = RandomLetterGenerator.generateRandomLetter();


        Map<String, String> typeToConstant = new HashMap<>();
        typeToConstant.put("No Style", Constant.NO_STYLE);
        typeToConstant.put("Realistic", Constant.REALISTIC);
        typeToConstant.put("Anime", Constant.ANIME);
        typeToConstant.put("Cinematic", Constant.CINEMATIC);
        typeToConstant.put("Photographic", Constant.PHOTOGRAPHIC);
        typeToConstant.put("Fantasy", Constant.FANTASY);
        typeToConstant.put("Cartoon", Constant.CARTOON);
        typeToConstant.put("Cyberpunk", Constant.CYBERPUNK);
        typeToConstant.put("Manga", Constant.MANGA);
        typeToConstant.put("Digital Art", Constant.DIGITAL_ART);
        typeToConstant.put("Colorful", Constant.COLORFUL);
        typeToConstant.put("Robot", Constant.ROBOT);
        typeToConstant.put("Neonpunk", Constant.NEONPUNK);
        typeToConstant.put("Pixel Art", Constant.PIXEL_ART);
        typeToConstant.put("Disney", Constant.DISNEY);
        typeToConstant.put("3D Model", Constant.A3D_MODEL);


        Map<String, String> typeToConstantNegative = new HashMap<>();
        typeToConstantNegative.put("No Style", Constant.NEGATIVE_NO_STYLE);
        typeToConstantNegative.put("Realistic", Constant.NEGATIVE_REALISTIC);
        typeToConstantNegative.put("Anime", Constant.NEGATIVE_ANIME);
        typeToConstantNegative.put("Cinematic", Constant.NEGATIVE_CINEMATIC);
        typeToConstantNegative.put("Photographic", Constant.NEGATIVE_PHOTOGRAPHIC);
        typeToConstantNegative.put("Fantasy", Constant.NEGATIVE_FANTASY);
        typeToConstantNegative.put("Cartoon", Constant.NEGATIVE_CARTOON);
        typeToConstantNegative.put("Cyberpunk", Constant.NEGATIVE_CYBERPUNK);
        typeToConstantNegative.put("Manga", Constant.NEGATIVE_MANGA);
        typeToConstantNegative.put("Digital Art", Constant.NEGATIVE_DIGITAL_ART);
        typeToConstantNegative.put("Colorful", Constant.NEGATIVE_COLORFUL);
        typeToConstantNegative.put("Robot", Constant.NEGATIVE_ROBOT);
        typeToConstantNegative.put("Neonpunk", Constant.NEGATIVE_NEONPUNK);
        typeToConstantNegative.put("Pixel Art", Constant.NEGATIVE_PIXEL_ART);
        typeToConstantNegative.put("Disney", Constant.NEGATIVE_DISNEY);
        typeToConstantNegative.put("3D Model", Constant.NEGATIVE_A3D_MODEL);

        extraPrompt = typeToConstant.get(type);
        extraPrompt = extraPrompt.replace("{prompt}", prompt);
        negative = typeToConstantNegative.get(type);

        if (size.equals("1:1")) {
            width = 1024;
            height = 1024;
        } else if (size.equals("9:16")) {
            width = 576;
            height = 1024;
        } else if (size.equals("16:9")) {
            width = 1024;
            height = 576;
        } else if (size.equals("3:4")) {
            width = 700;
            height = 1024;
        } else if (size.equals("4:5")) {
            width = 1024;
            height = 700;
        } else {
            width = 1024;
            height = 1024;
        }

        generateGet = URL1 + "/search/videos?query=" + prompt + "&per_page=1";


    }

    private void isBtnEnabled(Boolean status) {
        if (status) {
            btnRegenerate.setEnabled(true);
            btnDownload.setEnabled(true);
            btnDownload.setAlpha((float) 1);
            btnRegenerate.setAlpha((float) 1);
        } else {
            btnDownload.setAlpha((float) 0.2);
            btnRegenerate.setAlpha((float) 0.2);
            btnRegenerate.setEnabled(false);
            btnDownload.setEnabled(false);
        }

    }





    private void showGeneratedImage(final String job) {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getArtWork(job);
                    }
                });
            }
        }, 10000);
    }

    private void getArtWork(String job) {
        MyJsonFetcher jsonFetcher = new MyJsonFetcher(this);
        jsonFetcher.fetchJsonData(AndroidCipher.getCipher(URL2) + job, new MyJsonFetcher.JsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String job = response.getString("job");
                    String status = response.getString("status");
                    Log.d("ymgss", response + "");
                    if (status.equals("generating")) {
                        getArtWork(job);
                    } else {
                        String imageUrl = response.getString("imageUrl");
                        showArtWork(imageUrl);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    private void showArtWork(String videoUrl) {
        isBtnEnabled(true);
        loadingView.setVisibility(View.GONE);
        generateCardView.setVisibility(View.GONE);
        generatedVideo.setVisibility(View.VISIBLE);
//        final String url = imageUrl.replace(AndroidCipher.getCipher("Fzqn6Ddu857RHPX/71A9Mw==:ZmVkY2JhOTg3NjU0MzIxMA=="), AndroidCipher.getCipher("Zlvrfxx/Tr6CEPkKTsfT6A==:ZmVkY2JhOTg3NjU0MzIxMA=="));
        Glide.with(this)
                .load(videoUrl)
                .into(generatedImage);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(GenerateActivity.this)
                        .asBitmap()
                        .load(videoUrl)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                saveBitmap(bitmap);
                            }
                        });
            }
        });
    }


    public static class RandomLetterGenerator {

        public static char generateRandomLetter() {
            Random random = new Random();
            char letter = (char) (random.nextInt(26) + 'A'); // A to Z
            return letter;
        }
    }



    private void saveBitmap(Bitmap bitmap) {

        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            String customFolderPath = "Pictures/" + getString(R.string.app_name);
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, customFolderPath);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Toast.makeText(this, "File Saved !!", Toast.LENGTH_SHORT).show();

            try {
                // Open an OutputStream to the image URI
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));

                // Compress and save the bitmap as JPEG
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                // Flush and close the OutputStream
                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String fileName = UUID.randomUUID() + ".jpg";
            String path = Environment.getExternalStorageDirectory().toString();
            File folder = new File(path + "/Pictures/" + getString(R.string.app_name));
            folder.mkdir();

            File file = new File(folder, fileName);
            if (file.exists())
                file.delete();

            try {

                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                //send pictures to gallery
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                Toast.makeText(this, "File Saved !!", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        TextView size512 = dialog.findViewById(R.id.size512);

        size512.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(GenerateActivity.this, "Print is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInterstitialAd() {
        if (adsPref.getInterstitialAdCounter() >= adsPref.getInterstitialAdInterval()) {
            adsPref.updateInterstitialAdCounter(1);
            adsManager.showInterstitialAd();
        } else {
            adsPref.updateInterstitialAdCounter(adsPref.getInterstitialAdCounter() + 1);
        }
    }

    private class FetchVideoUrlTask extends AsyncTask<String, Void, String> {
        private Context mContext;

        public FetchVideoUrlTask(Context context) {
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "", "Loading Video...", true);
        }


        @Override
        protected String doInBackground(String... urls) {
            String apiUrl = urls[0];
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String videoUrl) {
            try {
                if (videoUrl != null) {
                    JSONObject jsonVideoUrl = new JSONObject();
                    jsonVideoUrl.put("videoUrl", videoUrl);
                    onVideoUrlFetched(jsonVideoUrl.toString());
                    playVideo(videoUrl);
                }
            } catch (Exception f) {
                Log.e("FetchVideoUrlTask", "Error " + f.getMessage());
            }
            progressDialog.dismiss();
        }

        private void onVideoUrlFetched(String jsonVideoUrl) {
            try {
                JSONObject jsonObject = new JSONObject(jsonVideoUrl);
                String videoUrl = jsonObject.getString("videoUrl");
                generatedVideo.setVideoURI(Uri.parse(videoUrl));
                generatedVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        progressDialog.dismiss();
                        generatedVideo.start();
                    }
                });
                generatedVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                    }
                });
                generatedVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        progressDialog.dismiss();
                        Toast.makeText(GenerateActivity.this, "Failed to load video", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                generatedVideo.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            }



        private void playVideo(String videoUrl) {
            progressDialog = ProgressDialog.show(this, "", "Loading Video...", true);

            Uri uri = Uri.parse(videoUrl);
            generatedVideo.setVideoURI(uri);

            generatedVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                generatedVideo.start();
            }
            });
            generatedVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });
            generatedVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    progressDialog.dismiss();
                    Toast.makeText(GenerateActivity.this, "Failed to load video", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            generatedVideo.setVisibility(View.VISIBLE);
        }







    private String performGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
}

