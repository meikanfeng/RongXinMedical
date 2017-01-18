package com.huagu.RX.rongxinmedical.Interface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fff on 2016/8/19.
 */
public interface RequestListener {

    /**
     * 请求成功的回调
     * @param method    请求的接口方法
     * @param result    返回的结果（JSONObject）
     * @throws JSONException
     */
    void Success(String method,JSONObject result) throws JSONException;

    /**
     * 请求失败的回调
     * @param str           错误来源（似乎没什么用）
     * @param method        请求的接口方法
     * @param errorStr     返回的错误码
     */
//    void Failure(String str,String method,int errorCode);
    void Failure(String str,String method,String errorStr);

    /**
     * 请求错误的回调
     * @param str       错误来源（似乎没什么用）
     * @param method    请求的接口方法
     * @param ex        错误抛出的异常信息
     */
    void Error(String str,String method,Throwable ex);

}
