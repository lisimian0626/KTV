package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/8/24.
 */

public class RoomSongList implements Serializable {

    @Expose
    public List<RoomSongItem> songs;//已点

    @Expose
    public List<RoomSongItem> sung;//已唱
}
