package com.bestarmedia.libcommon.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.R;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.ErrorException;
import com.bestarmedia.libcommon.util.LogRecorder;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by J Wong on 2018/6/21 15:08.
 * 网络请求,V4.0
 */
public class HttpRequestV4 implements BaseHttpRequestListener {

    private final static int REQUEST_SUCCESS_CODE = 0;
    private final static String TAG = HttpRequestV4.class.getSimpleName();
    private String mMethod;
    private HttpRequestListener mHttpRequestListener;
    private Map<String, String> mParams;
    private Class<?> mConvert2Object;
    private TypeToken<?> mConvert2Token;
    private Context mContext;
    private BaseHttpRequest mBaseHttpRequest;
    private String mApiPath;

    public HttpRequestV4(Context context, String method) {
        this.mContext = context.getApplicationContext();
        this.mMethod = method;
        mBaseHttpRequest = new BaseHttpRequest();
        mBaseHttpRequest.setBaseHttpRequestListener(this);
    }

    public HttpRequestV4 setApiPath(String apiPath) {
        mApiPath = apiPath;
        return this;
    }

    @Override
    public void onRequestCompletion(String url, String body) {
        try {
            doResolveSucceed(body);
        } catch (Exception e) {
            Log.e(TAG, "onRequestCompletion :", e);
            sendFailMessage(mContext.getString(R.string.data_fromat_err));
        }
    }

    @Override
    public void onRequestFail(String url, String body) {
        try {
            Log.e(TAG, "onRequestFail url:" + url + " body:" + body);
            doResolveFail(body);
        } catch (Exception e) {
            Log.e(TAG, "onFailure :", e);
            sendFailMessage(mContext.getString(R.string.data_fromat_err));
        }
    }

    @Override
    public void onRequestError(String url, String err) {
        sendErrorMessage(mContext.getString(R.string.network_failure));
    }

    public HttpRequestV4 setHttpRequestListener(HttpRequestListener listener) {
        this.mHttpRequestListener = listener;
        return this;
    }

    public HttpRequestV4 addParam(String key, String value) {
        if (mParams == null)
            mParams = new HashMap<>();
        mParams.put(key, value);
        return this;
    }

