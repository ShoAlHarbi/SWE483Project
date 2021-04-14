package com.example.swe483project;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService extends Service {

    private String TAG = "TimerService";
    public static final String COUNTDOWN_BR = "com.example.TimerService";
    private static final String CHANNEL_ID = "TSC";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG,"Starting timer...");

        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG,"Countdown seconds remaining:" + millisUntilFinished / 1000);
                intent.putExtra("countdown",millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                Log.i(TAG,"Time is up !!!!");
                intent.putExtra("finish",true);
                sendBroadcast(intent);
                //sen email
                stopForeground(true);
                stopSelf();
            }
        };
        countDownTimer.start();
    }//end onCreate


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent != null) {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, createTimerNotification());
            } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createTimerNotification() {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, VerificationActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent,0);


        return new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Verification Timer")
                    .setContentText("on going verification process")
                    .setSmallIcon(R.drawable.ic_protection)
                    .setContentIntent(pendingIntent)
                    .build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "TimerServiceChannel";
        String description = "A channel for the verification timer notifications";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
