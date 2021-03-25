package com.example.assignment_readme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button1, button2;
    TextView textView;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.start);
        button2 = findViewById(R.id.stop);
        textView = findViewById(R.id.textview);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("API_DATA"));


        context = this;


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
            }
        });


    }

    public void startService() {
        Intent serviceIntent = new Intent(MainActivity.this, My_Service.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    public void stopService() {
        Intent serviceIntent = new Intent(MainActivity.this, My_Service.class);
        stopService(serviceIntent);
    }


    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder data = new StringBuilder();
                    data.append(intent.getStringExtra("timezone"));
                    data.append(":");
                    data.append(intent.getStringExtra("ip"));
                    textView.setText(data.toString());
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}