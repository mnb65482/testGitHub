package com.hcll.fishshrimpcrab.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hong on 2018/2/23.
 */

public class MainFragment extends BaseFragment {
    @BindView(R.id.main_invitation_code_et)
    EditText mainInvitationCodeEt;
    @BindView(R.id.main_to_game_iv)
    ImageView mainToGameIv;
    @BindView(R.id.main_create_game_iv)
    ImageView mainCreateGameIv;

    @Override
    public void onInitView() {
        initTopBar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View fragView = getFragView();
        ButterKnife.bind(this, fragView);
        initView();
        return view;
    }

    private void initView() {

    }

    private void initTopBar() {
        getTopBar().setTitle(getString(R.string.tab_main));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main;
    }


    @OnClick({R.id.main_to_game_iv, R.id.main_create_game_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_to_game_iv:
                ToastUtils.showLong("确认入局 邀请码：" + mainInvitationCodeEt.getText().toString());
                break;
            case R.id.main_create_game_iv:
                ToastUtils.showLong("我要组局");
                break;
        }
    }
}
