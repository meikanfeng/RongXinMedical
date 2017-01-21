package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.StringUitls;
import com.huagu.RX.rongxinmedical.View.WindowsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
public class RegisterDocActivity extends BaseActivity implements View.OnClickListener, RequestListener {
    private Context context =RegisterDocActivity.this;
    private EditText user_name,user_password,confirm_password,first_name,last_name,institution,job_title,email_address,phone_num;
    private TextView organization_type,country;
    private TextView register_submit,register_cancle;
    private List<GoodsNameAndId>  countrylist =new ArrayList<>();
    private List<GoodsNameAndId>  typelist =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        InitView();
        getCountryList();
    }

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.Registration);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        user_name =(EditText)findViewById(R.id.user_name);
        user_password =(EditText)findViewById(R.id.user_password);
        confirm_password =(EditText)findViewById(R.id.confirm_password);
        first_name =(EditText)findViewById(R.id.first_name);
        last_name =(EditText)findViewById(R.id.last_name);
        institution =(EditText)findViewById(R.id.institution);
        job_title =(EditText)findViewById(R.id.job_title);
        email_address =(EditText)findViewById(R.id.email_address);
        phone_num =(EditText)findViewById(R.id.phone_num);

        organization_type =(TextView)findViewById(R.id.organization_type);
        country =(TextView)findViewById(R.id.country);
        register_submit =(TextView)findViewById(R.id.register_submit);
        register_cancle=(TextView)findViewById(R.id.register_cancle);

        organization_type.setOnClickListener(this);
        country.setOnClickListener(this);
        register_submit.setOnClickListener(this);
        register_cancle.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.organization_type://组织类型
                WindowsUtils.showStringListPopupWindow(view, typelist, new WindowsUtils.OnStringItemClickListener() {
                    @Override
                    public void onStringItemClick(int position) {
                        organization_type.setText(typelist.get(position).getGc_name());

                    }
                });
                break;
            case R.id.country://组织类型
                if(countrylist!=null&&countrylist.size()>0){
                  WindowsUtils.showStringListPopupWindow(view, countrylist, new WindowsUtils.OnStringItemClickListener() {
                      @Override
                      public void onStringItemClick(int position) {
                            country.setText(countrylist.get(position).getGc_name());
                      }
                  });
                }else {
                    getCountryList();
                }

                break;
            case R.id.register_submit://提交
                String name = user_name.getText().toString().trim();
                String passWord = user_password.getText().toString().trim();
                String surePassWord = confirm_password.getText().toString().trim();
                String ming = first_name.getText().toString().trim();
                String xing = last_name.getText().toString().trim();
                String company = institution.getText().toString().trim();
                String organization = organization_type.getText().toString().trim();
                String title = job_title.getText().toString().trim();
                String email = email_address.getText().toString().trim();
                String phone = phone_num.getText().toString().trim();
                String city = country.getText().toString();

                if(StringUitls.isEmtpy(name)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_name,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(passWord)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_password,Toast.LENGTH_LONG).show();
                    return;
                }
                if(passWord.length() < 8 ){
                    Toast.makeText(RegisterDocActivity.this,R.string.forget_passWord_err,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(passWord.length() > 16 ){
                    Toast.makeText(RegisterDocActivity.this,R.string.forget_passWord_long,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(StringUitls.isEmtpy(surePassWord)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_password,Toast.LENGTH_LONG).show();
                    return;
                }
                if(surePassWord.length() < 8 ){
                    Toast.makeText(RegisterDocActivity.this,R.string.forget_passWord_err,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(surePassWord.length() > 16 ){
                    Toast.makeText(RegisterDocActivity.this,R.string.forget_passWord_long,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!surePassWord.equals(passWord)) {
                    Toast.makeText(RegisterDocActivity.this,R.string.password_err,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(StringUitls.isEmtpy(ming)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_ming,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(xing)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_xing,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(company)){
                    Toast.makeText(RegisterDocActivity.this, R.string.organization_err,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(title)){
                    Toast.makeText(RegisterDocActivity.this, R.string.position_err,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(email)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_email,Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUitls.isEmail(email)){
                    Toast.makeText(RegisterDocActivity.this,R.string.forget_email_err,Toast.LENGTH_LONG).show();
                    return ;
                }
                if(StringUitls.isEmtpy(phone)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_phone,Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUitls.isEmtpy(city)){
                    Toast.makeText(RegisterDocActivity.this, R.string.forget_city,Toast.LENGTH_LONG).show();
                    return;
                }

                HashMap<String,String> hash = new HashMap<String,String>();
                hash.put("USERNAME", name);
                hash.put("PASSWORD", passWord);
                hash.put("repassword", surePassWord);
                hash.put("FIRST_NAME", ming);
                hash.put("LAST_NAME", xing);
                hash.put("COMPANY", company);
                hash.put("INSTITUTUIN_ID", organization);
                hash.put("JOBTITLE", title);
                hash.put("EMAIL", email);
                hash.put("PHONE", phone);
                hash.put("COUNTRY",city);
                HttpRequest.getInstance().Request("doctor/register",hash,RegisterDocActivity.this);
                break;
            case R.id.register_cancle:
                finish();
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject result) throws JSONException {
        Log.i("JSONObject",result.toString());
        if (!StringUitls.isEmtpy(result.toString())) {
            if(result.getString("data").equals("regiser success")){
                Toast.makeText(RegisterDocActivity.this,result.getString("data"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterDocActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterDocActivity.this.finish();
            }else {
                Toast.makeText(RegisterDocActivity.this,result.getString("data"),Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(RegisterDocActivity.this,R.string.hint_err,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Failure(String str, String method, String errorStr) {
        Toast.makeText(RegisterDocActivity.this,errorStr,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Error(String str, String method, Throwable ex) {
        Toast.makeText(RegisterDocActivity.this,str,Toast.LENGTH_SHORT).show();
    }

    private void getCountryList(){
        typelist =new ArrayList<>();
        typelist.add(new GoodsNameAndId("Hospital","0"));
        typelist.add(new GoodsNameAndId("Sleep Lab","1"));
        typelist.add(new GoodsNameAndId("Other","2"));

        //获取城市列表
        HashMap<String,String> map = new HashMap<>();
        map.put("country","1");
        HttpRequest.getInstance().Request("arealist", map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                if(!StringUitls.isEmtpy(result.getJSONObject("data").getJSONArray("varList").toString())){
                    try {
                        JSONArray jarr =result.getJSONObject("data").getJSONArray("varList");
                        for(int i =0;i<jarr.length();i++){
                            GoodsNameAndId gd =new GoodsNameAndId(jarr.getJSONObject(i).getString("country_name"),jarr.getJSONObject(i).getString("country_id"));
                            countrylist.add(gd);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(RegisterDocActivity.this,result.getJSONObject("data").getString("msg"),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Toast.makeText(RegisterDocActivity.this,errorStr,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Toast.makeText(RegisterDocActivity.this,str,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
