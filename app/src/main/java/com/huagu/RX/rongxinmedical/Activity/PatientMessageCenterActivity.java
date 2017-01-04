package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huagu.RX.rongxinmedical.Adapter.MessageListAdapter;
import com.huagu.RX.rongxinmedical.Adapter.PatientMessageAdapter;
import com.huagu.RX.rongxinmedical.Entity.Message;
import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Entity.PatientMessage;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PatientMessageCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ImageView back,refresh;
    private TextView title;
    private Context context;

    private ListView patient_message;

    private HashMap<String,String> map;
    private String pid;

    private List<PatientMessage> patientmsglist;
    private PatientMessageAdapter pma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_message_center);
        context = this;
        InitView();

        pid = usershared.getString("USER_ID","");

        map = new HashMap<String,String>();
        map.put("pid", pid);
        getData("patienMessage",map);
    }

    private void getData(String method,HashMap<String,String> map) {
        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());
                patientmsglist = JSON.parseArray(result.getString("data"), PatientMessage.class);
                pma = new PatientMessageAdapter(context,patientmsglist);
                patient_message.setAdapter(pma);
            }
            @Override
            public void Failure(String str, String method, int errorCode) {
                Log.e(method,str.toString());
            }
            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method,str.toString());
            }
        });
    }

    private void InitView() {
        back = (ImageView) this.findViewById(R.id.menu);
        refresh = (ImageView) this.findViewById(R.id.refresh);
        title = (TextView) this.findViewById(R.id.title);

        refresh.setVisibility(View.GONE);
        title.setText(R.string.MessageList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        patient_message = (ListView) this.findViewById(R.id.patient_message);
        patient_message.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,MessageListActivity.class);
        intent.putExtra("did",patientmsglist.get(i).getSENDER_ID());
        intent.putExtra("photo",patientmsglist.get(i).getHEAD_PHOTO());
        startActivity(intent);
    }
}
