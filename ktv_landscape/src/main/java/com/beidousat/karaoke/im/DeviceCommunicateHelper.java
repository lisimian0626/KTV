package com.beidousat.karaoke.im;

import android.util.Log;

import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.dto.AirConStatus;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.dto.CoolScreenStatus;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libnetty.client.NettyDeviceClient;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.server.NettyServerChannelManager;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.device.DeviceProto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.channel.Channel;

/**
 * 房间内设备之间通讯类
 * 通讯协议Netty，主屏设备开启服务端，其他设备以客户端身份连接至主屏
 * 数据格式proto
 */
public class DeviceCommunicateHelper {

    private final static String TAG = "DeviceCommunicateHelper";

    /**
     * 广播炫屏状态
     */
    public static void broadcastCoolScreenStatus(CoolScreenStatus screenStatus) {
        DeviceProto.CoolScreenStatus coolScreenStatus = DeviceProto.CoolScreenStatus.newBuilder().setIsOpen(screenStatus.isOpen).setIsRandom(screenStatus.isRandom)
                .setCurrentId(screenStatus.currentId).setCurrentUrl(screenStatus.currentUrl).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880011.id, coolScreenStatus);
        send2NettyClients(protoFrame);
    }

    /**
     * 发送闯关开始信号给副屏
     *
     * @param level    关卡
     * @param page     页码
     * @param position 歌曲位置
     */
    public static void broadcastGameStart(int level, int page, int position) {
        DeviceProto.GameStart start = DeviceProto.GameStart.newBuilder().setLevel(level).setPage(page).setPosition(position).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000007.id, start);
        Log.d(TAG, "sendGameOperation2Main 闯关游戏开始level:" + level + " page:" + page + " position:" + position);
        send2NettyClients(protoFrame);
    }


    /**
     * 发送闯关开始信号给主屏
     *
     * @param level    关卡
     * @param page     页码
     * @param position 歌曲位置
     */
    public static void sendGameStart2Main(int level, int page, int position) {
        DeviceProto.GameStart start = DeviceProto.GameStart.newBuilder().setLevel(level).setPage(page).setPosition(position).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000007.id, start);
        Log.d(TAG, "sendGameOperation2Main 闯关游戏开始level:" + level + " page:" + page + " position:" + position);
        send2NettyServer(protoFrame);
    }

    /**
     * @param operateType    操作指令 1暂停/播放，2退出关闭游戏
     * @param operationValue 操作值
     */
    public static void sendGameOperation2Main(int operateType, int operationValue) {
        DeviceProto.GameOperation operation = DeviceProto.GameOperation.newBuilder().setOp(operateType).setValue(operationValue).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000006.id, operation);
        Log.d(TAG, "sendGameOperation2Main operateType:" + operateType + " operationValue:" + operationValue);
        send2NettyServer(protoFrame);
    }

    /**
     * 发送游戏状态给副屏
     *
     * @param passLevel    已通关数
     * @param playStatus   0未开始，1视频加载中,2已开始，3已结束，4已关闭
     * @param isPause      是否暂停
     * @param playingLevel 当前正在玩的关卡
     */
    public static void broadcastGameStatus(int passLevel, int playStatus, int isPause, int playingLevel) {
        DeviceProto.GameStatus status = DeviceProto.GameStatus.newBuilder().setPassLevel(passLevel).setPlayStatus(playStatus)
                .setPlayingLevel(playingLevel).setIsPause(isPause)
                .build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880010.id, status);
        send2NettyClients(protoFrame);
    }

    /**
     * 同步广播屏保广告
     *
     * @param adModel 广告
     */
    public static void syncAd(ADModel adModel) {
        DeviceProto.Ad ad = DeviceProto.Ad.newBuilder().setId(adModel.getId()).setName(adModel.getName())
                .setAdPosition(adModel.getAdPosition())
                .setAdContent(adModel.getAdContent()).setBrandName(adModel.getBrandName()).setAdTypeId(adModel.getAdTypeId())
                .setNamingSongListId(adModel.getNamingSongListId())
                .setNamingSongListType(adModel.getNamingSongListType()).build();
        DeviceProto.SyncAd syncAd = DeviceProto.SyncAd.newBuilder().setType(1).setAd(ad).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880009.id, syncAd);
        send2NettyClients(protoFrame);
    }

    /**
     * 广播下载状态
     */
    public static void broadcastSongDownloadStatus(Map<String, SongSimple> map) {
        List<DeviceProto.Song> list = new ArrayList<>();
        for (Map.Entry<String, SongSimple> entry : map.entrySet()) {
            SongSimple value = entry.getValue();
            list.add(DeviceProto.Song.newBuilder().setId(value.id).setSongName(value.songName).addAllSingerMid(value.singerMid).addAllSinger(value.singer)
                    .setVideoType(value.videoType).setIsHd(value.isHd).setIsCloud(value.isCloud).setMainLyric(value.mainLyric).setHot(0).setIsPay(value.isPay)
                    .setIsRedPacket(value.isRedPacket).setDownloadStatus(value.downloadStatus).setDownloadPercent(value.downloadPercent).setDownloadSort(value.downloadSort)
                    .setIsShowSinger(value.isShowSingers).setIsToTop(value.isToTop).build());

        }
        DeviceProto.DownloadSongList downloadSongList = DeviceProto.DownloadSongList.newBuilder().addAllSong(list).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880008.id, downloadSongList);
        send2NettyClients(protoFrame);
    }

    /**
     * 广播已点歌曲编号
     */
    public static void broadcastChooseChanged(List<RoomSongItem> list) {
        DeviceProto.RoomSongListChanged changed = DeviceProto.RoomSongListChanged.newBuilder().setType(1).setCount(list.size()).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880007.id, changed);
        send2NettyClients(protoFrame);
    }

    /**
     * 广播已唱歌曲变化
     */
    public static void broadcastSungChanged(List<RoomSongItem> list) {
        DeviceProto.RoomSongListChanged changed = DeviceProto.RoomSongListChanged.newBuilder().setType(2).setCount(list.size()).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880007.id, changed);
        send2NettyClients(protoFrame);
    }

    /**
     * 广播按钮状态给其他客户端
     */
    public static void broadcastButtonStatus(ButtonStatus buttonStatus) {
        DeviceProto.ButtonStatus buttonStatusProto = DeviceProto.ButtonStatus.newBuilder().setIsPause(buttonStatus.isPause).setOriginOn(buttonStatus.isOriginal)
                .setIsMute(buttonStatus.isMute).setCurrentVolume(buttonStatus.currentVolume).setServiceMode(buttonStatus.serviceMode).setScoreMode(buttonStatus.scoreMode)
                .setIsLightAuto(buttonStatus.isLightAuto).setIsHdmiBlack(buttonStatus.isHdmiBack).setIsRecord(buttonStatus.isRecord).setCurrentLightCode(buttonStatus.currentLightCode)
                .build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880005.id, buttonStatusProto);
        send2NettyClients(protoFrame);
    }

    /**
     * 空调状态
     */
    public static void broadcastAirConStatus(AirConStatus airConStatus) {
        DeviceProto.AirConStatus status = DeviceProto.AirConStatus.newBuilder().setAirConOpen(airConStatus.airConOpen).setMode(airConStatus.mode)
                .setWindOpen(airConStatus.windOpen).setWindSpeed(airConStatus.windSpeed).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15880006.id, status);
        send2NettyClients(protoFrame);
    }

    /**
     * @param operateType    1切歌，2原伴唱，3暂停/播放，4静音/取消静音，5音乐音量，
     *                       6重唱，7评分，8录音，9麦克风音量，10麦克风音调，
     *                       11音乐音调，12弹幕，13表情，14服务铃，15点自制MV，
     *                       16优先点自制MV，17点歌，18优先点歌，19优先已点，20删除已点,
     *                       21炫屏，22自动灯光，23音效,24混响，25灯光码，
     *                       26灯光模式，27亮度，28电视屏黑屏，29重置二维码，30空调模式
     *                       31空调风速，32排风开关，33泡泡机，34烟雾机，35空调开关
     *                       36温度，37炫屏状态
     * @param operationValue 操作值
     */
    public static void sendPlayerOperate2Main(int operateType, int operationValue) {
        sendPlayerOperate2Main(operateType, operationValue, "");
    }

    /**
     * @param operateType   1切歌，2原伴唱，3暂停/播放，4静音/取消静音，5音乐音量，
     *                      6重唱，7评分，8录音，9麦克风音量，10麦克风音调，
     *                      11音乐音调，12弹幕，13表情，14服务铃，15点自制MV，
     *                      16优先点自制MV，17点歌，18优先点歌，19优先已点，20删除已点,
     *                      21炫屏，22自动灯光，23音效,24混响，25灯光码，
     *                      26灯光模式，27亮度，28电视屏黑屏，29重置二维码，30空调模式
     *                      31空调风速，32排风开关，33泡泡机，34烟雾机，35空调开关
     *                      36温度，37炫屏状态,38动态表情
     * @param operationText 操作值
     */
    public static void sendPlayerOperate2Main(int operateType, String operationText) {
        sendPlayerOperate2Main(operateType, 0, operationText);
    }

    /**
     * @param operateType    1切歌，2原伴唱，3暂停/播放，4静音/取消静音，5音乐音量，
     *                       6重唱，7评分，8录音，9麦克风音量，10麦克风音调，
     *                       11音乐音调，12弹幕，13表情，14服务铃，15点自制MV，
     *                       16优先点自制MV，17点歌，18优先点歌，19优先已点，20删除已点,
     *                       21炫屏，22自动灯光，23音效,24混响，25灯光码，
     *                       26灯光模式，27亮度，28电视屏黑屏，29重置二维码，30空调模式
     *                       31空调风速，32排风开关，33泡泡机，34烟雾机，35空调开关
     *                       36温度，37炫屏状态,38动态表情
     * @param operationValue 操作值
     * @param operationText  操作值
     */
    public static void sendPlayerOperate2Main(int operateType, int operationValue, String operationText) {
        DeviceProto.Operation operation = DeviceProto.Operation.newBuilder().setOperateType(operateType)
                .setOperateValue(operationValue).setOperateText(operationText).build();
        ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H15000005.id, operation);
        Log.d(TAG, "sendPlayerOperate2Main operateType:" + operateType + " operationText:" + operationText);
        send2NettyServer(protoFrame);
    }

    /**
     * 发送信息给netty各个客户端
     */
    private static void send2NettyClients(ProtoFrame protoFrame) {
        List<Channel> channels;
        if (!(channels = NettyServerChannelManager.getConnectedList()).isEmpty()) {
            for (Channel channel : channels) {
                Log.d(TAG, "发生消息给客户端，id:" + protoFrame.id);
                channel.writeAndFlush(protoFrame);
            }
        }
    }

    /**
     * 发送信息给netty服务端
     */
    private static void send2NettyServer(ProtoFrame protoFrame) {
        NettyDeviceClient.getInstance().sendMessage(protoFrame);
        Log.d(TAG, "send2NettyServer id:" + protoFrame.id);
    }
}
