package com.bestarmedia.libcommon.http;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by J Wong on 2015/10/28 10:05.
 */
public class OkHttpUtil {

    //    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();
//    static {
//        mOkHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
//        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
//        mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
//    }

    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });
    }

    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 取消网络请求
     */
    public static void cancelAll() {
        mOkHttpClient.dispatcher().cancelAll();
    }

}