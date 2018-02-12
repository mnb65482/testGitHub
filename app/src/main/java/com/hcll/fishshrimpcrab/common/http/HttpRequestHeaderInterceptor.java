package com.hcll.fishshrimpcrab.common.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统一添加请求头
 * Created by hong on 2018/1/11.
 */
public class HttpRequestHeaderInterceptor implements Interceptor {

    private Map<String, String> headers = new HashMap<>();

    public HttpRequestHeaderInterceptor() {

    }

    /**
     * 添加请求头
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder rb = chain.request().newBuilder();
        if(null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                rb.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return chain.proceed(rb.build());
    }
}
