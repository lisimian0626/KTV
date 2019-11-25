package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by J Wong on 2018/11/14.
 */

public class SongUpdateLog {


    public List<SongLog> log;

    public static class SongLog {

        @SerializedName("week_of_year")//第几周
        public int weekOfYear;

        @SerializedName("begin_date")//周一日期
        public String beginDate;

        @SerializedName("end_date")//周日日期
        public String endDate;

        @SerializedName("new_song")//新歌
        public long newSong;

        @SerializedName("update")//更新
        public int update;

        @SerializedName("down")//下架
        public int down;
    }
}
