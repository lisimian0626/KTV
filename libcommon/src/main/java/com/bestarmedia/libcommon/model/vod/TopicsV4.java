package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/19 16:53.
 */
public class TopicsV4 implements Serializable {

    @Expose
    @SerializedName("song-list")
    public TopicBaseListV4 topic;

}
