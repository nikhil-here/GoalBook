package com.application.goalbook.Database;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.application.goalbook.Utility.Constants;
import com.application.goalbook.Utility.StringFormatter;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity(tableName = "goal_table")
public class Goal {

    @ColumnInfo(name = "gid")
    @PrimaryKey(autoGenerate = true)
    private int gid;


    private String coverImage;
    private String title;
    private String description;
    private ArrayList<String> tags;
    private String color;
    private int status;

    @ColumnInfo(name = "start_date")
    private Long startDate;

    @ColumnInfo(name = "end_date")
    private Long endDate;

    @ColumnInfo(name = "reminder_frequencey")
    private int reminderFrequency;




    public Goal(String coverImage, String title, String description, ArrayList<String> tags, String color, int status, Long startDate, Long endDate, int reminderFrequency) {
        this.coverImage = coverImage;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.color = color;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderFrequency = reminderFrequency;
    }

    public int getGid() {
        return gid;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getColor() {
        return color;
    }

    public int getStatus() {
        return status;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public int getReminderFrequency() {
        return reminderFrequency;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "gid=" + gid +
                ", coverImage=" + coverImage +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", color='" + color + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reminderFrequency=" + reminderFrequency +
                '}';
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public void setReminderFrequency(int reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public String getRemainingTime()
    {
        DecimalFormat countFormat = new DecimalFormat("00");
        Long currentDateInSeconds = System.currentTimeMillis() / 1000;
        Long endDateInSeconds = this.endDate / 1000;
        float difference;
        String tense = "";


        String [] periods = {"second","minute","hour","day","week","month","year","decade"};
        float [] lengths = {60,60,24,7, (float) 4.35,12,10};

        if (currentDateInSeconds > endDateInSeconds)
        {
            difference = currentDateInSeconds - endDateInSeconds;
            tense = "passed";
        }else{
            difference = endDateInSeconds - currentDateInSeconds;
            tense = "remaining";
        }

        int i = 0;
        while((difference >= lengths[i]) && (i < lengths.length))
        {
            difference = difference / lengths[i];
            i++;
        }

        difference = Math.round(difference);

        if (difference != 1)
        {
            periods[i] = periods[i] + "s";
        }

        return countFormat.format(difference)+" "+periods[i]+ " "+tense;
    }

    public Boolean showNotification()
    {
        Date StartDate = new Date(this.startDate);
        Date EndDate = new Date(this.endDate);

        //Goal End Date is not passed and reminder is not set to none
        if (endDate > startDate && this.reminderFrequency != Constants.REMINDER_NONE && this.status == Constants.STATUS_PENDING)
        {
            int days = (int)( (EndDate.getTime() - StartDate.getTime()) / (1000 * 60 * 60 * 24));
            int divider = 1;
            switch (this.reminderFrequency)
            {
                case Constants.REMINDER_DAILY :
                    divider = 1;
                    break;
                case Constants.REMINDER_WEEKLY :
                    divider = 7;
                    break;
                case Constants.REMINDER_MONTHLY :
                    divider = 31;
                    break;
                case Constants.REMINDER_YEARLY :
                    divider = 365;
                    break;
            }
            Log.i("goal", "showNotification: days : "+days+" divider "+divider + " reminder : "+(days%divider));


            if (days%divider == 0)
            {
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

}
