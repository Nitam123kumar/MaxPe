package com.vuvrecharge.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vuvrecharge.api.ApiServices;
import com.vuvrecharge.api.CustomInterceptor;
import com.vuvrecharge.preferences.APIStorePreferences;
import com.vuvrecharge.preferences.PreferencesProvider;
import com.vuvrecharge.utils.LocaleHelper;
import com.vuvrecharge.utils.network.CheckNet;

//import net.one97.paytm.nativesdk.PaytmSDK;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ApiServices service;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    int versionCode = 0;
    @Override
    public void onCreate() {
        CheckNet.init(this);
        mInstance = this;
        PreferencesProvider.init(mInstance);
        service = createRetrofitService(ApiServices.class, ApiServices.BASE_URL);
        try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        LocaleHelper.setLocale(this, lang);
    }

    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        Gson gson = new GsonBuilder().create();
        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .connectionPool(new ConnectionPool(0, 60 * 1000, TimeUnit.SECONDS))
                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), versionCode+""))
                .cache(cache)
                .build();

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(clazz);
    }


    public ApiServices getApiInterface() {
        return service;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
            Log.d("DashBoardData", "App closed, clearing prefs");
            APIStorePreferences preferences = new APIStorePreferences(activity);
            preferences.putDashBoardData("");
            preferences.putString("home_api_response", "");

    }
}
