package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class PkRequestBody implements Serializable {
    @Expose
    public int id;
    //    @Expose
//    public int song_id;
//    @Expose
//    public String song_name;
//    @Expose
//    public String singer_name;
//    @Expose
//    public String launch_ktv_net_code;
//    @Expose
//    public String launch_ktv_net_name;
//    @Expose
//    public String launch_room_code;
//    @Expose
//    public String launch_room_name;
//    @Expose
//    public String launch_room_session;
//    @Expose
//    public int launch_user_id;
//    @Expose
//    public String launch_user_name;
//    @Expose
//    public String launch_user_avatar;
//    @Expose
//    public int launch_user_sex;
//    @Expose
//    public float launch_score;
//    @Expose
//    public String launch_slogan;
    @Expose
    public String accept_ktv_net_code;
    @Expose
    public String accept_ktv_net_name;
    @Expose
    public String accept_room_code;
    @Expose
    public String accept_room_name;
    @Expose
    public String accept_room_session;
    @Expose
    public int accept_user_id;
    @Expose
    public String accept_user_name;
    @Expose
    public String accept_user_avatar;
//    @Expose
//    public int accept_user_sex;
//    @Expose
//    public float accept_score;
//    //0无效 1可应战 2应战中 3结束 4记胜负
//    @Expose
//    public int status;
//    @Expose
//    public Date acceptAt;

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
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
