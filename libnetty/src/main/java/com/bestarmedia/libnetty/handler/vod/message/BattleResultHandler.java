package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.vod.BattleContract;

import io.netty.channel.Channel;


/**
 * 斗歌结果(12880023)
 */
public class BattleResultHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            BattleContract msg = BattleContract.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "斗歌结果id:" + msg.getId());
            EventBusUtil.postSticky(EventBusId.ImId.BATTLE_RESULT, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}