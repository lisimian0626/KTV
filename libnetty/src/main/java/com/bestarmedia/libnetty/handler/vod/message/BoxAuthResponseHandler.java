package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.vod.BoxAuthResponse;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 * 设备认证回复
 */
public class BoxAuthResponseHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            BoxAuthResponse msg = BoxAuthResponse.parseFrom(protoFrame.byteBuf);
            Log.i("NettyServerHandler", "VOD服务器回复设备认证 code:" + msg.getCode());
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }
}
