package com.huagu.RX.rongxinmedical.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.StringUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone_num,verification_code,user_name,email_address;
    private TextView verification,findpassword_next,findpassword_submit;
    private ImageView refresh;

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
        tcd.setText(R.string.find_password);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone_num = (EditText) this.findViewById(R.id.phone_num);
        verification_code = (EditText) this.findViewById(R.id.verification_code);

        refresh = (ImageView) findViewById(R.id.refresh);
        verification = (TextView) this.findViewById(R.id.verification);
        findpassword_next = (TextView) this.findViewById(R.id.findpassword_next);
        findpassword_submit = (TextView) findViewById(R.id. findpassword_submit);
        user_name = (EditText) findViewById(R.id.user_name);
        email_address = (EditText) findViewById(R.id.email_address);

        refresh.setVisibility(View.GONE);
//        findpassword_next.setEnabled(false);
        verification.setOnClickListener(this);
        findpassword_next.setOnClickListener(this);
        findpassword_submit.setOnClickListener(this);
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
            case R.id.findpassword_submit://提交
//                startActivity(new Intent(FindPasswordActivity.this,ModifyActivity.class));
//                FindPasswordActivity.this.finish();
                String name = user_name.getEditableText().toString().trim();
                String email = email_address.getEditableText().toString().trim();
                if(StringUitls.isEmtpy(name)){
                    Toast.makeText(FindPasswordActivity.this, R.string.forget_name,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(email)){
                    Toast.makeText(FindPasswordActivity.this, R.string.forget_email,Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUitls.isEmail(email)){
                    Toast.makeText(FindPasswordActivity.this,R.string.forget_email_err,Toast.LENGTH_LONG).show();
                    return ;
                }
                HashMap<String,String> map = new HashMap<>();
                map.put("USERNAME",name);
                map.put("EMAIL",email);
                HttpRequest.getInstance().Request("findPwdSend", map, new RequestListener() {
                    @Override
                    public void Success(String method, JSONObject result) throws JSONException {

                        if(!StringUitls.isEmtpy(result.toString())){
                            try {
                                String msg =  result.getJSONObject("data").getString("result");
                                if(msg.equals("success")){
                                    Intent intent = new Intent(FindPasswordActivity.this,ModifyActivity.class);
                                    startActivity(intent);
                                    FindPasswordActivity.this.finish();
                                }else {
                                    Toast.makeText(FindPasswordActivity.this,result.getJSONObject("data").getString("msg"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(FindPasswordActivity.this,R.string.service_err,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Failure(String str, String method, String errorStr) {
                        Toast.makeText(FindPasswordActivity.this,errorStr,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void Error(String str, String method, Throwable ex) {
                        Toast.makeText(FindPasswordActivity.this,str,Toast.LENGTH_SHORT).show();
                    }
                });
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
