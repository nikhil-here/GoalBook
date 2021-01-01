package com.application.goalbook.Utility;

import android.content.Context;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StringFormatter {

    private Context context;

    public StringFormatter(Context context) {
        this.context = context;
    }

    public  String countFormatter(int count)
    {
        DecimalFormat countFormat = new DecimalFormat("00");
        return countFormat.format(count);
    }

    public String timeLineFormatter(Long timestamp)
    {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(new Date(TimeUnit.MILLISECONDS.toMillis(timestamp)));
    }
}
