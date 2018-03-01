package com.hcll.fishshrimpcrab.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.Any;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.Record.fragment.MainRecordFragment;
import com.hcll.fishshrimpcrab.base.BaseAtivity;
import com.hcll.fishshrimpcrab.club.fragment.MainClubFragment;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.widget.NoScrollViewPager;
import com.hcll.fishshrimpcrab.game.fragment.MainGameFragment;
import com.hcll.fishshrimpcrab.login.MD5Utils;
import com.hcll.fishshrimpcrab.login.activity.LoginActivity;
import com.hcll.fishshrimpcrab.me.fragment.MainMeFragment;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.animate.AnimationType;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

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

import static com.hcll.fishshrimpcrab.login.activity.PerfectInfoActivity.EXTRA_USER_ID;

public class MainActivity extends BaseAtivity implements OnTabSelectListener {

    public final static String BROADCAST_ACTION_RESREQ_RESULT = "wu2dou.braodcast.action.resreq.result";

    public static final String EXTRA_RESP_LOGIN = "login_resp";
    public static final String EXTRA_GENDER = "gender";
    public static final String EXTRA_NICK = "nick";
    public static final String EXTRA_PORTRAIT = "portrait";
    public static final String EXTRA_DIAMONDS = "diamonds";

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

    /**
     * 本地广播管理
     */
    private LocalBroadcastManager mLocalBroadcastManager;
    private ResReqBroadcastReceiver broadcastReceiverRes;
    private GameLogin.LoginResponse loginResponse;
    private Dialog dialog;
    private Socket socket;

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

        dialog = DialogUtils.createProgressDialog(this, null);
        dialog.show();
    }


    private void initBroadcast() {

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastReceiverRes = new ResReqBroadcastReceiver();
        mLocalBroadcastManager.registerReceiver(broadcastReceiverRes, new IntentFilter(BROADCAST_ACTION_RESREQ_RESULT));
    }

    private void initData() {

        if (loginResponse == null) {
            try {
                InetAddress inetAddress = InetAddress.getByName(AppCommonInfo.socket_host);
                socket = new Socket(inetAddress, AppCommonInfo.socket_port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestLoginInfo();
        } else {

        }
    }

    private void requestLoginInfo() {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (socket != null && socket.isConnected()) {
                    try {
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
                        } else {
                            loginReq = GameLogin.LoginReq.newBuilder()
                                    .setImsi(Constant.IMEI)
                                    .setToken(AppCommonInfo.getToken())
                                    .setDeviceType(2)
                                    .setUserType(2).build();
                        }
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
                            mHander.sendMessage(message);
                        } else {
                            mHander.sendEmptyMessage(head.getErrCode());
                        }
                    } catch (IOException e) {
                        mHander.sendEmptyMessage(4);
//                    e.printStackTrace();
                    }
                } else {
                    mHander.sendEmptyMessage(4);
                }
            }
        });
    }

    public static Intent createActivit(Context context, GameLogin.LoginResponse response) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_RESP_LOGIN, response);
        return intent;
    }

    public static Intent createLoginResReceiver(int gender, String nick, String portrait, int diamonds) {
        Intent intent = new Intent(BROADCAST_ACTION_RESREQ_RESULT);
        intent.putExtra(EXTRA_GENDER, gender);
        intent.putExtra(EXTRA_NICK, nick);
        intent.putExtra(EXTRA_PORTRAIT, portrait);
        intent.putExtra(EXTRA_DIAMONDS, diamonds);
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
        mLocalBroadcastManager.unregisterReceiver(broadcastReceiverRes);
        super.onDestroy();
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:

                    break;
                case 1://token 失效
                    ToastUtils.showLong(R.string.login_token_invalid);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    break;
                case 2://用户名密码错误
                    break;
                case 3://系统异常
                default:
                    ToastUtils.showLong(R.string.login_system_erro);
                    break;
            }
        }
    };

    /**
     * 资源信息的广播接收器
     */
    private class ResReqBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

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

}
