package com.wind.carmanager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间辅助类
 *
 * @author specter
 */
public class DateUtils {
    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat timeformat = new SimpleDateFormat(
            "HH:mm");
    private static SimpleDateFormat formatNoMinute = new SimpleDateFormat(
            "yyyy/MM/dd");
    private static SimpleDateFormat formatHour = new SimpleDateFormat(
            "yyyy/MM/dd/HH");

    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    private DateUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 取yyyy-MM-dd HH:mm:ss的yyyy-MM-dd.
     *
     * @param str
     * @return
     */
    public static String getDate(String str) {
        str = str.split(" ")[0];
        return str;
    }

    /**
     * 根据时间戳返回日期格式字符串.
     *
     * @param timeStamp
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateStr(long timeStamp) {
        Date date = new Date(timeStamp);
        return format.format(date);
    }

    /**
     * 返回当前时间.yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDate() {
        Date date = new Date();
        return format.format(date);
    }

    /**
     * 返回当前时间.yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        return timeformat.format(date);
    }

    /**
     * 返回当前时间.yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDateNoMinute() {
        Date date = new Date();
        return formatNoMinute.format(date);
    }

    /**
     * 返回当前时间.yyyy/MM/dd/HH
     *
     * @return
     */
    public static String getCurrentDateHour() {
        Date date = new Date();
        return formatHour.format(date);
    }

    /**
     * 格式化时间
     *
     * @return
     */
    public static String getFormatDay(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = sdf.format(date1);
        return str;
    }

    /**
     * 得到明天的日期
     *
     * @return
     */
    public static String getTomorrowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse(getCurrentDateNoMinute(), new ParsePosition(0));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);// -1天
        Date yesterday = c.getTime();
        return sdf.format(yesterday);
    }

    /**
     * 得到15以后的日期
     *
     * @param strDate
     * @return
     */
    public static String getFifteenDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(strDate, new ParsePosition(0));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 15);// 15天后的日期
        Date yesterday = c.getTime();
        return sdf.format(yesterday);
    }


    /**
     * 得到最近七天(含今天)的日期
     * @return
     */
    public static String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH");
        Date date = sdf.parse(getCurrentDateHour(), new ParsePosition(0));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -6);// -6天
        Date yesterday = c.getTime();
        return sdf.format(yesterday);
    }

    public static int compareDate(String data1, String data2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(data1);
            Date dt2 = df.parse(data2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 两个时间相减得到的差,返回分钟和秒
     */
    public static String getTimeDifference(String currentTime, String oldTime) {

        long startT = fromDateStringToLong(currentTime); //定义上机时间
        long endT = fromDateStringToLong(oldTime);  //定义下机时间

        long ss = (startT - endT) / (1000); //共计微秒数
        int hour = (int) (ss / 3600);//共计小时数
        int dd = hour / 24;   //共计天数
        int minute = (int) ((ss - hour * 3600) / 60); //共计分钟数
        int second = (int) (ss - hour * 3600 - minute * 60); //共计秒数
        if (minute < 0) {
            return "0";
        }
        return minute + ":" + second;
    }

    public static long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
        Date date = null;   //定义时间类型
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();   //返回毫秒数
    }


    /**
     * 返回小时时间
     *
     * @return
     */
    public static String getFormatHours(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        try {
            date1 = sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = sdf.format(date1);
        return str;
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static String UpdatedAtValue(long lastTime) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastTime;
        long timeIntoFormat;
        String updateAtValue;
        if (timePassed < 0) {
            updateAtValue = "刚刚";
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = "刚刚";
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = value + "前";
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = value + "前";
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = value + "前";
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = value + "前";
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = value + "前";
        }
        return updateAtValue;
    }

}
