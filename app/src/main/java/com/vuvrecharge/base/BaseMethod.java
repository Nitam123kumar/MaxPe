package com.vuvrecharge.base;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.vuvrecharge.modules.model.PlanData;
import com.vuvrecharge.modules.view.DefaultView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaseMethod {

    public static String teacher_want = "view";
    public static String secret_key = "e700X5r3XUvHihDLLPigVOMIxEOuAm";

    public static SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    public static SimpleDateFormat date = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    public static SimpleDateFormat date_s = new SimpleDateFormat("MMM", Locale.ENGLISH);
    public static SimpleDateFormat date_s_w = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
    public static SimpleDateFormat day = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    public static SimpleDateFormat day_s = new SimpleDateFormat("E", Locale.ENGLISH);
    public static SimpleDateFormat month = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static SimpleDateFormat formatFull = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat format_new = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.ENGLISH);
    public static SimpleDateFormat callDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat callDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
    public static SimpleDateFormat date_month = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

    public static String CHANNEL_ID = "999998";
    public static String CHANNEL_NAME = "the hello u Notification";
    public static String CHANNEL_DESCRIPTION = "https://rechargep.in/apis/";

    public static String amount = "";
    public static String mobile = "";
    public static String refresh = "No";
    public static DefaultView mainDefaultView;

    public static String key = "8977897789778977"; // 128 bit key
    public static String iv = "0677067706770673"; // 16 bytes IV
    public static String call_number = "";
    public static LinearLayout dial_pad = null;

    public static String call_first = "Yes";
    public static String from = "Yes";
    public static List<PlanData> offerDataList = new ArrayList<>();
    public static List<PlanData> fullDataList = new ArrayList<>();
    public static List<PlanData> topDataList = new ArrayList<>();
    public static List<PlanData> dataDataList = new ArrayList<>();
    public static void hideSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static Typeface getFonts(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Lobster_1.3.otf");
    }

    public static Typeface getFontsBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Lobster_1.3.otf");
    }

    //2019-08-30 19:45:25
    public static GradientDrawable getGradientDrawable(int fill_color) {
        int strokeWidth = 5;
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(fill_color);
        gD.setShape(GradientDrawable.OVAL);
        return gD;
    }

    public static GradientDrawable getGradientDrawableRe(Drawable fill_color) {
        GradientDrawable gD = new GradientDrawable();
        gD.setShape(GradientDrawable.RECTANGLE);
        gD.setCornerRadius(5f);
        return gD;
    }

    public static GradientDrawable getGradientStroke(int str_color, int fill_color) {
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(fill_color);
        gD.setStroke(3, str_color);
        gD.setShape(GradientDrawable.RECTANGLE);
        return gD;
    }
    public static GradientDrawable getGradientDrawableRe1(int fill_color) {
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(fill_color);
        gD.setStroke(3, fill_color);
        gD.setShape(GradientDrawable.RECTANGLE);
        gD.setCornerRadius(5f);
        return gD;
    }


}
