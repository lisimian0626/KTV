package com.bestarmedia.libnetty.codec;

import com.bestarmedia.libnetty.netty.ProtoFrame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageLiteEncoder extends MessageToByteEncoder<ProtoFrame> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoFrame protoFrame, ByteBuf byteBuf) throws Exception {
        if (protoFrame == null) {
            throw new Exception("消息不能为空");
        }

        byteBuf.writeInt(protoFrame.id);
        byteBuf.writeInt(protoFrame.length);
        byteBuf.writeBytes(protoFrame.byteBuf);
    }
}
