package com.wind.carmanager.model;

import java.io.Serializable;

/**
 * Created by W010003373 on 2018/7/17.
 */

public class AllDeviceRealtimesBean implements Serializable{
    private int code;
    private String message;
    private ResultBean result;

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public ResultBean getResultBean(){
        return result;
    }

    public void setResultBean(ResultBean resultBean){
        this.result = resultBean;
    }

    public static class ResultBean{
        private RealTimes[] realtimes;

        public void setRealTimes(RealTimes[] realTimes){
            this.realtimes = realTimes;
        }

        public RealTimes[] getRealTimes(){
            return this.realtimes;
        }

        public static class RealTimes{
            private String car_address;
            private int car_battery;
            private int car_distance;
            private int car_gprs;
            private int car_gps;
            private double car_latitude;
            private double car_longitude;
            private int car_speed;
            private int car_temperature;
            private int device_id;
            private String time;

            public String getCarAddress(){
                return this.car_address;
            }

            public void setCarAddress(String address){
                this.car_address = address;
            }

            public int getCarBattery(){
                return this.car_battery;
            }

            public void setCarBattery(int battery){
                this.car_battery = battery;
            }

            public int getCarDistance(){
                return this.car_distance;
            }

            public void setCarDistance(int distance){
                this.car_distance = distance;
            }

            public int getCarGprs(){
                return this.car_gprs;
            }

            public void setCarGprs(int gprs){
                this.car_gprs = gprs;
            }

            public int getCarGps(){
                return this.car_gps;
            }

            public void setCarGps(int gps){
                this.car_gps = gps;
            }

            public double getCarLatitude(){
                return this.car_latitude;
            }

            public void setCarLatitude(double latitude){
                this.car_latitude =latitude;
            }

            public double getCarLongitude(){
                return this.car_longitude;
            }

            public void setCarLongitude(double longitude){
                this.car_longitude = longitude;
            }

            public int getCarSpeed(){
                return this.car_speed;
            }

            public void setCarSpeed(int speed){
                this.car_speed = speed;
            }

            public int getCarTemperature(){
                return this.car_temperature;
            }

            public void setCarTemperature(int temperature){
                this.car_temperature = temperature;
            }

            public int getDeviceId(){
                return this.device_id;
            }

            public void setDeviceId(int id){
                this.device_id = id;
            }

            public String getTime(){
                return this.time;
            }

            public void setTime(String time){
                this.time = time;
            }
        }
    }
}
