package com.bestarmedia.libnetty.handler.device;


import com.bestarmedia.libnetty.handler.device.message.AirConStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.ButtonStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.CoolScreenStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.DeviceAuthResponseHandler;
import com.bestarmedia.libnetty.handler.device.message.DownloadSongListProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameStartProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.RoomSongListProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.SyncAdProtoHandler;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyDeviceClientMessageHandler {

    private static Map<String, Object> protoHandlerMap = new ConcurrentHashMap<>();

    public static void initNettyHandler() {
        protoHandlerMap.put(NettyMessageEnum.H15000004.messageHandlerInterface.getSimpleName(), new DeviceAuthResponseHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880005.messageHandlerInterface.getSimpleName(), new ButtonStatusProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880006.messageHandlerInterface.getSimpleName(), new AirConStatusProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880007.messageHandlerInterface.getSimpleName(), new RoomSongListProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880008.messageHandlerInterface.getSimpleName(), new DownloadSongListProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880009.messageHandlerInterface.getSimpleName(), new SyncAdProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880010.messageHandlerInterface.getSimpleName(), new GameStatusProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15000007.messageHandlerInterface.getSimpleName(), new GameStartProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15880011.messageHandlerInterface.getSimpleName(), new CoolScreenStatusProtoHandler());

    }

    public static ProtoHandlerInterface getProtoHandler(String name) throws Exception {
        if (protoHandlerMap.containsKey(name)) {
            return (ProtoHandlerInterface) protoHandlerMap.get(name);
        } else {
            throw new Exception("不存在消息处理器: " + name);
        }
    }
}
