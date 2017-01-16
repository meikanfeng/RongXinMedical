package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/14.
 */
public class CommonStringAdapter extends BaseAdapter {
    Context context;
    List<GoodsNameAndId> list;

    public CommonStringAdapter(Context context, List<GoodsNameAndId> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_common_string, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_ics);
            viewHolder.img_selected=(ImageView)convertView.findViewById(R.id.img_selected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameTextView.setText(list.get(position).getGc_name());
//        if(position==pos){
//            viewHolder.img_selected.setVisibility(View.VISIBLE);
//        }else {
            viewHolder.img_selected.setVisibility(View.GONE);
//        }
        return convertView;
    }

    private class ViewHolder {
        TextView nameTextView;// 展示的内容
        ImageView img_selected;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
