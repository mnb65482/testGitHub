package com.hcll.fishshrimpcrab.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.Any;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.Record.fragment.MainRecordFragment;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.fragment.MainClubFragment;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.widget.NoScrollViewPager;
import com.hcll.fishshrimpcrab.game.fragment.MainGameFragment;
import com.hcll.fishshrimpcrab.login.MD5Utils;
import com.hcll.fishshrimpcrab.login.activity.LoginActivity;
import com.hcll.fishshrimpcrab.login.service.LoginService;
import com.hcll.fishshrimpcrab.me.fragment.MainMeFragment;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.animate.AnimationType;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import LoginProto.GameLogin;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements OnTabSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public final static String BROADCAST_ACTION_RESREQ_RESULT = "wu2dou.braodcast.action.resreq.result";

    public static final String EXTRA_RESP_LOGIN = "login_resp";

    public static final String EXTRA_RESP_RES_LOGIN = "res_login_resp";


    @Titles
    private static final int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};

    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.main_tab_game_select, R.drawable.main_tab_club_select,
            R.drawable.main_tab_record_select, R.drawable.main_tab_me_select};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.main_tab_game, R.drawable.main_tab_club,
            R.drawable.main_tab_record, R.drawable.main_tab_me};

    @BindView(R.id.main_view_pager)
    NoScrollViewPager mainViewPager;
    @BindView(R.id.main_tabbar)
    JPTabBar mainTabbar;

    /**
     * 线程池
     */
    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private List<Fragment> list = new ArrayList<>();
    private ResReqBroadcastReceiver mBroadcastReceiverRes;
    private GameLogin.LoginResponse loginResponse;
    private Dialog mDialog;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParam();
        initView();
        initBroadcast();
        initData();
    }


    private void initParam() {
        Serializable serializable = getIntent().getSerializableExtra(EXTRA_RESP_LOGIN);
        if (serializable instanceof GameLogin.LoginResponse) {
            loginResponse = (GameLogin.LoginResponse) serializable;
        }
    }

    private void initView() {
        MainGameFragment gameFragment = new MainGameFragment();
        MainClubFragment clubFragment = new MainClubFragment();
        MainRecordFragment recordFragment = new MainRecordFragment();
        MainMeFragment meFragment = new MainMeFragment();
        MainFragment mainFragment = new MainFragment();

        list.add(gameFragment);
        list.add(clubFragment);
        list.add(recordFragment);
        list.add(meFragment);
        list.add(mainFragment);

        mainTabbar.setTabListener(this);
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.setAdapter(new MainFragAdapter(getSupportFragmentManager(), list));

        mainTabbar.setContainer(mainViewPager);
        mainTabbar.setTabListener(this);
        mainTabbar.setUseScrollAnimate(true);
        mainTabbar.setAnimation(AnimationType.SCALE);
        mainTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTabbar.setAllNotSelect();
                mainViewPager.setCurrentItem(mainViewPager.getAdapter().getCount() - 1, false);
            }
        });
        mainViewPager.setCurrentItem(mainViewPager.getAdapter().getCount() - 1, false);
        mDialog = DialogUtils.createProgressDialog(this, null);
    }


    private void initBroadcast() {

        mBroadcastReceiverRes = new ResReqBroadcastReceiver();
        registerReceiver(mBroadcastReceiverRes, new IntentFilter(BROADCAST_ACTION_RESREQ_RESULT));
    }

    private void initData() {

        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }

        if (loginResponse == null) {
//            threadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    initSocket();
//                }
//            });
            requestLoginInfo();
        } else {
            startService(LoginService.createService(MainActivity.this, loginResponse.getUserId(),
                    loginResponse.getKey(), loginResponse.getIp(), Integer.parseInt(loginResponse.getPort())));
        }
    }

//    /**
//     * 初始化socket
//     */
//    private void initSocket() {
//        try {
//            InetAddress inetAddress = InetAddress.getByName(AppCommonInfo.socket_host);
//            mSocket = new Socket(inetAddress, AppCommonInfo.socket_port);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void requestLoginInfo() {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

