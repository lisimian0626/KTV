package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.vod.emoji.Barrage;
import com.bestarmedia.libcommon.model.vod.emoji.Emoji;
import com.bestarmedia.libcommon.model.vod.emoji.EmojiDetail;

import java.util.List;

/**
 * Created by J Wong on 2018/10/9.
 */

public interface EmojiListener {
    //表情包列表
    void onEmoji(List<Emoji> emojiList);

    //表情包详情列表
    void onEmojiDetial(List<EmojiDetail> emojiDetailList);

    //弹幕列表
    void onBarrage(List<Barrage> barrageList);
}
