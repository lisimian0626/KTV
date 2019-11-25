package com.bestarmedia.libnetty.server;

/**
 * Created by J Wong on 2019/5/10.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class NettyServerChannelManager {

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Channel>> SESSION_MAP = new ConcurrentHashMap<>();

    public static void set(String key, Channel channel) {
        if (SESSION_MAP.containsKey(key)) {
            SESSION_MAP.get(key).put(channel.id().asShortText(), channel);
        } else {
            ConcurrentHashMap<String, Channel> childMap = new ConcurrentHashMap<>();
            childMap.put(channel.id().asShortText(), channel);
            SESSION_MAP.put(key, childMap);
        }
    }

    public static ConcurrentHashMap<String, Channel> get(String key) {
        return SESSION_MAP.get(key);
    }

    public static void del(Channel channel) {
        String channelId = channel.id().asShortText();
        for (Map.Entry<String, ConcurrentHashMap<String, Channel>> entry : SESSION_MAP.entrySet()) {
            String key = entry.getKey();
            ConcurrentHashMap<String, Channel> value = entry.getValue();
            for (Map.Entry<String, Channel> entry2 : value.entrySet()) {
                if (channelId.equals(entry2.getKey())) {
                    entry.getValue().entrySet().remove(entry2);
                }
            }
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static List<Channel> getConnectedList() {
        List<Channel> channels = new ArrayList<>();
        for (Map.Entry<String, ConcurrentHashMap<String, Channel>> entry : SESSION_MAP.entrySet()) {
            ConcurrentHashMap<String, Channel> value = entry.getValue();
            for (Map.Entry<String, Channel> entry2 : value.entrySet()) {
                channels.add(entry2.getValue());
            }
        }
        return channels;
    }
}

