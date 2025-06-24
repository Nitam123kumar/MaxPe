package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class IntroPreferences {
    private Context context;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String TAG_SLIDE = "status";

    public IntroPreferences(@NonNull Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("intro", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setIntroStatus(String introSlide1) {
        editor.putString(TAG_SLIDE, introSlide1);
        editor.commit();
    }

    public HashMap<String, String> mapData(){
        HashMap<String, String> map = new HashMap<>();
        map.put(TAG_SLIDE, preferences.getString(TAG_SLIDE,null));
        return map;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
