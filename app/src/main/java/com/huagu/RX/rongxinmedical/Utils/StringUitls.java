package com.huagu.RX.rongxinmedical.Utils;

/**
 * Created by fff on 2017/1/13.
 */

public class StringUitls {
    public static  boolean isEmtpy(String str){
        if (str.trim() == null || str.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }
}
