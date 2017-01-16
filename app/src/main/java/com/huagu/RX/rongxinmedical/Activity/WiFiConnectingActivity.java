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
import com.huagu.RX.rongxinmedical.Utils.StringUitls;
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
                if (mService != null){
                    mService.close();
                }
                Toast.makeText(WiFiConnectingActivity.this,"断开服务",Toast.LENGTH_SHORT).show();
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
            if (!StringUitls.isEmtpy(result.getDevice().getName()) && "resvent".equals(result.getDevice().getName())) {
                Log.e("TAG","发现名称");
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mService.enableTXNotification();
                    }
                },1000);
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
                String txValue = intent.getStringExtra(UartService.EXTRA_DATA);
                Log.e("xxxxxxxxxxxxx","  ffffff: "+ txValue);
                try {
                    JSONObject json = new JSONObject(txValue);
                    Log.i("TAG",json.toString());
                    if (sendwifi) {
                        sendwifi = false;
                        setSendwifi();
                        return;
                    }
                    if ("profile".equals(json.getJSONObject("header").getString("module"))){
                        if (!json.getJSONObject("body").has("result_code")) return;
                        int resultCode = json.getJSONObject("body").getInt("result_code");
                        if (resultCode == 144) {
                            timeouthandler.removeCallbacks(getststusrunnable);
                            timeouthandler.removeCallbacks(timeoutrunnable);
                            //wifi链接成功
                        } else if (resultCode == 145) {
                            Toast.makeText(WiFiConnectingActivity.this, "wifi连接中...", Toast.LENGTH_SHORT).show();
                        } else if (resultCode == 146) {
//                            timeouthandler.removeCallbacks(timeoutrunnable);
//                            timeouthandler.removeCallbacks(getststusrunnable);
                            Toast.makeText(WiFiConnectingActivity.this, "连接路由器失败", Toast.LENGTH_SHORT).show();
                        } else if (resultCode == 147) {
                            timeouthandler.removeCallbacks(getststusrunnable);
                            timeouthandler.removeCallbacks(timeoutrunnable);
                            Toast.makeText(WiFiConnectingActivity.this, "请打开设备wifi", Toast.LENGTH_SHORT).show();
                        } else if (resultCode == 148) {
                            timeouthandler.removeCallbacks(getststusrunnable);
                            timeouthandler.removeCallbacks(timeoutrunnable);
                            Toast.makeText(WiFiConnectingActivity.this, "请检查路由器是否有网", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };


    private void senfGetstatus() {
        Log.i("TAG","发送WiFi名称和密码");
        Map<String, String> header = WriteDataUtils.getInstance().getHeader("14", device.getAddress(), "get", "profile");
        Map<String, String> body = new HashMap<String, String>();
        Map<String, Map<String, String>> wifidata = new HashMap<String, Map<String, String>>();
        wifidata.put("header", header);
        wifidata.put("body", body);
        try {
            JSONObject json = new JSONObject(wifidata);

            ToPacket topacket = new ToPacket();
            IDField.RetCode idfield = topacket.build(json);
            Log.e("buildstatus", idfield.name());
            byte[] b = topacket.getData();
            Log.e("xxxxxxxxxxxxxx", "getstatus :" + System.currentTimeMillis());
            mService.write(b, "data", topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timeouthandler.removeCallbacks(getststusrunnable);
        timeouthandler.postDelayed(getststusrunnable, 10000);
    }

    private boolean sendwifi = true;

    public void setSendwifi() {
        Log.e("wifi名称",wifiname);
        Log.e("wifi密码",wifipassword);
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
            Log.e("xxxxxxxxxxxxxx", "sendDate :" + System.currentTimeMillis());
            mService.write(b, "data", topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timeouthandler.removeCallbacks(getststusrunnable);
        timeouthandler.removeCallbacks(timeoutrunnable);

        timeouthandler.postDelayed(getststusrunnable, 10000);
        timeouthandler.postDelayed(timeoutrunnable, 90000);
    }

    Handler timeouthandler = new Handler();

    Runnable timeoutrunnable = new Runnable() {
        @Override
        public void run() {
            setSendwifi();
        }
    };

    Runnable getststusrunnable = new Runnable() {
        @Override
        public void run() {
            senfGetstatus();
        }
    };

}
