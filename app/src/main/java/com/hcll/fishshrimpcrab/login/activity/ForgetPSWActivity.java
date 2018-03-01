package com.hcll.fishshrimpcrab.login.activity;

import android.app.Dialog;
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
import com.google.gson.JsonObject;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.login.LoginApi;
import com.hcll.fishshrimpcrab.login.MD5Utils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPSWActivity extends AppCompatActivity {

    @BindView(R.id.reset_topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.reset_account_phone)
    EditText resetAccountPhone;
    @BindView(R.id.reset_sms_psw)
    EditText resetSmsPsw;
    @BindView(R.id.reset_getsms_tv)
    TextView resetGetsmsTv;
    @BindView(R.id.reset_account_psw)
    EditText resetAccountPsw;
    @BindView(R.id.reset_psw_visibility_cb)
    CheckBox resetPswVisibilityCb;
    @BindView(R.id.reset_account_commit_tv)
    TextView resetAccountCommitTv;
    private LoginApi loginApi;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_forget_psw);
        ButterKnife.bind(this);
        initTopBar();

        //密码可见性设置
        resetPswVisibilityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    resetAccountPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    resetAccountPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        loginApi = HttpUtils.createRetrofit(this, LoginApi.class);
        dialog = DialogUtils.createProgressDialog(this, null);

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
        TextView title = mTopbar.setTitle(getString(R.string.title_reset));
        title.setTextColor(Color.WHITE);
    }

    @OnClick({R.id.reset_getsms_tv, R.id.reset_account_commit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_getsms_tv:

                if (!StringUtils.isEmpty(resetAccountPhone.getText())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", resetAccountPhone.getText().toString());
                    map.put("type", LoginApi.RESET);
                    JSONObject jsonObj = new JSONObject(map);
                    RequestBody body = RequestBody.create(MediaType.parse("Content-Type,application/json"), jsonObj.toString());
                    Call<BaseResponseEntity> call = loginApi.getSms(body);
                    call.enqueue(smsCallBack);
                } else {
                    ToastUtils.showShort(getString(R.string.input_phonenum));
                    return;
                }

                new MyCountDownTimer(60 * 1000, 1000).start();

                break;
            case R.id.reset_account_commit_tv:

                if (checkNotNull()) {
                    dialog.show();
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone", resetAccountPhone.getText().toString());
                    map.put("code", resetSmsPsw.getText().toString());
                    map.put("passwd", MD5Utils.getMD5(resetAccountPsw.getText().toString()));
                    RequestBody body = JsonUtils.createJsonRequestBody(map);
                    Call<BaseResponseEntity> call = loginApi.resetPsw(body);

                    call.enqueue(new Callback<BaseResponseEntity>() {
                        @Override
                        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                            dialog.dismiss();
                            BaseResponseEntity entity = response.body();
                            if (entity != null) {
                                switch (entity.getStatus()) {
                                    //成功
                                    case 0:
                                        ToastUtils.showLong(R.string.reset_success);
                                        finish();
                                        break;
                                    //失败
                                    case 1:
                                        ToastUtils.showLong(R.string.reset_failuer);
                                        break;
                                    //手机号未注册
                                    case 2:
                                        ToastUtils.showLong(R.string.phone_isnot_register);
                                        break;
                                    //验证码错误
                                    case 3:
                                        ToastUtils.showLong(R.string.sms_failuer);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
                            ToastUtils.showLong(R.string.reset_failuer);
                            dialog.dismiss();
                        }
                    });
                }

                break;
        }
    }


    private boolean checkNotNull() {
        if (StringUtils.isEmpty(resetAccountPhone.getText())) {
            ToastUtils.showShort(getString(R.string.input_phonenum));
            return false;
        }

        if (StringUtils.isEmpty(resetSmsPsw.getText())) {
            ToastUtils.showShort(getString(R.string.input_sms));
            return false;
        }

        if (StringUtils.isEmpty(resetAccountPsw.getText())) {
            ToastUtils.showShort(getString(R.string.input_psw));
            return false;
        }

        return true;
    }


    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            resetGetsmsTv.setText("重发" + l / 1000 + "秒");
            resetGetsmsTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            resetGetsmsTv.setEnabled(true);
            resetGetsmsTv.setText("重新获取");
        }
    }

    private Callback<BaseResponseEntity> smsCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            BaseResponseEntity entity = response.body();
            if (entity != null) {
                switch (entity.getStatus()) {
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
}
