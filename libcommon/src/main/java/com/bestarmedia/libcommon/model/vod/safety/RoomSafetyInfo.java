package com.bestarmedia.libcommon.model.vod.safety;

import com.google.gson.annotations.SerializedName;

public class RoomSafetyInfo {
    public String name;
    public String status;
    @SerializedName("status_val")
    public int statusVal;
}
