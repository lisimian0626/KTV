package com.bestarmedia.libcommon.model.vod;

/**
 * Created by J Wong on 2019/4/17.
 */

public class LightItem {

    //按键名称
    public String name;
    //上行码
    public String inCode;
    //下行码
    public String outCode;
    //模式
    public int mode;
    /**
     * @param name    名称
     * @param inCode  上行码
     * @param outCode 下行码
     */
    public LightItem(String name, String inCode, String outCode,int mode) {
        this.name = name;
        this.inCode = inCode;
        this.outCode = outCode;
        this.mode=mode;
    }
}
