package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2016/7/18.
 */
public class MvInfo extends SongSimple {

    @SerializedName("mv_id")
    public int mvId;

    @SerializedName("mv_galleries")
    public String mvGalleries;

    @SerializedName("user_id")
    public int userId;

    public MvInfo(int mvId, String mvGalleries, int userId) {
        this.mvId = mvId;
        this.mvGalleries = mvGalleries;
        this.userId = userId;
    }
}
