package com.huagu.RX.rongxinmedical.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fff on 2016/8/23.
 */
public class DateUtils {


    private static SimpleDateFormat sf = null;
    private static Calendar calendar = null;

    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    /**
     * 时间戳转换成字符窜
     * @param time 时间戳
     * @param format 返回的时间格式
     */
    public static String getDateToString(long time,String format) {
        Date d = new Date(time);
        sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getTimeStamp(int year, int month, int day,int hour) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long milli = (calendar.getTimeInMillis()/(long)1000)*(long)1000;
        return milli;
    }

    public static int getweeks(int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }

}
