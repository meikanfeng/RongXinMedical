package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

/**
 * Created by Administrator on 2017/1/12.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Context context =RegisterActivity.this;
    private RadioGroup radio;
    private TextView register_next,register_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        register_cancle =(TextView)findViewById(R.id.register_cancle);
        register_next =(TextView)findViewById(R.id.register_next);

        register_next.setOnClickListener(this);
        register_cancle.setOnClickListener(this);
        radio =(RadioGroup)findViewById(R.id.radio);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                register_next.setClickable(true);
                if(i == R.id.radio_patient){

                }else if(i == R.id.radio_doctor){

                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_next:

                if(((RadioButton)findViewById(R.id.radio_patient)).isChecked()){ //患者
                    startActivity(new Intent(RegisterActivity.this,RegisterPatientActivity.class));
                }else if(((RadioButton)findViewById(R.id.radio_doctor)).isChecked()){//医生
                    startActivity(new Intent(RegisterActivity.this,RegisterDocActivity.class));
                }

                break;
            case R.id.register_cancle:
                finish();
                break;
        }
    }
}
