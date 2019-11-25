package com.bestarmedia.libcommon.util;

import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;

/**
 * Created by J Wong on 2016/2/4 11:04.
 */
public class Logger {

    public static void e(String tag, String msg) {
        if (OkConfig.DEBUG)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        if (OkConfig.DEBUG)
            Log.e(tag, msg, e);
    }

    public static void w(String tag, String msg) {
        if (OkConfig.DEBUG)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (OkConfig.DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (OkConfig.DEBUG)
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (OkConfig.DEBUG)
            Log.v(tag, msg);
    }
}
