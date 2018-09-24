package com.yaratech.servicesampleproject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;

import java.util.Random;

/**
 * Created by Vah on 9/23/2018.
 */

public class MyThread implements Runnable {

    int serviceId;
    int sum;
    Context context;
    NotificationManager notificationManager;
    static SharedPreferences SUM;

    public MyThread(Context context, int serviceId, int sum) {
        this.serviceId = serviceId;
        this.sum = sum;
        this.context = context;
        SUM = context.getSharedPreferences(MainActivity.MAIN_ACTIVITY_TAG, Context.MODE_PRIVATE);
    }

    @Override
    public void run() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                sum += random.nextInt(100);
                showNotification(sum+"");
                handler.postDelayed(this, MainActivity.DELAY);
            }
        };
        handler.post(runnable);
    }

    private void showNotification(String msg){
        NotificationCompat.Builder nBuild = new NotificationCompat.Builder(context);
        nBuild.setContentTitle("No Title")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_background);
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, nBuild.build());
    }

    void onStopService(){
        SUM.edit().putInt(MainActivity.MAIN_ACTIVITY_TAG, sum).apply();
    }
}
