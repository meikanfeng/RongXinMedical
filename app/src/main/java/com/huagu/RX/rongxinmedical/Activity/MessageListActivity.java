package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.huagu.RX.rongxinmedical.Adapter.MessageListAdapter;
import com.huagu.RX.rongxinmedical.Adapter.PatientListAdapter;
import com.huagu.RX.rongxinmedical.Entity.Message;
import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MessageListActivity extends BaseActivity {


    private ListView message_listview;
    private TextView tcd;
    private ImageView back, refresh;
    private LinearLayout bottom;
    private Context context;
    private EditText message_content;
    private Button send;

    private String pid, did, photo;

    private List<Message> messagelist;

    private MessageListAdapter mla;

    private String type;

    protected HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        context = this;

        pid = usershared.getString("USER_ID", "");
        type = usershared.getString("TYPE", "");
        Initview();

        map = new HashMap<String, String>();
        Intent intent = getIntent();
        photo = intent.getStringExtra("photo");
        if (intent.hasExtra("pid")) {//pid;患者id;did:医生id     /分别对应的是发送这id和接收着id（只有医生可以发消息）
            did = intent.getStringExtra("pid");
            map.put("senderId", pid);
            map.put("receiverId", did);
        } else if (intent.hasExtra("did")) {
            did = intent.getStringExtra("did");
            map.put("senderId", did);
            map.put("receiverId", pid);
        }
        getData("message_list", map);
//        getData("doctorsMessage", map);
    }


    private void Initview() {
        tcd = (TextView) this.findViewById(R.id.title);
        back = (ImageView) this.findViewById(R.id.menu);
        refresh = (ImageView) this.findViewById(R.id.refresh);

        tcd.setText(R.string.MessageList);
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
                getData("doctorsMessage", map);
            }
        });

        bottom = (LinearLayout) this.findViewById(R.id.bottom);

        message_listview = (ListView) this.findViewById(R.id.message_listview);

        if ("3".equals(type)) {
            bottom.setVisibility(View.GONE);
        }

        message_content = (EditText) this.findViewById(R.id.message_content);
        send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = message_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(context, getResources().getString(R.string.messagecontent_tips), Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("senderId", pid);
                hashMap.put("receiverId", did);
                hashMap.put("msgContent", content);
                getData("send_msg", hashMap);
            }
        });
    }

    private void getData(String method, HashMap<String, String> map) {
        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());
                if ("message_list".equals(method)) {
                    messagelist = JSON.parseArray(result.getString("data"), Message.class);
                    if (messagelist.size() < 3) {
                        message_listview.setStackFromBottom(false);
                    } else {
                        message_listview.setStackFromBottom(true);
                    }
                    mla = new MessageListAdapter(context, messagelist);
                    mla.setAvatar(photo);
                    message_listview.setAdapter(mla);
                } else if ("send_msg".equals(method)) {
                    message_content.setText("");
                    getData("message_list", MessageListActivity.this.map);
                }
            }

            @Override
            public void Failure(String str, String method, int errorCode) {
                Log.e(method, str.toString());
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method, str.toString());
            }
        });
    }
}
