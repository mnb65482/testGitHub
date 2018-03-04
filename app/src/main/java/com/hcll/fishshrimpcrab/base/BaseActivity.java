package com.hcll.fishshrimpcrab.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hcll.fishshrimpcrab.R;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

/**
 * Created by hong on 2018/2/23.
 */

public class BaseActivity extends AppCompatActivity {

    private QMUITopBar topBar;
    private ViewGroup topbarLayout;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View baseView = inflater.inflate(R.layout.activity_base, viewGroup);
        topbarLayout = (ViewGroup) baseView.findViewById(R.id.base_topbar_layout);
        topBar = (QMUITopBar) baseView.findViewById(R.id.base_topbar);
        ViewGroup container = (ViewGroup) baseView.findViewById(R.id.container_body);

        if (layoutResID != 0) {
            view = inflater.inflate(layoutResID, null);
            container.removeAllViews();
            container.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        topbarLayout.setVisibility(View.GONE);
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        if (view != null) {
            return view.findViewById(id);
        }
        return super.findViewById(id);
    }

    public QMUITopBar getTopBar() {
        return topBar;
    }

    public void showTopBar() {
        topbarLayout.setVisibility(View.VISIBLE);
    }


}
