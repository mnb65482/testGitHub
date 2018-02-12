package com.hcll.fishshrimpcrab.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by linzehong on 2016/3/16.
 */
public class MyFgAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public MyFgAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    //返回一个Fragment
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    //返回Fragment总数
    @Override
    public int getCount() {
        return list.size();
    }
}
