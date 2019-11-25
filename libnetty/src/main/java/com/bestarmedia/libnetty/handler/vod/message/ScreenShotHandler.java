package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.node.VodScreenShotRequest;

import io.netty.channel.Channel;


/**
 * 截图
 */
public class ScreenShotHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            VodScreenShotRequest msg = VodScreenShotRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "截图 room_code:" + msg.getRoomCode() + " address:" + msg.getAddress()
                    + " platform:" + msg.getPlatform() + " user_id:" + msg.getUserId() + " " + msg.getEndpoint());
            EventBusUtil.postSticky(EventBusId.ImId.SCREEN_SHOT, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }
}
