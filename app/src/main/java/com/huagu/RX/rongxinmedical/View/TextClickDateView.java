package com.huagu.RX.rongxinmedical.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Fragment.HomeFragment;
import com.huagu.RX.rongxinmedical.Interface.OnDateSelectListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.DateUtils;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;
import com.huagu.RX.rongxinmedical.widget.CustomTextView;
import com.huagu.RX.rongxinmedical.widget.DateGridView;
import com.huagu.RX.rongxinmedical.widget.PercentLinearLayout;

import org.xutils.common.util.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fengm on 2016/8/2.
 * 自定义的 title上显示日期的控件
 */
public class TextClickDateView extends LinearLayout {


    public TextClickDateView(Context context) {
        super(context);
//        pw = new AppCompatPopupWindow(context,null,0);
        InitView(context);
    }

    public TextClickDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        pw = new AppCompatPopupWindow(context,attrs,0);
        InitView(context);
    }

    public TextClickDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        pw = new AppCompatPopupWindow(context,attrs,defStyleAttr);
        InitView(context);
    }

    public TextClickDateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        pw = new AppCompatPopupWindow(context,attrs,defStyleAttr);
        InitView(context);
    }

    public Context context;

    public PercentLinearLayout pll;
    public CustomTextView date;

    private LayoutInflater lif;
    private Calendar calendar;

    private final long ONEDAY_TIMESTAMP = 86400000;

    private int curyear, curmonth, curweek, curday;
    private OnDateSelectListener ondateselect;
    public void setDateSelectListener(OnDateSelectListener ondateselect) {
        this.ondateselect = ondateselect;
    }

    public void InitView(Context context) {
        this.context = context;
        lif = LayoutInflater.from(context);
        pll = (PercentLinearLayout) lif.inflate(R.layout.textclickdateview, null);
        this.addView(pll);

        date = (CustomTextView) pll.findViewById(R.id.date);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        maxyear = calendar.get(Calendar.YEAR);
        maxmonth = calendar.get(Calendar.MONTH);
        maxday = calendar.get(Calendar.DAY_OF_MONTH);

        curyear = maxyear;
        curmonth = maxmonth;
        curday = maxday;
        curweek = calendar.get(Calendar.DAY_OF_WEEK);

//        date.setText(String.format("%1$s %2$s %3$s,%4$d", weeks[curweek - 1], months[curmonth], ((curday < 10) ? "0" + curday : "" + curday), curyear));

        initclick();
    }

    public void setText(String text){
        date.setText(text);
    }

    private int maxyear,maxmonth,maxday;
    private int staryear,starmonth,starday;

    public void setStartDate(String date){
        long times = Long.valueOf(date);
        calendar.setTimeInMillis(times);
        staryear = calendar.get(Calendar.YEAR);
        starmonth = calendar.get(Calendar.MONTH);
        starday = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void initclick() {
        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bl) {
                    ppa = new PopupPagerAdapter();
                    showPopupwindow(view);
                }
            }
        });
    }

    public boolean isshow() {
        if (pw != null) {
            return pw.isShowing();
        }
        return false;
    }
    private boolean bl = false;

    /**
     * 点击是否打开日历
     * @param bl    打开or不打开
     */
    public void setPopupEnabled(boolean bl){
        this.bl = bl;
    }

    public String[] weeks = getResources().getStringArray(R.array.week);
    public String[] months = getResources().getStringArray(R.array.month);

    private PopupWindow pw;
    private View PopupVIew;//popup的view

    private ViewPager viewpager;//暂时没用
//    private DateGridView dategridview;//日历
    private PopupPagerAdapter ppa;
    public void showPopupwindow(View v) {

        PopupVIew = lif.inflate(R.layout.date_popupview, null, false);

        viewpager = (ViewPager) PopupVIew.findViewById(R.id.datePager);
        ppa = new PopupPagerAdapter();
        ppa.setdata(curyear, curmonth);
        viewpager.setAdapter(ppa);
        viewpager.setCurrentItem(ppa.y_mlist.size()-1);
//        dategridview.setDateAdapter();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
//                ppa.y_mlist.get(position).getMonth()
                date.setText(String.format("%1$s %2$s %3$s,%4$d", weeks[curweek - 1], months[ppa.y_mlist.get(position).getMonth()], ((curday < 10) ? "0" + curday : "" + curday), curyear));
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pw = new PopupWindow(PopupVIew, MyWindowsManage.getWidth(context), MyWindowsManage.getWidth(context) - DensityUtil.dip2px(50));
        pw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        pw.setAnimationStyle(R.style.popwin_anim_style);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.update();
        pw.showAsDropDown(v);

    }


    public class PopupPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return y_mlist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public List<Y_M> y_mlist;

        public void setdata(int year, int month) {
            y_mlist = new ArrayList<Y_M>();
            if (staryear<year){
                for (int h=starmonth;h<12;h++){
                    Y_M ym = new Y_M(staryear,h);
                    y_mlist.add(ym);
                }
            }
            for (int i=(staryear+1);i<year;i++){
                for (int j=0;j<12;j++){
                    Y_M ym = new Y_M(i,j);
                    y_mlist.add(ym);
                }
            }
            if (staryear<year) {
                for (int k = 0; k <= month; k++) {
                    Y_M ym = new Y_M(year, k);
                    y_mlist.add(ym);
                }
            }else{
                for (int k = starmonth; k <= month; k++) {
                    Y_M ym = new Y_M(year, k);
                    y_mlist.add(ym);
                }
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.dateviewpager,null);
            DateGridView dategridview = (DateGridView) view.findViewById(R.id.dategridview);

            dategridview.setData(y_mlist.get(position).getYear(), y_mlist.get(position).getMonth());
            dategridview.setDateAdapter();
            dategridview.setItemClickLintener(new DateGridView.DataGridItemClick() {
                @Override
                public void Itemclick(int year, int month, int day) {
                    long time = DateUtils.getTimeStamp(year, month, day, 12);
                    if (time >= DateUtils.getTimeStamp(staryear, starmonth, starday, 12) && time <= DateUtils.getTimeStamp(maxyear, maxmonth, maxday, 12)) {
                        curyear = year;
                        curday = day;
                        curmonth = month;
                        curweek = DateUtils.getweeks(year, month, day);
//                        date.setText(String.format("%1$s %2$s %3$s,%4$d", weeks[curweek - 1], months[month], ((day < 10) ? "0" + day : "" + day), year));
                        Log.e("xxxxxxxxxxxxxxxxxxxx", String.format("%1$s %2$s %3$s,%4$d", weeks[curweek - 1], months[month], ((day < 10) ? "0" + day : "" + day), year));
                        ondateselect.onDateSelect(year, month, day, curweek - 1);
                        pw.dismiss();
                    } else {
                        Toast.makeText(context, "选择了无效的日期", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        class Y_M{

            int year;
            int month;

            public Y_M(int year, int month) {
                this.year = year;
                this.month = month;
            }

            public int getYear() {
                return year;
            }

            public int getMonth() {
                return month;
            }
        }

    }

}
