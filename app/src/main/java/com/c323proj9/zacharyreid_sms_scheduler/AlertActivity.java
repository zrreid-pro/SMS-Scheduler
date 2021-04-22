package com.c323proj9.zacharyreid_sms_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {
    private String phoneNumber, message;
    private String number;
    TextView phoneNumberText;
    EditText messageText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Intent intent = getIntent();

        phoneNumber = intent.getStringExtra("NUMBER");
        message = intent.getStringExtra("MESSAGE");

        phoneNumberText = findViewById(R.id.phoneNumberText_alert);
        messageText = findViewById(R.id.messageText_alert);
        sendButton = findViewById(R.id.sendButton);

        phoneNumberText.setText(phoneNumber);
        messageText.setText(message);
        number = phoneNumber.substring(1,4) + phoneNumber.substring(6,9) + phoneNumber.substring(10);
        //Log.i("DEBUG_ALERT", number);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS(phoneNumber, message);
                finish();
            }
        });

        Intent updateIntent = new Intent(this, IndicatorWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IndicatorWidget.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(updateIntent);
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
