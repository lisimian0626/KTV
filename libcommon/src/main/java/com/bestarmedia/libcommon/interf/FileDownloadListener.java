package com.bestarmedia.libcommon.interf;

import java.io.File;

/**
 * Created by J Wong on 2015/12/7 18:14.
 */
public interface FileDownloadListener {

    void onDownloadCompletion(File file, String url, long size);

    void onDownloadFail(String url);

    void onUpdateProgress(File mDesFile, long progress, long total);
}
