package com.bestarmedia.libcommon.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.bestarmedia.libcommon.config.OkConfig;

import java.io.File;

/**
 * Created by J Wong on 2016/8/19.
 */
public class CacheUtil {

    public static void cleanCache(final Context context) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //刪除3.7文件
                    File file1 = new File("/sdcard/play_err_info.txt");
                    if (file1.exists()) {
                        file1.delete();
                    }
                    File file2 = new File("/sdcard/player_error.txt");
                    if (file2.exists()) {
                        file2.delete();
                    }
                    File file3 = new File("/sdcard/hdmilog.txt");
                    if (file3.exists()) {
                        file3.delete();
                    }
                    File file4 = new File("/sdcard/net_conn.txt");
                    if (file4.exists()) {
                        file4.delete();
                    }
                    File file5 = new File("/sdcard/SingerImg150.zip");
                    if (file5.exists()) {
                        file5.delete();
                    }
                    cleanInternalCache(context);
                    cleanExternalCache(context);
                } catch (Exception e) {
                    Logger.w("CacheUtil", "cleanCache ex:" + e.toString());
                }
                super.run();
            }
        }.start();
        if (OkConfig.boxManufacturer() == 1) {
            Intent intent = new Intent();
            intent.setAction("com.ynh.clear_cache");
            context.sendBroadcast(intent);
        }
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                try {
                    item.delete();
                } catch (Exception e) {
                    Logger.w("CacheUtil", "deleteFilesByDirectory ex:" + e.toString());
                }
            }
        }
    }
}
