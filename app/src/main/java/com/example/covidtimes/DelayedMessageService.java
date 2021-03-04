package com.example.covidtimes;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 104; //arbitrary number, can change later
    private static final String CHANNEL_ID = "notif_channel_id_104";

    public DelayedMessageService(){
        super ("DelayedMessageService");
    }
    @Override protected void onHandleIntent(Intent intent){
        synchronized(this){
            try{
                wait(10000);//change this wait timeout value later
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text){
        Intent broadIntent = new Intent(this, MyReceiver.class);
        broadIntent.setAction("Check new cases for : " + text);
        broadIntent.putExtra(DelayedMessageService.EXTRA_MESSAGE, dateStringHelper.stateToAbbrev(text));
        PendingIntent broadPending = PendingIntent.getBroadcast(this, 0 , broadIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getResources().getString(R.string.notif_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launcher_foreground, "Check new cases for: " + text, broadPending)
                //.setVibrate(new long[] {0,100})
                .setAutoCancel(true);
        Intent actionIntent = new Intent(this, appMainPage.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this,
                0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notification channel for covid-times");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}
