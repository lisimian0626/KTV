package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class CloudSongDownload implements Serializable {

    @SerializedName(value = "song_id")
    private int songId;

    @SerializedName(value = "song_name")
    private String songName;

    @SerializedName(value = "song_file_path")
    private String songFilePath;

    @SerializedName(value = "download_url")
    private String downloadUrl;

    @SerializedName(value = "song_file_md5")
    private String songFileMd5;

    @SerializedName(value = "song_version")
    private String songVersion;

    @SerializedName(value = "singer_id")
    private String singerId;

    @SerializedName(value = "singer_name")
    private String singerName;

    @SerializedName(value = "room_code")
    private String roomCode;

    @SerializedName(value = "ktv_room_code")
    private String ktvRoomCode;

    public CloudSongDownload() {
    }

    public CloudSongDownload(Integer songId, String songName, String songFilePath, String downloadUrl, String songFileMd5,
                             String songVersion, String singerId, String singerName, String roomCode, String ktvRoomCode) {
        this.songId = songId;
        this.songName = songName;
        this.songFilePath = songFilePath;
        this.downloadUrl = downloadUrl;
        this.songFileMd5 = songFileMd5;
        this.songVersion = songVersion;
        this.singerId = singerId;
        this.singerName = singerName;
        this.roomCode = roomCode;
        this.ktvRoomCode = ktvRoomCode;
    }

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
