package com.bestarmedia.libcommon.model.security;

import com.google.gson.annotations.SerializedName;

public class JWTMessage {

    @SerializedName(value = "ktv_net_code")
    public String ktvNetCode;

    @SerializedName(value = "ktv_name")
    public String ktvName;

    @SerializedName(value = "room_code")
    public String roomCode;

    @SerializedName(value = "ktv_room_code")
    public String ktvRoomCode;

    @SerializedName(value = "device_serial_no")
    public String deviceSerialNo;

}
