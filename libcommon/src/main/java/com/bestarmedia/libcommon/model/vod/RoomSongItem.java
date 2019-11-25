package com.bestarmedia.libcommon.model.vod;

import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J Wong on 2018/10/26.
 */
public class RoomSongItem {

    @SerializedName(value = "uuid")
    public String uuid;//记录UUID

    @SerializedName("status")
    public int status;//歌曲状态 0未唱(已点) 1已唱

    @SerializedName("song_code")
    public String songCode;//歌曲编号

    @SerializedName("simp_name")
    public String simpName;//歌曲编号

    @SerializedName("song_type")
    public int songType;//曲种

    @SerializedName("song_version")
    public String songVersion;//歌曲版本

    @SerializedName("language_type")
    public String languageType;//语种

//    @SerializedName("is_ad_song")
//    public int isAdSong;//是否广告歌 0否 1是

    @SerializedName("ad_id")
    public int adId;//广告id

    @SerializedName("singer_id")
    public String singerId;//歌手ID，多个用"|"分隔

    @SerializedName("singer_name")
    public String singerName;//歌手名称,多个用"|"分隔

    @SerializedName("type")
    public int type;//类型：-4评分结果,-3插播视频；-2公益广告；-1贴片广告；0歌曲；1电影；2直播；

    @SerializedName("grade_lib_file")
    public String gradeLibFile;//评分文件名称

    @SerializedName("song_file_path")
    private String songFilePath;//歌曲文件相对路径

    @SerializedName("song_url")
    public String songUrl;//歌曲文件绝对路径

    @SerializedName("volume")
    public int volume;//歌曲均衡音量，取值范围0-100

    @SerializedName("audio_track")
    public int audioTrack;//原伴唱信息；1一轨原唱，2二轨原唱，3左原唱，4右原唱，5纯欣赏

//    @SerializedName("light_effect")
//    public int lightEffect;//灯光

    @SerializedName("is_hd")
    public int isHd;//是否高清歌曲：0否，1是

    @SerializedName("is_red_packet")
    public int isRedPacket;//是否红包歌曲：0否，1是

    @SerializedName("mv_id")
    public int mvId;//自制MV id

    @SerializedName("galleries")
    public String galleries;//自制MV图片url集合，多张图片以“|”分隔

    @SerializedName("score")
    public int score;//评分歌曲最后演唱得分，取值范围0-100

    @SerializedName("record_file")
    public String recordFile;//已唱歌曲录音文件名称

    @SerializedName("user_id")
    public int userId;//手机点歌用户ID

    @SerializedName("user_name")
    public String userName;//手机点歌用户昵称

    @SerializedName("user_avatar")
    public String userAvatar;//手机点歌用户头像url

    @SerializedName("win_percent")
    public int winPercent;//战胜百分比

    @SerializedName("has_song_file")
    public int hasSongFile;

    @SerializedName("is_to_top")
    public boolean isToTop;

    @SerializedName("ad_position")
    public String adPosition;

    @SerializedName("duration")
    public long duration;

    @SerializedName("current")
    public long current;


    public Movie toMovieDetail() {
        Movie detail = new Movie();
        detail.MovieFilePathUrl = songUrl;
        detail.SimpName = simpName;
        detail.ID = songCode;
        return detail;
    }

    public Live toLiveDetail() {
        Live detail = new Live();
        detail.live_address = songUrl;
        detail.live_name = simpName;
        detail.id = songCode;
        return detail;
    }


    private List<Integer> getSingerIdList() {
        List<Integer> singerIdList = new ArrayList<>();
        try {
            String[] ids = singerId.split("\\|");
            if (ids.length > 0) {
                for (String id : ids) {
                    singerIdList.add(Integer.parseInt(id));
                }
            }
        } catch (Exception e) {
            Log.e("RoomSongItem", "歌手id串转list异常：" + e.toString());
        }
        return singerIdList;
    }

    private List<String> getSingerNameList() {
        List<String> singerNameList = new ArrayList<>();
        String[] ids = singerName.split("\\|");
        try {
            Collections.addAll(singerNameList, ids);
        } catch (Exception e) {
            Log.e("RoomSongItem", "歌手名字串转list异常：" + e.toString());
        }
        return singerNameList;
    }

    public com.bestarmedia.libcommon.model.v4.SongSimple toSongSimple() {
        String gradeLibFile1 = "";
        String gradeLibFile2 = "";
        if (!TextUtils.isEmpty(this.gradeLibFile)) {
            String[] gradeLibFiles = gradeLibFile.split("\\|");
            gradeLibFile1 = gradeLibFiles.length > 0 ? gradeLibFiles[0] : "";
            gradeLibFile2 = gradeLibFiles.length > 1 ? gradeLibFiles[1] : "";
        }
        return new SongSimple(songCode, simpName, getSingerIdList(),
                getSingerNameList(), songVersion, isHd, TextUtils.isEmpty(gradeLibFile1) || TextUtils.isEmpty(gradeLibFile2) ? 0 : 1, 0, "", 0, 0);
    }

    public String getGradeLibFile1() {
        String gradeLibFile1 = "";
        if (!TextUtils.isEmpty(this.gradeLibFile)) {
            String[] gradeLibFiles = gradeLibFile.split("\\|");
            gradeLibFile1 = gradeLibFiles.length > 0 ? gradeLibFiles[0] : "";
        }
        return gradeLibFile1;
    }

    public String getGradeLibFile2() {
        String gradeLibFile2 = "";
        if (!TextUtils.isEmpty(this.gradeLibFile)) {
            String[] gradeLibFiles = gradeLibFile.split("\\|");
            gradeLibFile2 = gradeLibFiles.length > 1 ? gradeLibFiles[1] : "";
        }
        return gradeLibFile2;
    }
}
