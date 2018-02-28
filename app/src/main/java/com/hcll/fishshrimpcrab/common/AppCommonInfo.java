package com.hcll.fishshrimpcrab.common;

/**
 * Created by hong on 2018/2/26.
 */

public class AppCommonInfo {

    /**
     * Userid登陆后保存
     */
    //192937 测试用
    public static int userid = 192937;

    /**
     * token 登录成功后保存
     */
    public static String token = "";

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

}
