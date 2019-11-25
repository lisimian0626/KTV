package com.bestarmedia.libcommon.helper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.bestarmedia.libcommon.util.Logger;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by J Wong on 2016/12/26.
 */
public class UsbHubReseter {

    private static UsbHubReseter mUsbHubReseter;
    private Handler mHandlerTimer;
    private HandlerThread thread;

    public static UsbHubReseter getInstance() {
        if (mUsbHubReseter == null)
            mUsbHubReseter = new UsbHubReseter();

        return mUsbHubReseter;
    }


    private Handler getTimerHandler() {
        if (mHandlerTimer == null) {
            thread = new HandlerThread("UsbHubResetThread");
            thread.start();
            mHandlerTimer = new MyHandler(thread.getLooper());
        }
        return mHandlerTimer;
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    public void resetUsb() {
        getTimerHandler().removeCallbacks(runnable);
        getTimerHandler().postDelayed(runnable, 12 * 1000);
    }

    public void callback() {
        getTimerHandler().removeCallbacks(runnable);
    }

    private void resetser() {
        try {
            File file = new File("/sys/devices/virtual/adw/adwdev/adwgpio");
            FileOutputStream out = new FileOutputStream(file);
            Logger.d("UsbHubReseter", "reset usb hub >>>>>>>>");
            out.write('H');
            out.flush();
            out.close();
        } catch (Exception ex) {
            Logger.w("UsbHubReseter", "resetser ex:" + ex.toString());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(5);
                resetser();
                Thread.sleep(1);
            } catch (Exception ex) {
                Logger.w("UsbHubReseter", "resetThread ex:" + ex.toString());
            }
            Logger.w("UsbHubReseter", "TestThread isreset ");
//            getTimerHandler().postDelayed(this, 5 * 1000);
        }
    };
}
