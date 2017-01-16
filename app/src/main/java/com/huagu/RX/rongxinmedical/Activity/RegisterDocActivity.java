package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
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

        getCountryList();
    }
    public void getCountryList(){
        typelist =new ArrayList<>();
        typelist.add(new GoodsNameAndId("Hospital","0"));
        typelist.add(new GoodsNameAndId("Sleep Lab","1"));
        typelist.add(new GoodsNameAndId("Other","2"));


        String Country_URL =" http://192.168.1.115:8080/resvent/area/list?country=1";
        RequestParams rp = new RequestParams(Country_URL);
        x.http().get(rp, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray jarr =result.getJSONArray("varList");
                    for(int i =0;i<jarr.length();i++){
                        GoodsNameAndId gd =new GoodsNameAndId(jarr.getJSONObject(i).getString("country_name"),jarr.getJSONObject(i).getString("country_id"));
                        countrylist.add(gd);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(JSONObject result) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.organization_type:
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
            case R.id.register_submit://
                HashMap<String,String> hash = new HashMap<String,String>();
                hash.put("USERNAME", user_name.getText().toString());
                hash.put("PASSWORD", user_password.getText().toString());
                hash.put("repassword", confirm_password.getText().toString());
                hash.put("FIRST_NAME", first_name.getText().toString());
                hash.put("LAST_NAME", last_name.getText().toString());
                hash.put("COMPANY", institution.getText().toString());
                hash.put("INSTITUTUIN_ID", organization_type.getText().toString());
                hash.put("JOBTITLE", job_title.getText().toString());
                hash.put("EMAIL", email_address.getText().toString());
                hash.put("PHONE", phone_num.getText().toString());
                hash.put("COUNTRY",country.getText().toString());
                HttpRequest.getInstance().Request("doctor/register",hash,RegisterDocActivity.this);
                break;
            case R.id.register_cancle:
                finish();
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject result) throws JSONException {
        Toast.makeText(context,"注册成功",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void Failure(String str, String method, int errorCode) {

    }

    @Override
    public void Error(String str, String method, Throwable ex) {

    }
}
