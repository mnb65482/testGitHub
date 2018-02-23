package com.hcll.fishshrimpcrab.club.fragment;

import android.graphics.Color;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

/**
 * Created by hong on 2018/2/23.
 */

public class MainClubFragment extends BaseFragment {

    @Override
    public void onInitView() {
        initTopBar();
    }

    private void initTopBar() {
        QMUITopBar topBar = getTopBar();
        TextView title = topBar.setTitle(getString(R.string.tab2));
        title.setTextColor(Color.WHITE);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main_club;
    }
}
