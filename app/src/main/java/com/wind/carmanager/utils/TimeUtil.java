package com.wind.carmanager.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by W010003373 on 2018/7/23.
 */

public class TimeUtil {
    private static final String TAG = "TimeUtil";

    public static long timeToStamp(String data){
        long time = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(data);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "time = " + time);// time is ms
        return time;
    }

    public static String stampToChineseDate(long date){
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        str = sdf.format(date);
        return str;
    }

    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            DateTimes= days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(hours>0){
            DateTimes=hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        }else if(minutes>0){
            DateTimes=minutes + "分钟"
                    + seconds + "秒";
        }else{
            DateTimes=seconds + "秒";
        }

        return DateTimes;
    }
}
