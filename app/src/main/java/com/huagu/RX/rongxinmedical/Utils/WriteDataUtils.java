package com.huagu.RX.rongxinmedical.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fff on 2016/12/22.
 */

public class WriteDataUtils {

    private static WriteDataUtils instance;

    public static WriteDataUtils getInstance(){
        if (instance == null){
            instance = new WriteDataUtils();
        }
        return instance;
    }

    /**
     *
     * @param msg_id
     * @param dev_id
     * @param action
     * @param module
     * @return
     */
    public Map<String,String> getHeader(String msg_id,String dev_id, String action, String module){
        Map<String, String> header = new HashMap<String, String>();
        header.put("msg_id",msg_id);
        header.put("dev_id","000000000000");//dev_id
        header.put("action",action);
        header.put("module",module);

        return header;
    }

    public Map<String,String> getBody(String ssid, String type, String key){
        Map<String, String> header = new HashMap<String, String>();
        header.put("ssid",ssid);
        header.put("type",type);
        header.put("key",key);
        return header;
    }


}
