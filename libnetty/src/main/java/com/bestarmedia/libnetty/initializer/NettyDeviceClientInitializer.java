package com.bestarmedia.libnetty.initializer;


import com.bestarmedia.libnetty.codec.MessageLiteDecoder;
import com.bestarmedia.libnetty.codec.MessageLiteEncoder;
import com.bestarmedia.libnetty.handler.device.NettyDeviceClientHandler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


public class NettyDeviceClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
        channelPipeline.addLast(new MessageLiteDecoder());
        channelPipeline.addLast(new MessageLiteEncoder());
        channelPipeline.addLast(new NettyDeviceClientHandler());
    }
}
