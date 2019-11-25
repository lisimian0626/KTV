package com.bestarmedia.libcommon.model.vod;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:26.
 */
public class PublicBroadcasting implements Serializable {

    public String id;

    public int ordinal;

    public String title;

    public String file_path;

    public int is_play_room_open;

    public int is_play_room_close;

}