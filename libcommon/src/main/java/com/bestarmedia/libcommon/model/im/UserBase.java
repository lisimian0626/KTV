package com.bestarmedia.libcommon.model.im;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/6/16.
 */
public class UserBase implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("avatar")
    public String avatar;

    /**
     * 0：未保存；1：已经保存;2保存中
     */
    @Expose
    public int saveStatus = 0;
    
    public UserBase(int id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }


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
