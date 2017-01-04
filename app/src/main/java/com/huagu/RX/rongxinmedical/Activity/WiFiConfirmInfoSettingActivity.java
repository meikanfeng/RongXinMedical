package com.huagu.RX.rongxinmedical.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Dialog.DialogShows;
import com.huagu.RX.rongxinmedical.Interface.DialogShowsOkLinstener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Service.UartService;

public class WiFiConfirmInfoSettingActivity extends BaseActivity implements View.OnClickListener, DialogShowsOkLinstener.ClickOKLintener {


    private EditText wifi_name,wifi_password;

    private TextView search_device;

    private String wifiname,wifipassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_confirm_info_setting);


        InitView();

    }

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tcd.setText(R.string.Confirm_Wifi_Info);

        wifi_name = (EditText) this.findViewById(R.id.wifi_name);
        wifi_password = (EditText) this.findViewById(R.id.wifi_password);

        search_device = (TextView) this.findViewById(R.id.search_device);
        search_device.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_device:
                wifiname = wifi_name.getText().toString().trim();
                wifipassword = wifi_password.getText().toString().trim();
                if (TextUtils.isEmpty(wifiname)){
                    Toast.makeText(this,R.string.wifi_name_enter,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(wifipassword)){
                    Toast.makeText(this,R.string.wifi_password_enter,Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogShows.getInstance(this).TextDialogShow(getResources().getString(R.string.Confirm_Wifi_Info_tips),"").setDialogShowsOKLintener(this);
                break;
        }
    }


    @Override
    public void ClickOK(DialogShowsOkLinstener dialog,String tag) {
        Intent intent = new Intent(this,WiFiConnectingActivity.class);
        intent.putExtra("wifiname", wifiname);
        intent.putExtra("wifipassword", wifipassword);
        startActivity(intent);
    }
}
