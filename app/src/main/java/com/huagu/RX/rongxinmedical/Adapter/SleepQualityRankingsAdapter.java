package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.SleepRank;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.xutils.x;

import java.util.List;

/**
 * Created by fff on 2016/8/17.
 */
public class SleepQualityRankingsAdapter extends BaseAdapter {

    private int[] Image = new int[]{R.mipmap.no1,R.mipmap.no2,R.mipmap.no3,R.mipmap.no4,R.mipmap.no5,R.mipmap.no6};

    private Context context;

    private List<SleepRank> sleepRankslist;
    public SleepQualityRankingsAdapter(Context context, List<SleepRank> sleepRankslist) {
        this.context = context;
        this.sleepRankslist = sleepRankslist;
    }

    @Override
    public int getCount() {
        if (sleepRankslist==null)
            return 0;
        return sleepRankslist.size();
    }

    @Override
    public Object getItem(int i) {
        return sleepRankslist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.sleepqualityrankingsadapter_item,null,false);
            holder = new ViewHolder();
            holder.sleep_avatar = (ImageView) view.findViewById(R.id.sleep_avatar);
            holder.sleep_ranking = (ImageView) view.findViewById(R.id.sleep_ranking);
            holder.sleep_name = (TextView) view.findViewById(R.id.sleep_name);
            holder.sleep_performance = (TextView) view.findViewById(R.id.sleep_performance);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        SleepRank sleeprank = (SleepRank) getItem(i);

        holder.sleep_ranking.setImageResource(Image[i]);
        x.image().bind(holder.sleep_avatar, sleeprank.getPhoto(), MyApplication.getInstance().imageOptions);
        holder.sleep_name.setText(sleeprank.getName());
        holder.sleep_performance.setText(sleeprank.getScore());
        return view;
    }

    class ViewHolder{
        ImageView sleep_ranking,sleep_avatar;
        TextView sleep_name,sleep_performance;
    }

}
