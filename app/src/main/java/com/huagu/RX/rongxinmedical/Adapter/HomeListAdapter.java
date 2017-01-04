package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.CircleScheduleModel;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.View.ChartView;
import com.huagu.RX.rongxinmedical.View.CircleScheduleView;
import com.huagu.RX.rongxinmedical.View.TextClickDateView;
import com.huagu.RX.rongxinmedical.widget.CustomTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fff on 2016/8/4.
 */
public class HomeListAdapter extends BaseAdapter {

    private double[][] press, leak;
    private double[] ahi, usa;

    private Context context;

    private long timestamp;

    public HomeListAdapter(Context context,long timestamp) {
        this.context = context;
        this.timestamp = timestamp;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.homelist_item,null,false);
            holder.chartview = (ChartView) view.findViewById(R.id.chartview);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.units = (CustomTextView) view.findViewById(R.id.units);
            holder.bottom_ms = (CustomTextView) view.findViewById(R.id.bottom_ms);
            holder.max_95th_median = (LinearLayout) view.findViewById(R.id.max_95th_median);
//            holder.chartview.setyTitlesStrings(new String[]{"25.0", "20.0", "15.0", "10.0", "5.0", "0"});
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.chartview.setxTitles(timestamp);
        switch (i){
            case 0:
                holder.chartview.upData(new double[][]{usa},"usa");
                holder.name.setText("Usage");
                holder.units.setText("h");
                int num3=0;
                int count3 = 0;
                for (int j=0;j<usa.length;j++){
                    if (usa[j]>0){
                        num3 += usa[j];
                        count3++;
                    }
                }
                String ms3="0";
                if (count3>0){
                    ms3 = holder.chartview.getHours(num3/count3);
                }
                holder.bottom_ms.setText(ms3);
                holder.max_95th_median.setVisibility(View.GONE);
                break;
            case 1:
                holder.chartview.upData(press, "press");
                holder.name.setText("Pressure");
                holder.units.setText("cmH₂O");
                double num=0;
                int count = 0;
                for (int j=0;j<press[1].length;j++){
                    if (press[1][j]>0){
                        num += press[1][j];
                        count ++;
                    }
                }
                String ms="0";
                if (count>0){
                    ms = String.format("%.1f",(num/(double)count));
                }
                holder.bottom_ms.setText(ms);
                holder.max_95th_median.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.chartview.upData(leak,"leak");
                holder.name.setText("Leakage");
                holder.units.setText("L/min");
                double num1=0;
                int count1 = 0;
                for (int j=0;j<leak[1].length;j++){
                    if (leak[1][j]>0){
                        num1 += leak[1][j];
                        count1++;
                    }
                }
                String ms1="0";
                if (count1>0){
                    ms1 = String.format("%.1f",(num1/(double)count1));
                }
                holder.bottom_ms.setText(ms1);
                holder.max_95th_median.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.chartview.upData(new double[][]{ahi},"ahi");
                holder.name.setText("AHI");
                holder.units.setText("events/hr");
                double num2=0;
                int count2 = 0;
                for (int j=0;j<ahi.length;j++){
                    if (ahi[j]>0){
                        num2 += ahi[j];
                        count2++;
                    }
                }
                String ms2="0";
                if (count2>0){
                    ms2 = String.format("%.2f",num2/(double)count2);
                }
                holder.bottom_ms.setText(ms2);
                holder.max_95th_median.setVisibility(View.GONE);
                break;
        }

        holder.chartview.setcolor(new int[]{R.color.chart_yellow, R.color.chart_cyan, R.color.chart_blue});

        return view;
    }

    public void setData(double[][] press,double[][] leak,double[] ahi,double[] usa) {
        this.press = press;
        this.leak = leak;
        this.ahi = ahi;
        this.usa = usa;
    }

    class ViewHolder{
        private ChartView chartview;
        private CustomTextView units,bottom_name,bottom_ms;
        private TextView name;
        private LinearLayout max_95th_median;
    }

}
