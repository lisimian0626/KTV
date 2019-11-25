package com.bestarmedia.libcommon.http;


import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by J Wong on 2017/5/15.
 */

public class BaseHttpRequest implements BaseHttpRequestListener {

    private final static String TAG = BaseHttpRequest.class.getSimpleName();
    private BaseHttpRequestListener mListener;
    private Map<String, String> mParams;
    String mUrl;
    private static String mToken;

    public static void setToken(String token) {
        mToken = token;
    }

    public static String getToken() {
        return mToken;
    }

    public void setBaseHttpRequestListener(BaseHttpRequestListener listener) {
        mListener = listener;
    }

    public BaseHttpRequest addParam(String key, String value) {
        if (mParams == null)
            mParams = new HashMap<>();
        mParams.put(key, value);
        return this;
    }

    public BaseHttpRequest addParam(Map<String, String> params) {
        if (mParams == null)
            mParams = new HashMap<>();
        if (params != null)
            mParams.putAll(params);
        return this;
    }

    private RequestBody getRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (mParams != null && mParams.size() > 0) {
            Iterator iterator = mParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue() == null ? "" : entry.getValue().toString();
                Log.d(TAG, "getRequestBody key:" + key + "  val:" + val);
                builder.add(key, val);
            }
        }
        return builder.build();
    }

    public void delete(String url) {
        try {
            Log.d(TAG, "delete :" + url);
            Request request = createBuilder(url).url(url).delete(getRequestBody()).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "delete出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }


    public void put(String url) {
        try {
            Log.d(TAG, "put :" + url);
            Request request = createBuilder(url).url(url).put(getRequestBody()).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "put出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }

    public void post(String url) {
        try {
            Log.d(TAG, "post :" + url);
            Request request = createBuilder(url).url(url).post(getRequestBody()).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "post出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }

    public void postJson(String url, String json) {
        try {
            Log.d(TAG, "postJson :" + url + " json:" + json);
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = createBuilder(url).url(url).post(requestBody).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "postJson出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }

    public void putJson(String url, String json) {
        try {
            Log.d(TAG, "postJson :" + url + " json:" + json);
            //MediaType  设置Content-Type 标头中包含的媒体类型值
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = createBuilder(url).url(url).put(requestBody).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "putJson出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }


    public void get(String url) {
        try {
            url = url.replace("|", "%7C");
            Log.d(TAG, "get :" + url);
            Request request = createBuilder(url).url(url).build();
            enqueueRequest(url, request);
        } catch (Exception e) {
            Log.e(TAG, "get出错了", e);
            onRequestError(mUrl, parseErrorMsg(e));
        }
    }

    private Request.Builder createBuilder(String url) {
        Request.Builder builder = new Request.Builder();
        if (!TextUtils.isEmpty(getToken()) && !url.contains("eshop")) {
            builder.addHeader("jwt", getToken());
        }
        return builder;
    }

    void enqueueRequest(String url, final Request request) {
        mUrl = url;
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure:" + e.toString());
                onRequestError(mUrl, parseErrorMsg(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    String respBody = body.string();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse Url:" + mUrl + "\nbody :" + respBody);
                        onRequestCompletion(mUrl, respBody);
                    } else {
                        onRequestFail(mUrl, respBody);
                    }
                    body.close();
                } catch (Exception e) {
                    Log.e(TAG, "onResponse", e);
                }
            }
        });
    }


    @Override
    public void onRequestCompletion(String url, String body) {
        if (mListener != null) {
            mListener.onRequestCompletion(url, body);
        }
    }

    @Override
    public void onRequestFail(String url, String body) {
        Log.e(TAG, "onRequestFail Url:" + mUrl + "\nbody :" + body);
        if (mListener != null) {
            mListener.onRequestFail(url, body);
        }
    }

    @Override
    public void onRequestError(String url, String err) {
        Log.e(TAG, "onRequestFail Url:" + mUrl + " err :" + err);
        if (mListener != null) {
            mListener.onRequestError(url, err);
        }
    }

    private String parseErrorMsg(Exception e) {
        String error = "网络错误！";
        if (e instanceof SocketTimeoutException) {
            error = "网络超时！";
        }
        return error;
    }
}
