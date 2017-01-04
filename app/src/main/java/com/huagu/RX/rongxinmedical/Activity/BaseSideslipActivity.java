package com.huagu.RX.rongxinmedical.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.Item;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.widget.CircleImageView;
import com.huagu.RX.rongxinmedical.widget.PercentRelativeLayout;
import com.huagu.RX.rongxinmedical.widget.menudrawer.MenuDrawer;
import com.huagu.RX.rongxinmedical.widget.menudrawer.Position;

import org.xutils.x;


public abstract class BaseSideslipActivity extends BaseActivity implements View.OnClickListener {

    private static final String STATE_ACTIVE_POSITION = "com.huagu.RX.rongxinmedical.Activity.BaseSideslipActivity";

    protected MenuDrawer mMenuDrawer;
    private CircleImageView myavatar;

    private int mActivePosition = 0;


    public String Avatar_path;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);

        Avatar_path = getSharedPreferences("user",MODE_PRIVATE).getString("IMAGE_PATH","")+"/";

        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, getDrawerPosition(), getDragMode());

        View view = LayoutInflater.from(this).inflate(R.layout.layout_menu,null,false);

        prl[0] = (PercentRelativeLayout) view.findViewById(R.id.item1);
        prl[1] = (PercentRelativeLayout) view.findViewById(R.id.item2);
        prl[2] = (PercentRelativeLayout) view.findViewById(R.id.item3);
        prl[3] = (PercentRelativeLayout) view.findViewById(R.id.item4);
        prl[4] = (PercentRelativeLayout) view.findViewById(R.id.item5);
        prl[5] = (PercentRelativeLayout) view.findViewById(R.id.item6);
        prl[6] = (PercentRelativeLayout) view.findViewById(R.id.item7);

        imageview[0] = (ImageView) view.findViewById(R.id.one);
        imageview[1] = (ImageView) view.findViewById(R.id.two);
        imageview[2] = (ImageView) view.findViewById(R.id.three);
        imageview[3] = (ImageView) view.findViewById(R.id.four);
        imageview[4] = (ImageView) view.findViewById(R.id.five);
        imageview[5] = (ImageView) view.findViewById(R.id.six);
        imageview[6] = (ImageView) view.findViewById(R.id.seven);

        textview[0] = (TextView) view.findViewById(R.id.text1);
        textview[1] = (TextView) view.findViewById(R.id.text2);
        textview[2] = (TextView) view.findViewById(R.id.text3);
        textview[3] = (TextView) view.findViewById(R.id.text4);
        textview[4] = (TextView) view.findViewById(R.id.text5);
        textview[5] = (TextView) view.findViewById(R.id.text6);
        textview[6] = (TextView) view.findViewById(R.id.text7);


        TextView left_name = (TextView) view.findViewById(R.id.left_name);
        left_name.setText(String.format(getResources().getString(R.string.left_name),usershared.getString("FIRST_NAME",""),usershared.getString("LAST_NAME", "")));

        for (int i=0;i<prl.length;i++){
            prl[i].setOnClickListener(this);
        }

        myavatar = (CircleImageView) view.findViewById(R.id.myavatar);
        myavatar.setImageResource(R.mipmap.def_patient);
        if ("2".equals(usershared.getString("TYPE", ""))){
            prl[0].setVisibility(View.GONE);
            prl[1].setVisibility(View.GONE);
            prl[3].setVisibility(View.GONE);
            myavatar.setImageResource(R.mipmap.def_doctor);
        }

        myavatar.setOnClickListener(this);

        mMenuDrawer.setMenuView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String avatar_path = Avatar_path+usershared.getString("PHOTO", "");
        x.image().bind(myavatar,avatar_path, MyApplication.getInstance().imageOptions);
    }

    private PercentRelativeLayout[] prl = new PercentRelativeLayout[7];
    private ImageView[] imageview = new ImageView[7];
    private TextView[] textview = new TextView[7];

    protected abstract void onMenuItemClicked(View view);

    protected abstract int getDragMode();

    protected abstract Position getDrawerPosition();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item1:
                selectchange(0);
                startActivity(new Intent(this, MyDeviceActivity.class));
                break;
            case R.id.item2:
                selectchange(1);
                break;
            case R.id.item3:
                selectchange(2);
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.item4:
                selectchange(3);
                startActivity(new Intent(this, PatientMessageCenterActivity.class));
                break;
            case R.id.item5:
                selectchange(4);
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            case R.id.item6:
                selectchange(5);
                startActivity(new Intent(this, AboutUsActivity.class));
                usershared.edit().clear().commit();
                break;
            case R.id.item7:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                selectchange(6);
                break;
            case R.id.myavatar:
                startActivity(new Intent(this, MyInfoActivity.class));
                break;
        }
        onMenuItemClicked(view);
    }

    private int selected = 0;

    public void selectchange(int cur){
        prl[selected].setSelected(false);
        imageview[selected].setSelected(false);
        textview[selected].setTextColor(getResources().getColor(R.color.gray_left));
        selected = cur;
        prl[selected].setSelected(true);
        imageview[selected].setSelected(true);
        textview[selected].setTextColor(getResources().getColor(R.color.colorWhite));
    }

}
