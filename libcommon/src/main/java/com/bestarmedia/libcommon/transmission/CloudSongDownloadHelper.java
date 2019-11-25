package com.bestarmedia.libcommon.transmission;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.CloudSongDownloadListener;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.CloudSongDownload;
import com.bestarmedia.libcommon.model.vod.CloudSongDownloadResult;
import com.bestarmedia.libcommon.model.vod.SongDownload;
import com.bestarmedia.libcommon.model.vod.SongFileDownloadsV4;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2018/11/12.
 */

public class CloudSongDownloadHelper implements HttpRequestListener {

    private static CloudSongDownloadHelper mCloudSongDownloadHelper;
    private Context mContext;
    private List<CloudSongDownloadListener> mCloudSongDownloadListenerList = new ArrayList<>();
    public static Map<String, SongSimple> downloadStatus = new HashMap<>();
    private Song mSongDownload;

    public static CloudSongDownloadHelper getInstance(Context context) {
        if (mCloudSongDownloadHelper == null) {
            synchronized (CloudSongDownloadHelper.class) {
                if (mCloudSongDownloadHelper == null)
                    mCloudSongDownloadHelper = new CloudSongDownloadHelper(context.getApplicationContext());
            }
        }
        return mCloudSongDownloadHelper;
    }

    private CloudSongDownloadHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public Map<String, SongSimple> getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatusMap(Map<String, SongSimple> map) {
        if (!DeviceHelper.isMainVod(mContext)) {
            downloadStatus = map;
            if (mCloudSongDownloadListenerList != null && mCloudSongDownloadListenerList.size() > 0) {
                for (CloudSongDownloadListener listener : mCloudSongDownloadListenerList) {
                    listener.onSongDownloadChanged(downloadStatus);
                }
            }
        }
    }

    public void addCloudSongDownloadListener(CloudSongDownloadListener listener) {
        mCloudSongDownloadListenerList.add(listener);
    }

    public void removeCloudSongDownloadListener(CloudSongDownloadListener listener) {
        mCloudSongDownloadListenerList.remove(listener);
    }

    public void downloadSong(Song song) {
        Log.d(getClass().getSimpleName(), "downloadSong >>>>>>>>>>>>>>>>>>>> ");
        if (DeviceHelper.isMainVod(mContext)) {
            mSongDownload = song;
            HttpRequestV4 r = initRequestV4(RequestMethod.NODE_CLOUD_DOWNLOAD);
            CloudSongDownload download = new CloudSongDownload(Integer.valueOf(mSongDownload.id), mSongDownload.songName, "",
                    mSongDownload.mediaFilePath, mSongDownload.mediaFileMd5, mSongDownload.videoType,
                    mSongDownload.getSingerId(), mSongDownload.getSingerName(),
                    VodConfigData.getInstance().getRoomCode(), NodeRoomInfo.getInstance().getRoomName());
            List<CloudSongDownload> downloads = new ArrayList<>();
            downloads.add(download);
            Gson gson = new Gson();
            String json = gson.toJson(downloads);
            r.setConvert2Class(CloudSongDownloadResult.class);
            r.postJson(json);
        }
    }

    private void downloadStatusRequest(String ids) {
        if (DeviceHelper.isMainVod(mContext)) {
            HttpRequestV4 r = initRequestV4(RequestMethod.NODE_CLOUD_DOWNLOAD_PROGRESS);
            r.addParam("song_id", ids);
            r.setConvert2Class(SongFileDownloadsV4.class);
            r.get();
        }
    }


    private void startCheckTimer() {
        Log.d(getClass().getSimpleName(), "CloudSongStatusChecker startCheckTimer >>>>>>>>>>>>>>>>>>>>>");
        if (DeviceHelper.isMainVod(mContext)) {
            handler.postDelayed(runnable, 3000);
        }
    }

    private void stopCheckTimer() {
        handler.removeCallbacks(runnable);
    }


    private void check() {
        List<SongSimple> songs = getDownloadingSongs();
        if (songs != null && songs.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (SongSimple simple : songs) {
                Log.d(getClass().getSimpleName(), "append song id:" + simple.id);
                builder.append(simple.id).append(",");
            }
            if (builder.length() > 0) {
                Message message = new Message();
                message.what = 1;
                message.obj = builder.toString();
                handler.sendMessage(message);
            } else {
                Log.d(getClass().getSimpleName(), "not downloading list 1 !");
                stopCheckTimer();
            }
        } else {
            Log.d(getClass().getSimpleName(), "not downloading list 2 !");
            stopCheckTimer();
        }
    }

    public static SongSimple getSongStatus(String songID) {
        return downloadStatus.get(songID);
    }

    public static List<SongSimple> getDownloadingSongs() {
        List<SongSimple> songs = new ArrayList<>();
        for (Map.Entry<String, SongSimple> entry : downloadStatus.entrySet()) {
            SongSimple song = entry.getValue();
            if ((song.downloadStatus == 0 || song.downloadStatus == 2) && song.isCloud == 1)
                songs.add(song);
        }
        return songs;
    }

