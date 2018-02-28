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
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

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
//                    socket = new Socket(AppCommonInfo.socket_host, AppCommonInfo.socket_port);
                    InetAddress inetAddress = InetAddress.getByName(AppCommonInfo.socket_host);
                    socket = new Socket(inetAddress, AppCommonInfo.socket_port);

                    GameLogin.LoginReq loginReq = GameLogin.LoginReq.newBuilder().setPhoneNum(loginAccountPhone.getText().toString())
//                    .setPassword(MD5Utils.getMD5(loginAccountPsw.getText().toString())).build();
                            .setPassword(loginAccountPsw.getText().toString())
                            .setImsi(Constant.IMEI)
                            .setDeviceType(2)
                            .setUserType(1).build();
                    Any any = Any.pack(loginReq);
                    GameLogin.LoginBody loginBody = GameLogin.LoginBody.newBuilder().setBody(any).build();
                    GameLogin.LoginHead loginHead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.LOGIN).build();
                    GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(loginBody).setHead(loginHead).build();
                    loginMsg.writeTo(socket.getOutputStream());
//                    OutputStream outputStream = socket.getOutputStream();
//                    outputStream.write(loginMsg.toByteArray());
//                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (socket != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                            if (inputStream != null) {
                                byte[] bytes = new byte[1024];
                                int count = inputStream.read(bytes);
                                if (count < 0) continue;
                                byte[] temp = new byte[count];
                                for (int i = 0; i < count; i++) {
                                    temp[i] = bytes[i];
                                }
                                GameLogin.LoginMsg loginMsgResp = GameLogin.LoginMsg.parseFrom(temp);
                                GameLogin.LoginHead head = loginMsgResp.getHead();
                                if (head.getErrCode() == 0) {
                                    GameLogin.LoginResponse loginResponse = loginMsgResp.getBody().getBody().unpack(GameLogin.LoginResponse.class);
                                    Log.e("login", String.format("userid == %s ,ip == %s ,port == %s token == %s key == %s",
                                            loginResponse.getUserId() + "", loginResponse.getIp(), loginResponse.getPort(),
                                            loginResponse.getToken(), loginResponse.getKey()));
                                }
                            }
                            mhandler.sendEmptyMessage(0);
                        } catch (IOException e) {
                            mhandler.sendEmptyMessage(0);
                            e.printStackTrace();
                        }
                    }
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
        }
    };
}
