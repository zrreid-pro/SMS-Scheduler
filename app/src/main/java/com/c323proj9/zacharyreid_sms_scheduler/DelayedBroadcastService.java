package com.c323proj9.zacharyreid_sms_scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class DelayedBroadcastService extends Service {

    final class SideThread implements Runnable {
        public int serviceId;
        private CountDownTimer timer;
        private long timeLeft; //the time till the broadcast needs to go off in milliseconds
        private boolean timerRunning = false;

        public SideThread(int serviceId) {
            this.serviceId = serviceId;

        }
        @Override
        public void run() {

        }
    }
    public DelayedBroadcastService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
