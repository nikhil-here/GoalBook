package com.application.goalbook.Database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

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
}
