package com.application.goalbook.AlaramManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlaramHandler {
    private Context context;
    public static final String TAG = "AlaramHandler";

    public AlaramHandler(Context context)
    {
        this.context = context;
    }

    public void setAlaram()
    {
        Intent intent = new Intent(context,ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am!= null)
        {
            long triggerAfter = 60 * 1000; //this will trigger the service after 1 min
            long triggerEvery = 24 * 60 * 60 * 1000; //this will repeat it every 1 day after that
            am.setRepeating(AlarmManager.RTC_WAKEUP,triggerAfter,triggerEvery,pendingIntent);
        }

        Log.i(TAG, "setAlaram: alaram is set");
    }

    public void cancelAlarm()
    {
        Intent intent = new Intent(context,ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am!=null)
        {
            am.cancel(pendingIntent);
        }
        Log.i(TAG, "cancelAlarm: alaram is cancel");
    }

}
