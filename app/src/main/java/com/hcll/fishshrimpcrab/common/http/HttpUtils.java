package com.hcll.fishshrimpcrab.common.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.hcll.fishshrimpcrab.common.http.interceptor.HttpLoggingInterceptor;
import com.hcll.fishshrimpcrab.common.http.ssl.HostNameVerifier;
import com.hcll.fishshrimpcrab.common.http.ssl.HttpSSLContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by hWX201806 on 2018/1/10.
 */
public class HttpUtils {

    private static String BaseUrl = "http://www.wu2dou.com:8080/api/";

    /**
     * 设置基本的URL
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
    }

    /**
     * 创建Retrofit
     *
     * @param context
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createRetrofit(Context context, final Class<T> service) {
        return createRetrofit(context, BaseUrl, service);
    }

    public static <T> T createRetrofit(Context context, final Class<T> service, String projectCode, String token) {
        return createRetrofit(context, BaseUrl, service, projectCode, token);
    }

    /**
     * 创建Retrofit
     *
     * @param context
     * @param baseUrl
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createRetrofit(Context context, String baseUrl, final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(HttpUtils.createProjectClientBuilder(context).build())
                // .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }

    public static <T> T createRetrofit(Context context, String baseUrl, final Class<T> service, String projectCode, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(HttpUtils.createProjectClientBuilder(context, projectCode, token).build())
                // .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }


    /**
     * 创建项目的OkHttpClient.Builder
     *
     * @param context
     * @return
     */
    public static OkHttpClient.Builder createProjectClientBuilder(Context context) {
        return createClientBuilder(context, 0, false, "");
    }

    public static OkHttpClient.Builder createProjectClientBuilder(Context context, String projectCode, String token) {
        OkHttpClient.Builder clientBuilder = createClientBuilder(context, 0, false, "");
        HttpRequestHeaderInterceptor headerInterceptor = new HttpRequestHeaderInterceptor();
        headerInterceptor.addHeader("md5at", projectCode);
        headerInterceptor.addHeader("did", token);
        clientBuilder.interceptors().add(0, headerInterceptor);
        return clientBuilder;
    }

    /**
     * 创建OkHttpClient.Builder
     *
     * @param context
     * @return
     */
    public static OkHttpClient.Builder createClientBuilder(Context context) {
        return createClientBuilder(context, 0, false, "");
    }

    /**
     * 创建OkHttpClient.Builder
     *
     * @return
     */
    public static OkHttpClient.Builder createClientBuilder(Context context, int timeout, boolean sslFake, String sslCerFilePath) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (timeout > 0) {
            // 设置请求超时
            clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
            // 设置响应超时
            clientBuilder.readTimeout(10, TimeUnit.SECONDS);
            clientBuilder.writeTimeout(20, TimeUnit.SECONDS);
            Log.d(TAG, "登录超时时间:" + String.valueOf(20));
        }
        if (sslFake) {
            clientBuilder.sslSocketFactory(HttpSSLContext.getFakeSocketFactory());
            clientBuilder.hostnameVerifier(new HostNameVerifier());
        }
        if (!StringUtils.isEmpty(sslCerFilePath)) {
            clientBuilder.sslSocketFactory(HttpSSLContext.getSingleSocketFactoryByAssets(context, sslCerFilePath));
            clientBuilder.hostnameVerifier(new HostNameVerifier());
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        clientBuilder.addInterceptor(logging);
        return clientBuilder;
    }

}
