package com.application.goalbook.Database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_table")
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private int pid;
    private String name;
    private String purpose;
    private String mission;
    private String vision;
    @ColumnInfo(name = "profile_image")
    private String profileImage;
    @ColumnInfo(name = "color_preference")
    private String colorPreference;
    @ColumnInfo(name = "notification_status")
    private Boolean showNotification;
    @ColumnInfo(name = "last_updated_on")
    private Long lastUpdatedOn;

    public Profile(String name, String purpose, String mission, String vision, String profileImage, String colorPreference, Boolean showNotification, Long lastUpdatedOn) {
        this.name = name;
        this.purpose = purpose;
        this.mission = mission;
        this.vision = vision;
        this.profileImage = profileImage;
        this.colorPreference = colorPreference;
        this.showNotification = showNotification;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getColorPreference() {
        return colorPreference;
    }

    public void setColorPreference(String colorPreference) {
        this.colorPreference = colorPreference;
    }

    public Boolean getShowNotification() {
        return showNotification;
    }

    public void setShowNotification(Boolean showNotification) {
        this.showNotification = showNotification;
    }

    public Long getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Long lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", purpose='" + purpose + '\'' +
                ", mission='" + mission + '\'' +
                ", vision='" + vision + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", colorPreference='" + colorPreference + '\'' +
                ", showNotification=" + showNotification +
                ", lastUpdatedOn=" + lastUpdatedOn +
                '}';
    }
}
