package com.beidousat.karaoke.player;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;

import com.beidousat.karaoke.player.proxy.CacheFile;
import com.beidousat.karaoke.player.proxy.HttpGetProxy;
import com.beidousat.karaoke.player.proxy.Utils;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.util.ServerFileUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecodePlayer extends BasePlayer {

    private HttpGetProxy proxy;
    private ExecutorService executorService;

    private final static String TAG = "DecodePlayer";

    public DecodePlayer(SurfaceView mainSurfaceView, SurfaceView minorSurfaceView) {
        super(mainSurfaceView, minorSurfaceView);
    }

    @Override
    public void release() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        super.release();
    }

    public void playEncode(final String path, final String preloadPath) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileUrl = ServerFileUtil.getFileUrl(path);
                    if (TextUtils.isEmpty(fileUrl)) {
                        onPlayError(fileUrl, "播放地址为空！");
                        return;
                    }
                    String filePreloadUrl = ServerFileUtil.getFileUrl(preloadPath);
                    if (OkConfig.boxManufacturer() != 1) {//晨芯需要代理服務器解密
                        if (proxy == null) {
                            proxy = new HttpGetProxy(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProxyBuffer/files", 4 * 1024 * 1024, 20);
                        }
                        String id = "";
                        try {
                            id = Utils.stringMD5(fileUrl);
                            proxy.startDownload(id, fileUrl, false);
                        } catch (Exception e) {
                            Log.e(TAG, "启动代理解密服务器发生异常：" + e.toString());
                        }
                        CacheFile.getInstance().add(fileUrl, !TextUtils.isEmpty(filePreloadUrl) ? filePreloadUrl : fileUrl);
                        fileUrl = proxy.getLocalURL(id);
                    }
                    if (!TextUtils.isEmpty(fileUrl)) {
                        playMedia(fileUrl);
                    } else {
                        Log.e(TAG, "播放地址为空！");
                        onPlayError(path, "播放地址为空！");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "播放异常path：" + path, e);
                    onPlayError(path, e.getMessage());
                }
            }
        });
    }
}
