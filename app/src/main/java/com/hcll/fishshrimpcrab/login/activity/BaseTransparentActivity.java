package com.hcll.fishshrimpcrab.login.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hong on 2018/2/16.
 */

public abstract class BaseTransparentActivity extends Activity {
    @BindView(R.id.transparent_cancel_tv)
    TextView transparentCancelTv;
    @BindView(R.id.transparent_second_tv)
    TextView transparentSecondTv;
    @BindView(R.id.transparent_first_tv)
    TextView transparentFirstTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_transparent_select);
        ButterKnife.bind(this);
        transparentFirstTv.setText(getFirstBtnName());
        transparentSecondTv.setText(getSecondBtnName());
    }

    @OnClick({R.id.transparent_cancel_tv, R.id.transparent_second_tv, R.id.transparent_first_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.transparent_cancel_tv:
                finish();
                break;
            case R.id.transparent_second_tv:
                secondBtnClick();
                break;
            case R.id.transparent_first_tv:
                firstBtnClick();
                break;
        }
    }

    protected abstract String getFirstBtnName();

    protected abstract String getSecondBtnName();

    protected abstract void firstBtnClick();

    protected abstract void secondBtnClick();

}
