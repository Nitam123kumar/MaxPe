package com.vuvrecharge.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class Utils {
    public static boolean isDevMode(@NonNull Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }

    public static void hideSoftKeyboard(@NonNull Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}
