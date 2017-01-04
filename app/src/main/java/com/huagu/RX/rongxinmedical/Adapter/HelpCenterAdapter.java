package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Activity.HelpCenterActivity;
import com.huagu.RX.rongxinmedical.Entity.HelpProblem;
import com.huagu.RX.rongxinmedical.Entity.Patient;
import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.R;

import java.util.List;

/**
 * Created by fff on 2016/8/15.
 */
public class HelpCenterAdapter extends BaseAdapter {

    private Context context;
    private List<HelpProblem> helpList;
    private RightClickCallBack rcc;

    public HelpCenterAdapter(Context context, List<HelpProblem> helpList) {
        this.helpList = helpList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (helpList == null)
            return 0;
        return helpList.size();
    }

    @Override
    public Object getItem(int i) {
        return helpList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.helpcenteradapter_item, null, false);
            holder = new ViewHolder();
            holder.answer = (TextView) view.findViewById(R.id.answer);
            holder.question = (TextView) view.findViewById(R.id.question);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HelpProblem hp = (HelpProblem) getItem(i);
        holder.answer.setText(" " + hp.getCONTENT());
        holder.question.setText(" " + hp.getTITLE());
        return view;
    }

    class ViewHolder {
        TextView question, answer;
    }

    public void setsearchcallback(RightClickCallBack rcc) {
        this.rcc = rcc;
    }

}
