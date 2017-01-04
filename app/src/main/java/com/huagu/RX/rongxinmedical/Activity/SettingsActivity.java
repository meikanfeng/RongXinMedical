package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.R;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private TextView height, weight, pressure, leak_flow, format, check_update, adout_me;
    private LinearLayout height_lin, weight_lin, pressure_lin, leak_flow_lin, format_lin, check_update_lin, adout_me_lin;

    private Context context;

    private String[] heights, weights, pressures, leak_flows, formats;
    private String[][] datas;


    private final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        heights = getResources().getStringArray(R.array.height);
        weights = getResources().getStringArray(R.array.weight);
        pressures = getResources().getStringArray(R.array.pressure);
        leak_flows = getResources().getStringArray(R.array.leak_flow);
        formats = getResources().getStringArray(R.array.format);
        datas = new String[][]{heights, weights, pressures, leak_flows, formats};

        InitView();

    }

    private TextView[] textview = new TextView[5];
    private LinearLayout[] linear = new LinearLayout[5];

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.Settings);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textview[0] = (TextView) this.findViewById(R.id.height);
        textview[1] = (TextView) this.findViewById(R.id.weight);
        textview[2] = (TextView) this.findViewById(R.id.pressure);
        textview[3] = (TextView) this.findViewById(R.id.leak_flow);
        textview[4] = (TextView) this.findViewById(R.id.format);
        check_update = (TextView) this.findViewById(R.id.check_update);
        adout_me = (TextView) this.findViewById(R.id.adout_me);
        for (int i = 0; i < textview.length; i++) {
            textview[i].setText(datas[i][0]);
        }

        linear[0] = (LinearLayout) this.findViewById(R.id.height_lin);
        linear[1] = (LinearLayout) this.findViewById(R.id.weight_lin);
        linear[2] = (LinearLayout) this.findViewById(R.id.pressure_lin);
        linear[3] = (LinearLayout) this.findViewById(R.id.leak_flow_lin);
        linear[4] = (LinearLayout) this.findViewById(R.id.format_lin);
        check_update_lin = (LinearLayout) this.findViewById(R.id.check_update_lin);
        adout_me_lin = (LinearLayout) this.findViewById(R.id.adout_me_lin);
        for (int i = 0; i < textview.length; i++) {
            linear[i].setOnClickListener(this);
        }
        check_update_lin.setOnClickListener(new Click(this));
        adout_me_lin.setOnClickListener(new Click(this));
    }

    public class Click implements View.OnClickListener {
        SettingsActivity settingsActivity;

        public Click(SettingsActivity settingsActivity) {
            this.settingsActivity = settingsActivity;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check_update_lin:
                    Toast.makeText(settingsActivity,R.string.No_update,Toast.LENGTH_SHORT).show();
                    break;
                case R.id.adout_me_lin:
                    Intent intent = new Intent(settingsActivity, AboutUsActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ModifySettingActivity.class);
        switch (view.getId()) {
            case R.id.height_lin:
                intent.putExtra("order", 0);
                intent.putExtra("title", "Height");
                break;
            case R.id.weight_lin:
                intent.putExtra("order", 1);
                intent.putExtra("title", "Weight");
                break;
            case R.id.pressure_lin:
                intent.putExtra("order", 2);
                intent.putExtra("title", "Pressure");
                break;
            case R.id.leak_flow_lin:
                intent.putExtra("order", 3);
                intent.putExtra("title", "Leak/Flow");
                break;
            case R.id.format_lin:
                intent.putExtra("order", 4);
                intent.putExtra("title", "Format");
                break;
            default:
                Log.e("11111111111111", "xxxxxxxxxxxxxxxxx");
                break;
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int order = data.getIntExtra("order", 0);
                int selected = data.getIntExtra("selected", 0);
                textview[order].setText(datas[order][selected]);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
