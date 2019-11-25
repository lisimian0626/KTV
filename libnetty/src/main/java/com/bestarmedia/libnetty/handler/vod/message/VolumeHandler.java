package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.node.VolumeRequest;

import io.netty.channel.Channel;


/**
 * 定时降音量
 */
public class VolumeHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            VolumeRequest msg = VolumeRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "定时降音量 op:" + msg.getOp() + " volume:" + msg.getVolume());
            EventBusUtil.postSticky(EventBusId.ImId.VOLUME_CONTROL, msg.getVolume());
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
