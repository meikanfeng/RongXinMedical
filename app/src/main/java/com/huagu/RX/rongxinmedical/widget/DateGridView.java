package com.huagu.RX.rongxinmedical.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.Month;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fengm on 2016/8/2.
 */
public class DateGridView extends GridView {


    private List<Month> monthofday;

    private Context context;

    public DateGridView(Context context) {
        super(context);
        this.context = context;
    }

    public DateGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DateGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public DateGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(7);
    }

    public void setDateAdapter() {
        this.setAdapter(new DateAdapter());

        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
            }
        });

    }

    int upyear,year,nextyear;
    int upmonth,month,nextmonth;

    /**
     * 設置年月
     * @param year  年
     * @param month 月（java中月份從0开始，所以真实月份传进来前需 -1 ）
     */
    public void setData(int year,int month){
        Calendar calendar = Calendar.getInstance();

        if (month==1){
            upyear = year-1;
            upmonth =12;
        }else{
            upyear = year;
            upmonth = month-1;
        }
        if(month==12){
            nextyear = year+1;
            nextmonth = 1;
        }else{
            nextyear = year;
            nextmonth = month+1;
        }

        if (monthofday==null)
            monthofday = new ArrayList<Month>();
        if (monthofday.size()>0)
            monthofday.clear();

        calendar.set(Calendar.YEAR, upyear);//先指定年份
        calendar.set(Calendar.MONTH, upmonth);//再指定月份 Java月份从0开始算
        int updaysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int updayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Month upmonths = new Month(upyear,upmonth,new int[updaysCountOfMonth],updaysCountOfMonth,updayOfWeek);
        monthofday.add(upmonths);

        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month);//再指定月份 Java月份从0开始算
        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Month months = new Month(year,month,new int[daysCountOfMonth],daysCountOfMonth,dayOfWeek);
        monthofday.add(months);

        calendar.set(Calendar.YEAR, nextyear);//先指定年份
        calendar.set(Calendar.MONTH, nextmonth);//再指定月份 Java月份从0开始算
        int daysCountOfMonthnext = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeeknext = calendar.get(Calendar.DAY_OF_WEEK);
        Month monthsnext = new Month(nextyear,nextmonth,new int[daysCountOfMonthnext],daysCountOfMonthnext,dayOfWeeknext);
        monthofday.add(monthsnext);

//        if (this.getAdapter()!=null)
//            this.getAdapter().notifyAll();
    }

    public interface DataGridItemClick{
        void Itemclick(int year,int month,int day);
    }
    private DataGridItemClick dgic;

    public void setItemClickLintener(DataGridItemClick dgic){
        this.dgic = dgic;
    }

    private String[] weeks = getResources().getStringArray(R.array.weeks);

    public class DateAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 49;
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
            ViewHolder vh;
            if (view == null){
                vh = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.date_item,null,false);
                vh.day = (TextView) view.findViewById(R.id.day);
                vh.rootview = (LinearLayout) view.findViewById(R.id.rootview);
                view.setTag(vh);
            }else{
                vh = (ViewHolder) view.getTag();
            }

            AbsListView.LayoutParams vlp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            vlp.width = (MyWindowsManage.getWidth(context)-100)/7;
            vlp.height = (MyWindowsManage.getWidth(context)-100)/8;
            vh.rootview.setLayoutParams(vlp);

            if (i<7){
                vh.day.setText(weeks[i]);
                vh.day.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                vh.day.setTextColor(Color.BLACK);
            }else if((i-7)<(monthofday.get(1).getDayofweeks()-1)) {
                vh.day.setText((monthofday.get(0).getDay_num()+(i-7)-(monthofday.get(1).getDayofweeks()-1)+1)+"");
                vh.day.setPaintFlags(Paint.HINTING_ON);
                vh.day.setTextColor(Color.GRAY);
            }else if(i<(monthofday.get(1).getDayofweeks()+7+(monthofday.get(1).getDay_num()-1))){
                vh.day.setText((i-7-(monthofday.get(1).getDayofweeks()-1))+1+"");
                vh.day.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                vh.day.setTextColor(Color.BLACK);
                vh.day.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setSelected(true);
                        dgic.Itemclick(monthofday.get(1).getYear(),monthofday.get(1).getMonth(),Integer.valueOf(((TextView)view).getText().toString()));
                    }
                });
            }else{
                vh.day.setText((i-7-(monthofday.get(1).getDayofweeks()-1)-monthofday.get(1).getDay_num())+1+"");
                vh.day.setPaintFlags(Paint.HINTING_ON);
                vh.day.setTextColor(Color.GRAY);
            }
            return view;
        }

        class ViewHolder{
            TextView day;
            LinearLayout rootview;
        }

    }





}
