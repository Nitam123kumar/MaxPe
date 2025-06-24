package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Fingerprint {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String FINGERPRINT = "fingerprint";

    public Fingerprint(@NonNull Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("finger", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setFingerprint(String fingerprint){
        editor.putString(FINGERPRINT, fingerprint);
        editor.commit();
    }

    public HashMap<String, String> getDetails(){
        HashMap<String, String> map = new HashMap<>();
        map.put(FINGERPRINT,preferences.getString(FINGERPRINT,null));
        return map;
    }
}
