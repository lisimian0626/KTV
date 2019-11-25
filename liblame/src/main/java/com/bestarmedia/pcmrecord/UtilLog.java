package com.bestarmedia.pcmrecord;

import android.util.Log;

final class UtilLog {

    public static String TAG = "PcmRecord";

    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean mAllowD = true;// 打印debug信息
    public static boolean mAllowE = true;// 打印Error信息

    public static void d(String content) {
        if (!mAllowD)
            return;
        Log.d(TAG, content);
    }

    public static void e(String content) {
        if (!mAllowE)
            return;
        Log.e(TAG, content);
    }

    public static void scoreD(String content) {
        //Log.e("Score", content);
    }
}
