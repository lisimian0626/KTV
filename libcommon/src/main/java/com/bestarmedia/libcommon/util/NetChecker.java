package com.bestarmedia.libcommon.util;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J Wong on 2017/5/12.
 */

public class NetChecker {

    public static NetChecker mNetChecker;
    private Context mConext;
    private ScheduledExecutorService mScheduledExecutorService;

    public static NetChecker getInstance(Context context) {
        if (mNetChecker == null) {
            mNetChecker = new NetChecker(context.getApplicationContext());
        }
        return mNetChecker;
    }

    private NetChecker(Context context) {
        this.mConext = context;
    }

    public void check() {
        if (mScheduledExecutorService == null || mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduleAtFixedRate(mScheduledExecutorService);
        }
    }

    private void stop() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                boolean isNetAvail = NetWorkUtils.isNetworkAvailable(mConext);
                if (mOnNetworkStatusListener != null) {
                    mOnNetworkStatusListener.onNetworkStatus(isNetAvail);
                }
                if (isNetAvail) {
                    stop();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private OnNetworkStatusListener mOnNetworkStatusListener;

    public NetChecker setOnNetworkStatusListener(OnNetworkStatusListener listenner) {
        this.mOnNetworkStatusListener = listenner;
        return this;
    }

    public interface OnNetworkStatusListener {
        void onNetworkStatus(boolean status);
    }
}
