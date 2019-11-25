package com.bestarmedia.libcommon.security;


import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.interf.FileDownloadListener;
import com.bestarmedia.libcommon.transmission.FileDownloader;
import com.bestarmedia.libcommon.util.FileUtil;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;

import java.io.File;

/**
 * Created by J Wong on 2015/12/15 10:30.
 */
public class PrivateKeyUpdater implements FileDownloadListener {

    private int mDownloadRetryTimes;

    private final static String KEY_URL = OkConfig.NECESSARY_FILE_DIR + "SongSecurity.key";

    public PrivateKeyUpdater() {
    }


    public void updateKey() {
        File keyFile = KaraokeSdHelper.getSongSecurityKeyFile();
        if (keyFile != null && keyFile.exists() && keyFile.length() == 32) {//不需要更新密钥
            Log.d("PrivateKeyUpdater", "updateKey keyFile key exist , not need to update !!");
            return;
        }
        Log.d("PrivateKeyUpdater", "updateKey keyFile !!");
        updateKey(keyFile);
    }

    private void updateKey(File file) {
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.download(file, KEY_URL, this);
    }

    @Override
    public void onDownloadCompletion(File file, String url, long fileSize) {
        Log.d("PrivateKeyUpdater", "Update private key success !");
        /**
         *打开密钥权限
         */
        try {
            FileUtil.chmod777File(KaraokeSdHelper.getSongSecurityKeyFile());
        } catch (Exception e) {
            Log.e("PrivateKeyUpdater", "chmod key file exception ");
        }
    }

    @Override
    public void onDownloadFail(String url) {
        Log.d("PrivateKeyUpdater", "Update private key fail time:" + mDownloadRetryTimes);
        if (mDownloadRetryTimes < 5) {//retry
            updateKey();
            mDownloadRetryTimes++;
        }
    }

    @Override
    public void onUpdateProgress(File mDesFile, long progress, long total) {
    }
}
