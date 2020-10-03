package com.ayw.weathertest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final String TAG = "MyService";
    String notify;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null) {
            return Service.START_STICKY;
        } else {
            notify = intent.getStringExtra("no");

            Log.d(TAG, "전달받은 데이터: " + notify);
        }

        if (notify.equals("1")) {
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = null;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelID = "channel_01";
                String channelName = "MyChannel01";

                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channelID);
            } else {
                builder = new NotificationCompat.Builder(this, null);
            }

            builder.setContentTitle("침수지역진입");
            builder.setContentText("곧 비가 옵니다. 확인해주세요");
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentIntent(mPendingIntent);
            builder.setVibrate(new long[]{0, 1000});
            builder.setAutoCancel(true);

            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
