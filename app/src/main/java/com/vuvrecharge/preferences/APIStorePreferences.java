package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class APIStorePreferences {
    private static final String PREFS_NAME = "my_app_prefs";
    private SharedPreferences sharedPreferences;

    public APIStorePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void putOperatorString(String value) {
        sharedPreferences.edit().putString("operators_data", value).apply();
    }

    public String getOperatorString() {
        return sharedPreferences.getString("operators_data", "");
    }

    public void putDashBoardData(String value) {
        sharedPreferences.edit().putString("home_api_response", value).apply();
    }

    public String getDashBoardData() {
        return sharedPreferences.getString("home_api_response", "");
    }

    public void putTime(Long value) {
        sharedPreferences.edit().putLong("last_fetch_time", value).apply();
    }

    public long getTime() {
        return sharedPreferences.getLong("last_fetch_time", 0);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
