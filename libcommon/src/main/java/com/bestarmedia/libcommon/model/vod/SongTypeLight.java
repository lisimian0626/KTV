package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

public class SongTypeLight {

    @SerializedName("song_type_light")
    public LightMode songTypeLight;

    public static class LightMode {
        @SerializedName("light_mode")
        public int lightMode;
        @SerializedName("light_name")
        public String lightName;
    }
}
