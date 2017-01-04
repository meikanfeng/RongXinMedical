package com.huagu.RX.rongxinmedical.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.R;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone_num,verification_code;
    private TextView verification,findpassword_next;

    private String vcode;
    private String phone;

    private Handler mhandler = new Handler();
    private int time = 60;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time -- ;
            if (time>0){
                verification.setText(time+"s");
                mhandler.postDelayed(runnable,1000);
            }else{
                verification.setText(R.string.veification);
                verification.setEnabled(true);
                time = 60;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);


        InitView();
    }

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.find_password);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone_num = (EditText) this.findViewById(R.id.phone_num);
        verification_code = (EditText) this.findViewById(R.id.verification_code);

        verification = (TextView) this.findViewById(R.id.verification);
        findpassword_next = (TextView) this.findViewById(R.id.findpassword_next);
//        findpassword_next.setEnabled(false);
        verification.setOnClickListener(this);
        findpassword_next.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verification:
                if (!phoneorcodeisnull(false)){
                    Toast.makeText(this,getResources().getString(R.string.no_phone_Tips),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time==60){
                    verification.setText("60s");
                    verification.setEnabled(false);
                    mhandler.postDelayed(runnable,1000);
                }
                break;
            case R.id.findpassword_next:
                startActivity(new Intent(this,AmendPasswordActivity.class));
                break;
        }
    }

    /**
     * 判断手机号或验证码是否为空
     * @param needcode  是否判断验证码
     * @return  根据判断内容返回是否为空
     */
    public boolean phoneorcodeisnull(boolean needcode){
        if (needcode){
            vcode = verification_code.getText().toString().trim();
            if (TextUtils.isEmpty(vcode))
                return false;
        }
        String telRegex = "[1][3458]\\d{9}";
        phone = phone_num.getText().toString().trim();
        if (TextUtils.isEmpty(vcode)){
            return false;
        }else{
            Toast.makeText(this,getResources().getString(R.string.phone_error_Tips),Toast.LENGTH_SHORT).show();
            return phone.matches(telRegex);
        }
    }

}
