package com.bestarmedia.libcommon.security;

/**
 * Created by J Wong on 2017/11/20.
 */

public interface StoreKeyHandlerListener {

    void onKeyAuthSuccess();

    void onKeyAuthTip(String tip);

    void onKeyAuthFail(String error);
}
