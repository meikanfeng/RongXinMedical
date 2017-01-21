package com.huagu.RX.rongxinmedical.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

/**
 * Created by fff on 2017/1/16.
 */

public class ModifyActivity extends BaseActivity {

    private TextView title,tvSuccess,tvSuccessTip;
    private ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        title = (TextView) findViewById(R.id.title);
        refresh = (ImageView) findViewById(R.id.refresh);
        title.setText(R.string.modify_title);
        refresh.setVisibility(View.GONE);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyActivity.this,LoginActivity.class);
                startActivity(intent);
                ModifyActivity.this.finish();
            }
        });
    }
}
