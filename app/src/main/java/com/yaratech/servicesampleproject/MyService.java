package com.yaratech.servicesampleproject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by Vah on 9/22/2018.
 */

public class MyService extends Service {

    private final String TAG = MyService.class.getSimpleName();
    public int sum;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private final IBinder mBinder = new LocalService();

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        generateRandomSum();
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

    private void generateRandomSum() {

        sum = sharedPreferences.getInt(TAG, 0);
        final Handler handler = new Handler();
        Runnable serviceRunnable = new Runnable() {
            @Override
            public void run() {
                sum += new Random().nextInt(100);

                if (!isAppOnForeground(getApplicationContext()))
                    showNotification(String.valueOf(sum));
                sharedPreferences.edit().putInt(TAG, sum).apply();
                handler.postDelayed(this, MainActivity.DELAY);
            }
        };
        handler.post(serviceRunnable);
    }

    private void showNotification(String msg) {
        NotificationCompat.Builder nBuild = new NotificationCompat.Builder(getApplicationContext());
        nBuild.setContentTitle("No Title")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_background);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, nBuild.build());
    }


    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: " );
        sharedPreferences.edit().putInt(TAG, 0).apply();
        this.stopSelf();
        super.onDestroy();
    }

}
