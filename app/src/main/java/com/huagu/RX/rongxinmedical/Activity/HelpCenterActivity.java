package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.huagu.RX.rongxinmedical.Adapter.HelpCenterAdapter;
import com.huagu.RX.rongxinmedical.Adapter.PatientListAdapter;
import com.huagu.RX.rongxinmedical.Entity.HelpProblem;
import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.View.TextClickDateView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class HelpCenterActivity extends BaseActivity implements RightClickCallBack {


    private ListView helpcenter_listview;
    private TextView tcd;
    private ImageView back ,refresh;

    private String pid;
    private Context context;

    private HelpCenterAdapter hca;
    private List<HelpProblem> helpList;
    private HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        context = this;
        pid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("USER_ID","");

        InitView();

        map = new HashMap<String,String>();
        map.put("did", pid);
        getData("help", map);

    }


    private void InitView() {
        tcd = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.menu);
        refresh = (ImageView) findViewById(R.id.refresh);

        tcd.setText(getResources().getString(R.string.HelpCenter));
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
                getData("help", map);
            }
        });
        helpcenter_listview = (ListView) this.findViewById(R.id.helpcenter_listview);
        View view = LayoutInflater.from(context).inflate(R.layout.helpcenteradapter_item_one,null,false);
//        helpcenter_listview.addHeaderView(view);
        TextView problem = (TextView) view.findViewById(R.id.problem);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getData(String method,HashMap<String,String> map) {

        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());

                helpList = JSON.parseArray(result.getString("data"),HelpProblem.class);

                hca = new HelpCenterAdapter(context,helpList);
                hca.setsearchcallback(HelpCenterActivity.this);
                helpcenter_listview.setAdapter(hca);
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Log.e(method, str.toString());
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method, str.toString());
            }
        });
    }


    @Override
    public void RightClick(String str,int i) {
        Log.e("1111111111111111", str);
    }

}
