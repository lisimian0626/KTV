package com.bestarmedia.libcommon.data;

import android.util.Log;

import com.bestarmedia.libcommon.ad.AdModelDefault;
import com.bestarmedia.libcommon.interf.FileDownloadListener;
import com.bestarmedia.libcommon.transmission.FileDownloader;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.ServerFileUtil;

import java.io.File;

/**
 * Created by J Wong on 2017/7/25.
 */

public class VideoFileCache implements FileDownloadListener {

    volatile private static VideoFileCache mVideoFileCache;
    private String[] mUrls = new String[]{AdModelDefault.getPublicServiceAd().get(0).getAdContent(),
            AdModelDefault.getPatchDefaultAd().get(0).getAdContent(), AdModelDefault.getScoreResultVideo()};
    private FileDownloader mFileDownloader;
    private int mCurPs = 0;

    public static VideoFileCache getInstance() {
        if (mVideoFileCache == null) {
            synchronized (VideoFileCache.class) {
                if (mVideoFileCache == null)
                    mVideoFileCache = new VideoFileCache();
            }
        }
        return mVideoFileCache;
    }

    private VideoFileCache() {
        mFileDownloader = new FileDownloader();
        mFileDownloader.setIgnoreByFileLen(true);
    }

    public void loadVideo() {
        if (mCurPs < mUrls.length) {
            String url = ServerFileUtil.getFileUrl(mUrls[mCurPs % mUrls.length]);
            File desFile = new File(KaraokeSdHelper.getVideoCacheDir(), ServerFileUtil.getFileName(url));
            Log.d("VideoFileCache", "下载必要文件：" + url + " 存放位置：" + desFile.getAbsolutePath());
            mFileDownloader.download(desFile, url, this);
//            if (mVideoFileCacheListener != null) {
//                mVideoFileCacheListener.onInitStart(mCurPs + 1, mUrls.length);
//            }
        } else {
            Log.d("VideoFileCache", "下载必要文件已完成>>>>>>>>>>>>>");
//            if (mVideoFileCacheListener != null) {
//                mVideoFileCacheListener.onInitCompletion(mUrls.length, mUrls.length);
//            }
        }
    }

//    private VideoFileCacheListener mVideoFileCacheListener;
//
//    public void setVideoFileCacheListener(VideoFileCacheListener listener) {
//        this.mVideoFileCacheListener = listener;
//    }

    @Override
    public void onDownloadCompletion(File file, String url, long size) {
        Log.i("VideoFileCache", "下载必要文件：" + url + " 完成！ 文件位置：" + file.getAbsolutePath() + " 文件大小：" + size);
//        if (mVideoFileCacheListener != null) {
//            mVideoFileCacheListener.onInitCompletion(mCurPs + 1, mUrls.length);
//        }
        mCurPs++;
        if (mCurPs < mUrls.length) {
            loadVideo();
        }
    }

//    private int mFailNum;

    @Override
    public void onDownloadFail(String url) {
        Log.e("VideoFileCache", "下载必要文件：" + url + " 失败！");
//        mFailNum++;
//        if (mVideoFileCacheListener != null) {
//            mVideoFileCacheListener.onInitFail(mFailNum, mUrls.length);
//        }
        mCurPs++;
        if (mCurPs < mUrls.length) {
            loadVideo();
        }
    }

    @Override
    public void onUpdateProgress(File desFile, long progress, long total) {
        Log.d("VideoFileCache", "下载必要文件：" + desFile.getAbsolutePath() + " 进度：" + progress + "  总文件大小：" + total);
//        if (mVideoFileCacheListener != null) {
//            mVideoFileCacheListener.onInitLoading(mCurPs + 1, mUrls.length, (float) progress / (float) total);
//        }
    }


//    public interface VideoFileCacheListener {
//
//        void onInitStart(int num, int total);
//
//        void onInitLoading(int num, int total, float percent);
//
//        void onInitCompletion(int num, int total);
//
//        void onInitFail(int failNum, int total);
//    }

    /**
     * 查找本地是否存在缓存文件
     *
     * @param url 文件url
     * @return 本地文件
     */
    public File getCacheFile(String url) {
        String urlFileName = ServerFileUtil.getFileName(url);
        for (String cache : mUrls) {
            String cacheFileName = ServerFileUtil.getFileName(cache);
            if (cacheFileName.equalsIgnoreCase(urlFileName)) {
                File file = new File(KaraokeSdHelper.getVideoCacheDir(), cacheFileName);
                if (file.exists() && file.length() > 1024) {
                    return file;
                }
            }
        }
        return null;
    }
}
