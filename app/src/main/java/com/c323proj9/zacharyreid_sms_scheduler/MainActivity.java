package com.c323proj9.zacharyreid_sms_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText phoneNumberText, messageText;
    Button deleteButton, timeSetButton;
    private Calendar scheduledTime;
    private String phoneNumber, message;
    private SharedPreferences shprefs;
    private int alarmCount;
    private int maxAlarms;
    //private int setHour, setMinute;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberText = findViewById(R.id.phoneNumberText);
        messageText = findViewById(R.id.messageText);
        deleteButton = findViewById(R.id.deleteButton);
        timeSetButton = findViewById(R.id.timeSetButton);

        if(!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        shprefs = getSharedPreferences("SMS_Scheduler", MODE_PRIVATE);
        alarmCount = shprefs.getInt("COUNT", 0);
        maxAlarms = shprefs.getInt("MAX", 0);

        Log.i("DEBUG_ALARM_CHECK", String.valueOf(alarmCount));

        phoneNumberText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Log.i("DEBUG_ALARM_CHECK1", String.valueOf(checkForAlarms()));

        timeSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                final TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.i("DEBUG_TIME_SET", "Time chosen: "+selectedHour+":"+selectedMinute);
                        scheduledTime = Calendar.getInstance();
                        scheduledTime.set(currentTime.get(Calendar.YEAR),
                                currentTime.get(Calendar.MONTH),
                                currentTime.get(Calendar.DAY_OF_MONTH),
                                selectedHour, selectedMinute, 0);
                        scheduleSMS(scheduledTime.getTimeInMillis(),
                                phoneNumberText.getText().toString(),
                                messageText.getText().toString());
                        phoneNumberText.setText("");
                        messageText.setText("");
                        Log.i("DEBUG_ALARM_CHECK2", String.valueOf(checkForAlarms()));

                    }
                }, hour, minute, false);
                timePicker.setButton(DialogInterface.BUTTON_POSITIVE, "SCHEDULE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  }
                });


                timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i("DEBUG_SCHEDULE", "Hello");
                        phoneNumberText.setText("");
                        messageText.setText("");
                    }
                });
                timePicker.show();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSchedule();
            }
        });

        if(checkForAlarms()) {
            deleteButton.setEnabled(true);
        } else {
            deleteButton.setEnabled(false);
        }



    }

    private void scheduleSMS(long time, String phoneNumber, String message) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ScheduledBroadcastReceiver.class);
        intent.putExtra("NUMBER", phoneNumber);
        intent.putExtra("MESSAGE", message);

        PendingIntent pIntent = PendingIntent.getBroadcast(this, alarmCount, intent, 0);

        alarmCount++;
        maxAlarms++;
        SharedPreferences.Editor editor = shprefs.edit();
        editor.putInt("COUNT", alarmCount);
        editor.putInt("MAX", maxAlarms);
        editor.commit();
        Log.i("DEBUG_ALARM_CHECK_SCHEDULE", String.valueOf(alarmCount));

        Intent updateIntent = new Intent(this, IndicatorWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IndicatorWidget.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(updateIntent);



        manager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);

        deleteButton.setEnabled(true);

        Log.i("DEBUG_MAIN", "ALARM SET");
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if(context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkForAlarms() {
        boolean alarmSet;
        for(int i = 0; i < maxAlarms; i++) {
            alarmSet = (PendingIntent.getBroadcast(this, i, new Intent(this, ScheduledBroadcastReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
            if(alarmSet) { return true; }
        }
        return false;
    }

    private void clearSchedule() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for(int i = 0; i < maxAlarms; i++) {
            Intent intent = new Intent(this, ScheduledBroadcastReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, i, intent, 0);
            manager.cancel(pi);
            pi.cancel();
        }
        alarmCount = 0;
        maxAlarms = 0;
        SharedPreferences.Editor editor = shprefs.edit();
        editor.putInt("COUNT", alarmCount);
        editor.putInt("MAX", maxAlarms);
        editor.commit();

        Intent updateIntent = new Intent(this, IndicatorWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IndicatorWidget.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(updateIntent);

        deleteButton.setEnabled(false);
    }

    @Override
    protected void onStop() {
        /*SharedPreferences.Editor editor = shprefs.edit();
        editor.putInt("COUNT", alarmCount);
        editor.putInt("MAX", maxAlarms);
        editor.commit();*/
        super.onStop();
    }
}
