package com.wind.carmanager.model;

import java.io.Serializable;

/**
 * Created by W010003373 on 2018/7/17.
 */

public class HistoryTrackBean implements Serializable {
    private int code;
    private String message;
    private ResultBean result;

    public int getCode(){
        return this.code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setResultBean(ResultBean result){
        this.result = result;
    }

    public ResultBean getResultBean(){
        return this.result;
    }

    public static class ResultBean{
        private Traces[] traces;

        public void setTraces(Traces[] traces){
            this.traces = traces;
        }

        public Traces[] getTraces(){
           return this.traces;
        }

        public static class Traces {
            private String average_speed;
            private int device_id;
            private String end_address;
            private String end_time;
            private float single_distance;
            private int single_time;
            private String start_address;
            private String start_time;
            private String time;

            public void setAverageSpeed(String speed){
                this.average_speed = speed;
            }

            public String getAverageSpeed(){
                return this.average_speed;
            }

            public void setDeviceId(int id){
                this.device_id = id;
            }

            public int getDeviceId(){
                return this.device_id;
            }

            public void setEndAddress(String endAddress){
                this.end_address = endAddress;
            }

            public String getEndAddress(){
                return this.end_address;
            }

            public void setEndTime(String endTime){
                this.end_time = endTime;
            }

            public String getEndTime(){
                return this.end_time;
            }

            public void setSingleDistance(float distance){
                this.single_distance = distance;
            }

            public float getSingleDistance(){
                return this.single_distance;
            }

            public void setSingleTime(int time){
                this.single_time = time;
            }

            public int getSingleTime(){
                return this.single_time;
            }

            public void setStartAddress(String address){
                this.start_address = address;
            }

            public String getStartAddress(){
                return this.start_address;
            }

            public void setStartTime(String time){
                this.start_time = time;
            }

            public String getStartTime(){
                return this.start_time;
            }

            public void setTime(String time){
                this.time = time;
            }

            public String getTime(){
                return this.time;
            }
        }
    }


}
