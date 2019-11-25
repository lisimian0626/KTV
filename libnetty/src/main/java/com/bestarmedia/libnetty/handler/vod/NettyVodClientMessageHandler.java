package com.bestarmedia.libnetty.handler.vod;


import com.bestarmedia.libnetty.handler.vod.message.BarrageHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleBarrageHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleGiveUpHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleOpponentResultHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleResultHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleResultScoreHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleScoreHandler;
import com.bestarmedia.libnetty.handler.vod.message.BattleStartHandler;
import com.bestarmedia.libnetty.handler.vod.message.BoxAuthResponseHandler;
import com.bestarmedia.libnetty.handler.vod.message.BoxControlHandler;
import com.bestarmedia.libnetty.handler.vod.message.BusinessBroadcastHandler;
import com.bestarmedia.libnetty.handler.vod.message.FireAlarmHandler;
import com.bestarmedia.libnetty.handler.vod.message.ImageMessageHandler;
import com.bestarmedia.libnetty.handler.vod.message.MobileMessageBroadcastHandler;
import com.bestarmedia.libnetty.handler.vod.message.MobileUserHandler;
import com.bestarmedia.libnetty.handler.vod.message.PayHandler;
import com.bestarmedia.libnetty.handler.vod.message.RoomStatusBroadcastHandler;
import com.bestarmedia.libnetty.handler.vod.message.ScreenShotHandler;
import com.bestarmedia.libnetty.handler.vod.message.TextMessageHandler;
import com.bestarmedia.libnetty.handler.vod.message.VideoMessageHandler;
import com.bestarmedia.libnetty.handler.vod.message.VolumeHandler;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyVodClientMessageHandler {

    private static Map<String, Object> protoHandlerMap = new ConcurrentHashMap<>();

    public static void initNettyHandler() {
        protoHandlerMap.put(NettyMessageEnum.H11000011.messageHandlerInterface.getSimpleName(), new BoxAuthResponseHandler());//设备认证回复
        protoHandlerMap.put(NettyMessageEnum.H11000013.messageHandlerInterface.getSimpleName(), new FireAlarmHandler());//火警请求
        protoHandlerMap.put(NettyMessageEnum.H11000015.messageHandlerInterface.getSimpleName(), new VolumeHandler());//定时降音量处理
        protoHandlerMap.put(NettyMessageEnum.H11000017.messageHandlerInterface.getSimpleName(), new BoxControlHandler());//关机、重启机顶盒
        protoHandlerMap.put(NettyMessageEnum.H11000019.messageHandlerInterface.getSimpleName(), new ScreenShotHandler());//截图
        protoHandlerMap.put(NettyMessageEnum.H11000021.messageHandlerInterface.getSimpleName(), new RoomStatusBroadcastHandler());//房态变化

        protoHandlerMap.put(NettyMessageEnum.H12000010.messageHandlerInterface.getSimpleName(), new MobileMessageBroadcastHandler());//手机遥控
        protoHandlerMap.put(NettyMessageEnum.H12000011.messageHandlerInterface.getSimpleName(), new MobileUserHandler());//扫码登录
        protoHandlerMap.put(NettyMessageEnum.H12000013.messageHandlerInterface.getSimpleName(), new TextMessageHandler());//推送文字到屏幕请求
        protoHandlerMap.put(NettyMessageEnum.H12000015.messageHandlerInterface.getSimpleName(), new ImageMessageHandler());//推送图片到屏幕请求
        protoHandlerMap.put(NettyMessageEnum.H12000017.messageHandlerInterface.getSimpleName(), new VideoMessageHandler());//推送视频到屏幕请求
        protoHandlerMap.put(NettyMessageEnum.H12000019.messageHandlerInterface.getSimpleName(), new BarrageHandler());//发送弹幕请求

        protoHandlerMap.put(NettyMessageEnum.H12880020.messageHandlerInterface.getSimpleName(), new BattleStartHandler());//启动斗歌
        protoHandlerMap.put(NettyMessageEnum.H12880021.messageHandlerInterface.getSimpleName(), new BattleGiveUpHandler());//放弃斗歌
        protoHandlerMap.put(NettyMessageEnum.H12880022.messageHandlerInterface.getSimpleName(), new BattleOpponentResultHandler());//对方斗歌结果
        protoHandlerMap.put(NettyMessageEnum.H12880023.messageHandlerInterface.getSimpleName(), new BattleResultHandler());//斗歌结果

        protoHandlerMap.put(NettyMessageEnum.H12880024.messageHandlerInterface.getSimpleName(), new BattleScoreHandler());//斗歌过程中对方分数
        protoHandlerMap.put(NettyMessageEnum.H12880025.messageHandlerInterface.getSimpleName(), new BattleResultScoreHandler());//斗歌过程中对方最终得分
        protoHandlerMap.put(NettyMessageEnum.H12880026.messageHandlerInterface.getSimpleName(), new BattleBarrageHandler());//斗歌过程中对方弹幕
        protoHandlerMap.put(NettyMessageEnum.H14880001.messageHandlerInterface.getSimpleName(), new BusinessBroadcastHandler());//店家营业成功

        protoHandlerMap.put(NettyMessageEnum.H11000023.messageHandlerInterface.getSimpleName(), new PayHandler());//支付通知

    }

    public static ProtoHandlerInterface getProtoHandler(String name) throws Exception {
        if (protoHandlerMap.containsKey(name)) {
            return (ProtoHandlerInterface) protoHandlerMap.get(name);
        } else {
            throw new Exception("不存在消息处理器: " + name);
        }
    }
}
