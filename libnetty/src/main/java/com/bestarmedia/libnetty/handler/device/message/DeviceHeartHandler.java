package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class DeviceHeartHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            Log.i(NettyConfig.TAG, "收到客户端心跳：" + channel.remoteAddress());
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }
}
