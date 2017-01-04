package com.huagu.RX.rongxinmedical.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by fff on 2016/8/26.
 */
public class Util {



    //版本名
    public static String getVersionName(Context context) {
        if (getPackageInfo(context)!=null)
            return getPackageInfo(context).versionName;
        else
            return "";
    }

    //版本号
    public static int getVersionCode(Context context) {
        if (getPackageInfo(context)!=null)
            return getPackageInfo(context).versionCode;
        else
            return 0;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

}
