package com.vuvrecharge.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFcmListenerService extends FirebaseMessagingService {

    int dummyuniqueInt = new Random().nextInt(543254);

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() == null) {
            Map<String, String> data = remoteMessage.getData();

            String title = data.get("title");
            String message = data.get("message");
            String image_url = data.get("image_url");
            String id = data.get("id");
            if (title == null || title.isEmpty()) {
                title = getResources().getString(R.string.app_name);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(BaseMethod.CHANNEL_ID, BaseMethod.CHANNEL_NAME, importance);
                mChannel.setDescription(BaseMethod.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
//                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }

            if (image_url == null || image_url.equals("")) {
                MyNotificationManager.getInstance(this).displayNotification(id, title, message);
            } else {
                String finalTitle = title;
                try {
                    new Thread(() -> new sendNotification(getApplicationContext())
                            .execute(id, finalTitle, message, image_url)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }


    @Override
    public void onNewToken(@NonNull String token) {
        Log.i(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (token != null){
            sendRegistrationToServer(token);
        }
    }

    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String title = "";
        String id = "";
        String image = "";
        String body = "";
        boolean is_correct_image_path = false;

        public sendNotification(Context ctx) {
            super();
            this.ctx = ctx;
        }

        @Nullable
        @Override
        protected Bitmap doInBackground(@NonNull String... params) {

            InputStream in;
            id = params[0];
            title = params[1];
            body = params[2];
            image = params[3];
            try {

                image = image.replaceAll(" ", "%20");
                URL url = new URL(image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                is_correct_image_path = true;
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ee) {
                ee.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            MyNotificationManager.getInstance(getApplicationContext())
                    .displayNotificationImage(id, title, body, result);
        }
    }


}
