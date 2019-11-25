package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/9/28.
 */

public class SongFileDownloadsV4 implements Serializable {

    @SerializedName("download_progress")
    public List<SongDownload> cloudDownload;

}