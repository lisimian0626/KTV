package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/7/27.
 */
public class SongFeedbackArrayV4 implements Serializable {

    public List<SongFeedback> lack_songs;

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
