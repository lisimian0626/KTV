package com.bestarmedia.libcommon.model.vod.emoji;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class EmojiDetailV4 extends BaseListV4 implements Serializable {

    @SerializedName("emoji_detail")
    public EmojiDetailBaseListV4 emojiDetailList;

}
