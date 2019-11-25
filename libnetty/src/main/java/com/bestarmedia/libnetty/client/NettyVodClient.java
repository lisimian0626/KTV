package com.bestarmedia.libnetty.client;


import android.util.Log;

import com.bestarmedia.libnetty.initializer.NettyVodClientInitializer;
import com.bestarmedia.libnetty.netty.ProtoFrame;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyVodClient {

    private String TAG = NettyVodClient.class.getSimpleName();
    private static NettyVodClient nettyDeviceClient;
    private String host;
    private int port;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;

    public static NettyVodClient getInstance() {
        if (nettyDeviceClient == null) {
            synchronized (NettyVodClient.class) {
                if (nettyDeviceClient == null) {
                    nettyDeviceClient = new NettyVodClient();
                }
            }
        }
        return nettyDeviceClient;
    }

    private NettyVodClient() {
    }

    public void setHostPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void socketClientBootstrap() {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(4))
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new NettyVodClientInitializer());
    }


    private class InitNettyThread extends Thread {
        @Override
        public void run() {
            super.run();
            connect();
        }
    }

    public void connect() {
        if (bootstrap == null) {
            socketClientBootstrap();
        }
        channelFuture = bootstrap.connect();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    Log.e(TAG, "客户端 5 秒后重连:" + channelFuture.cause().getMessage());
                    reconnect();
                } else {
                    Log.i(TAG, "客户端 Netty 连接成功");
                }
            }
        });
    }

    private void reconnect() {
        Log.e(TAG, "客户端 5 秒后重连 >>>>>>>>>>>>>>>>>>>>>> ");
        final EventLoop loop = channelFuture.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "服务端链接不上，开始重连操作...");
                connect();
            }
        }, 5L, TimeUnit.SECONDS);
    }

    public void start() {
        new InitNettyThread().start();
    }


    public void stop() {
        try {//断开连接
            channelFuture.channel().closeFuture().sync();
            bootstrap.config().group().shutdownGracefully();
            bootstrap = null;
            channelFuture = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "stop: " + e.getMessage());
        }
    }

    /**
     * 发生消息
     *
     * @param protoFrame
     */
    public void sendMessage(ProtoFrame protoFrame) {
        if (NettyVodClientChannelManager.getInstance() != null) {
            NettyVodClientChannelManager.getInstance().writeAndFlush(protoFrame);
        }
    }
}
