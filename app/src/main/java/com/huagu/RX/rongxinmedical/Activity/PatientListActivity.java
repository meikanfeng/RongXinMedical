package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.huagu.RX.rongxinmedical.Adapter.PatientListAdapter;
import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;
import com.huagu.RX.rongxinmedical.widget.menudrawer.MenuDrawer;
import com.huagu.RX.rongxinmedical.widget.menudrawer.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PatientListActivity extends BaseSideslipActivity implements RightClickCallBack, AdapterView.OnItemClickListener {

    private final String Tag = "com.huagu.RX.rongxinmedical.Activity.PatientListActivity";

    private ListView helpcenter_listview;

    private String pid;
    private List<Patient> patientlist;
    private Context context;
    private PatientListAdapter pla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        mMenuDrawer.setContentView(R.layout.activity_list);
        mMenuDrawer.setSlideDrawable(R.drawable.menu);
        mMenuDrawer.setDrawerIndicatorEnabled(true);

        mMenuDrawer.setMenuSize(MyWindowsManage.getWidth(this) / 3 * 2);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y) {

                return false;
            }
        });

        pid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("USER_ID", "");

        InitView();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("did", pid);
        getData("patientList", map);
    }

    @Override
    protected void onMenuItemClicked(View view) {
        mMenuDrawer.closeMenu();
    }

    @Override
    protected int getDragMode() {
        return mMenuDrawer.MENU_DRAG_CONTENT;
    }

    @Override
    protected Position getDrawerPosition() {
        return Position.LEFT;
    }

    private void getData(String method, HashMap<String, String> map) {

        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());
                patientlist = JSON.parseArray(result.getString("data"), Patient.class);

                pla = new PatientListAdapter(context, patientlist);
                pla.setsearchcallback(PatientListActivity.this);
                helpcenter_listview.setAdapter(pla);
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

    private void InitView() {
        super.initTile();
        refresh.setVisibility(View.GONE);
        tcd.setText(R.string.PatientList);
        back.setImageResource(R.drawable.menu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeoropenMenu();
            }
        });

        helpcenter_listview = (ListView) this.findViewById(R.id.helpcenter_listview);
        View view = LayoutInflater.from(context).inflate(R.layout.patientlist_search,null,false);
        helpcenter_listview.addHeaderView(view);
        helpcenter_listview.setOnItemClickListener(this);

        ImageView search_btn = (ImageView) view.findViewById(R.id.search_btn);
        final EditText searchedit = (EditText) view.findViewById(R.id.searchedit);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleClick(searchedit.getText().toString().trim());
            }
        });
    }
    public void closeoropenMenu(){
        if (mMenuDrawer.isMenuVisible()){
            mMenuDrawer.closeMenu();
        }else{
            mMenuDrawer.openMenu();
        }
    }
    public void titleClick(String str) {
        Log.e("1111111111111111", str + "");
//        if (TextUtils.isEmpty(str)) {
//            Toast.makeText(context, getResources().getString(R.string.search_content), Toast.LENGTH_SHORT).show();
//            return;
//        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("did", pid);
        map.put("search", str);
        getData("patientListByPatientIdOrPatientName", map);
    }

    @Override
    public void RightClick(String str,int i) {
        if (TextUtils.isEmpty(str)) {
            try {
                throw new IllegalArgumentException(Tag+": id为空");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            return;
        }
        Intent intent = new Intent(this,MessageListActivity.class);
        intent.putExtra("pid",str);
        intent.putExtra("photo",usershared.getString("PHOTO", ""));
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (helpcenter_listview.getAdapter()!=null){
            String patient_id = ((Patient)helpcenter_listview.getAdapter().getItem(i)).getUSER_ID();
            String reg_time = ((Patient)helpcenter_listview.getAdapter().getItem(i)).getADD_TIME();
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("pid",patient_id);
            intent.putExtra("reg_time", reg_time);
            startActivity(intent);
        }
    }
}
