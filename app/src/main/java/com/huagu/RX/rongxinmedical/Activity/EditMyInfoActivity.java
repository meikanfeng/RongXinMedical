package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditMyInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestListener {

    private ListView userinfolist;
    private LinearLayout listselect, editlayout;
    private String title, titles;
    private String type;
    private String value;

    private String[] data;

    private Context context;
    private EditText editinput1,editinput2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myinfo);

        context = this;

        type = getIntent().getStringExtra("type");
        value = getIntent().getStringExtra("value");
        title = getIntent().getStringExtra("title");

        InitView();

    }

    private void InitView() {
        super.initTile();
        titles = title;
        if (!"Instant Messaging Account".equals(title)) {
            titles = title.substring(0, title.length() - 1);
        }

        refresh.setImageResource(R.drawable.selected_white);
        tcd.setText(titles);
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
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("USER_ID", usershared.getString("USER_ID", ""));
                if ("Sex:".equals(title))
                    map.put(getUpperCase(title), data[selected]);
                else if ("Name:".equals(title)){
                    map.put("FIRST_NAME", editinput1.getText().toString().trim());
                    map.put("LAST_NAME", editinput2.getText().toString().trim());
                } else
                    map.put(getUpperCase(title), editinput1.getText().toString().trim());
                amendInfo(map);
            }
        });

        listselect = (LinearLayout) this.findViewById(R.id.listselect);
        editlayout = (LinearLayout) this.findViewById(R.id.editlayout);
        editinput1 = (EditText) this.findViewById(R.id.editinput1);
        editinput2 = (EditText) this.findViewById(R.id.editinput2);


        editinput1.setText(value);

        if ("Sex:".equals(title)) {
            listselect.setVisibility(View.VISIBLE);
            editlayout.setVisibility(View.GONE);
            data = getResources().getStringArray(R.array.Sexs);
            userinfolist = (ListView) this.findViewById(R.id.userinfolist);

            userinfolist.setAdapter(new EditUserInfoAdapter());
            userinfolist.setOnItemClickListener(this);
        }else if ("Name:".equals(title)){
            editinput1.setText(value.split("#")[0]);
            editinput2.setVisibility(View.VISIBLE);
            editinput2.setText(value.split("#")[1]);
        }
        if ("Age:".equals(title) || "Height:".equals(title) || "Weight:".equals(title) || "Phone:".equals(title)){
            editinput1.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public String getUpperCase(String str){
        return str.toUpperCase().replace(" ","_").replace(":","");
    }

    private int selected;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((LinearLayout) parent.getChildAt(selected)).getChildAt(1).setSelected(false);
        ImageView image = (ImageView) ((LinearLayout) parent.getChildAt(position)).getChildAt(1);
        image.setSelected(true);
        selected = position;
    }

    @Override
    public void Success(String method, JSONObject result) throws JSONException {
        Intent intent = new Intent();
        if ("Sex:".equals(title))
            intent.putExtra("result", data[selected]);
        else if ("Name:".equals(title))
            intent.putExtra("result", editinput1.getText().toString().trim()+"#"+editinput2.getText().toString().trim());
        else
            intent.putExtra("result", editinput1.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void Failure(String str, String method, int errorCode) {
        Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Error(String str, String method, Throwable ex) {
        Log.e(method,str);
    }

    private class EditUserInfoAdapter extends BaseAdapter {
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
            if (data[i].equals(value)){
                units_select.setSelected(true);
            }else{
                units_select.setSelected(false);
            }
            units_name.setText(data[i]);
            return view;
        }
    }

    public void amendInfo(HashMap<String,String> hashmap){
        HttpRequest.getInstance().Request("edit_info",hashmap,this);
    }

}
