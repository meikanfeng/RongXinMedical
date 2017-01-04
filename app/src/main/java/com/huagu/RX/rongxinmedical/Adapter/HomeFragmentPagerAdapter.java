package com.huagu.RX.rongxinmedical.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.huagu.RX.rongxinmedical.Entity.Date_YMD;
import com.huagu.RX.rongxinmedical.Fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fff on 2016/8/9.
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {


    public List<HomeFragment> hflist;

    public HomeFragmentPagerAdapter(FragmentManager fm, List<Date_YMD> dymd) {
        super(fm);
        this.hflist = new ArrayList<HomeFragment>();
        for (int i = 0; i < dymd.size(); i++) {
            hflist.add(HomeFragment.newInstance(dymd.get(i)));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return hflist.get(position);
    }

    @Override
    public int getCount() {
        return hflist.size();
    }
}
