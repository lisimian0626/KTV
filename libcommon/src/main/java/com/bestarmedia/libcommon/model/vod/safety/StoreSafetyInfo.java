package com.bestarmedia.libcommon.model.vod.safety;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreSafetyInfo implements Serializable {
    public int network;
    @SerializedName("begin_at")
    public String beginAt;

    @SerializedName("end_at")
    public String endAt;

    @SerializedName("is_opening")
    public String isOpening;

    @SerializedName("safety_code")
    public String safetyCode;

    @SerializedName("check_at")
    public String checkAt;
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
