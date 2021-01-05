package com.application.goalbook.AlaramManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.application.goalbook.App;
import com.application.goalbook.Database.Database;
import com.application.goalbook.Database.Goal;
import com.application.goalbook.Database.GoalViewModel;
import com.application.goalbook.Database.Profile;
import com.application.goalbook.Database.ProfileViewModel;
import com.application.goalbook.HomeScreen.MainActivity;
import com.application.goalbook.Profile.ProfileActivity;
import com.application.goalbook.R;
import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.ImageSaver;

import java.util.List;
import java.util.Random;

public class ReminderService extends BroadcastReceiver {

    private Context context;
    private NotificationManagerCompat notificationManagerCompat;
    private GoalViewModel goalViewModel;
    private ProfileViewModel profileViewModel;
    private Database db;
    private LiveData<List<Goal>> allGoals;
    private LiveData<Profile> profile;

    public static final String TAG = "ReminderService";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
        db = Database.getInstance(context);
        allGoals = db.goalDao().getAllGoals();
        profile = db.profileDao().getProfileLiveData();




        allGoals.observeForever(new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                for(int i = 0; i<goals.size(); i++)
                {
                    Goal goal = goals.get(i);
                    if (goal.showNotification())
                    {
                        showGoalNotification(goal);
                    }
                }
            }
        });

        if (profile != null)
        {

            profile.observeForever(new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    if (profile.getShowNotification())
                    {
                        showProfileNotification(profile);
                    }
                }
            });
        }






    }

    private void showProfileNotification(Profile profile) {
        Log.i(TAG, "onReceive: showProfileNotification is called ");

        int pid = profile.getPid();
        String title = profile.getName();
        String message = Constants.NOTIFICATION_MSG_ABOUT;
        String largeIcon = profile.getProfileImage();

        Intent generalIntent = new Intent(context, ProfileActivity.class);
        PendingIntent generalPendingIntent = PendingIntent.getActivity(context, 0, generalIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID_GENERAL);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(generalPendingIntent);


        if (largeIcon == null) {
            //build notification without image
            Notification notification = builder.build();
            notificationManagerCompat.notify(pid, notification);
        } else {
            Bitmap bitmap = new ImageSaver(context).
                    setFileName(largeIcon).
                    setDirectoryName(Constants.DIRECTORY_NAME).
                    load();

            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null));
            Notification notification = builder.build();
            notificationManagerCompat.notify(new Random().nextInt(1000), notification);
        }

    }

    private void showGoalNotification(Goal goal) {
        Log.i(TAG, "onReceive: showGoalNotification is called ");

        int gid = goal.getGid();
        String title = goal.getTitle();
        String message = goal.getDescription();
        String remainingTime = goal.getRemainingTime();
        String largeIcon = goal.getCoverImage();


        Intent generalIntent = new Intent(context, MainActivity.class);
        PendingIntent generalPendingIntent = PendingIntent.getActivity(context, 0, generalIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID_GENERAL);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().setSummaryText(remainingTime))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(generalPendingIntent);


        if (largeIcon == null) {
            //build notification without image
            Notification notification = builder.build();
            notificationManagerCompat.notify(gid, notification);
        } else {
            Bitmap bitmap = new ImageSaver(context).
                    setFileName(largeIcon).
                    setDirectoryName(Constants.DIRECTORY_NAME).
                    load();

            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null));
            Notification notification = builder.build();
            notificationManagerCompat.notify(new Random().nextInt(1000), notification);
        }
    }
}
