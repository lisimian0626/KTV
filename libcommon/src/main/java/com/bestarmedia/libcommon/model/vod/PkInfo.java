package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/9/8.
 */
public class PkInfo implements Serializable {
    public UserBase userBase1;
    public UserBase userBase2;
    public Song song;

    public PkInfo(UserBase userBase1, UserBase userBase2, Song song) {
        this.userBase1 = userBase1;
        this.userBase2 = userBase2;
        this.song = song;
    }
}
