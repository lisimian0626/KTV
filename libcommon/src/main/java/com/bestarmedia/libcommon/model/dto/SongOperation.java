package com.bestarmedia.libcommon.model.dto;

import android.view.View;

import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SongOperation implements Serializable {

    @SerializedName("is_top")
    public int isTop;//是否优先

    @SerializedName("song")
    public SongSimple song;

    @SerializedName("view")
    public View view;

    @SerializedName("choose_id")//已点记录id
    public String chooseId;

    @SerializedName("user")
    public UserBase userBase;

    @SerializedName("mv_info")
    public MvInfo mvInfo;


    public SongOperation(int isTop, SongSimple song, View view, String chooseId, UserBase userBase, MvInfo mvInfo) {
        this.isTop = isTop;
        this.song = song;
        this.view = view;
        this.chooseId = chooseId;
        this.userBase = userBase;
        this.mvInfo = mvInfo;
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
