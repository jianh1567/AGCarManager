package com.wind.carmanager.model;

/**
 * Created by houjian on 2018/7/4.
 */

public class HistoryTrackInfo {
    private String startTime;
    private String endTime;
    private String trackDate;

    public HistoryTrackInfo(String startTime, String endTime, String trackDate){
        this.startTime = startTime;
        this.endTime = endTime;
        this.trackDate = trackDate;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public void setTrackDate(String trackDate){
        this.trackDate = trackDate;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public String getTrackDate(){
        return  this.trackDate;
    }
}
