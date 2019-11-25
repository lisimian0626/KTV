package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/10 11:13.
 */
public class Pk implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("song_id")
    public String songId;
    @SerializedName("song_name")
    public String songName;
    @SerializedName("singer_name")
    public String singerName;

    @SerializedName("launch_ktv_net_code")
    public String launchKtvNetCode;
    @SerializedName("launch_ktv_net_name")
    public String launchKtvNetName;
    @SerializedName("launch_room_code")
    public String launchRoomCode;
    @SerializedName("launch_room_name")
    public String launchRoomName;
    @SerializedName("launch_room_session")
    public String launchRoomSession;

    @SerializedName("launch_user_id")
    public int launchUserId;
    @SerializedName("launch_user_name")
    public String launchUserName;
    @SerializedName("launch_user_avatar")
    public String launchUserAvatar;
    @SerializedName("launch_user_sex")
    public int launchUserSex;
    @SerializedName("launch_score")
    public float launchScore;
    @SerializedName("launch_slogan")
    public String launchSlogan;

    @SerializedName("accept_ktv_net_code")
    public String acceptKtvNetCode;
    @SerializedName("accept_ktv_net_name")
    public String acceptKtvNetName;
    @SerializedName("accept_room_code")
    public String acceptRoomCode;
    @SerializedName("accept_room_name")
    public String acceptRoomName;
    @SerializedName("accept_room_session")
    public String acceptRoomSession;

    @SerializedName("accept_user_id")
    public int acceptUserId;
    @SerializedName("accept_user_name")
    public String acceptUserName;
    @SerializedName("accept_user_avatar")
    public String acceptUserAvatar;
    @SerializedName("accept_user_sex")
    public int acceptUserSex;
    @SerializedName("accept_score")
    public float acceptScore;

    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("accept_at")
    public String acceptAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("over_at")
    public String overAt;

    @SerializedName("status")
    public int status;//0已过期，1可应战，2应战中，3已完成

    @SerializedName("expire_time")//剩余时间，单位秒
    public int expireTime;


}
