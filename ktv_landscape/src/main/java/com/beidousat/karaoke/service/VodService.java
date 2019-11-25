package com.beidousat.karaoke.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.beidousat.karaoke.helper.BnsSkinManager;
import com.beidousat.karaoke.helper.ScreenAdSyncTimer;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libnetty.client.NettyDeviceClient;
import com.bestarmedia.libnetty.client.NettyVodClient;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.server.NettyServerBootstrap;

public class VodService extends Service {

    private final static String TAG = "VodService";

    private NettyServerBootstrap nettyServerBootstrap;

    @Override
    public void onCreate() {
        super.onCreate();
        new BnsSkinManager(getApplicationContext()).changeSkinFromServer(true);
        try {
            NettyVodClient.getInstance().setHostPort(OkConfig.getServerAddress(), 7200);
            NettyVodClient.getInstance().start();

            if (DeviceHelper.isMainVod(getApplicationContext())) {//主屏开启netty服务端
                nettyServerBootstrap = new NettyServerBootstrap();
                nettyServerBootstrap.start();
                ScreenAdSyncTimer.getInstance(getApplicationContext()).startTimer();
            } else {//副屏开启netty客户端
                if (!TextUtils.isEmpty(DeviceHelper.getMainVodIp())) {
                    NettyDeviceClient.getInstance().setHostPort(DeviceHelper.getMainVodIp(), NettyConfig.SERVER_PORT);
                    NettyDeviceClient.getInstance().start();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "启动Netty失败", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private LocalBinder localBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        VodService getService() {
            return VodService.this;
        }
    }


    @Override
    public void onDestroy() {
        ScreenAdSyncTimer.getInstance(getApplicationContext()).stopTimer();
        if (nettyServerBootstrap != null) {
            try {
                nettyServerBootstrap.destroy();
            } catch (Exception e) {
                Log.e(TAG, "关闭服务端Netty失败", e);
            }
        }
        NettyVodClient.getInstance().stop();
        NettyDeviceClient.getInstance().stop();
        super.onDestroy();
    }
}
