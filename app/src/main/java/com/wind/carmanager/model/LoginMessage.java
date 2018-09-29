package com.wind.carmanager.model;

/**
 * Created by houjian on 2018/5/31.
 */

public class LoginMessage {
    private String msg;
    private int status;

    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getMsg(){
        return this.msg;
    }

    public int getStatus(){
        return this.status;
    }
}
