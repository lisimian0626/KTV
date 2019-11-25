package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/7/6.
 */

public class BaseWord {

    @Expose
    @SerializedName("enable_letter")//键盘可点击字母，
    public String enableLetter;

    @Expose
    @SerializedName("next_word")//手写联想下一个字
    public String nextWord;

    @Expose
    @SerializedName("search")//搜索关键词
    public String search;

}