    public static List<SongSimple> getDownloadFailSongs() {
        List<SongSimple> songs = new ArrayList<>();
        for (Map.Entry<String, SongSimple> entry : downloadStatus.entrySet()) {
            SongSimple song = entry.getValue();
            if (song.downloadStatus >= 3 && song.isCloud == 1)
                songs.add(song);
        }
        return songs;
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            downloadStatusRequest(msg.obj.toString());
            return false;
        }
    });


    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        Log.d(getClass().getSimpleName(), "onSuccess >>>>>>>>>>>>> " + method);
        if (RequestMethod.NODE_CLOUD_DOWNLOAD_PROGRESS.equalsIgnoreCase(method)) {
            SongFileDownloadsV4 downloadsV4;
            if (object instanceof SongFileDownloadsV4 && (downloadsV4 = (SongFileDownloadsV4) object).cloudDownload != null && downloadsV4.cloudDownload.size() > 0) {
                boolean hasChanged = false;
                for (SongDownload download : downloadsV4.cloudDownload) {
                    SongSimple song = getSongStatus(download.songId);
                    if (song != null) {
                        boolean statusChanged = false;
                        if (song.downloadStatus != download.Status) {
                            statusChanged = true;
                            hasChanged = true;
                            Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD_PROGRESS status changed  >>>>>> ");
                        } else if (song.downloadSort != download.sort) {
                            Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD_PROGRESS sort changed  >>>>>> ");
                            hasChanged = true;
                        } else if (song.downloadPercent != download.progress) {
                            Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD_PROGRESS progress changed  >>>>>> ");
                            hasChanged = true;
                        }
                        Log.d(getClass().getSimpleName(), "当前云端下载状态：" + download.Status);
//                        SongSimple newSong = song;
                        song.downloadStatus = download.Status;
                        song.downloadPercent = (int) download.progress;
                        song.isCloud = download.Status == 1 ? 0 : 1;
                        song.downloadSort = download.sort;
                        downloadStatus.put(download.songId, song);
                        Log.d(getClass().getSimpleName(), "当前本地下载状态：" + getSongStatus(download.songId).downloadStatus);

                        if (mCloudSongDownloadListenerList != null && mCloudSongDownloadListenerList.size() > 0) {
                            for (CloudSongDownloadListener listener : mCloudSongDownloadListenerList) {
                                if (statusChanged) {
                                    listener.onSongDownloadStatusChanged(download.songId, getSongStatus(download.songId).downloadStatus);
                                }
                            }
                        }
                    }
                }
                if (hasChanged) {
                    if (mCloudSongDownloadListenerList != null && mCloudSongDownloadListenerList.size() > 0) {
                        for (CloudSongDownloadListener listener : mCloudSongDownloadListenerList) {
                            listener.onSongDownloadChanged(downloadStatus);
                        }
                    }
                }
            }
            startCheckTimer();
        } else if (RequestMethod.NODE_CLOUD_DOWNLOAD.equalsIgnoreCase(method)) {
            CloudSongDownloadResult cloudSongDownloadResult;
            if (object instanceof CloudSongDownloadResult && !TextUtils.isEmpty((cloudSongDownloadResult = (CloudSongDownloadResult) object).id)) {
                Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD >>>>>> id " + cloudSongDownloadResult.id + " song id:" + mSongDownload.id);
                Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD startCheckTimer >>>>>> ");
                mSongDownload.downloadStatus = 0;
                mSongDownload.downloadPercent = 0;
                mSongDownload.downloadSort = 0;
                mSongDownload.isCloud = 1;
                downloadStatus.put(mSongDownload.id, mSongDownload);
                startCheckTimer();
                if (mCloudSongDownloadListenerList != null && mCloudSongDownloadListenerList.size() > 0) {
                    for (CloudSongDownloadListener listener : mCloudSongDownloadListenerList) {
                        Log.d(getClass().getSimpleName(), "VOD_SONG_FILE_DOWNLOAD onSongDownloadChanged >>>>>> ");
                        listener.onSongDownloadChanged(downloadStatus);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_CLOUD_DOWNLOAD_PROGRESS.equalsIgnoreCase(method)) {
            startCheckTimer();
        } else if (RequestMethod.NODE_CLOUD_DOWNLOAD.equalsIgnoreCase(method)) {
            mSongDownload.downloadStatus = 3;
            mSongDownload.downloadPercent = 0;
            mSongDownload.downloadSort = 0;
            mSongDownload.isCloud = 1;
            downloadStatus.put(mSongDownload.id, mSongDownload);
            if (mCloudSongDownloadListenerList != null && mCloudSongDownloadListenerList.size() > 0) {
                for (CloudSongDownloadListener listener : mCloudSongDownloadListenerList) {
                    listener.onSongDownloadChanged(downloadStatus);
                }
            }
        }
    }

    @Override
    public void onError(String method, String error) {

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            check();
        }
    };
}
