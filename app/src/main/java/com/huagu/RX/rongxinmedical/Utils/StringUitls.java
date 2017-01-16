package com.huagu.RX.rongxinmedical.Utils;

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
}
