package com.huagu.RX.rongxinmedical.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */
public class RegisterPatientActivity extends BaseActivity implements View.OnClickListener, RequestListener {
    private Context context =RegisterPatientActivity.this;
    private EditText user_name,user_password,confirm_password,first_name,last_name,email_address,phone_num,serial_num;
    private TextView gender,date_birth,country,device_model;
    private TextView register_submit,register_cancle;
    private List<GoodsNameAndId> countrylist = new ArrayList<>();
    private List<GoodsNameAndId>  sexlist = new ArrayList<>();
    private List<GoodsNameAndId>  varlist = new ArrayList<>();
    private String sexid="3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        InitView();
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
        serial_num =(EditText)findViewById(R.id.serial_num);
        email_address =(EditText)findViewById(R.id.email_address);
        phone_num =(EditText)findViewById(R.id.phone_num);

        gender =(TextView)findViewById(R.id.gender);
        date_birth =(TextView)findViewById(R.id.date_birth);
        country =(TextView)findViewById(R.id.country);
        device_model =(TextView)findViewById(R.id.device_model);
        register_submit =(TextView)findViewById(R.id.register_submit);
        register_cancle=(TextView)findViewById(R.id.register_cancle);

        gender.setOnClickListener(this);
        date_birth.setOnClickListener(this);
        country.setOnClickListener(this);
        device_model.setOnClickListener(this);
        register_submit.setOnClickListener(this);

        getCountryList();
        getModel();
    }
    private void getModel() {
        String Country_URL ="http://192.168.1.115:8080/resvent/device/use";
        RequestParams rp = new RequestParams(Country_URL);
        x.http().get(rp, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(!StringUitls.isEmtpy(result.getJSONArray("varList").toString())){
                        JSONArray jarr = result.getJSONArray("varList");
                        for (int i = 0; i < jarr.length(); i++) {
                            GoodsNameAndId gd =new GoodsNameAndId(jarr.getJSONObject(i).getString("type_name"),(1 + i) + "");
                            varlist.add(gd);
                        }
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

    public void getCountryList(){
        sexlist =new ArrayList<>();
        sexlist.add(new GoodsNameAndId("Male","1"));
        sexlist.add(new GoodsNameAndId("Female","2"));
        sexlist.add(new GoodsNameAndId("Unspecified","3"));


        String Country_URL ="http://192.168.1.115:8080/resvent/area/list?country=1";
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
            case R.id.gender://性别
                WindowsUtils.showStringListPopupWindow(view, sexlist, new WindowsUtils.OnStringItemClickListener() {
                    @Override
                    public void onStringItemClick(int position) {
                        gender.setText(sexlist.get(position).getGc_name());
                        sexid =sexlist.get(position).getId();
                    }
                });
                break;
            case R.id.date_birth://出生日期
                ShowDate();
                break;
            case R.id.country://城市
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
            case R.id.device_model://设备类型
                if(varlist!=null&&varlist.size()>0){
                    WindowsUtils.showStringListPopupWindow(view, varlist, new WindowsUtils.OnStringItemClickListener() {
                        @Override
                        public void onStringItemClick(int position) {
                            device_model.setText(varlist.get(position).getGc_name());
                        }
                    });
                }else {
                    getModel();
                }
                break;
            case R.id.register_submit://提交
                HashMap<String,String> hash = new HashMap<String,String>();
                hash.put("USERNAME", user_name.getText().toString());
                hash.put("PASSWORD", user_password.getText().toString());
                hash.put("repassword", confirm_password.getText().toString());
                hash.put("FIRST_NAME", first_name.getText().toString());
                hash.put("LAST_NAME", last_name.getText().toString());
                hash.put("SEX", gender.getText().toString());//不知道他们是要ID还是NAME，ID的话改成 sexid;
                hash.put("BIRTH", date_birth.getText().toString());
                hash.put("EMAIL", email_address.getText().toString());
                hash.put("PHONE", phone_num.getText().toString());
                hash.put("COUNTRY",country.getText().toString());
                hash.put("DeviceType", device_model.getText().toString());
                hash.put("DEVICE_ID", serial_num.getText().toString());
                HttpRequest.getInstance().Request("patient/register",hash,RegisterPatientActivity.this);
                break;
            case R.id.register_cancle:
                finish();
                break;
        }
    }



    @Override
    public void Success(String method, JSONObject result) throws JSONException {

    }

    @Override
    public void Failure(String str, String method, int errorCode) {

    }

    @Override
    public void Error(String str, String method, Throwable ex) {

    }
    /*
    日期选择器
     */
    private void ShowDate() {
        Calendar calendar =Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker,
                                          int year, int month, int dayOfMonth) {
                        //Calendar月份是从0开始,所以month要加1
                        date_birth.setText("" + year + "-" +
                                (month+1) + "-" + dayOfMonth);
                    }
                };
        DatePickerDialog dialog = new DatePickerDialog(this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();

    }
}
