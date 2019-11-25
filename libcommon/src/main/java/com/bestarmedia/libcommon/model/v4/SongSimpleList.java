package com.bestarmedia.libcommon.model.v4;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongSimpleList extends BaseListV4 {
    @Expose
    @SerializedName("data")
    public List<SongSimple> data;
}
