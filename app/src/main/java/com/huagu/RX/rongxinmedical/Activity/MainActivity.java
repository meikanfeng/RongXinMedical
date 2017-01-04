package com.huagu.RX.rongxinmedical.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Adapter.DeviceAdapter;
import com.huagu.RX.rongxinmedical.Entity.CircleScheduleModel;
import com.huagu.RX.rongxinmedical.Entity.ScannedDevice;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.IDField;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToJson;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToPacket;
import com.huagu.RX.rongxinmedical.OperateData.spring.Hex;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;
import com.huagu.RX.rongxinmedical.Utils.BleUtil;
import com.huagu.RX.rongxinmedical.View.CircleScheduleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private BluetoothAdapter mBluetoothAdapter;

    private DeviceAdapter mDeviceAdapter;

    private static MainActivity instance;

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        instance = this;

        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        InitView();
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());

    }

    //==============蓝牙部分广播注册===============
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_Notification);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        intentFilter.addAction(UartService.DEVICE_WRITE_SUCCESS);
        intentFilter.addAction(UartService.DEVICE_WRITE_FAIL);
        intentFilter.addAction(UartService.IS_SUCCESS);
        return intentFilter;
    }

    private BluetoothDevice mDevice = null;
    private TextView connect,log,log1;
    private ListView device;
    private static Boolean is_connect = true;
    private void InitView() {
        connect = (TextView) this.findViewById(R.id.connect);
        log = (TextView) this.findViewById(R.id.log);
        log1 = (TextView) this.findViewById(R.id.log1);
        device = (ListView) this.findViewById(R.id.device);
        mDeviceAdapter = new DeviceAdapter(this, R.layout.listitem_device,new ArrayList<ScannedDevice>());
        device.setAdapter(mDeviceAdapter);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.write(null,"",0);
            }
        });

        log1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String jsonstring = "{\"header\":{\"msg_id\":14,\"dev_id\":\"EEEEEEEEEEEE\",\"action\":\"set\",\"module\":\"profile\"},\"body\":{\"ssid\":\"RongXin-2.4G\",\"type\":\"wpa\",\"key\":\"happywork\"}}";
                tojsonsend(jsonstring,"data");


            }
        });

        device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);

                ScannedDevice item = mDeviceAdapter.getItem(position);
                BluetoothDevice device = item.getDevice();
                String deviceAddress = device.getAddress();
                mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                mService.connect(deviceAddress);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_connect) {
                    BluetoothManager manager = BleUtil.getManager(MainActivity.this);
                    if (manager != null) {
                        mBluetoothAdapter = manager.getAdapter();
                    }
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(MainActivity.this, "mBluetoothAdapter=null 蓝牙没有打开", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    mDeviceAdapter.clear();
                    if (mScanning) {
                        mDeviceAdapter.clear();
                        scanLeDevice(false);
                    } else {
                        mDeviceAdapter.clear();
                        scanLeDevice(true);
                    }

                }
            }
        });

        this.findViewById(R.id.textviewtest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//     FD2400EEEEEEEEEEEE2200270057FAF91C0001A5E000008CA000001C200001000200020002000200020002000001F4000001C2A5A5
//                try {
//                    ToJson reqjson = new ToJson();
//
//                    byte[] txValue1 = dataop("FD2400EEEEEEEEEEEE2200270057FAF91C0001A5E000008CA000001C200001000200020002000200020002000001F4000001C2A5A5".getBytes());
//                    IDField.RetCode retCode = reqjson.parse(txValue1, txValue1.length);
//
//                    if(retCode == IDField.RetCode.RetOK){
//                        Log.e("xxxx",reqjson.toString());
//                        Log.e("xxxxx","xxxxxxxxx: "+retCode);
//                        data =null;
//                        data1 =null;
//                    }else {
//                        Log.e("xxxxx","xxxxxxxxx: "+retCode);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }

    public void tojsonsend(String jsonstring,String type){
        try {
            JSONObject jsonObject = new JSONObject(jsonstring);

            ToPacket topacket = new ToPacket();
            IDField.RetCode idfield = topacket.build(jsonObject);
            Log.e("buildstatus",idfield.name());
            byte[] b=topacket.getData();
            mService.write(b,type,topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private byte[] data = null;
    private byte[] data1 = null;
    public byte[] dataop(byte[] txValue) throws JSONException {
        if (data == null){
            data = new byte[txValue.length];
            data = txValue;
        }else{
            data = new byte[data1.length+txValue.length];
            for (int i=0;i<data.length;i++){
                if (i<data1.length){
                    data[i] = data1[i];
                }else{
                    data[i] = txValue[i-data1.length];
                }
            }
        }
        data1 = new byte[data.length];
        for (int i=0;i<data.length;i++){
            data1[i] = data[i];
        }
        String xxx = Hex.byte2HexStr(data1, data1.length);
        Log.e("xxxxxxxxx", xxx);
        return data1;
    }

    UartService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
//            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            //    	mService.disconnect(mDevice);
            mService = null;
        }
    };


    boolean mScanning;

    private void scanLeDevice(final boolean enable) {

        if (enable) {
        	/*
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
					mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //mEmptyList.setText("");
                    progressBar.setVisibility(View.GONE);
                }
            }, SCAN_PERIOD);
            */
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice newDeivce, final int newRssi,  final byte[] newScanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDeviceAdapter.update(newDeivce, newRssi, newScanRecord);
                        }
                    });
        }
    };


    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           /*GATT 连接成功*/
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接成功..", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mService.setdiscoverServices();
                            }
                        },1000);
                    }
                });
            }

            /*GATT 断开连接*/
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,"断开连接..",Toast.LENGTH_LONG).show();
                    }
                });
            }

            /*GATT 发现服务*/
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                Toast.makeText(MainActivity.this,"发现服务..",Toast.LENGTH_LONG).show();
                mService.enableTXNotification();
            }

            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:" + text, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("异常", e.toString());
                        }
                    }
                });
            }
            
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)){
                boolean boo = intent.getBooleanExtra(UartService.IS_SUCCESS, false);
                if (boo){
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:成功" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:失败", Toast.LENGTH_SHORT).show();
                }
            }
            if (action.equals(UartService.DEVICE_WRITE_SUCCESS)){
                String act = intent.getStringExtra("act");
                if ("data".equals(act)){
                    mhandler.postDelayed(runnable,3000);
                }
                Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " 蓝牙写入:成功" , Toast.LENGTH_SHORT).show();

            }
            if (action.equals(UartService.DEVICE_WRITE_FAIL)){
                Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " DEVICE_WRITE_FAIL:失败", Toast.LENGTH_SHORT).show();
                String act = intent.getStringExtra("act");
                if ("data".equals(act)){
                    mhandler.postDelayed(runnable,3000);
                }
            }
            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_Notification)) {// 接收到通知消息
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }
        }

    };


    Handler mhandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            String reqstatus = "{\"header\":{\"msg_id\":14,\"dev_id\":\"EEEEEEEEEEEE\",\"action\":\"get\",\"module\":\"profile\"},\"body\":{}}";
            tojsonsend(reqstatus,"data");
        }
    };
//    public void tojsonsend(String jsonstring,String type){
//        try {
//            JSONObject jsonObject = new JSONObject(jsonstring);
//
//            ToPacket topacket = new ToPacket();
//            IDField.RetCode idfield = topacket.build(jsonObject);
//            Log.e("buildstatus",idfield.name());
//            byte[] b=topacket.getData();
//            mService.write(b,type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


}
