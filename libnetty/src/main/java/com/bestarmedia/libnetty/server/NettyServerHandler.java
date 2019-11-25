package com.bestarmedia.libnetty.server;


import android.util.Log;

import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<ProtoFrame> {

    private final static String TAG = "NettyServerHandler";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.i(TAG, "建立连接: " + ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.i(TAG, "断开连接: " + ctx.channel().remoteAddress());
        NettyServerChannelManager.del(ctx.channel());
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoFrame msg) {
        //将消息转给对应的 handler 处理
        try {
            Log.i(TAG, "收到消息： " + msg.id);
            String className = NettyMessageEnum.valueOf("H" + msg.id).messageHandlerInterface.getSimpleName();
            NettyServerMessageHandler.getProtoHandler(className).handle(ctx.channel(), msg);
        } catch (Exception e) {
            Log.e(TAG, "消息：" + msg.id + " 处理错误:" + e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.e(TAG, "连接异常: ", cause);
        NettyServerChannelManager.del(ctx.channel());
        ctx.channel().close();
        super.exceptionCaught(ctx, cause);
    }
}
