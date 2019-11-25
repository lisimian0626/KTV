package com.bestarmedia.libcommon.util;

import java.io.File;

/**
 * Created by J Wong on 2016/8/19.
 */
public class LogFileUtil {

    public static void deleteFiles() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (KaraokeSdHelper.existSDCard()) {
                        //刪除3.7文件
                        File file1 = new File(KaraokeSdHelper.getSdCard(), "play_err_info.txt");
                        if (file1.exists()) {
                            file1.delete();
                        }
                        File file2 = new File(KaraokeSdHelper.getSdCard(), "player_error.txt");
                        if (file2.exists()) {
                            file2.delete();
                        }
                        File file3 = new File(KaraokeSdHelper.getSdCard(), "hdmilog.txt");
                        if (file3.exists()) {
                            file3.delete();
                        }
                        File file4 = new File(KaraokeSdHelper.getSdCard(), "net_conn.txt");
                        if (file4.exists()) {
                            file4.delete();
                        }
                        File file5 = new File(KaraokeSdHelper.getSdCard(), "SingerImg150.zip");
                        if (file5.exists()) {
                            file5.delete();
                        }
                        File file6 = new File(KaraokeSdHelper.getSdCard(), "X3_Portrait.apk");
                        if (file6.exists()) {
                            file6.delete();
                        }
                        File file7 = new File(KaraokeSdHelper.getSdCard(), "X3_Landscape.apk");
                        if (file7.exists()) {
                            file7.delete();
                        }
                        deleteFilesByDirectory(KaraokeSdHelper.getSkinDir());//删除皮肤包
                        deleteFilesByDirectory(KaraokeSdHelper.getNote());//删除皮肤文件
                        deleteFilesByDirectory(KaraokeSdHelper.getCrash());//删除crash日志
                        deleteFilesByDirectory(KaraokeSdHelper.getScreenShotDir());//删除截图
                        deleteFilesByDirectory(KaraokeSdHelper.getScreenShotCompressDir());//删除压缩后的截图
                    }
                } catch (Exception e) {
                    Logger.w("LogFileUtil", "deleteFiles ex:" + e.toString());
                }
                super.run();
            }
        }.start();
    }


    /**
     * 删除目录下文件
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
