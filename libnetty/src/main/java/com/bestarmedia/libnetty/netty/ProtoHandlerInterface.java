package com.bestarmedia.libnetty.netty;

import io.netty.channel.Channel;

public interface ProtoHandlerInterface<T extends ProtoFrame> {

    void handle(Channel channel, T object) throws Exception;
}
