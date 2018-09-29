package com.wind.carmanager.okhttp.service;

/**
 * 作者：Created by luow on 2018/7/23
 * 注释：操作设备
 */
public class OperateDeviceRequest {

    private int device_id;
    private String target;
    private String target_value;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget_value() {
        return target_value;
    }

    public void setTarget_value(String target_value) {
        this.target_value = target_value;
    }
}
