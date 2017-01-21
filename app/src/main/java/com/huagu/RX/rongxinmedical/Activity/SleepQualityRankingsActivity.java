package com.huagu.RX.rongxinmedical.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huagu.RX.rongxinmedical.Adapter.SleepQualityRankingsAdapter;
import com.huagu.RX.rongxinmedical.Entity.SleepRank;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class SleepQualityRankingsActivity extends BaseActivity {


    private ListView qualitylistview;
    private TextView tcd;
    private ImageView back ,refresh;

    private String pid;
    private String timestamp = "";
    private SleepQualityRankingsAdapter sqra;
    private List<SleepRank> sleepRankslist;
    private HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_quality_rankings);

        pid = usershared.getString("USER_ID","");
        Log.e("timestamp",""+timestamp);

        InitView();
        timestamp = getIntent().getStringExtra("timestamp");
        map = new HashMap<String,String>();
        map.put("pid",pid);
        map.put("timestamp",timestamp.isEmpty() ? "" : timestamp);
        getData("sleepQualityRankings", map);
    }

    private void InitView() {
        tcd = (TextView) this.findViewById(R.id.title);
        back = (ImageView) this.findViewById(R.id.menu);
        refresh = (ImageView) this.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData("sleepQualityRankings", map);
            }
        });
        tcd.setText(R.string.SleepQualityRankings);
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        qualitylistview = (ListView) this.findViewById(R.id.qualitylistview);

    }

    private void getData(String method, HashMap<String, String> map) {

        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());
                sleepRankslist = JSON.parseArray(result.getString("data"), SleepRank.class);
                sqra = new SleepQualityRankingsAdapter(SleepQualityRankingsActivity.this,sleepRankslist);
                qualitylistview.setAdapter(sqra);
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Log.e(method,str.toString());
            }
            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method,str.toString());
            }
        });
    }



}
