package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class RoomV4 implements Serializable {

    @SerializedName("room")
    public NodeRoomV4 room;

}
