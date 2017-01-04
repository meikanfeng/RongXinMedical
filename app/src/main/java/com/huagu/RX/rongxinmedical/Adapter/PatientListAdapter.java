package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SearchView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.Constant;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.widget.CircleImageView;
import com.huagu.RX.rongxinmedical.widget.PercentLinearLayout;


import org.xutils.x;

import java.util.List;

/**
 * Created by fff on 2016/8/16.
 */
public class PatientListAdapter extends BaseAdapter {

    private List<Patient> patientlist;
    private Context context;

    private RightClickCallBack scb;

    public PatientListAdapter(Context context, List<Patient> patientlist) {
        this.context = context;
        this.patientlist = patientlist;
        Log.e("1111111", patientlist.size() + "");
    }

    @Override
    public int getCount() {
        if (patientlist == null)
            return 0;
        return patientlist.size();
    }

    @Override
    public Object getItem(int i) {
        return patientlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.patientlistadapter_item, null, false);
            holder = new ViewHolder();
            holder.age_and_sex = (TextView) view.findViewById(R.id.age_and_sex);
            holder.patient_id = (TextView) view.findViewById(R.id.patient_id);
            holder.patient_name = (TextView) view.findViewById(R.id.patient_name);
            holder.message_date = (TextView) view.findViewById(R.id.message_date);
            holder.image_avatar = (CircleImageView) view.findViewById(R.id.image_avatar);
            holder.right = (PercentLinearLayout) view.findViewById(R.id.right);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Patient patient = (Patient) getItem(i);

        holder.age_and_sex.setText(String.format(context.getResources().getString(R.string.patientlist_ageandsex), "".equals(patient.getAGE()) ? "0" : patient.getAGE(), patient.getSEX()));
        holder.patient_id.setText(String.format(context.getResources().getString(R.string.patientlist_id), patient.getUSER_ID()));
        holder.message_date.setText(String.format(context.getResources().getString(R.string.patientlist_update), patient.getUpdated()));
        holder.patient_name.setText(patient.getName());

        holder.image_avatar.setImageResource(R.mipmap.logo);
        x.image().bind(holder.image_avatar, context.getSharedPreferences("user",Context.MODE_PRIVATE).getString("IMAGE_PATH","")+"/"+patient.getPHOTO(), MyApplication.getInstance().imageOptions);
//            Log.e("111111", holder.image_avatar.getScaleType().toString());
        holder.right.setTag(i);
        holder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scb.RightClick(((Patient) (getItem((int) view.getTag()))).getUSER_ID(), (int) view.getTag());
            }
        });
        return view;
    }

    public void setsearchcallback(RightClickCallBack scb) {
        this.scb = scb;
    }


    class ViewHolder {
        private TextView message_date, patient_name, patient_id, age_and_sex;
        private CircleImageView image_avatar;
        private PercentLinearLayout right;
    }


}
