package com.bestarmedia.libnetty.server;


import com.bestarmedia.libnetty.handler.device.message.DeviceAuthHandler;
import com.bestarmedia.libnetty.handler.device.message.DeviceHeartHandler;
import com.bestarmedia.libnetty.handler.device.message.GameOperateProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameStartProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.OperateProtoHandler;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServerMessageHandler {

    private static Map<String, Object> protoHandlerMap = new ConcurrentHashMap<>();

    public static void initNettyHandler() {
        protoHandlerMap.put(NettyMessageEnum.H15000001.messageHandlerInterface.getSimpleName(), new DeviceHeartHandler());
        protoHandlerMap.put(NettyMessageEnum.H15000003.messageHandlerInterface.getSimpleName(), new DeviceAuthHandler());
        protoHandlerMap.put(NettyMessageEnum.H15000005.messageHandlerInterface.getSimpleName(), new OperateProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15000006.messageHandlerInterface.getSimpleName(), new GameOperateProtoHandler());
        protoHandlerMap.put(NettyMessageEnum.H15000007.messageHandlerInterface.getSimpleName(), new GameStartProtoHandler());
    }

    public static ProtoHandlerInterface getProtoHandler(String name) throws Exception {
        if (protoHandlerMap.containsKey(name)) {
            return (ProtoHandlerInterface) protoHandlerMap.get(name);
        } else {
            throw new Exception("不存在消息处理器: " + name);
        }
    }
}
