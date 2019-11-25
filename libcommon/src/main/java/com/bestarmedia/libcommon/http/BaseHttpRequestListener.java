package com.bestarmedia.libcommon.http;

/**
 * Created by J Wong on 2017/8/24.
 */

public interface BaseHttpRequestListener {
    void onRequestCompletion(String url, String body);

    void onRequestFail(String url, String err);

    void onRequestError(String url,String err);
}