    public HttpRequestV4 addParam(Map<String, String> params) {
        if (mParams == null)
            mParams = new HashMap<>();

        mParams.putAll(params);
        return this;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public HttpRequestV4 setConvert2Class(Class<?> clazz) {
        this.mConvert2Object = clazz;
        return this;
    }

    public HttpRequestV4 setConvert2Token(TypeToken<?> token) {
        this.mConvert2Token = token;
        return this;
    }

    public void get() {
        String url = getUrl(mMethod);
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            mBaseHttpRequest.get(url);
        } else {
            LogRecorder.addNetworkLog("网络不可用get：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }


    public void post() {
        String url = (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + mMethod + "?";
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (getParams() != null)
                mBaseHttpRequest.addParam(getParams());
            mBaseHttpRequest.post(url);
        } else {
            LogRecorder.addNetworkLog("网络不可用post：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }

    public void postJson(String json) {
        String url = (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + mMethod + "?";
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (getParams() != null)
                mBaseHttpRequest.addParam(getParams());
            mBaseHttpRequest.postJson(url, json);
        } else {
            LogRecorder.addNetworkLog("网络不可用post：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }

    public void put() {
        String url = (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + mMethod + "?";
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (getParams() != null)
                mBaseHttpRequest.addParam(getParams());
            mBaseHttpRequest.put(url);
        } else {
            LogRecorder.addNetworkLog("网络不可用put：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }

    public void putJson(String json) {
        String url = (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + mMethod + "?";
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (getParams() != null)
                mBaseHttpRequest.addParam(getParams());
            mBaseHttpRequest.putJson(url, json);
        } else {
            LogRecorder.addNetworkLog("网络不可用put：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }

    public void delete() {
        String url = (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + mMethod + "?";
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (getParams() != null)
                mBaseHttpRequest.addParam(getParams());
            mBaseHttpRequest.delete(url);
        } else {
            LogRecorder.addNetworkLog("网络不可用delete：" + url);
            if (mHttpRequestListener != null) {
                mHttpRequestListener.onError(mMethod, mContext.getString(R.string.network_failure));
            }
        }
    }


    private String getUrl(String urlMethod) {
        StringBuilder builder = new StringBuilder();
        builder.append(urlMethod).append("?");
        int i = 0;
        if (mParams != null && mParams.size() > 0) {
            Iterator iterator = mParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue() == null ? "" : entry.getValue().toString();
                if (i > 0)
                    builder.append("&");
                builder.append(key).append("=").append(val);
                i++;
            }
        }
        return (!TextUtils.isEmpty(mApiPath) ? mApiPath : OkConfig.getServerApi()) + builder.toString();
    }


    private void doResolveSucceed(String response) {
        if (response != null) {
            BaseModelV4 baseModel = convert2BaseModel(response);
            if (baseModel != null) {
                if (baseModel.code == REQUEST_SUCCESS_CODE) {
                    Object result = baseModel.data;
                    if (mConvert2Object != null) {
                        result = convert2Object(baseModel.data);
                    } else if (mConvert2Token != null) {
                        result = convert2Token(baseModel.data);
                    }
                    sendSuccessMessage(result);
                } else {
                    sendSuccessMessage(baseModel);
                }
            } else {
                sendSuccessMessage(null);
            }
        } else {
            sendSuccessMessage(null);
        }
    }

    private void doResolveFail(String response) {
        if (response != null) {
            BaseModelV4 baseModel = convert2BaseModel(response);
            ErrorException errorException;
            if (baseModel != null && baseModel.code > 0) {
                sendFailMessage(baseModel);
            } else if ((errorException = convert2ErrorException(response)) != null && !TextUtils.isEmpty(errorException.error)) {
                sendFailMessage("服务器错误：" + errorException.error + "：" + errorException.status);
            } else {
                sendFailMessage("数据错误");
            }
        } else {
            sendFailMessage("返回空数据");
        }
    }

    private BaseModelV4 convert2BaseModel(String response) {
        BaseModelV4 baseModel = null;
        try {
            Gson gson = new Gson();
            baseModel = gson.fromJson(response, BaseModelV4.class);
        } catch (Exception e) {
            Log.e(TAG, "convert2BaseModel :", e);
        }
        return baseModel;
    }

    private ErrorException convert2ErrorException(String response) {
        ErrorException errorException = null;
        try {
            Gson gson = new Gson();
            errorException = gson.fromJson(response, ErrorException.class);
        } catch (Exception e) {
            Log.e(TAG, "convert2ErrorException :", e);
        }
        return errorException;
    }

    private Object convert2Object(Object object) {
        Object obj = null;
        if (object != null) {
            try {
                Gson gson = new Gson();
                obj = gson.fromJson(object.toString(), mConvert2Object);
            } catch (Exception e) {
                Log.d(TAG, "convert2Object :", e);
            }
        }
        return obj;
    }

    private Object convert2Token(Object object) {
        Object obj = null;
        try {
            String json = object.toString();
            Gson gson = new Gson();
            obj = gson.fromJson(json, mConvert2Token.getType());
        } catch (Exception e) {
            Log.e(TAG, "convert2Token:" + e);
        }
        return obj;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mHttpRequestListener != null && msg.obj != null) {
                        Log.e(TAG, "mHandler onFailed >>> " + msg.obj.toString());
                        mHttpRequestListener.onFailed(mMethod, msg.obj == null ? "" : msg.obj);
                    }
                    break;
                case 2:
                    if (mHttpRequestListener != null) {
                        Log.d(TAG, "mHandler onSuccess >>> ");
                        mHttpRequestListener.onSuccess(mMethod, msg.obj);
                    }
                    break;
                case 3:
                    if (mHttpRequestListener != null && msg.obj != null) {
                        Log.d(TAG, "mHandler onError >>> " + msg.obj.toString());
                        mHttpRequestListener.onError(mMethod, msg.obj == null ? "" : msg.obj.toString());
                    }
                    break;
            }
        }
    };


    private void sendFailMessage(Object obj) {
        Message message = new Message();
        message.what = 1;
        message.obj = obj;
        mHandler.sendMessage(message);
    }


    private void sendSuccessMessage(Object obj) {
        Message message = new Message();
        message.what = 2;
        message.obj = obj;
        mHandler.sendMessage(message);
    }

    private void sendErrorMessage(String msg) {
        Message message = new Message();
        message.what = 3;
        message.obj = msg;
        mHandler.sendMessage(message);
    }
}
