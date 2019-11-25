package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.v4.Song;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/26.
 */

public class SongDetailsV4 implements Serializable {

    @Expose
    public Song song;

}