package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.libnetty.server.NettyServerChannelManager;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class DeviceAuthResponseHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.DeviceAuthResponse msg = DeviceProto.DeviceAuthResponse.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "设备认证结果：" + msg.getCode());
//            NettyServerChannelManager.set(msg.getDeviceId(), channel);//记录连接
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }
}
