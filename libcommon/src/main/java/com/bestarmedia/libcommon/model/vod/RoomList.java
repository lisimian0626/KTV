package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/9/30.
 */

public class RoomList implements Serializable {

    @SerializedName("room_list")
    public List<NodeRoom> roomList;

}
