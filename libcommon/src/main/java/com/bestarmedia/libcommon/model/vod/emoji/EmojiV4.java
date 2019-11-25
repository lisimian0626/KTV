package com.bestarmedia.libcommon.model.vod.emoji;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class EmojiV4 implements Serializable {

    @SerializedName("emoji")
    public EmojiBaseListV4 emojiList;

}
