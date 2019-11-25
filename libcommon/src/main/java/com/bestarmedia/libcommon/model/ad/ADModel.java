package com.bestarmedia.libcommon.model.ad;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ADModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ad_position")
    private String adPosition;

    @SerializedName("ad_content")
    private String adContent;

    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("beacon")
    private Beacon beacon;

    @SerializedName("ad_type_id")
    private int adTypeId;

    @SerializedName("ori_singer")
    private List<String> oriSinger;

    @SerializedName("naming_song_list_id")
    private String namingSongListId;

    @SerializedName("naming_song_list_type")
    private String namingSongListType;

    public static class Beacon {
        @SerializedName("uuid")
        public String uuid;
        @SerializedName("majro")
        public String major;
        @SerializedName("minro")
        public String minor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public int getAdTypeId() {
        return adTypeId;
    }

    public void setAdTypeId(int adTypeId) {
        this.adTypeId = adTypeId;
    }

    public List<?> getOriSinger() {
        return oriSinger;
    }

    public void setOriSinger(List<String> oriSinger) {
        this.oriSinger = oriSinger;
    }

    public String getNamingSongListId() {
        return namingSongListId;
    }

    public void setNamingSongListId(String namingSongListId) {
        this.namingSongListId = namingSongListId;
    }

    public String getNamingSongListType() {
        return namingSongListType;
    }

    public void setNamingSongListType(String namingSongListType) {
        this.namingSongListType = namingSongListType;
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
