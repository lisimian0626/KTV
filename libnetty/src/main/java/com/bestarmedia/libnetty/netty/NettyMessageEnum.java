package com.bestarmedia.libnetty.netty;

import com.bestarmedia.libnetty.handler.device.message.AirConStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.ButtonStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.CoolScreenStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.DeviceAuthHandler;
import com.bestarmedia.libnetty.handler.device.message.DeviceAuthResponseHandler;
import com.bestarmedia.libnetty.handler.device.message.DeviceHeartHandler;
import com.bestarmedia.libnetty.handler.device.message.DownloadSongListProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameOperateProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameStartProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.GameStatusProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.OperateProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.RoomSongListProtoHandler;
import com.bestarmedia.libnetty.handler.device.message.SyncAdProtoHandler;
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
import com.bestarmedia.proto.device.DeviceProto;
import com.bestarmedia.proto.monitor.BusinessRoomBroadcast;
import com.bestarmedia.proto.node.BarrageRequest;
import com.bestarmedia.proto.node.BarrageResponse;
import com.bestarmedia.proto.node.BoxControlRequest;
import com.bestarmedia.proto.node.BoxControlResponse;
import com.bestarmedia.proto.node.BoxIdleBroadcast;
import com.bestarmedia.proto.node.FireAlarmRequest;
import com.bestarmedia.proto.node.FireAlarmResponse;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.node.ImageMessageResponse;
import com.bestarmedia.proto.node.PayMessageRequest;
import com.bestarmedia.proto.node.RoomStatusBroadcast;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.TextMessageResponse;
import com.bestarmedia.proto.node.VideoMessageRequest;
import com.bestarmedia.proto.node.VideoMessageResponse;
import com.bestarmedia.proto.node.VodScreenShotRequest;
import com.bestarmedia.proto.node.VodScreenShotResponse;
import com.bestarmedia.proto.node.VolumeRequest;
import com.bestarmedia.proto.node.VolumeResponse;
import com.bestarmedia.proto.vod.BattleContract;
import com.bestarmedia.proto.vod.BoxAuthRequest;
import com.bestarmedia.proto.vod.BoxAuthResponse;
import com.bestarmedia.proto.vod.BoxHeartbeat;
import com.bestarmedia.proto.vod.Button;
import com.bestarmedia.proto.vod.ExpiredSession;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.proto.vod.MobileUser;
import com.bestarmedia.proto.vod.RoomSongList;
import com.bestarmedia.proto.vod.ScoreToOpponent;
import com.google.protobuf.MessageLite;

/**
 * 消息ID + 消息体 + 消息handler
 */
public enum NettyMessageEnum {

    /**
     * vod
     */
    H11000000(11000000, BoxHeartbeat.getDefaultInstance(), null),//心跳包
    H11000010(11000010, BoxAuthRequest.getDefaultInstance(), null),//注册设备（设备认证）
    H11000011(11000011, BoxAuthResponse.getDefaultInstance(), BoxAuthResponseHandler.class),//注册设备（设备认证）回复
    H11000013(11000013, FireAlarmRequest.getDefaultInstance(), FireAlarmHandler.class),//火警请求
    H11000014(11000014, FireAlarmResponse.getDefaultInstance(), null),//火警回复
    H11000015(11000015, VolumeRequest.getDefaultInstance(), VolumeHandler.class),//定时降音量处理
    H11000016(11000016, VolumeResponse.getDefaultInstance(), null),//定时降音量回复
    H11000017(11000017, BoxControlRequest.getDefaultInstance(), BoxControlHandler.class),//关机、重启机顶盒
    H11000018(11000018, BoxControlResponse.getDefaultInstance(), null),//关机、重启机顶盒 回复
    H11000019(11000019, VodScreenShotRequest.getDefaultInstance(), ScreenShotHandler.class),//截图
    H11000020(11000020, VodScreenShotResponse.getDefaultInstance(), null),//截图回复
    H11000021(11000021, RoomStatusBroadcast.getDefaultInstance(), RoomStatusBroadcastHandler.class),//房态变化


