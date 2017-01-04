package com.huagu.RX.rongxinmedical.Activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Dialog.DialogShows;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;
import com.huagu.RX.rongxinmedical.Utils.BluetoothManager;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyDeviceWiFiSettingTipsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device_setting);

        InitView();

    }

    private TextView wifitips_next;

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.MyDevice);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wifitips_next = (TextView) this.findViewById(R.id.wifitips_next);
        wifitips_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifitips_next:
                startActivity(new Intent(this, WiFiConfirmInfoSettingActivity.class));
                break;
        }
    }

}
