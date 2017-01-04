package com.huagu.RX.rongxinmedical.Interface;

import android.content.Context;
import android.content.Intent;

/**
 * Created by fff on 2016/10/25.
 */
public interface OnBluetoothLintener {

    /**
     * 蓝牙的开启关闭监听
     */
    interface OnBluetoothOpenLintener{
        Context getContext();
        void OnStatusChange(int status);
    }

    /**
     * 蓝牙的连接断开状态变更
     */
    interface OnBluetoothConnectLintener{
        void OnConnectStatusChange(Intent intent,String connectstatus);
    }

    /**
     * 蓝牙扫描返回的结果
     */
    interface OnBluetoothScanLintener{
        void OnScanStatusChange(Intent intent);
    }

}
