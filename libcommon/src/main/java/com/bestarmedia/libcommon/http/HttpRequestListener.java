package com.bestarmedia.libcommon.http;

/**
 * Created by J Wong on 2015/10/9 17:59.
 */
public interface HttpRequestListener {

    void onStart(String method);

    void onSuccess(String method, Object object);

    void onFailed(String method, Object object);

    void onError(String method, String error);
}
