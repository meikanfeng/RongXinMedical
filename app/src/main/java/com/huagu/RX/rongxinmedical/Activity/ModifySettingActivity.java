package com.huagu.RX.rongxinmedical.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

public class ModifySettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private Context context;
    private String title;
    private int order;
    private ListView settinglist;

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_setting);
        context = this;
        title = getIntent().getStringExtra("title");
        order = getIntent().getIntExtra("order", 0);
        switch (order) {
            case 0:
                data = getResources().getStringArray(R.array.height);
                break;
            case 1:
                data = getResources().getStringArray(R.array.weight);
                break;
            case 2:
                data = getResources().getStringArray(R.array.pressure);
                break;
            case 3:
                data = getResources().getStringArray(R.array.leak_flow);
                break;
            case 4:
                data = getResources().getStringArray(R.array.format);
                break;
        }
        InitView();
    }

    private void InitView() {
        super.initTile();
        refresh.setImageResource(R.drawable.selected_white);
        tcd.setText(title);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("order", order);
                intent.putExtra("selected", selected);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        settinglist = (ListView) this.findViewById(R.id.settinglist);

        settinglist.setAdapter(new SettingAdapter());
        settinglist.setOnItemClickListener(this);
    }

    private int selected = 0;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((LinearLayout) adapterView.getChildAt(selected)).getChildAt(1).setSelected(false);
        ImageView image = (ImageView) ((LinearLayout) adapterView.getChildAt(i)).getChildAt(1);
        image.setSelected(true);
        selected = i;
    }

    private class SettingAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return data[0];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.settingadapter_item, null, false);
            TextView units_name = (TextView) view.findViewById(R.id.units_name);
            ImageView units_select = (ImageView) view.findViewById(R.id.units_select);
            LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear);
            units_name.setText(data[i]);
            return view;
        }
    }


}
