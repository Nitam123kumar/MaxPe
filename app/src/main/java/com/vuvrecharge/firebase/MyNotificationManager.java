package com.vuvrecharge.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.activities.SplashActivity;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {
    private Context mCtx;
    private static MyNotificationManager mInstance;

    public UserPreferences mDatabase = new UserPreferencesImpl();

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String id,  String title, String body) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Log.d("Random", "" + m);
        Intent resultIntent = null;
        if (mDatabase.isUserLogin()) {
            resultIntent = new Intent(mCtx, SplashActivity.class);
        } else {
            resultIntent = new Intent(mCtx, SplashActivity.class);
        }
        ringtone();
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx,
                0, resultIntent, PendingIntent.FLAG_MUTABLE);
        MediaSessionCompat mediaSession = new MediaSessionCompat(mCtx, "MyMediaSession");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, BaseMethod.CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setColor(mCtx.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle());

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

        mediaSession.setSessionActivity(pendingIntent);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(m, mBuilder.build());
        }
    }

    public void ringtone() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mCtx, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayNotificationImage(String id, String title, String body, Bitmap bitmap) {

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Log.d("Random", "" + m);
        Intent resultIntent = null;
        if (mDatabase.isUserLogin()) {
            resultIntent = new Intent(mCtx, SplashActivity.class);
        } else {
            resultIntent = new Intent(mCtx, SplashActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_MUTABLE);

        ringtone();

        NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mCtx, BaseMethod.CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon)
                        .setLargeIcon(bitmap)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setColor(mCtx.getResources().getColor(R.color.colorPrimary))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap));

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

        if (mNotifyMgr != null) {
            mNotifyMgr.notify(m, mBuilder.build());
        }
    }


}