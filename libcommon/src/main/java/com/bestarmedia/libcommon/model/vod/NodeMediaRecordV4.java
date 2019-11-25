package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class NodeMediaRecordV4 implements Serializable {

    @SerializedName("media")
    public NodeMediaRecord mediaRecord;

}
