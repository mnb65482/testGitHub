package com.hcll.fishshrimpcrab.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    //    @BindView(R.id.login_account_icon_qiv)
//    QMUIRadiusImageView loginAccountIconQiv;
    @BindView(R.id.login_account_phone)
    EditText loginAccountPhone;
    @BindView(R.id.login_account_psw)
    EditText loginAccountPsw;
    @BindView(R.id.login_psw_visibility_cb)
    CheckBox loginPswVisibilityCb;
    @BindView(R.id.login_account_commit_tv)
    TextView loginAccountCommitTv;
    @BindView(R.id.login_forget_psw_tv)
    TextView loginForgetPswTv;
    @BindView(R.id.login_to_register_tv)
    TextView loginToRegisterTv;

    private static final int REQUEST_CODE_2_REGISTER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPswVisibilityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loginAccountPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    loginAccountPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @OnClick({R.id.login_account_commit_tv, R.id.login_forget_psw_tv, R.id.login_to_register_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_account_commit_tv:
                commit();
                break;
            case R.id.login_forget_psw_tv:
                Intent forgetIntent = new Intent(this, ForgetPSWActivity.class);
                startActivity(forgetIntent);
                break;
            case R.id.login_to_register_tv:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_2_REGISTER);
                break;
        }
    }

    private void commit() {
        if (StringUtils.isEmpty(loginAccountPhone.getText())) {
            ToastUtils.showShort("请填写手机号！");
            return;
        }

        if (StringUtils.isEmpty(loginAccountPsw.getText())) {
            ToastUtils.showShort("请填写密码！");
            return;
        }

        //todo login

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
