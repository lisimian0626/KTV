package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/28.
 */

public class Scene implements Serializable {


    public String ID;

    public String SceneName;

    @SerializedName("SceneFileUrl")
    public String SceneFile;

    @SerializedName("SceneImgUrl")
    public String SceneImg;

}