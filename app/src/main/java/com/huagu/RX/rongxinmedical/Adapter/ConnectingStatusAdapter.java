package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

/**
 * Created by fff on 2016/9/7.
 */
public class ConnectingStatusAdapter extends BaseAdapter {

    private String[] status;

    private Context context;

    public ConnectingStatusAdapter(Context context) {
        this.context = context;
        status = context.getResources().getStringArray(R.array.Connecting_Wifi);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.connectingstatusadapter_item,null,false);
            holder = new ViewHolder();
            holder.status_text = (TextView) convertView.findViewById(R.id.status_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.status_text.setText(status[position]);
        return convertView;
    }

    class ViewHolder{
        TextView status_text;
    }

    private int size = 1;

    @Override
    public void notifyDataSetChanged() {
        if (size<=5){
            size ++;
        }
        super.notifyDataSetChanged();
    }
}
