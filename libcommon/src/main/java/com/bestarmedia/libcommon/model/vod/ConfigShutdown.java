package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/10/16.
 */

public class ConfigShutdown {

    @SerializedName("shutdown_status")
    public int shutdownStatus;

    @SerializedName("shutdown_time")
    public String shutdownTime;

}
