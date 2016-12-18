package com.project.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by sushil on 12/17/16.
 */

public class Notifier {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void compareDistance(Context context,double vehicleDistance){
        double savedDistance = AppLocalData.getDistanceSavedForNotification(context);
        int vDistance = Integer.valueOf((int) vehicleDistance);
        if(MainActivity.active) {
            MainActivity.distance.setText("" + vDistance+"m");
        }
        if (vehicleDistance<=savedDistance){
            sendNotification(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void sendNotification(Context context){
        NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        //set
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_action_gps);
        builder.setContentText("Contents");
        builder.setContentTitle("title");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        nm.notify(1,notification);
    }


}
