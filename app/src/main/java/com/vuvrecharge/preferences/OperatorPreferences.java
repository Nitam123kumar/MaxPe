package com.vuvrecharge.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class OperatorPreferences {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public OperatorPreferences(@NonNull Context context, String fileName) {
        this.context = context;
        preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setOperator(String type, String list){
        editor.putString("type", type);
        editor.putString("list", list);
        editor.commit();
    }

    public void warringMessage(String message){
        editor.putString("message", message);
        editor.commit();
    }

    public HashMap<String, String> getData(){
        HashMap<String,String> data = new HashMap<>();
        data.put("type",preferences.getString("type",null));
        data.put("list",preferences.getString("list",null));
        data.put("message",preferences.getString("message",null));
        return data;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
