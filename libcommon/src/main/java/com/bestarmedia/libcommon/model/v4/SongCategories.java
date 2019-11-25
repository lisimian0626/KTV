package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/7/6.
 */

public class SongCategories implements Serializable {

    @Expose
    public List<SongCate> song_type;

}
