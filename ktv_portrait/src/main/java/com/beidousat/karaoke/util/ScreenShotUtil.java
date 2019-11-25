package com.beidousat.karaoke.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.Logger;

import java.io.File;

/**
 * Created by J Wong on 2016/10/27.
 */

public class ScreenShotUtil {

    public static File shotHdmi(Context context, String fileName) {
        File dir = KaraokeSdHelper.getScreenShotDir();
        File file = new File(dir, fileName + ".png");
        Intent intent = new Intent();
        intent.setAction("com.screencap.caphdmi");
        intent.putExtra("path", file.getAbsolutePath());
        intent.putExtra("fb", 1);
        context.sendBroadcast(intent);
        Logger.d("ScreenShopUtil", "screencap");
        return file;
    }


    public static File hdmiCap() {
        long time = System.currentTimeMillis();
        Logger.d("ScreenShotUtil", "cap time :" + time);

        File img = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "BNS" + File.separator + "ScreenCap");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            img = new File(dir, System.currentTimeMillis() + ".png");

            Logger.d("ScreenShotUtil", "cap img :" + img.getAbsolutePath());

            Runtime.getRuntime().exec("su -c");

            Runtime.getRuntime().exec("su -c screencap -p " + img.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.d("ScreenShotUtil", "hdmi cap use time:" + (System.currentTimeMillis() - time));
        return img;
    }


}
