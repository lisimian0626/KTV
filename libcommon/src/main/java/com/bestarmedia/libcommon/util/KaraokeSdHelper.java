package com.bestarmedia.libcommon.util;

import android.os.Environment;

import com.bestarmedia.libcommon.config.OkConfig;

import java.io.File;

/**
 * Created by J Wong on 2015/12/7 17:20.
 */
public class KaraokeSdHelper {

    private final static String ROOT = "/BNS/";
    private final static String APK = ROOT + "APK/";
    private final static String CRASH = ROOT + "CRASH/";
    private final static String NOTE = ROOT + "Note/";

    private final static String SINGER_IMAGE = "/SingerImg150";

    private final static String SCREEN_SHOT = ROOT + "ScreenShot/";
    private final static String SCREEN_SHOT_P = ROOT + "Compressor/";

    private final static String OTA = ROOT + "OTA/";
    private final static String SKIN = ROOT + "Skin/";

    private final static String LOG = ROOT + "log/";
    private final static String VIDEO_CACHE = ROOT + "video/";


    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getSingerImgDir() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + SINGER_IMAGE;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static File getRoot() {
        if (!existSDCard())
            return null;
        String root = Environment.getExternalStorageDirectory() + ROOT;
        File file = new File(root);
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static File getApk() {
        if (!existSDCard())
            return null;
//        String apk = Environment.getExternalStorageDirectory() + APK;

        String apk = "/mnt/sdcard/";
        File file = new File(apk);
        if (!file.exists())
            file.mkdirs();
        return file;
    }


    public static File getCrash() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + CRASH;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static File getSdCard() {
        if (!existSDCard())
            return null;
        return Environment.getExternalStorageDirectory();
    }

//    public static File getSongSecurityKeyFile() {
////        /mnt/private/SongSecurity.key
////        if (!existSDCard())
////            return null;
//        String privateDir = "/data/private/";
//        File file = new File(privateDir);
//        if (!file.exists()) {
//            boolean ret = file.mkdirs();
//            Logger.i("KaraokeSdHelper", "privateDir create:" + ret);
//        } else {
//            Logger.i("KaraokeSdHelper", "privateDir exist");
//        }
//        File file1 = new File(file, "SongSecurity.key");
//        try {
//            file1.createNewFile();
//            file1.setExecutable(true);//设置可执行权限
//            file1.setReadable(true);//设置可读权限
//            file1.setWritable(true);//设置可写权限
//            Process p = Runtime.getRuntime().exec("chmod 777 " + file1.getAbsolutePath());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return file1;
//    }

    public static File getSongSecurityKeyFile() {
        if (OkConfig.boxManufacturer() == 1) {
            return getSongSecurityKeyFileFor901();
        } else {
            String privateDir = "/data/private/";
            if (!OkConfig.PROXY_DECODE) {
                privateDir = "/data/";
            }
            File file = new File(privateDir);
            if (!file.exists()) {
                boolean ret = file.mkdirs();
                Logger.i("KaraokeSdHelper", "privateDir create:" + ret);
            } else {
                Logger.i("KaraokeSdHelper", "privateDir exist");
            }
            File file1 = new File(file, OkConfig.PROXY_DECODE ? "SongSecurity.key" : "bd.key");
            try {
                file1.createNewFile();
                file1.setExecutable(true);//设置可执行权限
                file1.setReadable(true);//设置可读权限
                file1.setWritable(true);//设置可写权限
                String cmd = "chmod 777 " + file1.getAbsolutePath();
                Logger.i("KaraokeSdHelper", "run exec cmd:" + cmd);
                Process p = Runtime.getRuntime().exec(cmd);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return file1;
        }
    }


    private static File getSongSecurityKeyFileFor901() {
        String privateDir = "/data/";
        File file = new File(privateDir);
        if (!file.exists()) {
            boolean ret = file.mkdirs();
            Logger.i("KaraokeSdHelper", "privateDir create getSongSecurityKeyFileFor901:" + ret);
        } else {
            Logger.i("KaraokeSdHelper", "privateDir exist getSongSecurityKeyFileFor901");
        }
        File file1 = new File(file, "bd.key");
        try {
            file1.createNewFile();
            file1.setExecutable(true);//设置可执行权限
            file1.setReadable(true);//设置可读权限
            file1.setWritable(true);//设置可写权限
            FileUtil.chmod777File(file1);
        } catch (Exception ex) {
            Logger.e("KaraokeSdHelper", "su -c chmod 777 SongSecurityKey Exception");
            ex.printStackTrace();
        }
        return file1;
    }


    public static File getNote() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + NOTE;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }


    public static File getScreenShotDir() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + SCREEN_SHOT;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static File getScreenShotCompressDir() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + SCREEN_SHOT_P;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }


    private static File getOtaDir() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + OTA;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static File getOtaDownloadFile() {
        File file = new File(getOtaDir(), "ota.zip");
        return file;
    }

    public static File getOtaUpdateFile() {
        File file = new File(getOtaDir(), "update.zip");
        return file;
    }


    public static File getSkinDir() {
        if (!existSDCard())
            return null;
        String crash = Environment.getExternalStorageDirectory() + SKIN;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static String getHdmiLog() {
        if (!existSDCard())
            return null;
        String log = Environment.getExternalStorageDirectory() + LOG;
        File file = new File(log);
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath() + "hdmilog.txt";
    }

    public static String getPlayerLog() {
        if (!existSDCard())
            return null;
        String log = Environment.getExternalStorageDirectory() + LOG;
        File file = new File(log);
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath() + "player.txt";
    }


    public static String getNetworkLog() {
        if (!existSDCard())
            return null;
        String log = Environment.getExternalStorageDirectory() + LOG;
        File file = new File(log);
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath() + "network.txt";
    }


    public static File getVideoCacheDir() {
        if (!existSDCard())
            return null;
        String video = Environment.getExternalStorageDirectory() + VIDEO_CACHE;
        File file = new File(video);
        if (!file.exists())
            file.mkdirs();
        return file;
    }
}
