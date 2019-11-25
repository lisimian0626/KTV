package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.node.RoomStatusBroadcast;

import io.netty.channel.Channel;


/**
 * 房间状态变化推送
 */
public class RoomStatusBroadcastHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            RoomStatusBroadcast msg = RoomStatusBroadcast.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "房间状态 op:" + msg.getOp() + " room_code:" + msg.getRoomCode());
            EventBusUtil.postSticky(EventBusId.ImId.ROOM_STATUS_CHANGED, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
