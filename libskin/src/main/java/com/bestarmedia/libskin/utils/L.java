package com.bestarmedia.libskin.utils;

import android.util.Log;

/**
 * Created by J Wong on 2017/1/17.
 */

public class L {
    private static final String TAG = "Skin";

    private static boolean debug = false;

    public static void e(String msg) {
        if (debug)
            Log.w(TAG, msg);
    }

}
