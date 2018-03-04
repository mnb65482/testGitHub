package com.hcll.fishshrimpcrab.login.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.login.LoginApi;
import com.hcll.fishshrimpcrab.login.MD5Utils;
import com.hcll.fishshrimpcrab.login.entity.UserIdEntity;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERFECT = 112;

    @BindView(R.id.register_topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.register_account_phone)
    EditText mAccountPhone;
    @BindView(R.id.register_sms_psw)
    EditText mSmsPsw;
    @BindView(R.id.register_getsms_tv)
    TextView mGetsmsTv;
    @BindView(R.id.register_account_psw)
    EditText mAccountPsw;
    @BindView(R.id.register_psw_visibility_cb)
    CheckBox mPswVisibilityCb;
    @BindView(R.id.register_invite_code_et)
    EditText mInviteCodeEt;
    @BindView(R.id.register_account_commit_tv)
    TextView mAccountCommitTv;
    private LoginApi loginApi;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTopBar();

        //密码可见性设置
        mPswVisibilityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mAccountPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mAccountPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        loginApi = HttpUtils.createRetrofit(this, LoginApi.class);
        dialog = DialogUtils.createProgressDialog(this, null);

    }

    @OnClick({R.id.register_getsms_tv, R.id.register_account_commit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_getsms_tv:
                if (!StringUtils.isEmpty(mAccountPhone.getText())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", mAccountPhone.getText().toString());
                    map.put("type", LoginApi.REGISTER);
                    RequestBody body = JsonUtils.createJsonRequestBody(map);
                    Call<BaseResponseEntity> call = loginApi.getSms(body);
                    call.enqueue(smsCallBack);
                } else {
                    ToastUtils.showShort(getString(R.string.input_phonenum));
                    return;
                }

                new MyCountDownTimer(60 * 1000, 1000).start();
                break;
            case R.id.register_account_commit_tv:
                if (checkNotNull()) {
                    commit();
                }
                break;
        }
    }

    private void commit() {
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("phone", mAccountPhone.getText().toString());
        map.put("code", mSmsPsw.getText().toString());
        map.put("passwd", MD5Utils.getMD5(mAccountPsw.getText().toString()));
        map.put("invate_code", mInviteCodeEt.getText().toString());
        RequestBody body = JsonUtils.createJsonRequestBody(map);

        Call<BaseResponseEntity<UserIdEntity>> call = loginApi.register(body);
        call.enqueue(new Callback<BaseResponseEntity<UserIdEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseEntity<UserIdEntity>> call, Response<BaseResponseEntity<UserIdEntity>> response) {
                dialog.dismiss();
                BaseResponseEntity entity = response.body();
                if (entity != null) {
                    switch (entity.getStatus()) {
                        //注册成功
                        case 0:
                            Object data = entity.getData();
                            if (data instanceof UserIdEntity) {
                                UserIdEntity userinfo = (UserIdEntity) data;
                                //重置用户缓存信息
                                AppCommonInfo.setUserid(userinfo.getUserid());
                                AppCommonInfo.setToken("");
                                AppCommonInfo.setPhone(mAccountPhone.getText().toString());
                                AppCommonInfo.setPassword(mAccountPsw.getText().toString());

                                Intent intent = PerfectInfoActivity.createActivity(RegisterActivity.this, userinfo.getUserid());
                                startActivityForResult(intent, REQUEST_CODE_PERFECT);
                            }

                            break;
                        //失败
                        case 1:
                            ToastUtils.showLong(R.string.register_failuer);
                            break;
                        //手机号已注册
                        case 2:
                            ToastUtils.showLong(R.string.phone_is_register);
                            break;
                        //验证码错误
                        case 3:
                            ToastUtils.showLong(R.string.sms_failuer);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<UserIdEntity>> call, Throwable t) {
                ToastUtils.showLong(R.string.register_failuer);
                dialog.dismiss();
            }
        });


    }

    private boolean checkNotNull() {
        if (StringUtils.isEmpty(mAccountPhone.getText())) {
            ToastUtils.showShort(getString(R.string.input_phonenum));
            return false;
        }

        if (StringUtils.isEmpty(mSmsPsw.getText())) {
            ToastUtils.showShort(getString(R.string.input_sms));
            return false;
        }

        if (StringUtils.isEmpty(mAccountPsw.getText())) {
            ToastUtils.showShort(getString(R.string.input_psw));
            return false;
        }

//        if (StringUtils.isEmpty(mInviteCodeEt.getText())) {
//            ToastUtils.showShort(getString(R.string.input_inviteCode));
//            return false;
//        }

        return true;
    }

    private void initTopBar() {
        mTopbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        QMUIAlphaImageButton backBtn = mTopbar.addLeftBackImageButton();
        backBtn.setImageResource(R.drawable.topbar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_still, R.anim.slide_out_right);
            }
        });
        TextView title = mTopbar.setTitle(getString(R.string.title_register));
        title.setTextColor(Color.WHITE);

    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mGetsmsTv.setText("重发" + l / 1000 + "秒");
            mGetsmsTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mGetsmsTv.setEnabled(true);
            mGetsmsTv.setText("重新获取");
        }
    }


    private Callback<BaseResponseEntity> smsCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            BaseResponseEntity entity = response.body();
            if (entity != null) {
                switch (entity.getStatus()) {
                    case 0:
                        ToastUtils.showLong(R.string.sms_success);
                        break;
                    case 1:
                        ToastUtils.showLong(getString(R.string.getsms_failuer));
                        break;
                    case 2:
                        ToastUtils.showLong(getString(R.string.phone_is_register));
                        break;
                    case 3:
                        ToastUtils.showLong(getString(R.string.sms_invalid));
                        break;
                }
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            ToastUtils.showShort(getString(R.string.getsms_failuer));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CODE_PERFECT) {
            setResult(RESULT_OK);
            finish();
        }

    }
}
