package com.c323proj9.zacharyreid_sms_scheduler;

import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class IndicatorWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Log.i("DEBUG_UPDATE", "MADE IT HERE");

        SharedPreferences shprefs = context.getSharedPreferences("SMS_Scheduler", MODE_PRIVATE);
        int alarmCount = shprefs.getInt("COUNT", 0);

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.indicator_widget);
        RemoteViews views;
        String text;
        if(alarmCount > 0) {
            text = "GREEN";
            views = new RemoteViews(context.getPackageName(), R.layout.indicator_widget2);
        } else {
            text = "RED";
            views = new RemoteViews(context.getPackageName(), R.layout.indicator_widget);
        }
        views.setTextViewText(R.id.appwidget_text, text);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Log.i("DEBUG_ONUPDATE", "MADE IT HERE");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

