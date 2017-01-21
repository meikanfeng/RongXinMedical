package com.huagu.RX.rongxinmedical.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Adapter.DeviceInfoAdapter;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;
import com.huagu.RX.rongxinmedical.Utils.BluetoothManager;
import com.huagu.RX.rongxinmedical.Utils.StringUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fff on 2017/1/20.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DeviceListActivity extends  BaseActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private ListView lvDevice;
    private DeviceInfoAdapter infoAdapter;
    private List<BluetoothDevice> deviceList;
    private boolean isScan = false;
    private String wifiname;
    private String wifipassword;
    private static DeviceListActivity deviceActivity;

    public static DeviceListActivity getInstance(){
        return deviceActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceActivity = this;
        setContentView(R.layout.activity_device_list);

        wifiname = getIntent().getStringExtra("wifiname");
        wifipassword = getIntent().getStringExtra("wifipassword");

        initbluetooth();
    }

    private void initbluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothManager = BluetoothManager.getinstance();

        /**注册、开启服务*/
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        /**初始化控件*/
        InitView();
        /**开始进行蓝牙扫描设备*/
        Scanbluetooth();
    }

    /**
     * 服务连接
     */
    public UartService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            if (!mService.initialize()) {
                finish();
            }
            Scanbluetooth();
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    /**
     * 扫描二维码的回调 这个是21以下的版本才有的
     * 发现设备
     */
    BluetoothAdapter.LeScanCallback bluetoothcallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isScan) return;
                    if (!StringUitls.isEmtpy(device.getName()) && "resvent".equals(device.getName())) {
                        if(!deviceList.contains(device)){
                            deviceList.add(device);
                            infoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }
    };


    /**
     * 开始搜索蓝牙
     */
    public void Scanbluetooth() {
        if (mService != null && mService.isconnected() == UartService.STATE_DISCONNECTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//判断版本使用搜索蓝牙的回调
                isScan = true;
                mBluetoothAdapter.getBluetoothLeScanner().startScan(scancallback);
            } else
                mBluetoothAdapter.startLeScan(bluetoothcallback);;
        }
    }

    /**
     * 扫描二维码的回调 这个是21以上的版本才有的
     * 发现设备
     */
    ScanCallback scancallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isScan) return;
                    if (!StringUitls.isEmtpy(result.getDevice().getName()) && "resvent".equals(result.getDevice().getName())) {
                        if(!deviceList.contains(result.getDevice())){
                            deviceList.add(result.getDevice());
                            infoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };


    private void InitView() {
        super.initTile();
        tcd.setText(R.string.device_list);
        refresh.setVisibility(View.GONE);
        lvDevice = (ListView) findViewById(R.id.lvDevice);
        deviceList = new ArrayList<>();
        infoAdapter = new DeviceInfoAdapter(DeviceListActivity.this,deviceList);
        lvDevice.setAdapter(infoAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(mServiceConnection);
                finish();
            }
        });


        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                stopScan();
                Intent intent = new Intent(DeviceListActivity.this,WiFiConnectingActivity.class);
                intent.putExtra("wifiname", wifiname);
                intent.putExtra("wifipassword", wifipassword);
                intent.putExtra("address",deviceList.get(position).getAddress());
                startActivityForResult(intent,555);

            }
        });
    }

    /**
     * 停止扫描蓝牙
     */
    public void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scancallback);
        else mBluetoothAdapter.stopLeScan(bluetoothcallback);
        isScan = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceActivity = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            unbindService(mServiceConnection);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 555){
            /**开始进行蓝牙扫描设备*/
            Scanbluetooth();
        }
    }
}
