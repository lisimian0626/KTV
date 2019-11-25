package com.bestarmedia.libcommon.interf;


import com.bestarmedia.libcommon.model.v4.SongSimple;

import java.util.Map;

/**
 * Created by J Wong on 2018/11/12.
 */

public interface CloudSongDownloadListener {

    //    void onSongDownloadStart(String songId);
//
//    void onSongDownloadProgressChanged(String songId, int progress);
//
//    void onSongDownloadQueueChanged(String songId, int queue);
//
    void onSongDownloadStatusChanged(String songId, int status);

    void onSongDownloadChanged(Map<String, SongSimple> currentDownloadStatus);

//    void onSongDownloadFailure(String songId, String error);
}
