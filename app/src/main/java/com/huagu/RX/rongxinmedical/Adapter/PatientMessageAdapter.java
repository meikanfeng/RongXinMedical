package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.PatientMessage;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.widget.CustomTextView;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

/**
 * Created by fff on 2016/8/18.
 */
public class PatientMessageAdapter extends BaseAdapter {

    private Context context;
    private List<PatientMessage> patientmsglist;

    public PatientMessageAdapter(Context context, List<PatientMessage> patientmsglist) {
        this.context = context;
        this.patientmsglist = patientmsglist;
    }

    @Override
    public int getCount() {
        return patientmsglist.size();
    }

    @Override
    public Object getItem(int i) {
        return patientmsglist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.patientmessageadapter_item,null,false);
            holder = new ViewHolder();
            holder.avatat = (ImageView) view.findViewById(R.id.avatat);
            holder.unread = (CustomTextView) view.findViewById(R.id.unread);
            holder.message_time = (TextView) view.findViewById(R.id.message_time);
            holder.message_content = (TextView) view.findViewById(R.id.message_content);
            holder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        PatientMessage patientmsg = (PatientMessage) getItem(i);
        x.image().bind(holder.avatat, context.getSharedPreferences("user",Context.MODE_PRIVATE).getString("IMAGE_PATH","")+"/"+patientmsg.getHEAD_PHOTO(), MyApplication.getInstance().imageOptions);
        String NO_READ_COUNT = patientmsg.getNO_READ_COUNT();
        if (TextUtils.isEmpty(NO_READ_COUNT) || "0".equals(NO_READ_COUNT)){
            holder.unread.setVisibility(View.INVISIBLE);
        }else{
            holder.unread.setVisibility(View.VISIBLE);
            holder.unread.setText(NO_READ_COUNT);
        }
        holder.message_time.setText(patientmsg.getADD_TIME());
        holder.message_content.setText(patientmsg.getCONTENT());
        holder.doctor_name.setText(patientmsg.getUSERNAME());
        return view;
    }

    class ViewHolder{
        private TextView message_time,message_content,doctor_name;
        private CustomTextView unread;
        private ImageView avatat;
    }

}
