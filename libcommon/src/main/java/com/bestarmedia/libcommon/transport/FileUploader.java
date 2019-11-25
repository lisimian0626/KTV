package com.bestarmedia.libcommon.transport;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.dto.ImageUploadResult;
import com.bestarmedia.libcommon.model.vod.Material;
import com.bestarmedia.libcommon.model.vod.MaterialV4;
import com.bestarmedia.libcommon.util.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by J Wong on 2016/6/22.
 */
public class FileUploader {

    private final static String TAG = FileUploader.class.getSimpleName();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build();

    private FileUploadListener mFileUploadListener;
    private File mUploadFile;
    private Call mCurrentCall;

    public FileUploader() {

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 2://success
                    Bundle bundle = msg.getData();
                    Material material = (Material) bundle.getSerializable("material");
                    if (mFileUploadListener != null) {
                        mFileUploadListener.onUploadCompletion(mUploadFile, material);
                    }
                    break;

                case 3://fail
                    bundle = msg.getData();
                    String err = bundle.getString("err");
                    if (mFileUploadListener != null) {
                        mFileUploadListener.onUploadFailure(mUploadFile, err);
                    }
                    break;
            }
            return false;
        }
    });


    private void sendFail(String err) {
        Message message = new Message();
        message.what = 3;
        Bundle bundle = new Bundle();
        bundle.putString("err", err);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendSuccess(Material material) {
        Message message = new Message();
        message.what = 2;
        Bundle bundle = new Bundle();
        bundle.putSerializable("material", material);
        message.setData(bundle);
        handler.sendMessage(message);
    }


    public void setFileUploadListener(FileUploadListener listener) {
        mFileUploadListener = listener;
    }

//    public void  cancel(){
//        client.dispatcher().cancelAll();
//    }


    public void cancel() {
        mCurrentCall.cancel();
    }

    public void upload(File file, String url) {
        mUploadFile = file;
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), mUploadFile);
        RequestBody requestBody = new MultipartBody.Builder().addFormDataPart("file", mUploadFile.getName(), fileBody).build();
        uploadFile(requestBody, url);
    }

    public void uploadScreenShot(File file, String url) {
        mUploadFile = file;
//        List<FileUploadDTO.CutSize> sizes = new ArrayList<>();
//        sizes.add(new FileUploadDTO.CutSize(96, 54));
//        sizes.add(new FileUploadDTO.CutSize(256, 144));
//        sizes.add(new FileUploadDTO.CutSize(656, 369));
//        sizes.add(new FileUploadDTO.CutSize(1280, 720));
//        FileUploadDTO fileUploadDTO = new FileUploadDTO(mUploadFile.getName(), "", sizes);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), mUploadFile);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("cut_size[0][width]", "96")
                .addFormDataPart("cut_size[0][height]", "54")
                .addFormDataPart("cut_size[1][width]", "256")
                .addFormDataPart("cut_size[1][height]", "144")
                .addFormDataPart("cut_size[2][width]", "656")
                .addFormDataPart("cut_size[2][height]", "369")
                .addFormDataPart("file", mUploadFile.getName(), fileBody)
//                .addFormDataPart("cut_size", fileUploadDTO.toString())
//                .addPart(FormBody.create(MediaType.parse("application/json; charset=utf-8"), fileUploadDTO.toString()))
                .build();
        uploadFile(requestBody, url);
    }

    public void uploadFile(RequestBody requestBody, String url) {
        //这个是ui线程回调，可直接操作UI
        final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
                Logger.d(TAG, "onUIProgress bytesWrite:" + bytesWrite + "  contentLength:" + contentLength + "  done:" + done);
                //ui层回调
                if (mFileUploadListener != null)
                    mFileUploadListener.onUploading(mUploadFile, (float) bytesWrite / contentLength);
            }

            @Override
            public void onUIStart(long bytesWrite, long contentLength, boolean done) {
                super.onUIStart(bytesWrite, contentLength, done);
                Logger.d(TAG, "onUIStart bytesWrite:" + bytesWrite + "  contentLength:" + contentLength + "  done:" + done);
                if (mFileUploadListener != null)
                    mFileUploadListener.onUploadStart(mUploadFile);
            }

            @Override
            public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
                super.onUIFinish(bytesWrite, contentLength, done);
                Logger.d(TAG, "onUIFinish bytesWrite:" + bytesWrite + "  contentLength:" + contentLength + "  done:" + done);
            }
        };
        Logger.d(TAG, "uploadFile url : " + url);
        String targetUrl = url.startsWith("http://") || url.startsWith("https://") ? url : OkConfig.getServerApi() + url;
        Logger.d(TAG, "uploadFile targetUrl : " + targetUrl);
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder().url(targetUrl).post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
        //开始请求
        mCurrentCall = client.newCall(request);
        mCurrentCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e(TAG, "onFailure ex:" + e.toString());
                sendFail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String json = response.body().string();
                    Logger.d(TAG, "onResponse response:" + json);
                    Gson gson = new Gson();
                    BaseModelV4 baseModel = gson.fromJson(json, BaseModelV4.class);
                    if (baseModel.code == 0) {
                        JsonElement jsonData = baseModel.data;
                        Logger.d(TAG, "onResponse data:" + jsonData);
                        Gson dataGson = new Gson();
                        try {
                            MaterialV4 materialV4 = dataGson.fromJson(jsonData, MaterialV4.class);
                            if (materialV4.material != null) {
                                sendSuccess(materialV4.material);
                            } else {
                                ImageUploadResult imageUploadResult = dataGson.fromJson(jsonData, ImageUploadResult.class);
                                if (imageUploadResult != null) {
                                    sendSuccess(new Material("", imageUploadResult.url, ""));
                                }
                            }
                            return;
                        } catch (Exception e) {
                            Log.e(TAG, "解析为Material报错：" + e.getMessage());
                            try {
                                ImageUploadResult imageUploadResult = dataGson.fromJson(jsonData, ImageUploadResult.class);
                                sendSuccess(new Material("", imageUploadResult.url, ""));
                                return;
                            } catch (Exception e1) {
                                Log.e(TAG, "解析为Material报错：" + e.getMessage());
                            }
                        }
                        sendFail(baseModel.code + ":" + "数据无法解析为model！");
                    } else {
                        sendFail(baseModel.code + ":" + baseModel.msg);
                        return;
                    }
                } catch (Exception e) {
                    Logger.w(TAG, "onResponse ex:" + e.toString());
                }
                sendFail(response.toString());
            }
        });
    }

    /**
     * /**
     *
     * @param file   本地文件
     * @param url    上传文件地址
     * @param type   1头像 2 多媒体 3自制MV图片
     * @param rename 指定生成文件名
     *
    public void upload(File file, String url, int type, String rename) {
    mUploadFile = file;
    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
    .addFormDataPart("Type", String.valueOf(type))
    .addFormDataPart("FileName", mUploadFile.getName(), RequestBody.create(null, mUploadFile))
    .addFormDataPart("SaveName", rename)
    .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""), RequestBody.create(MediaType.parse("application/octet-stream"), mUploadFile))
    .build();
    uploadFile(requestBody, url);
    }*/

    /**
     * /**
     *
     * @param file  本地文件
     * @param url   上传文件地址
     * @param parts 参数

    public void upload(File file, String url, Map<String, String> parts) {
    mUploadFile = file;
    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    if (parts != null && parts.size() > 0) {
    Iterator iterator = parts.entrySet().iterator();
    while (iterator.hasNext()) {
    Map.Entry entry = (Map.Entry) iterator.next();
    String key = entry.getKey().toString();
    String val = entry.getValue() == null ? "" : entry.getValue().toString();
    builder.addFormDataPart(key, val);
    }
    }
    builder.addFormDataPart("FileName", mUploadFile.getName(), RequestBody.create(null, mUploadFile));
    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""),
    RequestBody.create(MediaType.parse("application/octet-stream"), mUploadFile));
    MultipartBody requestBody = builder.build();
    uploadFile(requestBody, url);
    }   */

}
