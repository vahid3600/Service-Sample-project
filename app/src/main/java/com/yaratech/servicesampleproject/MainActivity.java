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
        if (isBind){
            unbindService(mConnection);
            isBind = false;
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
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MyService.LocalService localService = (MyService.LocalService) iBinder;
            myService = localService.getService();
            myService.startSumService(sum);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sum = myService.getSumValue();
            Log.e("TAG", "onServiceDisconnected: "+sum );
            isBind = false;
        }
    };
}
