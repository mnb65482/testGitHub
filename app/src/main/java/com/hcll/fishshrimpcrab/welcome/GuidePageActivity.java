package com.hcll.fishshrimpcrab.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.common.widget.circleindicator.CircleIndicator;
import com.hcll.fishshrimpcrab.login.activity.LoginActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 2018/2/9.
 */

public class GuidePageActivity extends AppCompatActivity {

    private List<Fragment> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_guide_page);

        initFragmentList();

        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_page_viewpager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.guide_page_indicator);
        MyFgAdapter adapter = new MyFgAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    private void initFragmentList() {
        GuidePageFragment fragment1 = new GuidePageFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(GuidePageFragment.KEY_TITLE, getString(R.string.guidepage_title1));
        bundle1.putString(GuidePageFragment.KEY_DETAIL, getString(R.string.guidepage_detail1));
        fragment1.setArguments(bundle1);
        GuidePageFragment fragment2 = new GuidePageFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(GuidePageFragment.KEY_TITLE, getString(R.string.guidepage_title2));
        bundle2.putString(GuidePageFragment.KEY_DETAIL, getString(R.string.guidepage_detail2));
        fragment2.setArguments(bundle2);
        GuidePageFragment fragment3 = new GuidePageFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(GuidePageFragment.KEY_TITLE, getString(R.string.guidepage_title3));
        bundle3.putString(GuidePageFragment.KEY_DETAIL, getString(R.string.guidepage_detail3));
        bundle3.putBoolean(GuidePageFragment.KEY_ISLAST, true);
        fragment3.setArguments(bundle3);
        list = new ArrayList<>();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
    }
}
