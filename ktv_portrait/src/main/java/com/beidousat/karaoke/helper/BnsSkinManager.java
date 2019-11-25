package com.beidousat.karaoke.helper;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.data.Constant;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.FileDownloadListener;
import com.bestarmedia.libcommon.model.vod.play.SkinInfo;
import com.bestarmedia.libcommon.transmission.FileDownloader;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libskin.callback.ISkinChangingCallback;

import java.io.File;

/**
 * Created by J Wong on 2017/1/17.
 */

public class BnsSkinManager implements HttpRequestListener {

    private Context mContext;

    public BnsSkinManager(Context context) {
        mContext = context;
    }

    //landspace 0 横屏 1 竖屏
    public void changeSkinFromServer(boolean isLandscape) {
        HttpRequestV4 request = initRequest(RequestMethod.V4.SKIN_CURRENT);
        request.addParam("skin_version", Constant.SKIN_VERSION);
        request.addParam("version_type", String.valueOf(isLandscape ? 0 : 1));
        request.setConvert2Class(SkinInfo.class);
        request.get();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    /**
     * 还原默认皮肤
     */
    public void restoreDefaultSkin() {
        SkinManager.getInstance().removeAnySkin();
        PrefData.setSkinID(mContext, "");
        EventBusUtil.postSticky(EventBusId.Id.SKIN_CHANGED, -1);
    }


    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.SKIN_CURRENT.equals(method)) {
            if (object instanceof SkinInfo) {
                SkinInfo skinInfo = (SkinInfo) object;
                if (skinInfo.skin != null && !TextUtils.isEmpty(skinInfo.skin.id)) {
                    downloadAndChangSkin(skinInfo.skin.id, TextUtils.isEmpty(skinInfo.skin.fileUrl) ? skinInfo.skin.filePath : skinInfo.skin.fileUrl);
                } else {
                    restoreDefaultSkin();
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        if (RequestMethod.V4.SKIN_LIST.equals(method)) {
            restoreDefaultSkin();
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onStart(String method) {
    }

    /**
     * 下载皮肤并更换
     *
     * @param skinID 皮肤ID
     * @param path   皮肤下载地址
     */
    public void downloadAndChangSkin(final String skinID, String path) {
        if (!PrefData.getSkinID(mContext).equals(skinID)) {
            FileDownloader downloader = new FileDownloader();
            downloader.download(new File(KaraokeSdHelper.getSkinDir(), skinID + ".apk"), ServerFileUtil.getFileUrl(path), new FileDownloadListener() {
                @Override
                public void onDownloadCompletion(File file, String url, long size) {
                    if (file != null && file.exists()) {
                        String skinPath = file.getAbsolutePath();
                        String skinPackageName = PackageUtil.getPackageNameFromApk(mContext.getApplicationContext(), skinPath);
                        if (!TextUtils.isEmpty(skinPackageName)) {
                            SkinManager.getInstance().removeAnySkin();
                            SkinManager.getInstance().changeSkin(skinPath, skinPackageName,
                                    new ISkinChangingCallback() {
                                        @Override
                                        public void onStart() {
                                            if (mSwitchSkinListener != null) {
                                                mSwitchSkinListener.onSwitchSkin(0, "正在开始切换皮肤！");
                                            }
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            if (mSwitchSkinListener != null) {
                                                mSwitchSkinListener.onSwitchSkin(-1, "切换皮肤失败！");
                                            }
                                        }

                                        @Override
                                        public void onComplete() {
                                            PrefData.setSkinID(mContext, skinID);
                                            EventBusUtil.postSticky(EventBusId.Id.SKIN_CHANGED, skinID);
                                            if (mSwitchSkinListener != null) {
                                                mSwitchSkinListener.onSwitchSkin(1, "切换皮肤成功！");
                                            }
                                        }
                                    });
                        } else {
                            if (mSwitchSkinListener != null) {
                                mSwitchSkinListener.onSwitchSkin(-1, "皮肤包解析出错了！");
                            }
                        }
                    } else {
                        if (mSwitchSkinListener != null) {
                            mSwitchSkinListener.onSwitchSkin(-1, "下载出错了！");
                        }
                    }
                }

                @Override
                public void onDownloadFail(String url) {
                    if (mSwitchSkinListener != null) {
                        mSwitchSkinListener.onSwitchSkin(-1, "皮肤下载失败！");
                    }
                }

                @Override
                public void onUpdateProgress(File mDesFile, long progress, long total) {
                    if (mSwitchSkinListener != null) {
                        mSwitchSkinListener.onSwitchSkin(0, "正在下载皮肤：" + (100 * progress / total) + "%");
                    }
                }
            });
        } else {
            if (mSwitchSkinListener != null) {
                mSwitchSkinListener.onSwitchSkin(1, "切换皮肤成功！");
            }
            EventBusUtil.postSticky(EventBusId.Id.SKIN_CHANGED, skinID);
        }
    }

    private SwitchSkinListener mSwitchSkinListener;

    public BnsSkinManager setSwitchSkinListener(SwitchSkinListener listener) {
        this.mSwitchSkinListener = listener;
        return this;
    }


    public interface SwitchSkinListener {
        void onSwitchSkin(int status, String message);
    }
}
