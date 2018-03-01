package com.hcll.fishshrimpcrab.login.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.Any;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.login.MD5Utils;
import com.hcll.fishshrimpcrab.main.MainActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import LoginProto.GameLogin;
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
    private Dialog dialog;
    private Socket socket;

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
        dialog = DialogUtils.createProgressDialog(this, null);
        try {
            InetAddress inetAddress = InetAddress.getByName(AppCommonInfo.socket_host);
            socket = new Socket(inetAddress, AppCommonInfo.socket_port);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            ToastUtils.showShort(getString(R.string.input_phonenum));
            return;
        }

        if (StringUtils.isEmpty(loginAccountPsw.getText())) {
            ToastUtils.showShort(getString(R.string.input_psw));
            return;
        }

        login();

//        Intent intent = MainActivity.createActivit(this, 0);
//        startActivity(intent);
//        finish();
    }

    public void login() {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) return;
                    GameLogin.LoginReq loginReq = GameLogin.LoginReq.newBuilder().setPhoneNum(loginAccountPhone.getText().toString())
                            .setPassword(MD5Utils.getMD5(loginAccountPsw.getText().toString()))
                            .setImsi(Constant.IMEI)
                            //设备类型 2 安卓
                            .setDeviceType(2)
                            //登录类型:1是用户名密码 2是token模式
                            .setUserType(1).build();
                    Any any = Any.pack(loginReq);
                    GameLogin.LoginBody loginBody = GameLogin.LoginBody.newBuilder().setBody(any).build();
                    GameLogin.LoginHead loginHead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.LOGIN).build();
                    GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(loginBody).setHead(loginHead).build();
                    loginMsg.writeDelimitedTo(socket.getOutputStream());
                    InputStream inputStream = socket.getInputStream();
                    GameLogin.LoginMsg loginMsgResp = GameLogin.LoginMsg.parseDelimitedFrom(inputStream);
                    GameLogin.LoginHead head = loginMsgResp.getHead();
                    if (head.getErrCode() == 0) {
                        Message message = Message.obtain();
                        message.obj = loginMsgResp.getBody().getBody().unpack(GameLogin.LoginResponse.class);
                        message.what = 0;
                        mhandler.sendMessage(message);
                    } else {
                        mhandler.sendEmptyMessage(head.getErrCode());
                    }
                } catch (IOException e) {
                    mhandler.sendEmptyMessage(3);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CODE_2_REGISTER) {
            finish();
        }
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            switch (msg.what) {
                case 0:
                    if (msg.obj instanceof GameLogin.LoginResponse) {
                        GameLogin.LoginResponse response = (GameLogin.LoginResponse) msg.obj;
                        AppCommonInfo.setToken(response.getToken());
                        AppCommonInfo.setUserid(response.getUserId());
                        Intent intent = MainActivity.createActivit(LoginActivity.this, response);
                        startActivity(intent);
                        releaseSocket();
                        finish();
                    }
                    break;
                case 1://token 失效
                    break;
                case 2://用户名密码错误
                    ToastUtils.showLong(R.string.login_erro);
                    break;
                case 3://系统异常
                default:
                    ToastUtils.showLong(R.string.login_system_erro);
                    break;
            }
        }
    };

    private void releaseSocket() {
        try {
            if (null != socket) {
                if (!socket.isClosed()) {
                    socket.close();
                }
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
