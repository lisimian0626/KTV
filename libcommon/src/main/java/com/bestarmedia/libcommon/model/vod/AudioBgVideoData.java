package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/7/27.
 */

public class AudioBgVideoData implements Serializable {

    @Expose
    @SerializedName("audio_videos")
    public List<AudioBackgroundVideo> audioVideos;

}
