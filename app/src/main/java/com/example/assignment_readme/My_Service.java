package com.example.assignment_readme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class My_Service extends Service {

    Context context;
    Timer timer;
    TimerTask timerTask;
    int Your_X_SECS = 20;
    final Handler handler = new Handler();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context context;
        context = this;

        Intent intentNotify = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intentNotify}, 0);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("Channel_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this,
                "Channel_ID")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("message")
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);

        return START_STICKY;
    }
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5 * 1000, Your_X_SECS * 500);
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d(TAG, "run: call");
                        callApi();
                    }
                });
            }
        };
    }
    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
    }

    @Override
    public void onCreate() {
        context = this;
        startTimer();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void callApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://ip-api.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Data> call = jsonPlaceHolderApi.getPost();

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: "+response.body());
                Data posts = response.body();
                Intent intent = new Intent("API_DATA");
                intent.putExtra("ip", posts.getQuery());
                intent.putExtra("timezone", posts.getTimezone());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }

            @Override
            public void onFailure (Call < Data > call, Throwable t){
                t.printStackTrace();
            }
        });


    }
}

