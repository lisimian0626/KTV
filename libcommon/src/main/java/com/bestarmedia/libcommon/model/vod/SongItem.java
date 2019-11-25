package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/10/26.
 */

public class SongItem {

    @SerializedName("id")//id
    public String id;

    @SerializedName("simp_name")//歌曲名称
    public String simpName;

    @SerializedName("singer_name")//歌星名
    public String singerName;

    @SerializedName("singer_id")//歌星ID
    public String singerId;

    @SerializedName("song_version")//歌曲版本
    public String songVersion;

    @SerializedName("is_hd")//是否高清
    public int isHd;

    @SerializedName("has_song_file")//是否有文件
    public int hasSongFile;

    @SerializedName("is_red_envelopes")//是否红包歌曲
    public int isRedEnvelopes;

    @SerializedName("lyric")//歌词
    public String lyric;

    @SerializedName("is_pay")//是否付费歌曲
    public int isPay;

    @SerializedName("grade_lib_file")//是否评分歌曲
    public String gradeLibFile;

    public boolean isShowSingers = false;


    //0-排队;1-完成;2-下载中;3>=失败;
    public int downloadStatus = 1;

    public int downloadPercent = 0;

    public int downloadSort = 0;

    public boolean isToTop = false;

}
