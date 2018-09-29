package com.wind.carmanager.model;

import java.io.Serializable;

/**
 * Created by W010003373 on 2018/7/20.
 */

public class AllDeviceInfoBean implements Serializable{
    private int code;
    private String message;
    private ResultBean result;

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setResultBean(ResultBean resultBean){
        this.result = resultBean;
    }

    public ResultBean getResultBean(){
        return this.result;
    }

    public static class ResultBean{
        private Devices[] devices;

        public void setDevices(Devices[] devices){
            this.devices = devices;
        }

        public Devices[] getDevices(){
            return this.devices;
        }

        public static class Devices{
            private int id;
            private String name;
            private String nickname;
            private String status;
            private String sensitivity;
            private String create_time;
            private String update_time;

            public void setId(int id){
                this.id = id;
            }

            public int getId(){
                return this.id;
            }

            public void setName(String name){
                this.name = name;
            }

            public String getName(){
                return this.name;
            }

            public void setNickname(String nickname){
                this.nickname = nickname;
            }

            public String getNickname(){
                return this.nickname;
            }

            public void setStatus(String status){
                this.status = status;
            }

            public String getStatus(){
                return this.status;
            }

            public void setSensitivity(String sensitivity){
                this.sensitivity = sensitivity;
            }

            public String getSensitivity(){
                return this.sensitivity;
            }

            public void setCreate_time(String create_time){
                this.create_time = create_time;
            }

            public String getCreate_time(){
                return this.create_time;
            }

            public void setUpdate_time(String update_time){
                this.update_time = update_time;
            }

            public String getUpdate_time(){
                return this.update_time;
            }
        }
    }


}
