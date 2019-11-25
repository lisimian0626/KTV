package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.node.FireAlarmRequest;
import com.bestarmedia.proto.node.FireAlarmResponse;

import io.netty.channel.Channel;


/**
 * 火警
 */
public class FireAlarmHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            FireAlarmRequest msg = FireAlarmRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "收到火警警报信息 op:" + msg.getOp());
            EventBusUtil.postSticky(EventBusId.ImId.FIRE_ALARM, msg.getOp());
            FireAlarmResponse response = FireAlarmResponse.getDefaultInstance().toBuilder()
                    .setCode(1).setOp(msg.getOp()).setRoomCode(VodConfigData.getInstance().getRoomCode())
                    .build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H11000014.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
