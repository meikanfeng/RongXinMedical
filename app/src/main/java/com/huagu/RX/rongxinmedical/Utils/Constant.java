package com.huagu.RX.rongxinmedical.Utils;

import android.os.Environment;

/**
 * Created by fff on 2016/7/26.
 */
public class Constant {

//    public static final String URL = "http://java.lucland.com/resvent/app/api/v1/";
//    public static final String URL = "http://192.168.10.10:8080/resvent/app/api/v1/";
    public static final String URL = "http://120.76.206.62:8080/resvent/app/api/v1/";//线上
//    public static final String URL = "http://192.168.199.134:8080/resvent/app/api/v1/";//向军
//    public static final String URL = "http://192.168.1.110:8080/resvent/app/api/v1/";//



//    public static final String IMAGE_URL = "http://192.168.199.133:8080/resvent/resvent_data/head/";
//    public static final String IMAGE_URL = "http://120.76.206.62:8080/resvent/";

    /**
     * 融昕的本地文件目录
     */
    public static final String APP_ROOTPATH = Environment.getExternalStorageDirectory()+ "/RongXin";

    /**
     * 融昕的头像缓存目录
     */
    public static final String AVATAR_SAVEPATH = APP_ROOTPATH + "/avatar/";

















}
