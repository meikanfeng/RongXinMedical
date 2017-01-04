package com.huagu.RX.rongxinmedical.Utils;

import android.content.Context;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Interface.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fff on 2016/7/26.
 */
public class HttpRequest {

    public final String TAG = "HttpRequest";

    public static HttpRequest getInstance(){
        HttpRequest hr=new HttpRequest();
        return hr;
    }

    public RequestListener rlistener;

    public Callback.Cancelable Request(final String method,final HashMap<String,String> paramer,final RequestListener rlistener){
        this.rlistener = rlistener;
        RequestParams rp = new RequestParams(Constant.URL+method);//Constant.URL
        if (paramer != null){
            Iterator it = paramer.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String)entry.getKey();
                String val = (String)entry.getValue();
                if (!"action".equals(key)){
                    if (key.contains("file")||key.contains("avatar")){
                        rp.addBodyParameter(key,new File(val));
                    }else{// "multipart/form-data; charset=utf-8"
                        rp.addBodyParameter(key,val);
                    }
                }
            }
        }else{
            rlistener.Error(TAG,method,new Throwable("方法名不能为NULL"));
        }
        Callback.Cancelable cc = x.http().post(rp, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getInt("errorCode")==200){
                        rlistener.Success(method,result);
                    }else{
                        rlistener.Failure(TAG,method,result.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    rlistener.Error(TAG,method,e);
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                }
                rlistener.Error(TAG,method,ex);
            }
            @Override
            public void onCancelled(CancelledException cex) {
                rlistener.Error(TAG,method,cex);
            }
            @Override
            public void onFinished() {
            }
        });
        return cc;
    };


}
