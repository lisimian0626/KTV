package com.bestarmedia.libnetty.codec;

import com.bestarmedia.libnetty.netty.ProtoFrame;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 消息格式:
 * 头部
 */
public class MessageLiteDecoder extends ByteToMessageDecoder {

    private final static String TAG = "MessageLiteDecoder";

    private static final int HEAD_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) {
            throw new Exception("消息长度不满足HEAD要求");
        }

        int id = in.readInt();
        int length = in.readInt();
        int byteLen = in.readableBytes();
        if (byteLen != length) {
            throw new Exception("消息长度不正常");
        }
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        ProtoFrame protoFrame = new ProtoFrame(id, length, data);
        out.add(protoFrame);
    }

}
