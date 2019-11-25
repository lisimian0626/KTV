package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2017/9/19.
 */

public class SongDownload implements Serializable {

    @SerializedName("song_id")
    public String songId;

    //0-排队;1-完成;2-下载中;3>=失败;
    @SerializedName("status")
    public int Status = 0;

    @SerializedName("progress")
    public float progress;

    @SerializedName("sort")
    public int sort = 0;

}
