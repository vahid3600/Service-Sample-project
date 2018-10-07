package com.yaratech.servicesampleproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Vah on 9/22/2018.
 */

public class MyService extends Service {

    public int sum;
    private Thread thread;
    private MyThread myThread;
    private final IBinder mBinder = new LocalService();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalService extends Binder {

        MyService getService() {
            return MyService.this;
        }
    }

    public void startSumService(int sum){
        myThread = new MyThread(getApplicationContext(), sum);
        thread = new Thread(myThread);
        thread.start();
    }

    public int getSumValue(){
        return myThread.sum;
    }
}
