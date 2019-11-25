package com.bestarmedia.player;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.beidousat.karaoke.player.proxy.CacheFile;
import com.beidousat.karaoke.player.proxy.HttpGetProxy;
import com.beidousat.karaoke.player.proxy.Utils;
import com.bestarmedia.libcommon.data.VideoFileCache;
import com.bestarmedia.libcommon.util.MediaUtils;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.texture.MergeVideoTextureView;
import com.bestarmedia.texture.VideoTextureView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecodeTexturePlayer extends TexturePlayer {

    private HttpGetProxy proxy;
    private ExecutorService executorService;
    private final static String TAG = "DecodeTexturePlayer";

    public DecodeTexturePlayer(VideoTextureView mediaPlayerTextureView, MergeVideoTextureView mergeVideoTextureView) {
        super(mediaPlayerTextureView, mergeVideoTextureView);
    }

    public void playEncode(final String path, final String preloadPath) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (TextUtils.isEmpty(path)) {
                        onPlayError(path, "播放地址为空！");
                        return;
                    }
                    Log.d(TAG, "播放源：" + path);
                    String fileUrl = ServerFileUtil.getFileUrl(path);
                    File localFile;
                    if (MediaUtils.isLiveUrl(path)) {//直播源，直接播放源地址
                        Log.d(TAG, "播放直播源：" + path);
                        play(path);
                    } else if ((localFile = VideoFileCache.getInstance().getCacheFile(fileUrl)) != null) {//本地有缓存文件，播放本地文件
                        Log.d(TAG, "播放本地文件：" + localFile.getAbsolutePath());
                        play(localFile.getAbsolutePath());
                    } else {//网络文件，除音诺恒外，需要通过代理服务器解密
                        Log.d(TAG, "播放网络文件：" + fileUrl);
                        String filePreloadUrl = ServerFileUtil.getFileUrl(preloadPath);
                        if (!"rk3288_box".equalsIgnoreCase(android.os.Build.MODEL)) {//只有音诺恒是底层解密
                            if (proxy == null) {
                                proxy = new HttpGetProxy(Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/ProxyBuffer/files", 4 * 1024 * 1024, 20);
                            }
                            String id = "";
                            try {
                                id = Utils.stringMD5(fileUrl);
                                proxy.startDownload(id, fileUrl, false);
                            } catch (Exception e) {
                                Log.e(TAG, "启动代理解密服务器发生异常：", e);
                            }
                            CacheFile.getInstance().add(fileUrl, !TextUtils.isEmpty(filePreloadUrl) ? filePreloadUrl : fileUrl);
                            fileUrl = proxy.getLocalURL(id);
                        }
                        if (!TextUtils.isEmpty(fileUrl)) {
                            play(fileUrl);
                        } else {
                            Log.e(TAG, "播放地址为空！");
                            onPlayError(fileUrl, "播放地址为空！");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "播放异常path：" + path, e);
                    onPlayError(path, e.getMessage());
                }
            }
        });
    }
}
