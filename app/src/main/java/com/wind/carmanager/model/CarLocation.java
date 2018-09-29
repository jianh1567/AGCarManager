package com.wind.carmanager.model;

/**
 * Created by houjian on 2018/5/31.
 */

public class CarLocation {
    private double latitude;
    private double longitude;

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
