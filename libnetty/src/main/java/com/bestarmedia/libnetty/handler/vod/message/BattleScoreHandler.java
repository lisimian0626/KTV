package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.vod.ScoreToOpponent;

import io.netty.channel.Channel;


/**
 * 将我方斗歌过程中的分数发给对方(12880024)
 */
public class BattleScoreHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            ScoreToOpponent msg = ScoreToOpponent.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "对方斗歌得分:" + msg.getCurrentUser().getScore());
            EventBusUtil.postSticky(EventBusId.ImId.BATTLE_SCORE, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
