package com.huagu.RX.rongxinmedical.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
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
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Adapter.ConnectingStatusAdapter;
import com.huagu.RX.rongxinmedical.Dialog.DialogShows;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.IDField;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToPacket;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;
import com.huagu.RX.rongxinmedical.Utils.BluetoothManager;
import com.huagu.RX.rongxinmedical.Utils.WriteDataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WiFiConnectingActivity extends BaseActivity {

    private ListView status_list;
    private TextView upload_data;

    private String wifiname;
    private String wifipassword;

    public WiFiConnectingActivity instances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connecting);
        instances = this;

        wifiname = getIntent().getStringExtra("wifiname");
        wifipassword = getIntent().getStringExtra("wifipassword");

        initbluetooth();
    }

    private ConnectingStatusAdapter csa;

    private void InitView() {
        super.initTile();
        tcd.setText(R.string.Searching_Device);
        refresh.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        status_list = (ListView) this.findViewById(R.id.status_list);
        csa = new ConnectingStatusAdapter(this);
        status_list.setAdapter(csa);

        upload_data = (TextView) this.findViewById(R.id.upload_data);
    }


    private BluetoothAdapter mBluetoothAdapter;
    private MyDeviceWiFiSettingTipsActivity instance;
    private BluetoothManager bluetoothManager;

    private void initbluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothManager = BluetoothManager.getinstance();

        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        InitView();
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());

        Scanbluetooth();
    }

    private DialogShows dialog;
    private BluetoothDevice device;

    /**
     * 扫描二维码的回调 这个是21以下的版本才有的
     */
    BluetoothAdapter.LeScanCallback bluetoothcallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (!isScan) return;

            if ("resvent".equals(device.getName())) {
                WiFiConnectingActivity.this.device = device;
                connBluetooth();
                isScan = false;
            }
        }
    };

    /**
     * 扫描二维码的回调 这个是21以上的版本才有的
     */
    ScanCallback scancallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (!isScan) return;
            if ("resvent".equals(result.getDevice().getName())) {
                WiFiConnectingActivity.this.device = result.getDevice();
                connBluetooth();
                isScan = false;
            }
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

    private boolean isScan = false;

    public void connBluetooth() {
        csa.notifyDataSetChanged();
        upload_data.setSelected(true);
        stopScan();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mService.connect(device.getAddress());
            }
        }, 1500);
    }

    public void sendData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mService.write(null, "", 0);
            }
        }, 1500);
    }

    /**
     * 停止扫描蓝牙
     */
    public void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scancallback);
        else mBluetoothAdapter.stopLeScan(bluetoothcallback);
        isScan = false;
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public void Scanbluetooth() {
        if (mService != null && mService.isconnected() == UartService.STATE_DISCONNECTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//判断版本使用搜索蓝牙的回调
                isScan = true;
                mBluetoothAdapter.getBluetoothLeScanner().startScan(scancallback);
            } else
                mBluetoothAdapter.startLeScan(bluetoothcallback);
            dialog = DialogShows.getInstance(this).ShowProgressDialog("搜索蓝牙中");
        }
    }

    private UartService mService;
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


    //==============蓝牙部分广播注册===============
    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_Notification);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        intentFilter.addAction(UartService.IS_SUCCESS);
        return intentFilter;
    }

    private BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           /*GATT 连接成功*/
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                Toast.makeText(WiFiConnectingActivity.this, "连接成功..", Toast.LENGTH_LONG).show();
                csa.notifyDataSetChanged();
                upload_data.setSelected(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mService.setdiscoverServices();
                    }
                }, 1000);
            }

            /*GATT 断开连接*/
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                Toast.makeText(WiFiConnectingActivity.this, "断开连接..", Toast.LENGTH_LONG).show();
            }

            /*GATT 发现服务*/
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                Toast.makeText(WiFiConnectingActivity.this, "发现服务", Toast.LENGTH_LONG).show();
                csa.notifyDataSetChanged();
                upload_data.setSelected(true);
                mService.enableTXNotification();
                sendData();
            }

            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                try {
                    String text = new String(txValue, "UTF-8");
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:" + text, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("异常", e.toString());
                }
            }

            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                boolean boo = intent.getBooleanExtra(UartService.IS_SUCCESS, false);
                if (boo) {
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:失败", Toast.LENGTH_SHORT).show();
                }
            }

            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_Notification)) {  // 接收到通知消息
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                IDField.RetCode retCode = mService.SuperpositionDatac(txValue);
                if (retCode == IDField.RetCode.RetOK){
                    try {
                        JSONObject json = mService.reqjson_OK.getroot();
                        int resultCode = json.getJSONObject("body").getInt("result_code");
                        if (sendwifi) {
                            sendwifi = false;
                            setSendwifi();
                        }else if (resultCode == 0){
                            timeouthandler.removeCallbacks(timeoutrunnable);
                            //wifi链接成功
                        }else{
                            timeouthandler.postDelayed(timeoutrunnable,3000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private boolean sendwifi = true;

    public void setSendwifi() {
        Map<String, String> header = WriteDataUtils.getInstance().getHeader("14", device.getAddress(), "set", "profile");
        Map<String, String> body = WriteDataUtils.getInstance().getBody(wifiname, "wpa", wifipassword);
        Map<String, Map<String, String>> wifidata = new HashMap<String, Map<String, String>>();
        wifidata.put("header", header);
        wifidata.put("body", body);
        try {
            JSONObject json = new JSONObject(wifidata);

            ToPacket topacket = new ToPacket();
            IDField.RetCode idfield = topacket.build(json);
            Log.e("buildstatus", idfield.name());
            byte[] b = topacket.getData();
            mService.write(b, "data", topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timeouthandler.postDelayed(timeoutrunnable,90000);
    }

    Handler timeouthandler = new Handler();

    Runnable timeoutrunnable = new Runnable() {
        @Override
        public void run() {
            setSendwifi();
        }
    };

}
