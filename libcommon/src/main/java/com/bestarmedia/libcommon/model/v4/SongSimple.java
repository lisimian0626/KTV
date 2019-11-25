package com.bestarmedia.libcommon.model.v4;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SongSimple implements Serializable {
    //歌曲id
    @SerializedName("id")
    public String id;
    //歌曲名称
    @SerializedName("song_name")
    public String songName;
    //歌星id
    @SerializedName("singer_mid")
    public List<Integer> singerMid;
    //歌星名称
    @SerializedName("singer")
    public List<String> singer;
    //视频版本
    @SerializedName("video_type")
    public String videoType;
    //是否高清
    @SerializedName("is_hd")
    public int isHd;
    //是否评分
    @SerializedName("is_score")
    public int isScore;
    //是否云歌曲
    @SerializedName("is_cloud")
    public int isCloud;
    //主要歌词
    @SerializedName("main_lyric")
    public String mainLyric;
    //热度
    @SerializedName("hot")
    public long hot;
    //是否付费歌曲
    @SerializedName("is_pay")
    public int isPay;
    //是否红包歌曲
    @SerializedName("is_red_packet")
    public int isRedPacket;
    //0-排队;1-完成;2-下载中;3>=失败;
    @SerializedName("download_status")
    public int downloadStatus = 1;
    @SerializedName("download_percent")
    public int downloadPercent = 0;
    @SerializedName("download_sort")
    public int downloadSort = 0;
    @SerializedName("is_show_singer")
    public boolean isShowSingers = false;
    @SerializedName("is_to_top")
    public boolean isToTop = false;

    public SongSimple() {
    }

    public SongSimple(String id, String songName, List<Integer> singerMid, List<String> singer, String videoType,
                      int isHd, int isScore, int isCloud, String mainLyric, long hot, int isPay) {
        this.id = id;
        this.songName = songName;
        this.singerMid = singerMid;
        this.singer = singer;
        this.videoType = videoType;
        this.isHd = isHd;
        this.isScore = isScore;
        this.isCloud = isCloud;
        this.mainLyric = mainLyric;
        this.hot = hot;
        this.isPay = isPay;
    }


    public String getSingerId() {
        StringBuilder builder = new StringBuilder();
        try {
            if (singer != null) {
                for (Integer id : singerMid) {
                    builder.append(id).append("|");
                }
            }
        } catch (Exception e) {
            Log.e("SongSimple", "获取歌星id异常：" + e.toString());
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "";
    }

    public String getSingerName() {
        StringBuilder builder = new StringBuilder();
        try {
            if (singer != null) {
                for (String name : singer) {
                    builder.append(name).append("|");
                }
            }
        } catch (Exception e) {
            Log.e("SongSimple", "获取歌星名异常：" + e.toString());
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "";
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
