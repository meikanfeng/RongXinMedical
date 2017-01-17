package com.huagu.RX.rongxinmedical.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

/**
 * Created by fff on 2017/1/16.
 */

public class ModifyActivity extends BaseActivity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.modify_title);

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
