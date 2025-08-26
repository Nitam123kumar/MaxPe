package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class APIStorePreferences {
    private static final String PREFS_NAME = "my_app_prefs";
    private SharedPreferences sharedPreferences;

    // constructor me context lena padega
    public APIStorePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putOperatorString(String value) {
        sharedPreferences.edit().putString("operators_data", value).apply();
    }

    public String getOperatorString() {
        return sharedPreferences.getString("operators_data", "");
    }

    public void putStringHome(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStringHome(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putDashBoardData(String value) {
        sharedPreferences.edit().putString("home_api_response", value).apply();
    }

    public String getDashBoardData() {
        return sharedPreferences.getString("home_api_response", "");
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
