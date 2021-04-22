package com.c323proj9.zacharyreid_sms_scheduler;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

public class ScheduledBroadcastReceiver extends BroadcastReceiver {
    private String phoneNumber, message;
    private SharedPreferences shprefs;
    private int alarmCount;
    private int maxAlarms;


    @Override
    public void onReceive(Context context, Intent intent) {
        shprefs = context.getSharedPreferences("SMS_Scheduler", MODE_PRIVATE);
        alarmCount = shprefs.getInt("COUNT", 0);
        maxAlarms = shprefs.getInt("MAX", 0);

        Log.i("DEBUG_BROADCAST_RECEIVER", "ALARM FIRED");

        phoneNumber = intent.getStringExtra("NUMBER");
        message = intent.getStringExtra("MESSAGE");

        //Log.i("DEBUG_BROADCAST_RECEIVER", "Number: "+phoneNumber+"/Message: "+message);

        alarmCount--;
        Log.i("DEBUG_BROADCAST_RECEIVER", "Alarm Count: "+alarmCount);
        SharedPreferences.Editor editor = shprefs.edit();
        editor.putInt("COUNT", alarmCount);
        if(alarmCount == 0) { maxAlarms = 0; }
        editor.putInt("MAX", maxAlarms);
        editor.commit();

        /*Intent update = new Intent(context.getApplicationContext(),android.appwidget.AppWidgetProvider.class);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.sendBroadcast(update);*/

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.indicator_widget);
        ComponentName thisWidget = new ComponentName(context, IndicatorWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);


        Intent alertIntent = new Intent(context.getApplicationContext(), AlertActivity.class);
        alertIntent.putExtra("NUMBER", phoneNumber);
        alertIntent.putExtra("MESSAGE", message);
        alertIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alertIntent);
    }
}
