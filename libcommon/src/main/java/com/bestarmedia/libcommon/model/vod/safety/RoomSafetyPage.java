package com.bestarmedia.libcommon.model.vod.safety;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/7/6.
 */

public class RoomSafetyPage {

    @Expose
    @SerializedName(value = "device_security_detail_list")
    public RoomSafetyList roomSafetyList;

}
