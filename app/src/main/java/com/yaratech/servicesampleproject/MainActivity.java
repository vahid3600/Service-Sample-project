package com.yaratech.servicesampleproject;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView sumTextView;
    public static long DELAY = 10000;
    public int sum = 0;
    MyService myService;
    boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumTextView = findViewById(R.id.sum_text_view);
        ComponentName myService = startService(new Intent(this, MyService.class));
        bindService(new Intent(this, MyService.class), mConnection, BIND_AUTO_CREATE);
        startService(Intent.makeMainActivity(myService));

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: " );
        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            final MyService.LocalService localService = (MyService.LocalService) iBinder;

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    sum = localService.getService().sum;
                    sum += new Random().nextInt(100);
                    sumTextView.setText(sum + "");
                    handler.postDelayed(this, DELAY);
                }
            };
            handler.post(runnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myService.stopSelf();
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
