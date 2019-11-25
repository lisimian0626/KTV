package com.bestarmedia.libcommon.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by J Wong on 2018/1/10.
 */

public class SystemBroadcastSender {

//    private final static String PREF_KEY_SERIAL_SCREEN_BAUDRATE = "pref_key_serial_screen_baudrate";

    /**
     * @param context
     * @param mode     0:left 1:right other:both
     * @param progress 0-10
     */
    public static void setVol(Context context, int mode, int progress) {
        Intent intent = new Intent();
        intent.setAction("com.ynh.volume_control");
        if (progress > 10) {
            progress = 10;
        } else if (progress < 0) {
            progress = 0;
        }
        switch (mode) {
            case 0:
                intent.putExtra("command", 0);
                intent.putExtra("value", progress);
                context.sendBroadcast(intent);
                break;
            case 1:
                intent.putExtra("command", 1);
                intent.putExtra("value", progress);
                context.sendBroadcast(intent);
                break;
            default:
                intent.putExtra("command", 0);
                intent.putExtra("value", progress);
                context.sendBroadcast(intent);

                intent.putExtra("command", 1);
                intent.putExtra("value", progress);
                context.sendBroadcast(intent);
                break;
        }
    }

    public static void setSerialBaudrate(Context context, int baudrate) {
        Intent intent = new Intent();
        intent.setAction("com.ynh.set_uart");
        intent.putExtra("mode", 3);
        intent.putExtra("baudrate", baudrate);
        Log.d("SystemBroadcastSender", "setSerialBaudrate baudrate:" + baudrate);
        context.sendBroadcast(intent);
//        PreferenceUtil.setString(context, PREF_KEY_SERIAL_SCREEN_BAUDRATE, baudrate);
    }

    public static String getSerialBaudrate(Context context) {
        /* 读取数据 */
        BufferedReader br = null;
        try {
            File file = new File("/data/com.txt");
            FileUtil.chmod777File(file);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String lineTxt = null;
            boolean isCom3 = false;
            while ((lineTxt = br.readLine()) != null) {
                Logger.d("SystemBroadcastSender", "getSerialBaudrate line:" + lineTxt);
                if ("3".equals(lineTxt)) {//COM3
                    Logger.d("SystemBroadcastSender", "getSerialBaudrate line is  COM3 ");
                    isCom3 = true;
                    continue;
                }
                if (isCom3) {
                    Logger.d("SystemBroadcastSender", "getSerialBaudrate line is  COM3 Baudrate" + lineTxt);
                    return lineTxt;
                }
            }
        } catch (Exception e) {
            Log.e("SystemBroadcastSender", "getSerialBaudrate Exception :" + e.toString());

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return String.valueOf(9600);
//        return PreferenceUtil.getString(context, PREF_KEY_SERIAL_SCREEN_BAUDRATE, 9600 + "");
    }
}
