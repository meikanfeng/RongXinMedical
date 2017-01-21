package com.huagu.RX.rongxinmedical.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.R;

import java.util.List;

/**
 * Created by fff on 2017/1/20.
 */

public class DeviceInfoAdapter extends BaseAdapter {

    private Context context;
    private List<BluetoothDevice> deviceList;

    public DeviceInfoAdapter(Context context, List<BluetoothDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.device_item_view,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvSN = (TextView) convertView.findViewById(R.id.tvSN);
//            holder.tvSSI = (TextView) convertView.findViewById(R.id.tvSSI);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(deviceList.get(position).getName());
        holder.tvSN.setText(deviceList.get(position).getAddress());
//        holder.tvSSI.setText(R.string.device + (position + 1));

        return convertView;
    }

    class ViewHolder{
        private TextView tvName;
        private TextView tvSN;
//        private TextView tvSSI;
    }
}
