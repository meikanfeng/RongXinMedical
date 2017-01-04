package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by fff on 2016/8/18.
 */
public class MyInfoAdapter extends BaseAdapter {

    private Context context;
    private String[] names;
    private HashMap<String, String> info;
    private HashMap<String,String> unit;

    public MyInfoAdapter(Context context,String[] names, HashMap<String, String> info) {
        this.context = context;
        this.names = names;
        this.info = info;
        this.unit = new HashMap<String,String>();
        this.unit.put("Age:",context.getResources().getStringArray(R.array.unit)[0]);
        this.unit.put("Height:",context.getResources().getStringArray(R.array.unit)[1]);
        this.unit.put("Weight:",context.getResources().getStringArray(R.array.unit)[2]);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.myinfoadapter_item,null,false);
        TextView item_name = (TextView) view.findViewById(R.id.item_name);
        TextView item_value = (TextView) view.findViewById(R.id.item_value);
        LinearLayout item = (LinearLayout) view.findViewById(R.id.item);
        LinearLayout n_item = (LinearLayout) view.findViewById(R.id.n_item);
        ImageView right_into = (ImageView) view.findViewById(R.id.right_into);
        View bottomview = view.findViewById(R.id.bottomview);
        if (!"".equals(names[i])){
            item_name.setText(names[i]);
            item_value.setText((!"Name:".equals(names[i]))?info.get(names[i]):info.get("First Name:")+"."+info.get("Last Name:"));
            Log.e("===" + names[i], "===" + info.get(names[i]));
            if ("Age:".equals(names[i]) || "Height:".equals(names[i]) || "Weight:".equals(names[i])){
                item_value.append(unit.get(names[i]));
            }
            if ("Treating Doctor ID:".equals(names[i]) || "Institutuin ID:".equals(names[i]) || "ID:".equals(names[i]))
                right_into.setVisibility(View.INVISIBLE);
            item.setTag(i);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rcc.RightClick(names[(int)view.getTag()],(int)view.getTag());
                }
            });
        }else{
            item.setPadding(0,8,0,8);
            item.setBackgroundColor(context.getResources().getColor(R.color.backColor));
            n_item.setVisibility(View.GONE);
        }
        if (names.length>6){
            if ("Weight:".equals(names[i]) || "Instant Messaging Account".equals(names[i])){
                bottomview.setVisibility(View.VISIBLE);
            }
        }else{
            if ("Name:".equals(names[i])){
                bottomview.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }
    private RightClickCallBack rcc;

    public void setRightClick(RightClickCallBack rcc) {
        this.rcc = rcc;
    }
}
