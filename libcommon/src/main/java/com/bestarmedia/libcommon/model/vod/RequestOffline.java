package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestOffline implements Serializable {
    @SerializedName("safety_code")
    public String safetyCode;
    @SerializedName("is_opening")
    public int isOpening;

    public RequestOffline(String safetyCode, int isOpening) {
        this.safetyCode = safetyCode;
        this.isOpening = isOpening;
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
