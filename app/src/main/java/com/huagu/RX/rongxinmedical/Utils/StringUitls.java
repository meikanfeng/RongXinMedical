package com.huagu.RX.rongxinmedical.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fff on 2017/1/13.
 */

public class StringUitls {
    public static  boolean isEmtpy(String str){
        if (str == null || str.length() == 0){
            return true;
        }else{
            return false;
        }
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14"）
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        String[]  data = time.split("-");
        StringBuffer sb = new StringBuffer(data[0]);
        if(Integer.parseInt(data[1]) < 10){
            sb.append("-0" + data[1]);
        }else {
            sb.append("-" + data[1]);
        }
        if(Integer.parseInt(data[2]) < 10){
            sb.append("-0" + data[2]);
        }else {
            sb.append("-" + data[2]);
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(sb.toString());
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }
}
