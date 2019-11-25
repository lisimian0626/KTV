package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Start implements Serializable {


    /**
     * id : 1
     * musician_name : 张学友
     * img_file_path : https://test-newcms.oss-cn-shenzhen.aliyuncs.com/singer_pic/6a817311fa4ce3e90c69e4336853c654.png
     * local_img_file_path : /data/Img/SingerImg/9304132.jpg
     * hot : 0
     * img_file_url : http://172.30.1.230:10230/data/Img/SingerImg/9304132.jpg
     */
    @Expose
    public int id;
    @Expose
    @SerializedName("musician_name")
    public String musicianName;
    @Expose
    @SerializedName("img_file_path")
    public String imgFilePath;
    @Expose
    @SerializedName("localImgFilePath")
    public String local_img_file_path;
    @Expose
    public int hot;
    @Expose
    @SerializedName("img_file_url")
    public String imgFileUrl;

    public static List<Start> arrayStartFromData(String str) {

        Type listType = new TypeToken<ArrayList<Start>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
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
