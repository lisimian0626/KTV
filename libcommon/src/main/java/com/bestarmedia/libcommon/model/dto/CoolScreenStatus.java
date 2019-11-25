package com.bestarmedia.libcommon.model.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CoolScreenStatus implements Serializable {

    @SerializedName("is_open")
    public int isOpen;
    @SerializedName("is_random")
    public int isRandom = 0;
    @SerializedName("current_id")
    public int currentId = 0;
    @SerializedName("current_url")
    public String currentUrl = "";

    public CoolScreenStatus() {
    }

    public CoolScreenStatus(int isOpen, int isRandom, int currentId, String currentUrl) {
        this.isOpen = isOpen;
        this.isRandom = isRandom;
        this.currentId = currentId;
        this.currentUrl = currentUrl;
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
