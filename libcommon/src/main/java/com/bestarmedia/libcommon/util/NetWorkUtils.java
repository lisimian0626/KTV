package com.bestarmedia.libcommon.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by J Wong on 2015/11/6 08:40.
 */
public class NetWorkUtils {

    private final static String TAG = NetWorkUtils.class.getSimpleName();
    private final static String IPMATCHER = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//限定输入格式
    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
//    public static boolean checkEnable(Context context) {
//        NetworkInfo localNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
//            return true;
//        return false;
//    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private static String LOCAL_IP = "";

    // 得到本机ip地址
//    public static String getLocalHostIp() {
//        if (!TextUtils.isEmpty(LOCAL_IP)) {
//            return LOCAL_IP;
//        }
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            // 遍历所用的网络接口
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
//                Enumeration<InetAddress> inet = nif.getInetAddresses();
//                // 遍历每一个接口绑定的所有ip
//                while (inet.hasMoreElements()) {
//                    InetAddress ip = inet.nextElement();
//                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
//                        LOCAL_IP = ip.getHostAddress();
//                        if (OkConfig.IS_LOCAl_FILE_MODE) {
//                            LOCAL_IP = LOCAL_IP.substring(0, LOCAL_IP.length() - 1) + "0";
//                        }
//                        return LOCAL_IP;
//                    }
//                }
//
//            }
//        } catch (SocketException e) {
//            Logger.e(TAG, "获取本地ip地址失败");
//        }
//        return "";
//    }

    public static String getLocalHostIp() {
        if (!TextUtils.isEmpty(LOCAL_IP)) {
            return LOCAL_IP;
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        if (inetAddress instanceof Inet6Address) {
                            Log.w("NetWorkUtils", "getLocalHostIp is IPV6 :" + inetAddress.getHostAddress().toString());
                        } else if (inetAddress instanceof Inet4Address) {//判断是IPV4
                            LOCAL_IP = inetAddress.getHostAddress().toString();
                            Log.d("NetWorkUtils", "getLocalHostIp is IPV4 :" + LOCAL_IP);
                            return LOCAL_IP;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.e(TAG, "获取本地ip地址失败");
        }
        return "";
    }

    public static boolean checkIP(String url) {
        Pattern pattern = Pattern.compile(IPMATCHER);
        Matcher m = pattern.matcher(url);
        return m.matches();
    }
}