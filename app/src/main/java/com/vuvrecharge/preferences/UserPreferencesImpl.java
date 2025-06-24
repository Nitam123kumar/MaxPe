package com.vuvrecharge.preferences;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.modules.model.AlbumItem;
import com.vuvrecharge.modules.model.ArtistItem;
import com.vuvrecharge.modules.model.AudioItem;
import com.vuvrecharge.modules.model.ContactData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.model.VideoItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserPreferencesImpl implements UserPreferences {

    protected SharedPreferences mPreferences;

    public UserPreferencesImpl() {
        this.mPreferences = PreferencesProvider.providePreferences();
    }

    @Override
    public void setUserLogin(boolean status) {
        mPreferences.edit().putBoolean("IS_USER_LOGIN", status).apply();
    }

    @Override
    public boolean isUserLogin() {
        return mPreferences.getBoolean("IS_USER_LOGIN", false);
    }

    @Override
    public void setUserSkip(boolean status) {
        mPreferences.edit().putBoolean("IS_USER_SKIP", status).apply();
    }

    @Override
    public boolean isUserSkip() {
        return mPreferences.getBoolean("IS_USER_SKIP", false);
    }

    @Override
    public void setUserType(String statusType) {
        mPreferences.edit().putString("IS_USER_TYPE", statusType).apply();
    }

    @Override
    public String getUserType() {
        return mPreferences.getString("IS_USER_TYPE", "0");
    }

    @Override
    public String getUserId() {
        return getUserData().getMobile();
    }

    @Override
    public String getUsername() {
        return getUserData().getMobile();
    }

    @Override
    public void setUserData(String userData) {
        mPreferences.edit().putString("USER_DATA", userData).apply();
    }

    @Override
    public UserData getUserData() {
        Gson gson = new Gson();
        String json = mPreferences.getString("USER_DATA", null);
        if (json != null) {
            Log.e("USER_DATA", json);
        }
        Type type = new TypeToken<UserData>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public String getUserDataS() {
        return mPreferences.getString("USER_DATA", null);
    }


    @Override
    public void setAppLanguage(String data) {
        mPreferences.edit().putString("app_language", data).apply();
    }

    @Override
    public long getGoal() {
        return mPreferences.getLong("GOAL", 5000);
    }

    @Override
    public void setGoal(long data) {
        mPreferences.edit().putLong("GOAL", data).apply();
    }

    @Override
    public String getAppLanguage() {
        return mPreferences.getString("app_language", "en");
    }

    @Override
    public void clearUser() {
        mPreferences.edit()
                .putBoolean("IS_USER_LOGIN", false)
                .putString("IS_USER_TYPE", "0")
                .putString("USER_DATA", null)
                .apply();
    }

    @Override
    public String getToken() {
        return mPreferences.getString("user_token", "");
    }

    @Override
    public void setToken(String type) {
        mPreferences.edit().putString("user_token", type).apply();
    }

    @Override
    public String getEarnings() {
        return mPreferences.getString("user_earnings", "0.00");
    }

    @Override
    public void setEarnings(String type) {
        mPreferences.edit().putString("user_earnings", type).apply();
    }
    @Override
    public void setFcmToken(String fcmToken) {
        mPreferences.edit().putString("FCM_TOKEN", fcmToken).apply();
    }

    @Override
    public String getFcmToken() {
        return mPreferences.getString("FCM_TOKEN", "");
    }

    @Override
    public String getVersion() {
        return mPreferences.getString("app_version", "1.0");
    }

    @Override
    public void setVersion(String type) {
        mPreferences.edit().putString("app_version", type).apply();
    }

    @Override
    public String getViewType() {
        return mPreferences.getString("view_type", "2");
    }

    @Override
    public void setViewType(String type) {
        mPreferences.edit().putString("view_type", type).apply();
    }

    @Override
    public void setAllVideos(String videos) {
        mPreferences.edit().putString("all_videos", videos).apply();
    }

    @Override
    public List<VideoItem> getAllVideos() {
        List<VideoItem> dataList = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPreferences.getString("all_videos", null);
        if (json != null) {
            Log.e("all_videos", json);
            Type type = new TypeToken<List<VideoItem>>() {
            }.getType();
            dataList = gson.fromJson(json, type);
            return dataList;
        } else {
            return dataList;
        }
    }

    @Override
    public void setAllAudio(String videos) {
        mPreferences.edit().putString("all_audio", videos).apply();
    }

    @Override
    public List<AudioItem> getAllAudio() {
        List<AudioItem> dataList = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPreferences.getString("all_audio", null);
        if (json != null) {
            Log.e("all_audio", json);
            Type type = new TypeToken<List<AudioItem>>() {
            }.getType();
            dataList = gson.fromJson(json, type);
            return dataList;
        } else {
            return dataList;
        }
    }

    @Override
    public void setAllAlbum(String videos) {
        mPreferences.edit().putString("all_album", videos).apply();
    }

    @Override
    public List<AlbumItem> getAllAlbum() {
        List<AlbumItem> dataList = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPreferences.getString("all_album", null);
        if (json != null) {
            Log.e("all_album", json);
            Type type = new TypeToken<List<AlbumItem>>() {
            }.getType();
            dataList = gson.fromJson(json, type);
            return dataList;
        } else {
            return dataList;
        }
    }

    @Override
    public void setAllArtist(String videos) {
        mPreferences.edit().putString("all_artist", videos).apply();
    }

    @Override
    public List<ArtistItem> getAllArtist() {
        List<ArtistItem> dataList = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPreferences.getString("all_artist", null);
        if (json != null) {
            Log.e("all_artist", json);
            Type type = new TypeToken<List<ArtistItem>>() {
            }.getType();
            dataList = gson.fromJson(json, type);
            return dataList;
        } else {
            return dataList;
        }
    }


    @Override
    public List<ContactData> getContact() {
        List<ContactData> logMergeData = new ArrayList<>();
        Gson gson = new Gson();
        String json = mPreferences.getString("contact", null);
        if (json != null) {
            Log.e("contact", json);
            Type type = new TypeToken<List<ContactData>>() {
            }.getType();
            logMergeData = gson.fromJson(json, type);
            return logMergeData;
        } else {
            return logMergeData;
        }
    }

    @Override
    public void setContact(String contact) {
        mPreferences.edit().putString("contact", contact).apply();
    }

    @Override
    public void setBlockAmount(String blockAmt) {
        mPreferences.edit().putString("user_block_amount", blockAmt).apply();
    }

    @Override
    public String getBlockAmount() {
        return mPreferences.getString("user_block_amount", "");
    }

}
