package com.huagu.RX.rongxinmedical.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.Constant;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.StringUitls;
import com.huagu.RX.rongxinmedical.Utils.ToastUitls;
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
        register_cancle.setOnClickListener(this);

        getCountryList();
        getModel();
    }
    private void getModel() {
        //获取器件模型
        String Country_URL = Constant.URL + "deviceuse";
        RequestParams rp = new RequestParams(Country_URL);
        x.http().post(rp, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                    try {
                        if(!StringUitls.isEmtpy(result.getJSONObject("data").getJSONArray("varList").toString())){
                            JSONArray jarr =result.getJSONObject("data").getJSONArray("varList");
                            for(int i =0;i<jarr.length();i++){
                                GoodsNameAndId gd =new GoodsNameAndId(jarr.getJSONObject(i).getString("type_name"),(1 + i) + "");
                                varlist.add(gd);
                            }
                        }else {
                            Toast.makeText(RegisterPatientActivity.this,result.getJSONObject("data").getString("msg"),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterPatientActivity.this,result.getJSONObject("data").getString("msg"),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Toast.makeText(RegisterPatientActivity.this,str,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Toast.makeText(RegisterPatientActivity.this,str,Toast.LENGTH_SHORT).show();
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
                submitMethod();
                break;
            case R.id.register_cancle://取消
                finish();
                break;
        }
    }

    private void submitMethod() {
        String name  = user_name.getText().toString().trim();
        String firstName = first_name.getText().toString().trim();
        String userPassword = user_password.getText().toString().trim();
        String confirmPassword = confirm_password.getText().toString().trim();
        String lastName = last_name.getText().toString().trim();
        String sex = gender.getText().toString().trim();
        String dateBirth = date_birth.getText().toString().trim();
        String email = email_address.getText().toString().trim();
        String phone = phone_num.getText().toString().trim();
        String city = country.getText().toString().trim();
        String deviceModel = device_model.getText().toString().trim();
        String serialNum = serial_num.getText().toString().trim().toUpperCase();

        if(StringUitls.isEmtpy(name)){
            Toast.makeText(RegisterPatientActivity.this, R.string.forget_name,Toast.LENGTH_LONG).show();
            return ;
        }
        if(name.length() < 4 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.name_short,Toast.LENGTH_LONG).show();
            return ;
        }
        if(name.length() > 15 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.name_long,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(firstName)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_ming,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(userPassword)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_password,Toast.LENGTH_LONG).show();
            return ;
        }
        if(userPassword.length() < 8 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_passWord_err,Toast.LENGTH_LONG).show();
            return ;
        }
        if(userPassword.length() > 16 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_passWord_long,Toast.LENGTH_LONG).show();
            return ;
        }

        if(StringUitls.isEmtpy(confirmPassword)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_password,Toast.LENGTH_LONG).show();
            return ;
        }
        if(confirmPassword.length() < 8 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_passWord_err,Toast.LENGTH_LONG).show();
            return ;
        }
        if(confirmPassword.length() > 16 ){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_passWord_long,Toast.LENGTH_LONG).show();
            return ;
        }
        if(!confirmPassword.equals(userPassword)) {
            Toast.makeText(RegisterPatientActivity.this,R.string.password_err,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(lastName)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_xing,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(sex)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_gender,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(dateBirth)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_dateBirth,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(email)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_email,Toast.LENGTH_LONG).show();
            return ;
        }
        if(!StringUitls.isEmail(email)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_email_err,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(phone)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_phone,Toast.LENGTH_LONG).show();
            return ;
        }
        if(!(phone.length() >=6 && phone.length() <=15)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_phone,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(city)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_city,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(deviceModel)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_deviceModel,Toast.LENGTH_LONG).show();
            return ;
        }
        if(StringUitls.isEmtpy(serialNum)){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_num,Toast.LENGTH_LONG).show();
            return ;
        }
        if(!serialNum.startsWith("GA-") && !serialNum.startsWith("GB-")){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_num_err,Toast.LENGTH_LONG).show();
            return;
        }
        if(serialNum.length() != 11){
            Toast.makeText(RegisterPatientActivity.this,R.string.forget_num_err,Toast.LENGTH_LONG).show();
            return;
        }


        HashMap<String,String> hash = new HashMap<String,String>();
        hash.put("USERNAME", name);
        hash.put("PASSWORD", userPassword);
        hash.put("repassword", confirmPassword);
        hash.put("FIRST_NAME", firstName);
        hash.put("LAST_NAME", lastName);
        hash.put("SEX", sex);//不知道他们是要ID还是NAME，ID的话改成 sexid;
        hash.put("BIRTH", StringUitls.dataOne(dateBirth));
        hash.put("EMAIL", email);
        hash.put("PHONE", phone);
        hash.put("COUNTRY",city);
        hash.put("DeviceType", deviceModel);
        hash.put("DEVICE_ID", serialNum);
        HttpRequest.getInstance().Request("patient/register",hash,RegisterPatientActivity.this);
    }


    @Override
    public void Success(String method, JSONObject result) throws JSONException {
        Log.i("JSONObject",result.toString());
        if (!StringUitls.isEmtpy(result.toString())) {
            if(result.getString("data").equals("regiser success")){
                Toast.makeText(RegisterPatientActivity.this,result.getString("data"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterPatientActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterPatientActivity.this.finish();
            }else {
                Toast.makeText(RegisterPatientActivity.this,result.getString("data"),Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(RegisterPatientActivity.this,R.string.hint_err,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Failure(String str, String method, String errorStr) {
            Toast.makeText(RegisterPatientActivity.this,errorStr,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Error(String str, String method, Throwable ex) {
        Toast.makeText(RegisterPatientActivity.this,str,Toast.LENGTH_SHORT).show();
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
