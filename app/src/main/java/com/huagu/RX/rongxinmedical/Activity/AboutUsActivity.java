package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.Util;

public class AboutUsActivity extends BaseActivity {


    private TextView version;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        context = this;

        InitView();


    }

    private void InitView() {
        super.initTile();

        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.About);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        version = (TextView) this.findViewById(R.id.version);

        version.setText(String.format(getResources().getString(R.string.Resvent_Medical), Util.getVersionName(context)));

    }


}
