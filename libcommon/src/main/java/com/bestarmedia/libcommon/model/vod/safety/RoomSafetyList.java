package com.bestarmedia.libcommon.model.vod.safety;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomSafetyList extends BaseListV4 {
    @Expose
    @SerializedName("data")
    public List<RoomSafety> data;
}
