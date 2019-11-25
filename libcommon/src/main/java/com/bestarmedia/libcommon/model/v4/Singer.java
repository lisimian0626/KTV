package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Singer implements Serializable {


    /**
     * id : 8
     * musician_name : 李克勤
     * img_file_path : https://test-newcms.oss-cn-shenzhen.aliyuncs.com/singer_pic/b156257f83f5d0530d3eccc7579adc55.png
     * local_img_file_path : /data/Img/SingerImg/9304132.jpg
     * hot : 0
     * simp_name : LKQ
     * word_count : 3
     * sex : 1
     * birthday : 2019-05-30 00:00:00
     * role : 0
     * band : 0
     * part : 2
     * part_name : 香港
     * img_file_url : http://172.30.1.231:10230/data/Img/SingerImg/9304132.jpg
     */

    public int id;
    @SerializedName("musician_name")
    public String musicianName;
    @SerializedName("img_file_path")
    public String imgFilePath;
    @SerializedName("local_img_file_path")
    public String localImgFilePath;
    public int hot;
    @SerializedName("simp_name")
    public String simpName;
    @SerializedName("word_count")
    public int wordCount;
    public int sex;
    public String birthday;
    public int role;
    public int band;
    public int part;
    @SerializedName("part_name")
    public String partName;
    @SerializedName("img_file_url")
    public String imgFileUrl;


}
