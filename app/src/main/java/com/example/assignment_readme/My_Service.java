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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class My_Service extends Service {

    Context context;
    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 20 * 1000 - SystemClock.elapsedRealtime() % 1000);
            callApi();
        }
    };

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDataFetch();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        startDataFetch();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void startDataFetch() {
        //start timer for api call in 2 min interval
    }


    public void stopDataFetch() {
        //stop timer
    }

    public void callApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://ip-api.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<String> call = jsonPlaceHolderApi.getPost();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code "+response.code());
                    return;
                }

                String posts = response.body();
                Intent intent = new Intent("Api Data");
                intent.putExtra("status", posts);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }

            @Override
            public void onFailure (Call < String > call, Throwable t){
                return;
            }
        });


    }
}

