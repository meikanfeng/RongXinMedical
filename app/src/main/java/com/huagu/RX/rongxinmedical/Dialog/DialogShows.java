package com.huagu.RX.rongxinmedical.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Interface.DialogShowsOkLinstener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.ScreenUtils;

/**
 * Created by fff on 2016/9/6.
 */
public class DialogShows extends Dialog implements DialogShowsOkLinstener, View.OnClickListener {

    public DialogShows(Context context) {
        super(context);
    }

    public DialogShows(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogShows(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private static DialogShows ds;
    private static Context contexts;

    public static DialogShows getInstance(Context context){
        if (isShow()){
            ds.dismiss();
        }
        contexts = context;
        ds = new DialogShows(context);
        return ds;
    }


    public static boolean isShow(){
        if (ds != null && ds.isShowing()){
            return true;
        }
        return false;
    }

    private String curTag="";

    /**
     * 显示文字对话框 (带标记返回)
     * @param content   显示的内容
     * @param ok        按钮文字
     * @param tag       标记
     * @return 返回当前对象
     */
    public DialogShows TextDialogShow(String content, String ok, String tag){
        this.curTag = tag;
        return TextDialogShow(content,ok);
    }

    /**
     * 等待的提示框
     * @param content   提示的内容
     */
    public DialogShows ShowProgressDialog(String content){
        View view = LayoutInflater.from(contexts).inflate(R.layout.progressdialogshow,null,false);
        ((TextView)view.findViewById(R.id.tips_content)).setText(content);
        WindowManager.LayoutParams wmlp = new WindowManager.LayoutParams();
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ds.setContentView(view,wmlp);
        ds.show();
        return ds;
    }


    /**
     * 显示文字对话框
     * @param content   显示的内容
     * @param ok        按钮文字
     */
    public DialogShows TextDialogShow(String content, String ok){
        View view = LayoutInflater.from(contexts).inflate(R.layout.textdialogshow,null,false);
        ((TextView)view.findViewById(R.id.content)).setText(content);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.submit).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.submit)).setTextColor(contexts.getResources().getColor(R.color.ThemeColor));
        ((TextView)view.findViewById(R.id.submit)).setText(TextUtils.isEmpty(ok)?"OK":ok);
        WindowManager.LayoutParams wmlp = new WindowManager.LayoutParams();
        wmlp.height = ScreenUtils.getScreenHeight(contexts)/4;
        ds.setContentView(view,wmlp);
        ds.show();
        return ds;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.cancel();
                break;
            case R.id.submit:
                dsokl.ClickOK(this,curTag);
                this.dismiss();
                break;
        }
    }

    private DialogShowsOkLinstener.ClickOKLintener dsokl;

    public void setDialogShowsOKLintener(DialogShowsOkLinstener.ClickOKLintener dsokl){
        this.dsokl = dsokl;
    }


//    public void ShowListDialog(){
//
//    }




}
