package com.wind.carmanager.okhttp.api;

/**
 * 作者：Created by luow on 2018/6/29
 * 注释：url接口
 */
public class Api {

    //测试服务器地址
    private static final String BASE_URL = "http://101.132.73.104:5002/api";

    public static final String LOGIN = BASE_URL + "/user/token"; //登录接口
    public static final String GET_PHONE_CODE = BASE_URL + "/user/verifycode"; //获取验证码
    public static final String REGISTER = BASE_URL + "/user"; //注册
    public static final String PASSWORD = BASE_URL + "/user/password"; //找回密码/验证手机验证码
    public static final String EBIKE = BASE_URL + "/ebike"; //绑定设备
    public static final String WARN = BASE_URL + "/ebike/warn/"; //获取指定设备历史推送消息
    public static final String VERSION = BASE_URL + "/terminal/version"; //获取最新版本软件信息
    public static final String PACKAGE = BASE_URL + "/terminal/package"; //获取软件安装包

    public static final String ALL_DEVICE_REALTIME_INFO = BASE_URL + "/ebike/realtime";
    public static final String HISTORY_TRACK = BASE_URL + "/ebike/traces/";
    public static final String USER_INFO = BASE_URL + "/user";


}
