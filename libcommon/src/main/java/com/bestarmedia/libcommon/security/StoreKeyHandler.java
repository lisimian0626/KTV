package com.bestarmedia.libcommon.security;

import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.model.store.AuthKeystore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by J Wong on 2017/11/20.
 */

public class StoreKeyHandler {

    private String mKey;
    private String mKtvNetCode;
    private StoreKeyHandlerListener mListener;
    private long mServerTime;

    //密钥更新时间宽限为3天
    private final static long AUTH_TIME = 3 * 24 * 60 * 60;

    //超过1天没更新密钥提示更新
    private final static long AUTH_TIME_TIP = 1 * 24 * 60 * 60;

    //密钥更新时间宽限为3天
//    private final static long AUTH_TIME = 1 * 18 * 60 * 60;
    //超过1天没更新密钥提示更新
//    private final static long AUTH_TIME_TIP = 1 * 2 * 60 * 60;

    public StoreKeyHandler(String ktvNetCode, String key, long serverTimeSec, StoreKeyHandlerListener listener) {
        mKtvNetCode = ktvNetCode;
        mKey = key;
        mServerTime = serverTimeSec;
        mListener = listener;
    }

    public void desKey() {
        Log.d(getClass().getSimpleName(), "desKey Key:" + mKey);
        String authCodeDecode;
        AuthKeystore authKeystore;
        if (TextUtils.isEmpty(mKey)) {
            mListener.onKeyAuthFail("密钥为空！");
            return;
        }

        if (TextUtils.isEmpty(mKtvNetCode)) {
            mListener.onKeyAuthFail("店家编号为空！");
            return;
        }

        if (TextUtils.isEmpty(authCodeDecode = DesKeystore.authcodeDecode(mKey, mKtvNetCode + "QPSzNe8L56jEGmVB"))) {
            mListener.onKeyAuthFail("密钥无法解密！");
            return;
        }
        Log.d(getClass().getSimpleName(), "密钥解密: " + authCodeDecode);
        if ((authKeystore = DesKeystore.covert(authCodeDecode)) == null) {
            mListener.onKeyAuthFail("密钥无法解析！");
            return;
        }

        Log.d(getClass().getSimpleName(), "authKeystore " + authKeystore.action_time + " " + authKeystore.expired + " " + authKeystore.store_code);

        if (mServerTime - authKeystore.action_time > AUTH_TIME) {//3天以上未更新密钥
            mListener.onKeyAuthFail("3天以上未更新密钥，请将服务器联网更新！");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.d(getClass().getSimpleName(), "更新秘钥时间：" + df.format(new Date(authKeystore.action_time)) + "  当前服务器时间：" + df.format(new Date(mServerTime)));
            return;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expired = df.parse(authKeystore.expired);
            if (expired.getTime() <= mServerTime) {
                mListener.onKeyAuthFail("授权码过期！");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onKeyAuthFail("授权过期时间解析异常！");
            return;
        }

        Log.d("StoreKeyHandler", "KeyStore 授权通过！！！！！");

        mListener.onKeyAuthSuccess();

        if (mServerTime - authKeystore.action_time > AUTH_TIME_TIP && mServerTime - authKeystore.action_time <= AUTH_TIME) {//大于1天且小于3天未更新密钥时提示联网更新密钥
            mListener.onKeyAuthTip("授权密钥即将过期，请将服务器联网更新密钥！");
        }
    }

}
