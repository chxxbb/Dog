package com.example.administrator.rilegou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class HomeViewPageAdapter extends FragmentPagerAdapter {
    List<Fragment> flist;


    public HomeViewPageAdapter(FragmentManager fm, List<Fragment> flist) {
        super(fm);
        this.flist = flist;

    }

    @Override
    public Fragment getItem(int position) {
        return flist.get(position);
    }

    @Override
    public int getCount() {
        return flist.size();
    }


}
