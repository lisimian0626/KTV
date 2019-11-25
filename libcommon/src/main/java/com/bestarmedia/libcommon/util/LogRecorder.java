package com.bestarmedia.libcommon.util;

import android.content.Context;

import com.bestarmedia.libcommon.crash.CrashReporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by J Wong on 2016/11/16.
 */

public class LogRecorder {

    public static void addHdmiLog(Context context, String content) {
//        addString2File(KaraokeSdHelper.getHdmiLog(), content);
//        CrashReporter.getInstance(context.getApplicationContext()).reportCrash(content);
    }

    public static void addPlayerLog(Context context, String content) {
        addString2File(KaraokeSdHelper.getPlayerLog(), content);
        new CrashReporter(context.getApplicationContext()).reportCrash(content);
    }

    public static void addNetworkLog(String content) {
//        addString2File(KaraokeSdHelper.getNetworkLog(), content);
    }

    private static void addString2File(String fileName, String content) {
        FileWriter writer = null;
        try {
            File file = new File(fileName);
            if (file.length() > 300 * 1024 * 1024) {//超过300M删除
                file.delete();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            writer.write(time + "=====>" + content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
