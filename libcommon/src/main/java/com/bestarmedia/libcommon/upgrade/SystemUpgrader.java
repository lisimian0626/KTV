package com.bestarmedia.libcommon.upgrade;

import android.content.Context;
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
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.PackageUtil;

import java.io.File;

/**
 * Created by J Wong on 2015/12/7 17:12.
 */
public class SystemUpgrader implements HttpRequestListener, FileDownloadListener {

//    这个功能我要发新的升级包给你们才行
//    升级包放到/mnt/sdcard/，命名为update.zip
//    调用方法如下：
//    Intent intent = new Intent();
//    intent = new Intent("softwinner.intent.action.autoupdate");
//    startActivity(intent);

    private Context mContext;
    private ApkVersionInfo mVersionInfo;

    public SystemUpgrader(Context context) {
        mContext = context;
    }

    public void checkVersion(int type) {
        getSystemVersionInfo(type);
    }

    private void downloadZip(String url) {
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.download(KaraokeSdHelper.getOtaDownloadFile(), url, this);
        if (mOnSystemUpdateListener != null)
            mOnSystemUpdateListener.onSystemUpdateStart();
    }

    private void getSystemVersionInfo(int type) {
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
            if (object instanceof VersionV4 && (versionV4 = (VersionV4) object).version != null) {
                mVersionInfo = versionV4.version;
                Logger.i("SystemUpgrader", "onSuccess system local version:" + PackageUtil.getSystemVersionCode());
                if (mVersionInfo != null && mVersionInfo.versionNumber > PackageUtil.getSystemVersionCode()) {
                    if (!TextUtils.isEmpty(mVersionInfo.md5Code)) {
                        downloadZip(mVersionInfo.filePathUrl);
                        Logger.i("SystemUpgrader", "update.zip not exists");
                    } else {
                        if (mOnSystemUpdateListener != null)
                            mOnSystemUpdateListener.onSystemUpdateFail("固件更新包未填MD5值！");
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            if (mOnSystemUpdateListener != null)
                mOnSystemUpdateListener.onInterfaceError(baseModelV4.tips);
        } else if (obj instanceof String) {
            if (mOnSystemUpdateListener != null)
                mOnSystemUpdateListener.onInterfaceError(obj.toString());
        }

    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onDownloadCompletion(File file, String url, long fileSize) {
        if (file != null && file.exists()) {
            Logger.i("SystemUpgrader", "file len:" + file.length() + "  download len:" + fileSize);
            if (file.length() == fileSize) {
                long md5Time = System.currentTimeMillis();
                String fileMd5Code = FileUtil.getFileMD5String(file);
                Log.i("SystemUpgrader", "onDownloadCompletion ota zip md5 code:" + fileMd5Code);
                Logger.i("SystemUpgrader", "get MD5 time:" + (System.currentTimeMillis() - md5Time));
                if (!TextUtils.isEmpty(mVersionInfo.md5Code) && !TextUtils.isEmpty(fileMd5Code) && fileMd5Code != null && fileMd5Code.equalsIgnoreCase(mVersionInfo.md5Code)) {
                    File file1 = KaraokeSdHelper.getOtaUpdateFile();
                    if (file.renameTo(file1)) {
                        if (file1.exists()) {
                            if (OkConfig.boxManufacturer() == 0) {
                                PackageUtil.rk3288upgradeSystem(mContext, file1);
                            } else if (OkConfig.boxManufacturer() == 1) {
                                PackageUtil.rk3288upgradeSystemFor901(mContext, file1.getAbsolutePath());
                            } else if (OkConfig.boxManufacturer() == 2) {
                                PackageUtil.upgradeSystemForH6(mContext, file1.getAbsolutePath());
                            }
                            if (mOnSystemUpdateListener != null)
                                mOnSystemUpdateListener.onSystemUpdateCompletion();
                        } else {
                            if (mOnSystemUpdateListener != null)
                                mOnSystemUpdateListener.onSystemUpdateFail("文件不存在！");
                        }
                    } else {
                        if (mOnSystemUpdateListener != null)
                            mOnSystemUpdateListener.onSystemUpdateFail("文件命名失败！");
                    }
                } else {
                    if (mOnSystemUpdateListener != null)
                        mOnSystemUpdateListener.onSystemUpdateFail("固件更新包MD5校验失败！");
                }
            } else {
                if (mOnSystemUpdateListener != null)
                    mOnSystemUpdateListener.onSystemUpdateFail("文件被损坏！");
            }
        }
    }

    @Override
    public void onDownloadFail(String url) {
        if (mOnSystemUpdateListener != null)
            mOnSystemUpdateListener.onSystemUpdateFail("下载失败");
    }

    @Override
    public void onUpdateProgress(File mDesFile, long progress, long total) {
        if (mOnSystemUpdateListener != null)
            mOnSystemUpdateListener.onSystemUpdateProgress(progress, total);
    }

    private OnSystemUpdateListener mOnSystemUpdateListener;

    public void setOnSystemUpdateListener(OnSystemUpdateListener listener) {
        this.mOnSystemUpdateListener = listener;
    }

    public interface OnSystemUpdateListener {

        void onSystemUpdateStart();

        void onSystemUpdateProgress(long progress, long total);

        void onSystemUpdateCompletion();

        void onSystemUpdateFail(String msg);

        void onInterfaceError(String error);

    }
}
