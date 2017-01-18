package com.huagu.RX.rongxinmedical.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Dialog.DialogShows;
import com.huagu.RX.rongxinmedical.Interface.DialogShowsOkLinstener;
import com.huagu.RX.rongxinmedical.Interface.OnBluetoothLintener;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.BluetoothManager;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyDeviceActivity extends BaseActivity implements View.OnClickListener, DialogShowsOkLinstener.ClickOKLintener, RequestListener, OnBluetoothLintener.OnBluetoothOpenLintener {

    private ImageView back,refresh;
    private TextView title;
    private Context context;

    private TextView mydevice_model,mydevice_sn,mydevice_initial,mydevice_upload;
    private ImageView mydevice_img;
    private TextView model,sn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device);
        context = this;

        mybluetoothmanager = BluetoothManager.getinstance();

        mybluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        mybluetoothmanager.setOnBluetoothOpenLintener(this);

        InitView();
        
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("pid",usershared.getString("USER_ID",""));
        getDeviceInfo("myDevice", map);
    }

    private void getDeviceInfo(String method, HashMap<String, String> map) {
        HttpRequest.getInstance().Request(method,map,this);
    }

    private void InitView() {
        title = (TextView) this.findViewById(R.id.title);
        back = (ImageView) this.findViewById(R.id.menu);
        refresh = (ImageView) this.findViewById(R.id.refresh);
        title.setText(getResources().getString(R.string.MyDevice));
        refresh.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        model = (TextView) this.findViewById(R.id.model);
        sn = (TextView) this.findViewById(R.id.sn);

        mydevice_model = (TextView) this.findViewById(R.id.mydevice_model);
        mydevice_sn = (TextView) this.findViewById(R.id.mydevice_sn);
        mydevice_initial = (TextView) this.findViewById(R.id.mydevice_initial);
        mydevice_upload = (TextView) this.findViewById(R.id.mydevice_upload);
        mydevice_img = (ImageView) this.findViewById(R.id.mydevice_img);

        mydevice_initial.setOnClickListener(this);
        mydevice_upload.setOnClickListener(this);

        On_Or_Off(true);

    }


    public void On_Or_Off(boolean is){
        mydevice_model.setSelected(is);
        mydevice_sn.setSelected(is);
        mydevice_initial.setSelected(is);
        mydevice_upload.setSelected(is);
        mydevice_img.setSelected(is);
        if (is){
            mydevice_model.setTextColor(getResources().getColor(R.color.ThemeColor));
            mydevice_sn.setTextColor(getResources().getColor(R.color.ThemeColor));
            sn.setTextColor(getResources().getColor(R.color.ThemeColor));
            model.setTextColor(getResources().getColor(R.color.ThemeColor));
        }else{
            mydevice_model.setTextColor(getResources().getColor(R.color.black_gray));
            mydevice_sn.setTextColor(getResources().getColor(R.color.black_gray));
            sn.setTextColor(getResources().getColor(R.color.black_gray));
            model.setTextColor(getResources().getColor(R.color.black_gray));
        }
    }
    private BluetoothAdapter mybluetoothadapter;
    private BluetoothManager mybluetoothmanager;
    private DialogShows bledia ;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mydevice_initial:
                if (!BluetoothManager.isBluetoothSupported())
                    Toast.makeText(this,getResources().getString(R.string.ble_not_supported),Toast.LENGTH_SHORT).show();
                if (!mybluetoothadapter.isEnabled())
                    DialogShows.getInstance(this).TextDialogShow(getString(R.string.is_open_ble),getString(R.string.open_ble),"ble").setDialogShowsOKLintener(this);
                else
                    DialogShows.getInstance(this).TextDialogShow(getString(R.string.dialogcontent),getString(R.string.Setting),"tips").setDialogShowsOKLintener(this);
                break;
            case R.id.mydevice_upload:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mybluetoothmanager.unregestReceive(this);
    }

    @Override
    public void ClickOK(DialogShowsOkLinstener dialog,String dialogTag) {
        if ("ble".equals(dialogTag)){
            bledia = DialogShows.getInstance(this).ShowProgressDialog(getResources().getString(R.string.Opening_the_bluetooth));
            mybluetoothadapter.enable();
        }else if ("tips".equals(dialogTag)){
            startActivity(new Intent(this,MyDeviceWiFiSettingTipsActivity.class));//MainActivity.class  测试蓝牙
        }
    }

    @Override
    public Context getContext() {
        return MyDeviceActivity.this;
    }

    @Override
    public void OnStatusChange(int status) {
        if (status == BluetoothAdapter.STATE_ON){
            if (bledia!=null && bledia.isShowing())bledia.dismiss();
            DialogShows.getInstance(this).TextDialogShow(getString(R.string.dialogcontent),getString(R.string.Setting),"tips").setDialogShowsOKLintener(this);
        }
    }


    private String DEVICE_ID,DEVICE_TYPE,DEVICE_VERSION;

    @Override
    public void Success(String method, JSONObject result) throws JSONException {
        if ("myDevice".equals(method)){
            if (result.getJSONObject("data")!=null){
                DEVICE_ID = result.getJSONObject("data").getString("DEVICE_ID");
                DEVICE_TYPE = result.getJSONObject("data").getString("TYPE");
                DEVICE_VERSION = result.getJSONObject("data").getString("VERSION");
            }
        }
    }

    @Override
    public void Failure(String str, String method, String errorStr) {
        Log.e("method","请求失败了");
    }

    @Override
    public void Error(String str, String method, Throwable ex) {
        Log.e("method","请求错误了");
    }

}
