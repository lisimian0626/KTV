package com.bestarmedia.libcommon.model.v4;

import com.bestarmedia.libcommon.model.vod.BaseWord;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/7/6.
 */

public class StartSimplePage {

    @Expose
    @SerializedName(value = "musician")
    public StartSimpleList musician;

    @Expose
    @SerializedName(value = "active_word")
    public BaseWord activeWord;
}
