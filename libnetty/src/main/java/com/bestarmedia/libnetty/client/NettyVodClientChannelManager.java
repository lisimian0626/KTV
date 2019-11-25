package com.bestarmedia.libnetty.client;

/**
 * Created by J Wong on 2019/5/10.
 */

import io.netty.channel.Channel;

public class NettyVodClientChannelManager {

    private static Channel instance;

    public static void setInstance(Channel channel) {
        instance = channel;
    }

    public static void delInstance() {
        instance = null;
    }

    public static Channel getInstance() {
        return instance;
    }
}

