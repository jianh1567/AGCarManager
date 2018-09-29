package com.wind.carmanager.model;

/**
 * Created by houjian on 2018/7/3.
 */

public class CarStatusInfo {
    public int imageviewId;
    public String title;
    public int descbId;

    public CarStatusInfo(int imageviewId, String title, int descbId){
        this.imageviewId = imageviewId;
        this.title = title;
        this.descbId = descbId;
    }

    public void setImageviewId(int imageviewId){
        this.imageviewId = imageviewId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescbId(int descbId){
        this.descbId = descbId;
    }

    public int getImageviewId(){
        return  this.imageviewId;
    }

    public String getTitle(){
        return  this.title;
    }

    public int getDescbId(){
        return  this.descbId;
    }
}