//                if (mSocket != null && mSocket.isConnected()) {
                try {
                    InetAddress inetAddress = InetAddress.getByName(AppCommonInfo.socket_host);
                    mSocket = new Socket(inetAddress, AppCommonInfo.socket_port);
                    GameLogin.LoginReq loginReq;
                    if (StringUtils.isEmpty(AppCommonInfo.getToken())) {
                        loginReq = GameLogin.LoginReq.newBuilder()
                                .setPhoneNum(AppCommonInfo.getPhone())
                                .setPassword(MD5Utils.getMD5(AppCommonInfo.getPassword()))
                                .setImsi(Constant.IMEI)
                                //设备类型 2 安卓
                                .setDeviceType(2)
                                //登录类型:1是用户名密码 2是token模式
                                .setUserType(1).build();
                        Log.w(TAG, "mSocket 用户名密码登录。。" + loginReq.toString());
                    } else {
                        loginReq = GameLogin.LoginReq.newBuilder()
                                .setImsi(Constant.IMEI)
                                .setToken(AppCommonInfo.getToken())
                                .setDeviceType(2)
                                .setUserType(2).build();
                        Log.w(TAG, "mSocket token登录。。" + loginReq.toString());
                    }
                    Any any = Any.pack(loginReq);
                    GameLogin.LoginBody loginBody = GameLogin.LoginBody.newBuilder().setBody(any).build();
                    GameLogin.LoginHead loginHead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.LOGIN).build();
                    GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(loginBody).setHead(loginHead).build();
                    Log.w(TAG, "mSocket 登录中。。");
                    loginMsg.writeDelimitedTo(mSocket.getOutputStream());
                    InputStream inputStream = mSocket.getInputStream();
                    Log.w(TAG, "mSocket 登录成功，解析数据。。");
                    GameLogin.LoginMsg loginMsgResp = GameLogin.LoginMsg.parseDelimitedFrom(inputStream);
                    Log.w(TAG, "mSocket 数据解析成功  " + loginMsgResp.toString());
                    GameLogin.LoginHead head = loginMsgResp.getHead();
                    if (head.getErrCode() == 0) {
                        Message message = Message.obtain();
                        message.obj = loginMsgResp.getBody().getBody().unpack(GameLogin.LoginResponse.class);
                        message.what = 0;
                        mHander.sendMessage(message);
                    } else {
                        mHander.sendEmptyMessage(head.getErrCode());
                    }
                } catch (IOException e) {
                    //错误，mhander随便发个做当错误处理
                    mHander.sendEmptyMessage(10);
//                    e.printStackTrace();
                }
//                } else {
////                    initSocket();
//                }
            }
        });
    }

    public static Intent createActivit(Context context, GameLogin.LoginResponse response) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_RESP_LOGIN, response);
        return intent;
    }

    public static Intent createLoginResReceiver(GameLogin.LoginResResponse resResponse) {
        Intent intent = new Intent(BROADCAST_ACTION_RESREQ_RESULT);
        intent.putExtra(EXTRA_RESP_RES_LOGIN, resResponse);
        return intent;
    }

    @Override
    public void onTabSelect(int index) {

    }

    public JPTabBar getTabbar() {
        return mainTabbar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiverRes);
    }

    /**
     * socket消息处理类
     */
    @SuppressLint("HandlerLeak")
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj instanceof GameLogin.LoginResponse) {
                        GameLogin.LoginResponse response = (GameLogin.LoginResponse) msg.obj;
                        AppCommonInfo.setToken(response.getToken());
                        AppCommonInfo.setUserid(response.getUserId());
                        releaseSocket(mSocket);

                        startService(LoginService.createService(MainActivity.this, response.getUserId(),
                                response.getKey(), response.getIp(), Integer.parseInt(response.getPort())));
                    }
                    break;
                case 1://token 失效
                    dimissDialog();
                    loginOut(R.string.login_token_invalid);
                    break;
                case 2://用户名密码错误
                    break;
                case 3://系统异常
                default:
                    dimissDialog();
                    loginOut(R.string.login_system_erro);
                    break;
            }
        }
    };

    /**
     * 注销
     */
    private void loginOut(@StringRes final int infoResId) {
        releaseSocket(mSocket);
        ToastUtils.showLong(infoResId);
        AppCommonInfo.loginOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * 隐藏加载框
     */
    private void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 释放socket
     *
     * @param socket
     */
    private void releaseSocket(Socket socket) {
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


    /**
     * 资源信息的广播接收器
     */
    private class ResReqBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dimissDialog();
            Serializable serializable = intent.getSerializableExtra(EXTRA_RESP_RES_LOGIN);
            if (serializable instanceof GameLogin.LoginResResponse) {
                GameLogin.LoginResResponse resResponse = (GameLogin.LoginResResponse) serializable;
                AppCommonInfo.setUserGender(resResponse.getGender());
                AppCommonInfo.setUserNick(resResponse.getNick());
                AppCommonInfo.setUserPortrait(resResponse.getPortrait());
                AppCommonInfo.setUserDiamonds(resResponse.getDiamonds());
                Log.w(TAG, "接收RES登录返回成功: resResponse = " + resResponse.toString());
            } else {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                MainActivity.this.stopService(new Intent(MainActivity.this, LoginService.class));
                loginOut(R.string.login_forced_exit);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("确认退出？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        AppCommonInfo.clear();
                        MainActivity.this.stopService(new Intent(MainActivity.this, LoginService.class));
                        MainActivity.super.onBackPressed();
                    }
                }).show();
    }
}
