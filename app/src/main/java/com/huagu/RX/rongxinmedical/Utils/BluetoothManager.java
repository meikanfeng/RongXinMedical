package com.huagu.RX.rongxinmedical.Utils;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.huagu.RX.rongxinmedical.Activity.MyDeviceWiFiSettingTipsActivity;
import com.huagu.RX.rongxinmedical.Interface.OnBluetoothLintener;

/**
 * Created by fff on 2016/10/25.
 */
public class BluetoothManager {

    private static String OPEN_STATUS = BluetoothAdapter.ACTION_STATE_CHANGED;

    private static BluetoothManager bm;

    public static BluetoothManager getinstance(){
        if (bm == null) return bm = new BluetoothManager();
        else return bm;
    }

    /**
     * 当前 Android 设备是否支持 Bluetooth
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */
    public static boolean isBluetoothSupported()
    {
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (OPEN_STATUS.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                openLintener.OnStatusChange(state);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("aaa", "STATE_ON 手机蓝牙开启");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                        break;
                }
            }
        }
    };


    private OnBluetoothLintener.OnBluetoothOpenLintener openLintener;

    public void setOnBluetoothOpenLintener(OnBluetoothLintener.OnBluetoothOpenLintener openLintener){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OPEN_STATUS);
        openLintener.getContext().registerReceiver(broadcastReceiver, intentFilter);
        this.openLintener = openLintener;
    }

    public void unregestReceive(Context context){
        context.unregisterReceiver(broadcastReceiver);
    }


}
