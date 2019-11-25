package com.bestarmedia.libnetty.handler.device;

import android.util.Log;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libnetty.client.NettyDeviceClient;
import com.bestarmedia.libnetty.client.NettyDeviceClientChannelManager;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.device.DeviceProto;
import com.google.protobuf.Empty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 包房内设备间相互通讯处理类
 */
public class NettyDeviceClientHandler extends SimpleChannelInboundHandler<ProtoFrame> {

    private final static String TAG = "NettyClientHandler";
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.i(TAG, "建立连接: " + ctx.channel().remoteAddress().toString());
        NettyDeviceClientMessageHandler.initNettyHandler();
        NettyDeviceClientChannelManager.setInstance(ctx.channel());
        sendBoxAuth();
        startTimer();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.e(TAG, "断开连接: " + ctx.channel().remoteAddress().toString() + " 5秒后重连...");
        stopTimer();
        NettyDeviceClientChannelManager.delInstance();
        //使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                NettyDeviceClient.getInstance().connect();
                Log.i(TAG, "run: 重连>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }, 5L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoFrame msg) {
        Log.i(TAG, "收到消息 id: " + msg.id);
        //将消息转给对应的 handler 处理
        try {
            String className = NettyMessageEnum.valueOf("H" + msg.id).messageHandlerInterface.getSimpleName();
            NettyDeviceClientMessageHandler.getProtoHandler(className).handle(ctx.channel(), msg);
        } catch (Exception e) {
            Log.e(TAG, "消息:" + msg.id + " 处理错误: " + e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.e(TAG, "Netty异常: " + cause.getMessage());
        stopTimer();
        NettyDeviceClientChannelManager.delInstance();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                Log.w(TAG, "userEventTriggered: 长期没收到服务器推送数据");
                sendHeartBeat();
                //可以选择重新连接
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                Log.w(TAG, "userEventTriggered: 长期未向服务器发送数据");
                sendHeartBeat();
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                Log.w(TAG, "userEventTriggered: ALL");
            }
        }
    }

    private void sendHeartBeat() {
        if (NettyDeviceClientChannelManager.getInstance() != null) {
            DeviceProto.DeviceHeartbeat heartbeat = DeviceProto.DeviceHeartbeat.getDefaultInstance().toBuilder().setBeat(Empty.getDefaultInstance()).build();
            ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000001.id, heartbeat);
            NettyDeviceClientChannelManager.getInstance().writeAndFlush(protoFrame);
            Log.d(TAG, "发送心跳包给主屏》》》》》》》》》》》》");
        }
    }

    public void sendBoxAuth() {
        if (NettyDeviceClientChannelManager.getInstance() != null) {
            DeviceProto.DeviceAuthRequest request = DeviceProto.DeviceAuthRequest.getDefaultInstance().toBuilder()
                    .setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).setRoomCode(VodConfigData.getInstance().getRoomCode())
                    .setDeviceType(2).setSerialNo(DeviceUtil.getCupSerial()).setDeviceIp(NetWorkUtils.getLocalHostIp()).build();
            ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000003.id, request);
            NettyDeviceClientChannelManager.getInstance().writeAndFlush(protoFrame);
            Log.d(TAG, "发送设备认证信息给主屏》》》》》》》》》》》》");
        }
    }


    private void startTimer() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown())
            return;
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(scheduledExecutorService);
    }

    private void stopTimer() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
            scheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendHeartBeat();
            }
        }, 15, 15, TimeUnit.SECONDS);
    }
}
