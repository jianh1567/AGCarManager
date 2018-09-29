package com.wind.carmanager.model;

/**
 * 作者：Created by luow on 2018/7/4
 * 注释：消息中心返回数据
 */
public class MessageBean {

    private String data;
    private String title;
    private String time;
    private String desc;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
