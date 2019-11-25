package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NodeRoom {

    @Expose
    public String id;

    @Expose
    @SerializedName("room_code")
    public String roomCode;

    @Expose
    @SerializedName("ktv_room_code")
    public String ktvRoomCode;
    @Expose
    @SerializedName("room_device")
    public List<RoomDevice> roomDeviceList;
}
