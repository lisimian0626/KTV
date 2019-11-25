package com.bestarmedia.libcommon.upgrade;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.FileDownloadListener;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.ApkVersionInfo;
import com.bestarmedia.libcommon.model.vod.VersionV4;
import com.bestarmedia.libcommon.transmission.FileDownloader;
import com.bestarmedia.libcommon.util.FileUtil;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.PackageUtil;

import java.io.File;

/**
 * Created by J Wong on 2015/12/7 17:12.
 */
public class AppUpgrader implements HttpRequestListener, FileDownloadListener {


    private Context mContext;

    private ApkVersionInfo mVersionInfo;

    public AppUpgrader(Context context) {
        mContext = context;
    }

    /**
     * @param type 16:RK3288 横版
     *             17：RK3288 竖版
     */
    public void checkVersion(int type) {
        getApkVersionInfo(type);
    }

    public void downloadApk(String url) {
        if (mOnAppUpdateListener != null)
            mOnAppUpdateListener.onAppUpdateStart();
        FileDownloader fileDownloader = new FileDownloader();
        String fileName = "bestarmedia_ktv.apk";
        if (!TextUtils.isEmpty(fileName)) {
            if (OkConfig.boxManufacturer() == 0) {
                fileDownloader.download(new File(KaraokeSdHelper.getApk(), fileName), url, this);
            } else if (OkConfig.boxManufacturer() == 1) {//901
                if (KaraokeSdHelper.existSDCard()) {
                    File sdDir = Environment.getExternalStorageDirectory();
                    fileDownloader.download(new File(sdDir, fileName), url, this);
                } else {
                    Log.d("AppUpgrader", "downloadApk not existSDCard");
                    if (mOnAppUpdateListener != null)
                        mOnAppUpdateListener.onAppUpdateFail("Sdcard不存在！");
                }
            } else if (OkConfig.boxManufacturer() == 2) {//H6
                if (KaraokeSdHelper.existSDCard()) {
                    File sdDir = Environment.getExternalStorageDirectory();
                    fileDownloader.download(new File(sdDir, fileName), url, this);
                } else {
                    Log.d("AppUpgrader", "downloadApk not existSDCard");
                    if (mOnAppUpdateListener != null)
                        mOnAppUpdateListener.onAppUpdateFail("Sdcard不存在！");
                }
            } else {
                if (KaraokeSdHelper.existSDCard()) {
                    File sdDir = Environment.getExternalStorageDirectory();
                    fileDownloader.download(new File(sdDir, fileName), url, this);
                } else {
                    if (mOnAppUpdateListener != null)
                        mOnAppUpdateListener.onAppUpdateFail("SdCard不存在！");
                }
            }
        }
    }

    private void getApkVersionInfo(int type) {
        HttpRequestV4 r = initRequest(RequestMethod.VOD_VERSION);
        r.addParam("type", String.valueOf(type));
        r.setConvert2Class(VersionV4.class);
        r.get();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.VOD_VERSION.equals(method)) {
            VersionV4 versionV4;
            if (object instanceof VersionV4 && (versionV4 = (VersionV4) object) != null && versionV4.version != null) {
                mVersionInfo = versionV4.version;
                Log.d("AppUpgrader", "onSuccess server version:" + mVersionInfo.versionNumber);
                Log.d("AppUpgrader", "onSuccess local version:" + PackageUtil.getVersionCode(mContext));
                if (mVersionInfo.versionNumber > PackageUtil.getVersionCode(mContext)) {
                    if (!TextUtils.isEmpty(mVersionInfo.md5Code)) {
                        Log.d("AppUpgrader", "onSuccess to do upgrade !!!");
                        downloadApk(mVersionInfo.filePathUrl);
                    } else {
                        if (mOnAppUpdateListener != null)
                            mOnAppUpdateListener.onAppUpdateFail("APK更新包未填MD5值！");
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            if (mOnAppUpdateListener != null) {
                mOnAppUpdateListener.onInterfaceError(baseModelV4.tips);
            }
        } else if (obj instanceof String) {
            mOnAppUpdateListener.onInterfaceError(obj.toString());
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onDownloadCompletion(File file, String url, long fileSize) {
        Log.i("AppUpgrader", "download Completion file:" + file.getAbsolutePath());
        if (file != null && file.exists()) {
            if (file.length() == fileSize) {
                long md5Time = System.currentTimeMillis();
                String fileMd5Code = FileUtil.getFileMD5String(file);
                Log.i("AppUpgrader", "onDownloadCompletion APK md5 code:" + fileMd5Code);
                Log.i("AppUpgrader", "get MD5 time:" + (System.currentTimeMillis() - md5Time));
                if (!TextUtils.isEmpty(mVersionInfo.md5Code) && !TextUtils.isEmpty(fileMd5Code) && fileMd5Code.equalsIgnoreCase(mVersionInfo.md5Code)) {
                    String packageName = mContext.getPackageName();
                    Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                    String className = launchIntent.getComponent().getClassName();
                    Log.i("AppUpgrader", "packageName:" + packageName + " launchIntent:" + launchIntent + " className:" + className);
                    if (OkConfig.boxManufacturer() == 0) {//晨芯 RK3288 横屏、竖屏机顶盒自动升级
                        PackageUtil.installApk(mContext, file, className);
                        if (mOnAppUpdateListener != null)
                            mOnAppUpdateListener.onAppUpdateCompletion();
                    } else if (OkConfig.boxManufacturer() == 1) {//音诺恒 RK3288 横屏、竖屏机顶盒自动升级
                        PackageUtil.installApkFor901(mContext, file, className);
                        if (mOnAppUpdateListener != null)
                            mOnAppUpdateListener.onAppUpdateCompletion();
                    } else if (OkConfig.boxManufacturer() == 2) {
                        PackageUtil.installApkForH6(mContext, file, className);
                        if (mOnAppUpdateListener != null)
                            mOnAppUpdateListener.onAppUpdateCompletion();
                    } else {//其他使用原生API升级
                        PackageUtil.installApkByApi(mContext, file);
                        if (mOnAppUpdateListener != null)
                            mOnAppUpdateListener.onAppUpdateCompletion();
                    }
                } else {
                    if (mOnAppUpdateListener != null)
                        mOnAppUpdateListener.onAppUpdateFail("APK更新包MD5校验失败！");
                }
            } else {
                if (mOnAppUpdateListener != null)
                    mOnAppUpdateListener.onAppUpdateFail("APK文件被损坏！");
            }
        } else {
            if (mOnAppUpdateListener != null)
                mOnAppUpdateListener.onAppUpdateFail("APK不存在！");
        }
    }

    @Override
    public void onDownloadFail(String url) {
        if (mOnAppUpdateListener != null)
            mOnAppUpdateListener.onAppUpdateFail("APK下载失败");
    }

    @Override
    public void onUpdateProgress(File mDesFile, long progress, long total) {
        if (mOnAppUpdateListener != null)
            mOnAppUpdateListener.onAppUpdateProgress(progress, total);
    }


    private OnAppUpdateListener mOnAppUpdateListener;

    public void setOnSystemUpdateListener(OnAppUpdateListener listener) {
        this.mOnAppUpdateListener = listener;
    }

    public interface OnAppUpdateListener {

        void onAppUpdateStart();

        void onAppUpdateProgress(long progress, long total);

        void onAppUpdateCompletion();

        void onAppUpdateFail(String msg);

        void onInterfaceError(String error);

    }
}
