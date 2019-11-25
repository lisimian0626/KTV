package com.bestarmedia.libcommon.model.ad;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ResourceRequestBody {

    @SerializedName(value = "action")
    public int action;
    @SerializedName(value = "media_name")
    public String mediaName;

    public ResourceRequestBody(int action, String mediaName) {
        this.action = action;
        this.mediaName = mediaName;
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
