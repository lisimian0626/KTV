package com.bestarmedia.proto;

import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.google.protobuf.MessageLite;

public class ProtoUtil {

    public static ProtoFrame createProtoFrame(int id, MessageLite messageLite) {
        return new ProtoFrame(id, messageLite.toByteArray().length, messageLite.toByteArray());
    }

}
