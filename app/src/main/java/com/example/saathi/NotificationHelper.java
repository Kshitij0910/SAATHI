package com.example.saathi;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NotificationHelper extends ContextWrapper{





    private static final String TAG = "NotificationHelper";

    public static final String channelID = "channelID";
    public static final String channelName = "Medicines";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);

        //channelIDs.add(channelID);
        //channelNames.add(channelName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        //for (int i=0; i<channelIDs.size(); i++) {
            NotificationChannel  channel= new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(channel);

        //}






    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification() {
        Intent activityIntent=new Intent(this, MainActivity.class);
        activityIntent.putExtra("NotificationFragment", "MedicinesViewFragment");
        PendingIntent contentIntent=PendingIntent.getActivity(this, 0, activityIntent, 0);


        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("MEDICINE TIME")
                .setContentText("It is time for you to take your medicines for the day.")
                .setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo_icon);
    }
}

