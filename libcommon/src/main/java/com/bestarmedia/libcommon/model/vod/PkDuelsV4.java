package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/7/6.
 */

public class PkDuelsV4 implements Serializable {
    @Expose
    @SerializedName("song_duel")
    public PkDuelBaseListV4 songDuel;
}
