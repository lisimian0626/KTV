package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/6 09:10.
 */
public class PlayerStatus implements Serializable {

    /**
     * 评分模式：0：关闭  1：娱乐模式  2：专业模式
     */
    public int scoreMode = 1;

    public boolean isPlaying = true;

//    public boolean isAccom = true;
    /**
     * 0:伴唱 1：原唱 2：保持原唱
     */
    public boolean originOn;

//    public int micVol = 50;
//
//    public int musicVol = 50;

    public int tone;

    public boolean isMute = false;

    /**
     * 0:标准 1：明亮 ；2：柔和 ；3：抒情 4：动感；5浪漫；6朦胧；7演唱会；8激情；9清洁,10 全开,11全关
     */
//    public int lightMode = 0;
    //当前灯光模式
    public LightItem lightItem;
//    public boolean isServing;

    public boolean isLightOn = true;

    /***
     * 0:广告：1：歌曲 2:电影 3:直播 4:贴片,5:转场
     */
    public int playingType;

    public int volMusic;

    public int serviceMode = -1;

    /**
     * 歌曲灯光状态
     */
    public boolean isLightAuto = true;

    /**
     * 电视屏黑屏状态
     */
    public boolean isHdmiBlack = false;

    //是否开启录音
    public boolean isRecord;

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
