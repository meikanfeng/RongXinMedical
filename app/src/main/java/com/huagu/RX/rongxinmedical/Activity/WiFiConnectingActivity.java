package com.huagu.RX.rongxinmedical.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Adapter.ConnectingStatusAdapter;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.IDField;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToPacket;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.WriteDataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WiFiConnectingActivity extends BaseActivity {

    private ListView status_list;
    private TextView upload_data;

    private String wifiname;
    private String wifipassword;
    private String address;
    private boolean flag = true;

    public WiFiConnectingActivity instances;

    private Timer  mTimer = new Timer();

    private TimerTask sendOutWIFITask  = new TimerTask() {
        @Override
        public void run() {
            Log.e("TAG","延迟90秒重新发送");
            setSendwifi();
        }
    };

    private TimerTask getWIFITask = new TimerTask() {
        @Override
        public void run() {
            Log.e("TAG","延迟9秒重新获取wifi状态");
            senfGetstatus();
        }
    };

    /**延迟九十秒重新发送wifi*/
    private void delaySendWifi(){
        if(mTimer != null && sendOutWIFITask != null){
            mTimer.schedule(sendOutWIFITask,90000,90000);
        }
    }

    /**延迟9秒重新发获取wifi*/
    private void delayGetWifiStatue(){
        if(mTimer != null && getWIFITask != null){
            mTimer.schedule(getWIFITask,9000,9000);
        }
    }

    /**关闭定时器*/
    private void timerColse(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if(sendOutWIFITask != null){
            sendOutWIFITask.cancel();
            sendOutWIFITask = null;
        }
        if(getWIFITask != null){
            getWIFITask.cancel();
            getWIFITask = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connecting);
        flag = true;
        instances = this;

        wifiname = getIntent().getStringExtra("wifiname");
        wifipassword = getIntent().getStringExtra("wifipassword");
        address = getIntent().getStringExtra("address");

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
                LocalBroadcastManager.getInstance(WiFiConnectingActivity.this).unregisterReceiver(UARTStatusChangeReceiver);
                timerColse();
                exitActivity();
            }
        });

        status_list = (ListView) this.findViewById(R.id.status_list);
        csa = new ConnectingStatusAdapter(this);
        status_list.setAdapter(csa);

        upload_data = (TextView) this.findViewById(R.id.upload_data);
    }

    private void exitActivity() {
        if (DeviceListActivity.getInstance().mService != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DeviceListActivity.getInstance().mService.close();
                }
            },1500);
        }
        Toast.makeText(WiFiConnectingActivity.this,"断开服务",Toast.LENGTH_SHORT).show();
        WiFiConnectingActivity.this.finish();
    }

    private MyDeviceWiFiSettingTipsActivity instance;

    private void initbluetooth() {
        /**初始化控件*/
        InitView();
        /**注册广播*/
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
        /**开始进行蓝牙连接设备*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connBluetooth();
            }
        },2000);
    }

    private boolean isScan = false;

    /**
     * 开始连接设备
     */
    public void connBluetooth() {
        csa.notifyDataSetChanged();//正在连接设备
        upload_data.setSelected(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceListActivity.getInstance().mService.connect(address);//进行连接
            }
        },1500);
    }

    /**
     * 延迟发送，避免发送失败
     */
    public void sendData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceListActivity.getInstance().mService.write(null, "", 0);//发送a8a8
            }
        }, 1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSendwifi();//发送wifi
            }
        },1500);
        delaySendWifi();//延迟90秒在重新发送wifi
        delayGetWifiStatue();//延迟9秒在重新获取wifi状态
    }

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

    /**
     * 广播接收
     */
    private BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           /*GATT 连接成功*/
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
//                Toast.makeText(WiFiConnectingActivity.this, "连接成功..", Toast.LENGTH_LONG).show();
                csa.notifyDataSetChanged();
                upload_data.setSelected(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DeviceListActivity.getInstance().mService.setdiscoverServices();
                    }
                },2500);
            }

            /*GATT 断开连接*/
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                showTip("服务断开连接..");
            }

            /*GATT 发现服务*/
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                if(flag){
                    flag = false;
                    csa.notifyDataSetChanged();
                    upload_data.setSelected(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DeviceListActivity.getInstance().mService.enableTXNotification();//注册设备通知广播
                        }
                    },2500);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendData();//发送wifi
                        }
                    },1500);
                }
            }

            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                flag = true;
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                try {
                    String text = new String(txValue, "UTF-8");
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:" + text, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("异常", e.toString());
                }
            }

            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                flag = true;
                boolean boo = intent.getBooleanExtra(UartService.IS_SUCCESS, false);
                if (boo) {
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.getInstance(), System.currentTimeMillis() + " ACTION_DATA_AVAILABLE:失败", Toast.LENGTH_SHORT).show();
                }
            }

            /*GATT 接收数据*/
            if (action.equals(UartService.ACTION_DATA_Notification)) {  // 接收到通知消息
                flag = true;
                String txValue = intent.getStringExtra(UartService.EXTRA_DATA);
                Log.e("xxxxxxxxxxxxx","  ffffff: "+ txValue);
                try {
                    JSONObject json = new JSONObject(txValue);
                    Log.i("TAG",json.toString());
                    if (sendwifi) {
                        sendwifi = false;
                        /*if (!StringUitls.isEmtpy(device.getAddress()))
                            setSendwifi();
                            return;*/
                    }
                    if ("profile".equals(json.getJSONObject("header").getString("module"))){
                        if (!json.getJSONObject("body").has("result_code")) return;
                        int resultCode = json.getJSONObject("body").getInt("result_code");
                        if (resultCode == 144) {
                            timerColse();
                            csa.notifyDataSetChanged();
//                            String tip = "设备wifi连接成功";
//                            SpannableString msp = new SpannableString(tip);
//                            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            Toast.makeText(WiFiConnectingActivity.this, msp, Toast.LENGTH_SHORT).show();
                            showTip("wifi链接成功...");
                        } else if (resultCode == 145) {
//                            Toast.makeText(WiFiConnectingActivity.this, "wifi连接中...", Toast.LENGTH_SHORT).show();
                            Log.e("TAG","wifi连接中...");
                        } else if (resultCode == 146) {
                            showTip("连接路由器失败");
                        } else if (resultCode == 147) {
                            showTip("请打开设备wifi");
                        } else if (resultCode == 148) {
                            showTip("请检查路由器是否有网");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void showTip(final String tip){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WiFiConnectingActivity.this, tip, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 获取wifi连接状态
     */
    private void senfGetstatus() {
        Log.i("TAG","获取WiFi发送状态");
        Map<String, String> header = WriteDataUtils.getInstance().getHeader("0", address, "get", "profile");//   "14", address, "get", "profile"
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
            DeviceListActivity.getInstance().mService.write(b, "data", topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean sendwifi = true;

    /**
     * 发送wifi名称和密码
     */
    public void setSendwifi() {
        Log.i("TAG","发送WiFi名称和密码");
        Log.e("wifi名称",wifiname);
        Log.e("wifi密码",wifipassword);
        Map<String, String> header = WriteDataUtils.getInstance().getHeader("0", address, "set", "profile");//   "14", address, "get", "profile"
        //XXX 修改路由器的类型？？？？
        Map<String, String> body = WriteDataUtils.getInstance().getBody(wifiname, "wpa", wifipassword);
        Map<String, Map<String, String>> wifidata = new HashMap<String, Map<String, String>>();
        wifidata.put("header", header);
        wifidata.put("body", body);
        try {
            JSONObject json = new JSONObject(wifidata);
            Log.i("TAG",json.toString());
            ToPacket topacket = new ToPacket();
            IDField.RetCode idfield = topacket.build(json);
            Log.e("buildstatus", idfield.name());
            Log.e("xxxxxxxxxxxxxx", "sendDate :" + System.currentTimeMillis());
            DeviceListActivity.getInstance().mService.write(topacket.getData(), "data", topacket.getLength());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
            timerColse();
            exitActivity();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**发送连接状态*/
    private void sendState(String statue){
        HashMap<String,String> map = new HashMap<>();
        map.put("STATE",statue);
        HttpRequest.getInstance().Request("deviceOffLine", map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {

            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Toast.makeText(WiFiConnectingActivity.this, errorStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Toast.makeText(WiFiConnectingActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
    }
}
