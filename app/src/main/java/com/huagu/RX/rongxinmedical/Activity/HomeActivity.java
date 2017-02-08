package com.huagu.RX.rongxinmedical.Activity;

import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.huagu.RX.rongxinmedical.Adapter.HomeFragmentPagerAdapter;
import com.huagu.RX.rongxinmedical.Entity.Date_YMD;
import com.huagu.RX.rongxinmedical.Entity.Item;
import com.huagu.RX.rongxinmedical.Fragment.HomeFragment;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.DateUtils;
import com.huagu.RX.rongxinmedical.Utils.MyWindowsManage;
import com.huagu.RX.rongxinmedical.widget.menudrawer.MenuDrawer;
import com.huagu.RX.rongxinmedical.widget.menudrawer.Position;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends BaseSideslipActivity implements HomeFragment.OnFragmentInteractionListener {


    public final static long ONEDAY_TIMESTAMP = 86400000;

    public ViewPager home_viewpager;

    private static HomeActivity ha;

    public static HomeActivity getInstance() {
        return ha;
    }

    private int mPagerPosition;
    private int mPagerOffsetPixels;

    public String type;
    public String pid;
    public String reg_time;

    private HomeFragmentPagerAdapter hfpa;

    private int totalpager;

    private List<Date_YMD> dymd;
    private Calendar calendar;//日历对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ha = this;
        type = usershared.getString("TYPE", "");//用于判断是否需要侧滑，医生点击查看时不需要策划

        pid = getIntent().getStringExtra("pid");
        reg_time = getIntent().getStringExtra("reg_time");

        mMenuDrawer.setContentView(R.layout.activity_home);
        mMenuDrawer.setSlideDrawable(R.drawable.menu);
        mMenuDrawer.setDrawerIndicatorEnabled(true);

        mMenuDrawer.setMenuSize(MyWindowsManage.getWidth(this) / 3 * 2);
        if ("2".equals(type)) {
            mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        } else {
            mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        }
        mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y) {
                if (v == home_viewpager) {
                    return !(mPagerPosition == 0 && mPagerOffsetPixels == 0) || dx < 0;
                }
                return false;
            }
        });

        home_viewpager = (ViewPager) this.findViewById(R.id.home_viewpager);

        home_viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerPosition = position;
                mPagerOffsetPixels = positionOffsetPixels;
            }
        });
        getDateList();
        hfpa = new HomeFragmentPagerAdapter(getSupportFragmentManager(), dymd);
        home_viewpager.setAdapter(hfpa);
        home_viewpager.setCurrentItem(totalpager);
    }

    /**
     * 打开或者关闭侧滑
     */
    public void closeoropenMenu() {
        if (mMenuDrawer.isMenuVisible()) {
            mMenuDrawer.closeMenu();
        } else {
            mMenuDrawer.openMenu();
        }
    }

    private long time;//房钱时间戳

    /**
     * 初始化日期数据
     */
    public void getDateList() {
        dymd = new ArrayList<Date_YMD>();
        long starttime = Long.valueOf(reg_time);//注册日期
        time = System.currentTimeMillis();//当前日期
        totalpager = ((int) ((time - starttime) / ONEDAY_TIMESTAMP));
        calendar = Calendar.getInstance();
        for (int i = 0; i < (totalpager + 1); i++) {
            long timestamp = time - (time % ONEDAY_TIMESTAMP) - ((totalpager - i) * ONEDAY_TIMESTAMP);
            calendar.setTimeInMillis(timestamp);
            int year = calendar.get(Calendar.YEAR);//年
            int month = calendar.get(Calendar.MONTH);//月
            int day = calendar.get(Calendar.DAY_OF_MONTH);//日
            //设置在时间区间内的时间
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date_YMD dy = new Date_YMD((calendar.getTimeInMillis() / (long) 1000) * (long) 1000, year, month, day, i);
            dymd.add(dy);
        }
    }

    /**
     * 选择日期后跳转到指定页
     *
     * @param timestamp 日期的时间戳
     */
    public void selectpager(long timestamp) {
        int pager = (int) ((timestamp - dymd.get(0).getTimestamp()) / ONEDAY_TIMESTAMP);
        home_viewpager.setCurrentItem(pager);
    }

    @Override
    protected void onMenuItemClicked(View view) {
        mMenuDrawer.closeMenu();
    }

    @Override
    protected int getDragMode() {
        return mMenuDrawer.MENU_DRAG_CONTENT;
    }

    @Override
    protected Position getDrawerPosition() {
        return Position.LEFT;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
