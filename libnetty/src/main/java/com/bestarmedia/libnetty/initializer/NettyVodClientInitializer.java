package com.bestarmedia.libnetty.initializer;


import com.bestarmedia.libnetty.codec.MessageLiteDecoder;
import com.bestarmedia.libnetty.codec.MessageLiteEncoder;
import com.bestarmedia.libnetty.handler.vod.NettyVodClientHandler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;


public class NettyVodClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(10485760, 4, 4, 0, 0));
        pipeline.addLast(new MessageLiteDecoder());
        pipeline.addLast(new MessageLiteEncoder());
        pipeline.addLast(new NettyVodClientHandler());
    }
}
