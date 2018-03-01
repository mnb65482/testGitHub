package com.hcll.fishshrimpcrab.login.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.protobuf.Any;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

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
    private boolean _connect;
    private ReceiveThread mReceiveThread;
    private boolean receiveStop;
    private Date lastKeepAliveOkTime;
    private String resIp;
    private int resPort;
    private int userid;
    private long sendTime;

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
        String key = intent.getStringExtra(EXTRA_KEY);
        resIp = intent.getStringExtra(EXTRA_IP);
        resPort = intent.getIntExtra(EXTRA_PORT, 0);
        connect();

        GameLogin.LoginResReq loginResReq = GameLogin.LoginResReq.newBuilder().setKey(key).setUserId(userid).build();
        Any any = Any.pack(loginResReq);
        GameLogin.LoginBody body = GameLogin.LoginBody.newBuilder().setBody(any).build();
        GameLogin.LoginHead head = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.RES).build();
        GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(body).setHead(head).build();
        sendmessage(loginMsg);


//        GameLogin.LoginReq loginReq = GameLogin.LoginReq.newBuilder().setPhoneNum(phone).setPassword(psw).build();
//        Any any = Any.pack(loginReq);
//        GameLogin.LoginBody loginBody = GameLogin.LoginBody.newBuilder().setBody(any).build();
//        GameLogin.LoginHead loginHead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.LOGIN).build();
//        GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(loginBody).setHead(loginHead).build();


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

    public void connect() {
        Log.w(TAG, "准备链接...");
        try {
            socket = new Socket(resIp, resPort);
            socket.setSoTimeout(AppCommonInfo.reconnectTime);
            mReceiveThread = new ReceiveThread();
            mReceiveThread.start();
            Log.w(TAG, "链接成功.");

        } catch (Exception e) {
            Log.e(TAG, "链接出错.");
            e.printStackTrace();
        }
    }


    public void KeepAlive() {
//        // 判断socket是否已断开,断开就重连
//        if (lastKeepAliveOkTime != null) {
//            Log.w(TAG, "上次心跳成功时间:" + TimeUtils.date2String(lastKeepAliveOkTime));
//            Date now = Calendar.getInstance().getTime();
//            long between = (now.getTime() - lastKeepAliveOkTime.getTime());// 得到两者的毫秒数
//            if (between > 60 * 1000) {
//                Log.e(TAG, "心跳异常超过1分钟,重新连接:");
//                lastKeepAliveOkTime = null;
//                socket = null;
//            }
//
//        } else {
//            lastKeepAliveOkTime = Calendar.getInstance().getTime();
//        }
        lastKeepAliveOkTime = Calendar.getInstance().getTime();

        if (!checkIsAlive()) {
            Log.e(TAG, "链接已断开,重新连接.");
            connect();
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

    //然后发送数据的方法
    public void sendmessage(GameLogin.LoginMsg msg) {
//        if (!checkIsAlive())
//            return;
        Log.w(TAG, "准备发送消息:" + msg.toString());
        try {
            if (socket != null && socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    msg.writeTo(socket.getOutputStream());
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
            while (true) {
                try {
                    if (sendTime != 0 && System.currentTimeMillis() - sendTime > AppCommonInfo.reconnectTime) {
                        Log.e(TAG, "心跳包挂了:重启中。 ");
                        releaseLastSocket();
                        connect();
                        break;
                    }

                    if (socket != null && socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            InputStream inputStream = socket.getInputStream();
                            GameLogin.LoginMsg loginMsgResp = GameLogin.LoginMsg.parseFrom(inputStream);
                            GameLogin.LoginHead head = loginMsgResp.getHead();

                            if (head.getErrCode() == 0) {
                                switch (head.getCmdId().getNumber()) {
                                    //res登录返回
                                    case GameLogin.LoginCmd.RES_VALUE:
                                        GameLogin.LoginBody body = loginMsgResp.getBody();
                                        GameLogin.LoginResResponse resResponse = body.getBody().unpack(GameLogin.LoginResResponse.class);
                                        sendBroadcast(MainActivity.createLoginResReceiver(resResponse.getGender(),
                                                resResponse.getNick(), resResponse.getPortrait(), resResponse.getDiamonds()));

                                        sendHear();
                                        break;
                                    //接收到心跳包
                                    case GameLogin.LoginCmd.RES_HEARTBEAT_VALUE:
                                        Thread.sleep(AppCommonInfo.heartTime);
                                        sendHear();
                                        break;
                                    //被踢出,发送广播返回到登录
                                    case GameLogin.LoginCmd.RES_LOGOUT_VALUE:
                                        sendBroadcast(MainActivity.createLoginResReceiver(0, null, null, 0));
                                        break;
                                }
                            }
                        }
                    } else {
                        if (socket != null && !socket.isConnected()) {
                            Log.w(TAG, "链接状态:" + socket.isConnected());
                        }
                        releaseLastSocket();
                        connect();
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
        GameLogin.ResHeartBeatReq heartBeatReq = GameLogin.ResHeartBeatReq.newBuilder().setUserId(userid).build();
        GameLogin.ResHeartBeatReq.newBuilder().setUserId(userid).build();
        Any any = Any.pack(heartBeatReq);
        GameLogin.LoginBody hearbody = GameLogin.LoginBody.newBuilder().setBody(any).build();
        GameLogin.LoginHead hearhead = GameLogin.LoginHead.newBuilder().setCmdId(GameLogin.LoginCmd.RES_HEARTBEAT).build();
        GameLogin.LoginMsg loginMsg = GameLogin.LoginMsg.newBuilder().setBody(hearbody).setHead(hearhead).build();
        sendmessage(loginMsg);
    }

    private void releaseLastSocket() {
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
