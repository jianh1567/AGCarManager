package com.wind.carmanager.utils;

import android.content.Context;

/**
 * 作者：Created by luow on 2018/5/10
 * 注释：基本信息数据存取工具
 */
public class BaseInfoSPUtil {
    private static BaseInfoSPUtil instance;
    private SharedPreferenceUtil spUtil;//SharedPreference操作工具
    /*SP文件名*/
    private static final String FILE_NAME = "AG_CAR_MANAGER_INFO";
    /* key值 */
    public static final String KEY_USER_NAME = "KEY_USER_NAME";//用户名
    public static final String KEY_USER_PWD = "KEY_USER_PWD";//用户名密码
    public static final String KEY_IS_REMEMBER = "KEY_IS_REMEMBER";//是否记住密码
    public static final String KEY_LOGIN_TOKEN = "KEY_LOGIN_TOKEN";//登录凭证
    public static final String KEY_USER_PHONE_NUM = "KEY_USER_USER_PHONE_NUM";//用户手机号码
    public static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";//设备id
    public static final String KEY_SENSITIVITY = "KEY_SENSITIVITY";//获取灵敏度
    public static final String KEY_DEVICE_NAME = "KEY_DEVICE_NAME";//绑定的设备名称
    public static final String KEY_USER_ID = "KEY_USER_ID";

    public static final String KEY_BOUND_DEVICE_NAME = "KEY_BOUND_DEVICE_NAME";
    public static final String KEY_BOUND_DEVICE_ID = "KEY_BOUND_DEVICE_ID";

    /* 默认Value **/
    private static final String DEFAULT_USER_NAME = "";//默认 用户名
    private static final String DEFAULT_USER_PWD = "";//默认 用户名密码
    private static final boolean DEFAULT_IS_REMEMBER = false;//默认 false
    private static final String DEFAULT_LOGIN_TOKEN = "";//默认登录凭证
    private static final String DEFAULT_USER_PHONE_NUM = "";//用户手机号码
    private static final int DEFAULT_DEVICE_ID = -1;//设备id
    private static final String DEFAULT_SENSITIVITY = "";//灵敏度
    private static final String DEFAULT_DEVICE_NAME = "";//绑定的设备名称
	
	private static final String DEFAULT_BOUND_DEVICE_NAME="";
    private static final int DEFAULT_BOUND_DEVICE_ID = 1;
    private static final int DEFAULT_USER_ID = 1;

    //设置用户名
    public void setUserName(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_USER_NAME, token);
    }

    //获取用户名
    public String getUserName(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_USER_NAME, DEFAULT_USER_NAME);
    }

    //设置密码
    public void setUserPwd(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_USER_PWD, token);
    }

    //获取密码
    public String getUserPwd(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_USER_PWD, DEFAULT_USER_PWD);
    }

    //设置密码
    public Boolean isRemember(Context context) {
        return spUtil.getBooleanSP(context, FILE_NAME,
                KEY_IS_REMEMBER, DEFAULT_IS_REMEMBER);
    }

    //获取密码
    public void setRemember(Context context, Boolean value) {
        spUtil.setBooleanSP(context, FILE_NAME, KEY_IS_REMEMBER, value);
    }

    //存入登录Token
    public void setLoginToken(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_LOGIN_TOKEN, token);
    }

    //获取用户登录Token
    public String getLoginToken(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_LOGIN_TOKEN, DEFAULT_LOGIN_TOKEN);
    }

    //用户手机号
    public void setUserPhoneNum(Context context, String value) {
        spUtil.setStringSP(context, FILE_NAME, KEY_USER_PHONE_NUM, value);
    }

    //用户手机号
    public String getUserPhoneNum(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_USER_PHONE_NUM, DEFAULT_USER_PHONE_NUM);
    }


    //存储设备id
    public void setDeviceId(Context context, int value) {
        spUtil.setIntSP(context, FILE_NAME, KEY_DEVICE_ID, value);
    }

    //取设备id
    public int getDeviceId(Context context) {
        return spUtil.getIntSP(context, FILE_NAME,
                KEY_DEVICE_ID, DEFAULT_DEVICE_ID);
    }

    //存储灵敏度
    public void setSensitivity(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_SENSITIVITY, token);
    }

    //获取灵敏度
    public String getSensitivity(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_SENSITIVITY, DEFAULT_SENSITIVITY);
    }

    //存储绑定的设备名称
    public void setDeviceName(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_DEVICE_NAME, token);
    }

    //获取绑定的设备名称
    public String getDeviceName(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_DEVICE_NAME, DEFAULT_DEVICE_NAME);
    }

    public void setUserId(Context context, int id) {
        spUtil.setIntSP(context, FILE_NAME,
                KEY_USER_ID, id);
    }

    public int getUserId(Context context) {
        return spUtil.getIntSP(context,FILE_NAME,
                KEY_USER_ID, DEFAULT_USER_ID);
    }

    /**
     * 根据key删除SharedPreferences某数据
     *
     * @param context
     * @param key
     */
    public void removeSpData(Context context, String key) {
        spUtil.removeDataSP(context, FILE_NAME, key);
    }

    public static BaseInfoSPUtil getInstance() {
        if (instance == null) {
            instance = new BaseInfoSPUtil();
        }
        return instance;
    }

    private BaseInfoSPUtil() {
        spUtil = new SharedPreferenceUtil();
    }
}