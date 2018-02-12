package com.hcll.fishshrimpcrab;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

/**
 * Created by hong on 2018/1/30.
 */

public class FSCApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
