package com.hcll.fishshrimpcrab.login.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hcll.fishshrimpcrab.common.Constant;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by hong on 2018/2/28.
 */

public class LoginService extends Service {

    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_PASSWORD = "password";

    private Socket socket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket = new Socket(Constant.socket_host, Constant.socket_port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return Service.START_REDELIVER_INTENT;
    }

    public static Intent createService(Context context, String phone, String psw) {
        Intent intent = new Intent(context, LoginService.class);
        intent.putExtra(EXTRA_PHONE, phone);
        intent.putExtra(EXTRA_PASSWORD, psw);
        return intent;
    }
}
