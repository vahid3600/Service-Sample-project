package com.yaratech.servicesampleproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView sumTextView;
    public static String MAIN_ACTIVITY_TAG = "MainActivity";
    public static long DELAY = 10000;
    public int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumTextView = findViewById(R.id.sum_text_view);
        Log.e("TAG", "onCreate: "+isMyServiceRunning(MyService.class) );
        if (isMyServiceRunning(MyService.class)) {
            stopService(new Intent(this, MyService.class));
            sum = MyThread.SUM.getInt(MAIN_ACTIVITY_TAG, 0);
            Log.e("TAG", "onCreate: "+sum );
        }

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                sum += random.nextInt(100);
                sumTextView.setText(sum + "");
                handler.postDelayed(this, DELAY);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MAIN_ACTIVITY_TAG, sum);
        startService(intent);
        super.onStop();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }
}
