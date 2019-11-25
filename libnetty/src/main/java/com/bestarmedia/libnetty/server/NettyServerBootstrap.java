package com.bestarmedia.libnetty.server;

import android.util.Log;

import com.bestarmedia.libnetty.initializer.NettyServerInitializer;
import com.bestarmedia.libnetty.netty.NettyConfig;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyServerBootstrap {

    private EventLoopGroup master = new NioEventLoopGroup(4);
    private EventLoopGroup worker = new NioEventLoopGroup(4);

    public void start() throws InterruptedException {
        NettyServerMessageHandler.initNettyHandler();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(master, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(NettyConfig.SERVER_PORT))
                .childHandler(new NettyServerInitializer());
        ChannelFuture channelFuture = bootstrap.bind().sync();
        if (channelFuture.isSuccess()) {
            Log.i("NettyServerBootstrap", "Netty服务端启动成功！");
        }
        //channelFuture.channel().closeFuture().sync();
    }

    public void destroy() throws InterruptedException {
        master.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        Log.i("NettyServerBootstrap", "Netty服务端正常关闭！");
    }
}
