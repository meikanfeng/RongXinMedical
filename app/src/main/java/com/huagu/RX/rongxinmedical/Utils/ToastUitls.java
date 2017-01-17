package com.huagu.RX.rongxinmedical.Utils;

import android.content.Context;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Activity.RegisterPatientActivity;
import com.huagu.RX.rongxinmedical.R;


/**
 * Created by fff on 2017/1/16.
 */

public class ToastUitls {

    /**
     *
     * @param context
     * @param str
     * @param tipStr
     */
    public static void showTip(Context context,String str,int tipStr){
        if(StringUitls.isEmtpy(str)){
            Toast.makeText(context, tipStr,Toast.LENGTH_LONG).show();
            return ;
        }
    }

    public static void showTip(Context context,int tipStr){
        Toast.makeText(context, tipStr,Toast.LENGTH_LONG).show();
        return;
    }

    public static void smallShowTip(Context context,int tipStr,int num,String str){
        if(str.length() < num ){
            Toast.makeText(context, tipStr,Toast.LENGTH_LONG).show();
            return ;
        }
    }

    public static void largemallwTip(Context context,int tipStr,int num,String str){
        if(str.length() > num ){
            Toast.makeText(context, tipStr,Toast.LENGTH_LONG).show();
            return ;
        }
    }

    public static void showNormalTip(Context context,int tipStr){
        Toast.makeText(context, tipStr,Toast.LENGTH_LONG).show();
    }
}
