package com.vuvrecharge.utils.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private WeakReference<NetworkChangeListener> mNetworkChangeListenerWeakReference;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NetworkChangeListener networkChangeListener = mNetworkChangeListenerWeakReference.get();
            if (networkChangeListener != null) {
                networkChangeListener.onNetworkChange(isOnline(context));
            }
        } catch (NullPointerException e) {
            Log.d("TAG_DATA", "onReceive: "+e.getMessage());
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
//            return (netInfo != null && netInfo.isConnected());
            return (netInfo != null && (netInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                    netInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                    netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) && netInfo.isConnected());
        } catch (NullPointerException e) {
            Log.d("TAG_DATA", "isOnline: "+e.getMessage());
            return false;
        }
    }

    void setNetworkChangeListener(NetworkChangeListener networkChangeListener) {
        mNetworkChangeListenerWeakReference = new WeakReference<>(networkChangeListener);
    }

    void removeNetworkChangeListener() {
        if (mNetworkChangeListenerWeakReference != null) {
            mNetworkChangeListenerWeakReference.clear();
        }
    } 

    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null
        return (netInfo != null && (netInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                netInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) && netInfo.isConnected() && netInfo.isAvailable());

    }

    //Interface to send opt to listeners
    interface NetworkChangeListener {
        void onNetworkChange(boolean isNetworkAvailable);
    }
}
