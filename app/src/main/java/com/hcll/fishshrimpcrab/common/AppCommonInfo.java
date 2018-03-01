package com.hcll.fishshrimpcrab.common;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;

/**
 * Created by hong on 2018/2/26.
 */

public class AppCommonInfo {

    private static final String KEY_SP_TOKEN = "token";
    private static final String KEY_SP_USERID = "userid";
    //-------------配置相关
    /**
     * 图片压缩到的最大大小，单位B
     */
    public static int maxSize = 800 * 100;
    /**
     * 图片长或宽不超过的最大像素,单位px
     */
    public static int maxPixel = 320;

    /**
     * 图片下载路径
     */
    public static String ImageDownLoadPath = "http://static.wu2dou.com:8600/wudoufile/img/";

    /**
     * 登录host
     */
    public static final String socket_host = "www.wu2dou.com";

    /**
     * 登录port
     */
    public static final int socket_port = 8500;

    /**
     * 心跳包间隔时间
     */
    public static int heartTime = 10 * 1000;

    /**
     * 自动重连等待时间
     */
    public static int reconnectTime = 20;
    //-------------配置相关

    //-------用户信息
    /**
     * Userid登陆后保存
     */
    public static int userid = 0;

    /**
     * token 登录成功后保存
     */
    public static String token = "";
    /**
     * 手机号 注册后保存
     */
    public static String phone = "";

    /**
     * 密码 注册后保存
     */
    public static String password = "";


    public static void setToken(String token) {
        AppCommonInfo.token = token;
        SPUtils.getInstance().put(KEY_SP_TOKEN, token);
    }

    public static void setUserid(int userid) {
        AppCommonInfo.userid = userid;
        SPUtils.getInstance().put(KEY_SP_USERID, userid);
    }

    public static int getUserid() {
        if (userid == 0) {
            userid = SPUtils.getInstance().getInt(KEY_SP_USERID, 0);
        }

        return userid;
    }

    public static String getToken() {
        if (StringUtils.isEmpty(token)) {
            token = SPUtils.getInstance().getString(KEY_SP_TOKEN);
        }
        return token;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        AppCommonInfo.phone = phone;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AppCommonInfo.password = password;
    }
}
