package com.hcll.fishshrimpcrab.login.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;
import com.google.protobuf.Any;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executors;

import LoginProto.GameLogin;

/**
 * Created by hong on 2018/2/28.
 */

public class LoginService extends Service {

    public static final String TAG = LoginService.class.getSimpleName();

    public static final String EXTRA_USERID = "userid";
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_IP = "IP";
    public static final String EXTRA_PORT = "port";

    private Socket socket;
    /**
     * 是否连接
     */
    private boolean _connect = true;
    private ReceiveThread mReceiveThread;
    private String resIp;
    private int resPort;
    private int userid;
    private long sendTime;
    private String key;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userid = intent.getIntExtra(EXTRA_USERID, 0);
        key = intent.getStringExtra(EXTRA_KEY);
        resIp = intent.getStringExtra(EXTRA_IP);
        resPort = intent.getIntExtra(EXTRA_PORT, 0);
        _connect = true;
        mReceiveThread = new ReceiveThread();
        mReceiveThread.start();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                initSocket();
                GameLogin.LoginResReq loginResReq = GameLogin.LoginResReq.newBuilder().setKey(key).setUserId(userid).build();
                Any any = Any.pack(loginResReq);
                GameLogin.LoginBody body = GameLogin.LoginBody.newBuilder().setBody(any).build();
                GameLogin.LoginHead head = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.RES).build();
                GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(body).setHead(head).build();
                sendmessage(loginMsg);
            }
        });
        return Service.START_REDELIVER_INTENT;
    }

    public static Intent createService(Context context, int userid, String key, String ip, int port) {
        Intent intent = new Intent(context, LoginService.class);
        intent.putExtra(EXTRA_USERID, userid);
        intent.putExtra(EXTRA_KEY, key);
        intent.putExtra(EXTRA_IP, ip);
        intent.putExtra(EXTRA_PORT, port);
        return intent;
    }

    public void initSocket() {
        Log.w(TAG, "准备链接...");
        try {
            socket = new Socket(resIp, resPort);
            Log.w(TAG, "链接成功.");

        } catch (Exception e) {
            Log.e(TAG, "链接出错.");
            e.printStackTrace();
        }
    }

    //此方法是检测是否连接
    public boolean checkIsAlive() {
        if (socket == null)
            return false;
        try {
            socket.sendUrgentData(0xFF);
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    //发送数据的方法
    public void sendmessage(GameLogin.LoginMsg msg) {

        Log.w(TAG, "准备发送消息:" + msg.toString());
        try {
            if (socket != null && socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    msg.writeDelimitedTo(socket.getOutputStream());
                }
            }
            Log.w(TAG, "发送成功!");
        } catch (Exception e) {
            Log.e(TAG, "消息发送失败");
            e.printStackTrace();
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            while (_connect) {
                try {
                    if (sendTime != 0 && System.currentTimeMillis() - sendTime > AppCommonInfo.reconnectTime) {
                        Log.e(TAG, "心跳包挂了:重启中。 ");
                        releaseSocket();
                        initSocket();
                        sendTime = 0;
                        sendHear();
                        break;
                    }

                    if (socket != null && socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            Log.w(TAG, "获得输入流。");
                            InputStream inputStream = socket.getInputStream();
                            Log.w(TAG, "解析输入流。");
                            GameLogin.LoginMsg loginMsgResp = GameLogin.LoginMsg.parseDelimitedFrom(inputStream);
                            GameLogin.LoginHead head = loginMsgResp.getHead();
                            Log.w(TAG, "解析输入流成功。");

                            if (head.getErrCode() == 0) {
                                switch (head.getCmdId().getNumber()) {
                                    //res登录返回
                                    case GameLogin.LoginCmd.RES_VALUE:
                                        Log.w(TAG, "res登录返回成功 ");
                                        GameLogin.LoginBody body = loginMsgResp.getBody();
                                        GameLogin.LoginResResponse resResponse = body.getBody().unpack(GameLogin.LoginResResponse.class);
                                        sendBroadcast(MainActivity.createLoginResReceiver(resResponse));
                                        sendHear();
                                        break;
                                    //接收到心跳包
                                    case GameLogin.LoginCmd.RES_HEARTBEAT_VALUE:
                                        Log.w(TAG, "心跳包接收成功 ");
                                        Thread.sleep(AppCommonInfo.heartTime);
                                        sendHear();
                                        break;
                                    //被踢出,发送广播返回到登录
                                    case GameLogin.LoginCmd.RES_LOGOUT_VALUE:
                                        Log.w(TAG, "用户被踢出！ ");
                                        sendBroadcast(MainActivity.createLoginResReceiver(null));
                                        break;
                                }
                            }
                        }
                    } else {
                        if (socket != null && !socket.isConnected()) {
                            Log.w(TAG, "链接状态:" + socket.isConnected());
                        }
//                        releaseSocket();
//                        initSocket();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "监听出错:" + e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送心跳包
     */
    private void sendHear() {

        sendTime = System.currentTimeMillis();
        Log.w(TAG, "sendHear: sendTime = " + TimeUtils.date2String(new Date(sendTime)));

        GameLogin.ResHeartBeatReq heartBeatReq = GameLogin.ResHeartBeatReq.newBuilder().setUserId(userid).build();
        GameLogin.ResHeartBeatReq.newBuilder().setUserId(userid).build();
        Any any = Any.pack(heartBeatReq);
        GameLogin.LoginBody hearbody = GameLogin.LoginBody.newBuilder().setBody(any).build();
        GameLogin.LoginHead hearhead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.RES_HEARTBEAT).build();
        GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(hearbody).setHead(hearhead).build();
        sendmessage(loginMsg);
    }

    /**
     * 释放socket
     */
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

    @Override
    public void onDestroy() {
        _connect = false;
        releaseSocket();
        super.onDestroy();
    }
}
