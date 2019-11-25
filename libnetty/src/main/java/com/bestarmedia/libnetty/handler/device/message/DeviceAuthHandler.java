package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.libnetty.server.NettyServerChannelManager;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class DeviceAuthHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.DeviceAuthRequest msg = DeviceProto.DeviceAuthRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "客户端注册信息serial: " + msg.getSerialNo() + " device_type:" + msg.getDeviceType());
            NettyServerChannelManager.set(msg.getSerialNo(), channel);
            DeviceProto.DeviceAuthResponse response = DeviceProto.DeviceAuthResponse.newBuilder().setCode(DeviceProto.DeviceAuthResponse.CodeType.SUCCESS_VALUE).build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H15000004.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }
}
