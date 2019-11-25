package com.bestarmedia.libcommon.model.vod.emoji;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by J Wong on 2015/10/20 10:10.
 */
public class EmojiDetailBaseListV4 extends BaseListV4 {

    @Expose
    public List<EmojiDetail> data;


}
