package com.bestarmedia.libcommon.config;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.data.PreferenceKey;
import com.bestarmedia.libcommon.util.PreferenceUtil;

/**
 * Created by J Wong on 2015/9/30.
 */

public class OkConfig {

    //必要文件在服务器中的文件目录
    public static final String NECESSARY_FILE_DIR = "mnt/data/necessary/";
    //VOD服务器
    private final static String SERVER_ADDRESS = "172.30.1.232";
    //VOD服务器http端口号
    private final static int HTTP_PORT = 2800;
    //是否开启LOG
    public static final boolean DEBUG = true;
    //是否使用代理解密
    public static final boolean PROXY_DECODE = true;

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static int boxManufacturer() {
        String model = android.os.Build.MODEL;
        if ("rk3288_box".equalsIgnoreCase(model)) {//音诺恒
            return 1;
        } else if ("ktv".equalsIgnoreCase(model)) {//全志H6
            return 2;
        }
        return 0;
    }

    public static String boxManufacturerName() {
        int model = boxManufacturer();
        if (model == 1) {
            return "音诺恒";
        } else if (model == 2) {
            return "全志H6";
        }
        return "晨芯";
    }

    public static String getServerAddress() {
        String address = PreferenceUtil.getString(mContext, PreferenceKey.KEY_PREF_SERVER_IP, SERVER_ADDRESS);
        return TextUtils.isEmpty(address) ? SERVER_ADDRESS : address;
    }

    public static void setServerAddress(String ip) {
        PreferenceUtil.setString(mContext, PreferenceKey.KEY_PREF_SERVER_IP, ip);
    }

    public static String getServerApi() {
        return "http://" + getServerAddress() + ":" + HTTP_PORT + "/";
    }

    public static String getServerFile() {
        return "http://" + getServerAddress() + ":" + HTTP_PORT + "/";
    }
//
//    public static String getNodeApiPath() {
//        return "http://" + getServerAddress() + ":" + HTTP_PORT + "/";
//    }

}

