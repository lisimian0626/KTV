package com.beidousat.karaoke.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.beidousat.karaoke.util.ScreenShotUtil;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.model.vod.Material;
import com.bestarmedia.libcommon.transport.FileUploadListener;
import com.bestarmedia.libcommon.transport.FileUploader;
import com.bestarmedia.proto.node.VodScreenShotRequest;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by J Wong on 2017/1/12.
 * HDMI截屏上传
 */
public class HdmiScreenShotUploader {


    public static void shotAndUploadHdmi(Handler handler, Context context, final VodScreenShotRequest request, int playType, String playingId, String playingName,
                                         String playingAdId, String songVersion, String singerName) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            final File file = OkConfig.boxManufacturer() == 0 ? ScreenShotUtil.shotHdmi(context, System.currentTimeMillis() + "")
                    : OkConfig.boxManufacturer() == 1 ? ScreenShotUtil.hdmiCap() : ScreenShotUtil.shotHdmiForH6(context, System.currentTimeMillis() + "");
            Log.d("HdmiScreenShotUploader", "截图保存路径：" + file.getAbsolutePath());
            handler.postDelayed(() -> {
                if (file.exists()) {
                    try {
                        new Compressor(context)
                                .setMaxWidth(1280)
                                .setMaxHeight(720)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.PNG)
                                .compressToFileAsFlowable(file)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(fileCompressor -> {
                                    Log.i("HdmiScreenShotUploader", "压缩截图完成>>>>>>>>>>>>>>>>>>>>>");
                                    if (fileCompressor != null && fileCompressor.length() > 0) {
                                        Log.d("HdmiScreenShotUploader", "文件路径:" + file.getAbsolutePath() + " 图片大小: " + (file.length() / 1024) + "KB");
                                        FileUploader fileUploader = new FileUploader();
                                        fileUploader.setFileUploadListener(new FileUploadListener() {
                                            @Override
                                            public void onUploadStart(File file) {
                                                Log.d("HdmiScreenShotUploader", "onUploadStart file: " + file.getAbsolutePath());
                                            }

                                            @Override
                                            public void onUploading(File file, float progress) {
                                                Log.d("HdmiScreenShotUploader", "onUploading progress: " + progress);
                                            }

                                            @Override
                                            public void onUploadCompletion(File file, Material material) {
                                                Log.d("HdmiScreenShotUploader", "onUploadCompletion : " + material.absolutePath);
                                                VodCommunicateHelper.sendScreenShotResponse(request, Integer.valueOf(material.id), material.absolutePath, playType,
                                                        playingId, playingName, playingAdId, songVersion, singerName);
                                            }

                                            @Override
                                            public void onUploadFailure(File file, String error) {
                                                Log.d("HdmiScreenShotUploader", "onUploadFailure : " + error);
                                            }
                                        });
                                        fileUploader.uploadScreenShot(fileCompressor, request.getEndpoint());
                                    } else {
                                        Log.e("HdmiScreenShotUploader", "压缩截图完成，图片不存在或大小为0KB");
                                    }
                                }, throwable -> Log.e("HdmiScreenShotUploader", "压缩截图，出错了", throwable));
                    } catch (Exception e) {
                        Log.e("HdmiScreenShotUploader", "截图处理出错了", e);
                    }
                } else {
                    Log.e("HdmiScreenShotUploader", "截图失败！！！！");
                }

            }, 5000);
        });
    }
}
