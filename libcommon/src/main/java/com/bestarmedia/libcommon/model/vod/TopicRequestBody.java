package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class TopicRequestBody implements Serializable {

    @SerializedName("id")
    public String id;//id

    @SerializedName("topics_name")
    public String topicsName;

    public TopicRequestBody(String id, String topicsName) {
        this.id = id;
        this.topicsName = topicsName;
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