    H12000010(12000010, MobileMessageBroadcast.getDefaultInstance(), MobileMessageBroadcastHandler.class),//手机消息
    H12000011(12000011, MobileUser.getDefaultInstance(), MobileUserHandler.class),//手机用户扫码登录
    H12000013(12000013, TextMessageRequest.getDefaultInstance(), TextMessageHandler.class),//推送文字到屏幕请求
    H12000014(12000014, TextMessageResponse.getDefaultInstance(), null),//推送文字到屏幕请求回复
    H12000015(12000015, ImageMessageRequest.getDefaultInstance(), ImageMessageHandler.class),//推送图片到屏幕请求
    H12000016(12000016, ImageMessageResponse.getDefaultInstance(), null),//推送图片到屏幕请求 回复
    H12000017(12000017, VideoMessageRequest.getDefaultInstance(), VideoMessageHandler.class),//推送视频到屏幕请求
    H12000018(12000018, VideoMessageResponse.getDefaultInstance(), null),//推送视频到屏幕请求 回复
    H12000019(12000019, BarrageRequest.getDefaultInstance(), BarrageHandler.class),//发送弹幕请求
    H12000020(12000020, BarrageResponse.getDefaultInstance(), null),//发送弹幕回复
    H11880001(11880001, BoxIdleBroadcast.getDefaultInstance(), null),//机顶盒无任何操作通知

    H11000023(11000023, PayMessageRequest.getDefaultInstance(), PayHandler.class),//支付通知

    H12880010(12880010, Button.getDefaultInstance(), null),//按钮状态
    H12880011(12880011, RoomSongList.getDefaultInstance(), null),//已点已唱
    H12880012(12880012, ExpiredSession.getDefaultInstance(), null),//二维码过期

    H12880020(12880020, BattleContract.getDefaultInstance(), BattleStartHandler.class),//斗歌启动
    H12880021(12880021, BattleContract.getDefaultInstance(), BattleGiveUpHandler.class),//放弃斗歌
    H12880022(12880022, BattleContract.getDefaultInstance(), BattleOpponentResultHandler.class),//对方斗歌结果
    H12880023(12880023, BattleContract.getDefaultInstance(), BattleResultHandler.class),//对方斗歌结果
    H12880024(12880024, ScoreToOpponent.getDefaultInstance(), BattleScoreHandler.class),//斗歌过程中对方分数
    H12880025(12880025, ScoreToOpponent.getDefaultInstance(), BattleResultScoreHandler.class),//斗歌最终对方得分
    H12880026(12880026, ScoreToOpponent.getDefaultInstance(), BattleBarrageHandler.class),//对方发来的弹幕
    H14880001(14880001, BusinessRoomBroadcast.getDefaultInstance(), BusinessBroadcastHandler.class),//房间营业打卡成功

    /**
     * 房间设备间
     */
    H15000001(15000001, DeviceProto.DeviceHeartbeat.getDefaultInstance(), DeviceHeartHandler.class),//心跳包
    //    H15000002(15000002, DeviceProto.DeviceHeartbeat.getDefaultInstance(), null),//心跳包回复
    H15000003(15000003, DeviceProto.DeviceAuthRequest.getDefaultInstance(), DeviceAuthHandler.class), //设备认证请求
    H15000004(15000004, DeviceProto.DeviceAuthResponse.getDefaultInstance(), DeviceAuthResponseHandler.class), //设备认证回复
    H15000005(15000005, DeviceProto.Operation.getDefaultInstance(), OperateProtoHandler.class), //收到播放器操作信息
    //闯关
    H15000006(15000006, DeviceProto.GameOperation.getDefaultInstance(), GameOperateProtoHandler.class), //闯关游戏操作
    H15000007(15000007, DeviceProto.GameStart.getDefaultInstance(), GameStartProtoHandler.class), //闯关游戏开始指令

    H15880005(15880005, DeviceProto.ButtonStatus.getDefaultInstance(), ButtonStatusProtoHandler.class), //按钮状态
    H15880006(15880006, DeviceProto.AirConStatus.getDefaultInstance(), AirConStatusProtoHandler.class), //空调状态
    H15880007(15880007, DeviceProto.RoomSongListChanged.getDefaultInstance(), RoomSongListProtoHandler.class), //已点已唱
    H15880008(15880008, DeviceProto.DownloadSongList.getDefaultInstance(), DownloadSongListProtoHandler.class), //歌曲下载列表
    H15880009(15880009, DeviceProto.SyncAd.getDefaultInstance(), SyncAdProtoHandler.class), //广告同步
    //闯关
    H15880010(15880010, DeviceProto.GameStatus.getDefaultInstance(), GameStatusProtoHandler.class), //游戏状态
    H15880011(15880011, DeviceProto.CoolScreenStatus.getDefaultInstance(), CoolScreenStatusProtoHandler.class), //炫屏


    /**
     * 监控系统
     */
    ;

    public int id;

    public MessageLite messageLite;

    public Class<? extends ProtoHandlerInterface> messageHandlerInterface;

    NettyMessageEnum(int id, MessageLite messageLite, Class<? extends ProtoHandlerInterface> messageHandlerInterface) {
        this.id = id;
        this.messageLite = messageLite;
        this.messageHandlerInterface = messageHandlerInterface;
    }
}
