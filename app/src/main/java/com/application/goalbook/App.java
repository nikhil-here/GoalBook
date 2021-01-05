package com.application.goalbook;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.application.goalbook.AlaramManager.AlaramHandler;

public class App extends Application {

    public static final String CHANNEL_ID_GENERAL = "general";

    @Override
    public void onCreate() {
        createNotificationChannel();
        startReminderService();
        super.onCreate();
    }

    private void startReminderService() {
        AlaramHandler alaramHandler = new AlaramHandler(getApplicationContext());
        alaramHandler.setAlaram();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel generalChannel = new NotificationChannel(
                    CHANNEL_ID_GENERAL,
                    "General Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            generalChannel.setDescription("Showing Reminders for Goals");
            generalChannel.enableVibration(true);
            generalChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(generalChannel);
        }
    }
}
