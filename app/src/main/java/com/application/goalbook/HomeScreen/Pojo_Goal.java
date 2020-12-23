package com.application.goalbook.HomeScreen;

public class Pojo_Goal {

    private String coverUrl;
    private String title;
    private String tag;
    private String remainingTime;
    private Integer colorId;

    public Pojo_Goal(String coverUrl, String title, String tag, String remainingTime, Integer colorId) {
        this.coverUrl = coverUrl;
        this.title = title;
        this.tag = tag;
        this.remainingTime = remainingTime;
        this.colorId = colorId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }
}
