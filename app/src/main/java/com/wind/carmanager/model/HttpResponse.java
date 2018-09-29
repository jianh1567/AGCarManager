package com.wind.carmanager.model;

import java.io.Serializable;

/**
 * 作者：Created by luow on 2018/7/2
 * 注释：请求响应体
 */
public class HttpResponse implements Serializable {

    /**
     * code	String	返回状态码
     * msg	String	返回信息说明
     */
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
