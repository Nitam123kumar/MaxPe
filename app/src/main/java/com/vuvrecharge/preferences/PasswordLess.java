package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class PasswordLess {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String PASSWORD_LESS = "password";

    public PasswordLess(@NonNull Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("passwordLess", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setPasswordLessValue(String value){
        editor.putString(PASSWORD_LESS, value);
        editor.commit();
    }

    public HashMap<String,String> getPasswordLess(){
        HashMap<String, String> map = new HashMap<>();
        map.put(PASSWORD_LESS,preferences.getString(PASSWORD_LESS,null));
        return map;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
