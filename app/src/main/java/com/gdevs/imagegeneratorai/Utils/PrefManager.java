package com.gdevs.imagegeneratorai.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {


    private static final String PREF_NAME = "status_app";
    String TAG_NIGHT_MODE = "nightmode";
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    private String userId;
    private String userName;
    private String userEmail;
    private String userImage;


    public PrefManager(Context context) {
        this._context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void saveGif(String path, String gif_name) {
        editor.putString("path", path);
        editor.putString("gif_name", gif_name);
        editor.apply();
    }

    public String getPath() {
        return pref.getString("path", "0");
    }

    public String getGifName() {
        return pref.getString("gif_name", "0");
    }

    public void saveSettings(String onesignal_app_id, String youtube_api_key, String app_privacy_policy, Boolean enable_details_page, Boolean enable_external_player, String player_url) {
        editor.putString("onesignal_app_id", onesignal_app_id);
        editor.putString("youtube_api_key", youtube_api_key);
        editor.putString("app_privacy_policy", app_privacy_policy);
        editor.putBoolean("enable_details_page", enable_details_page);
        editor.putBoolean("enable_external_player", enable_external_player);
        editor.putString("player_url", player_url);
        editor.apply();
    }

    public void saveAuth(String userName, String userEmail, String userImage) {
        editor.putString("userName", userName);
        editor.putString("userEmail", userEmail);
        editor.putString("userImage", userImage);
        editor.apply();
    }

    public String getUserId() {
        return pref.getString("userId", "");
    }

    public String getUserName() {
        return pref.getString("userName", "");
    }

    public String getUserEmail() {
        return pref.getString("userEmail", "");
    }

    public String getUserImage() {
        return pref.getString("userImage", "");
    }

    public String getYoutubeAPIKey() {
        return pref.getString("youtube_api_key", "0");
    }

    public String getPrivacyPolicy() {
        return pref.getString("app_privacy_policy", "");
    }

    public String getPlayerUrl() {
        return pref.getString("player_url", "");
    }

    public String getOnesignalId() {
        return pref.getString("onesignal_app_id", "");
    }

    public Boolean getDetailsPage() {
        return pref.getBoolean("enable_details_page", true);
    }

    public Boolean getExternalPlayer() {
        return pref.getBoolean("enable_external_player", true);
    }

    public void setBoolean(String str, Boolean bool) {
        this.editor.putBoolean(str, bool.booleanValue());
        this.editor.commit();
    }

    public void setString(String str, String str2) {
        this.editor.putString(str, str2);
        this.editor.commit();
    }

    public void setInt(String str, int i) {
        this.editor.putInt(str, i);
        this.editor.commit();
    }

    public boolean getBoolean(String str) {
        return this.pref.getBoolean(str, true);
    }

    public void remove(String str) {
        if (this.pref.contains(str)) {
            this.editor.remove(str);
            this.editor.commit();
        }
    }

    public String getString(String str) {
        return this.pref.contains(str) ? this.pref.getString(str, null) : "";
    }

    public int getInt(String str) {
        return this.pref.getInt(str, 2);
    }




    public void updateWallpaperColumns(int columns) {
        editor.putInt("wallpaper_columns", columns);
        editor.apply();
    }

    public Integer getAppOpenToken() {
        return pref.getInt("app_open_token", 0);
    }

    public void updateAppOpenToken(int value) {
        editor.putInt("app_open_token", value);
        editor.apply();
    }

    public Integer getInAppReviewToken() {
        return pref.getInt("in_app_review_token", 0);
    }

    public void updateInAppReviewToken(int value) {
        editor.putInt("in_app_review_token", value);
        editor.apply();
    }


    //save
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor= pref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    //load
    public Boolean loadNightModeState(){
        Boolean state = pref.getBoolean("NightMode",false);
        return state;
    }

}
