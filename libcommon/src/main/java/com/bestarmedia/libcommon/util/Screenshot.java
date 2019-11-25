package com.bestarmedia.libcommon.util;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J Wong on 2016/8/18.
 */
public class Screenshot {

    private OnScreenshotListener mOnScreenshotListener;

    public Screenshot(OnScreenshotListener listener) {
        mOnScreenshotListener = listener;
    }

    public void shot(View v) {
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
            String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
            try {
                FileOutputStream out = new FileOutputStream(fname);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                if (mOnScreenshotListener != null) {
                    mOnScreenshotListener.OnScreenshot(new File(fname));
                }
            } catch (Exception e) {
                Logger.w("Screenshot", "shot write file ex:" + e.toString());
            }
        } else {
            Logger.w("Screenshot", "shot getDrawingCache is null ");
        }
    }

    public interface OnScreenshotListener {
        void OnScreenshot(File file);
    }
}
