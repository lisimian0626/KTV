package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class GameStartProtoHandler implements ProtoHandlerInterface<ProtoFrame> {
    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.GameStart msg = DeviceProto.GameStart.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "收到游戏开始信息 关卡:" + msg.getLevel() + " 页码:" + msg.getPage() + " 位置：" + msg.getPosition());
            EventBusUtil.postSticky(EventBusId.DeviceId.GAME_START, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
