package com.hcll.fishshrimpcrab.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hong on 2018/2/9.
 */
public class MainFragAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public MainFragAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }
}
