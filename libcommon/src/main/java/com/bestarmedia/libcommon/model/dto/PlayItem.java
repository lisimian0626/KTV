package com.bestarmedia.libcommon.model.dto;//package com.bestarmedia.libcommon.model.dto;

import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayItem implements Serializable {

    @SerializedName("playing")
    public RoomSongItem play;

    @SerializedName("next")
    public RoomSongItem next;

    public PlayItem(RoomSongItem play, RoomSongItem next) {
        this.play = play;
        this.next = next;
    }

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
