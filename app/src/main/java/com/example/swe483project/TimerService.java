package com.example.swe483project;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.security.Provider;

public class TimerService extends Service {

    public static final String COUNTDOWN_BR = "com.example.swe483project";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();


        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown",millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                //sen email
            }
        };
        countDownTimer.start();
    }//end onCreate


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        if(intent != null) {
//            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
//                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, createTimerNotification());
//            } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
//                stopForeground(true);
//                stopSelf();
//            }
//        }
//        return START_STICKY;
//
//    }

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

//    private Notification createTimerNotification() {
//        Intent notificationIntent = new Intent(this, HomeActivity.class);
//        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent,0);
//
//
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
//                    .setContentTitle("Verification Timer")
//                    .setContentText("on going verification process")
//                    .setSmallIcon(R.drawable.ic_protection)
//                    .setContentIntent(pendingIntent)
//                    .build();
//        }
//        return notification;
//    }
}
