package com.hcll.fishshrimpcrab.common.http.interceptor;

import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * HTTP日志拦截器
 * Created by hWX201806 on 2017/12/1.
 */
public class HttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * 日志记录级别
     */
    public enum Level {
        /**
         * 只记录基本信息
         */
        BASIC,
        /**
         * 记录所有信息
         */
        FULL
    }

    private volatile Level level = Level.FULL;
    private String tag = HttpLoggingInterceptor.class.getName();

    public HttpLoggingInterceptor() {
    }

    public HttpLoggingInterceptor(String tag) {
        if (tag == null) throw new NullPointerException("参数tag不能为空.");
        this.tag = tag;
    }

    public HttpLoggingInterceptor(Level level) {
        if (level == null) throw new NullPointerException("参数level不能为空.");
        this.level = level;
    }

    public HttpLoggingInterceptor(Level level, String tag) {
        if (level == null) throw new NullPointerException("参数level不能为空.");
        if (tag == null) throw new NullPointerException("参数tag不能为空.");
        this.level = level;
        this.tag = tag;
    }

    /**
     * 设置日志记录级别
     *
     * @param level
     * @return
     */
    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("参数level不能为空.");
        this.level = level;
        return this;
    }

    /**
     * 设置TAG
     *
     * @param tag
     * @return
     */
    public HttpLoggingInterceptor setTag(String tag) {
        if (tag == null) throw new NullPointerException("参数tag不能为空.");
        this.tag = tag;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Connection connection = chain.connection();
        logRequest(request, connection);

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Log.e(tag, "HTTP请求出错", e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        logResponse(response, tookMs);

        return response;
    }

    /**
     * 记录请求日志
     *
     * @param request
     * @param connection
     * @throws IOException
     */
    private void logRequest(Request request, Connection connection) throws IOException {
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Log.w(tag, "Request URL:" + request.url());
        Log.w(tag, "Request Time:" + TimeUtils.date2String(new Date()));
        Log.w(tag, "Request Method:" + request.method());
        Log.w(tag, "Protocol:" + (connection != null ? " " + connection.protocol() : ""));
        Log.w(tag, "Content-Length:" + (hasRequestBody ? " " + requestBody.contentLength() : "0"));
        Log.w(tag, "Content-Type:" + (hasRequestBody ? requestBody.contentType() : ""));

        if (Level.FULL == level) {
            logRequestHeaders(request.headers());
            if (hasRequestBody) {
                logRequestBody(requestBody);
            }
        }
    }

    /**
     * 记录请求头
     *
     * @param headers
     */
    private void logRequestHeaders(Headers headers) throws IOException {
        Log.w(tag, "Request Headers");
        for (int i = 0, count = headers.size(); i < count; i++) {
            Log.w(tag, "   " + headers.name(i) + ": " + headers.value(i));
        }
    }

    /**
     * 记录请求体
     *
     * @param requestBody
     * @throws IOException
     */
    private void logRequestBody(RequestBody requestBody) throws IOException {
        Log.w(tag, "Request Body");
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (null != contentType) {
            charset = contentType.charset(UTF8);
        }
        if (isPlaintext(buffer)) {
            Log.w(tag, buffer.readString(charset));
        }
    }

    /**
     * 记录响应日志
     *
     * @param response
     * @param time
     */
    private void logResponse(Response response, long time) throws IOException {
        // 这里先克隆一下，不影响后面
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();

        Log.w(tag, "Response");
        Log.w(tag, "Response Status:" + clone.code() + " " + (clone.message().isEmpty() ? "" : clone.message()));
        Log.w(tag, "Response Time:" + TimeUtils.date2String(new Date()));
        Log.w(tag, "请求所发时间:" + time + "ms");

        if (Level.FULL == level) {
            logResposneHeaders(clone);
            logResponseBody(responseBody);
        }
    }

    /**
     * 记录响应头
     *
     * @param response
     */
    private void logResposneHeaders(Response response) throws IOException {
        Log.w(tag, "Response Headers");
        Headers headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            Log.w(tag, "   " + headers.name(i) + ": " + headers.value(i));
        }
    }

    /**
     * 记录响应体
     *
     * @param responseBody
     * @throws IOException
     */
    private void logResponseBody(ResponseBody responseBody) throws IOException {
        Log.w(tag, "Response Body");
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        long contentLength = responseBody.contentLength();
        if (isPlaintext(buffer) && contentLength != 0) {
            Log.w(tag, buffer.clone().readString(charset));
        }
    }


    /**
     * 判断内容是否可读
     *
     * @param buffer
     * @return
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }


}
