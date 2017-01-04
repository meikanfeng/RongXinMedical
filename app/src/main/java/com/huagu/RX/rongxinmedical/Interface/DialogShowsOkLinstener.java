package com.huagu.RX.rongxinmedical.Interface;

import android.content.DialogInterface;

/**
 * Created by fff on 2016/9/7.
 */
public interface DialogShowsOkLinstener extends DialogInterface {


    interface ClickOKLintener{
        void ClickOK(DialogShowsOkLinstener dialog, String dialog_tag);
    }


}
