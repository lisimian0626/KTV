package com.beidousat.karaoke.im;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.util.DateUtil;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.bestarmedia.libnetty.client.NettyVodClient;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.node.BoxIdleBroadcast;
import com.bestarmedia.proto.node.VodScreenShotRequest;
import com.bestarmedia.proto.node.VodScreenShotResponse;
import com.bestarmedia.proto.vod.BoxAuthRequest;
import com.bestarmedia.proto.vod.Button;
import com.bestarmedia.proto.vod.ButtonStatusBroadcast;
import com.bestarmedia.proto.vod.ExpiredSession;
import com.bestarmedia.proto.vod.MobileUser;
import com.bestarmedia.proto.vod.RoomSongList;
import com.bestarmedia.proto.vod.SongSimple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * VOD通讯类
 * 通讯协议Netty
 * 手机点歌等VOD相关即时通讯
 * 数据格式proto
 */
public class VodCommunicateHelper {

    private final static String TAG = "VodCommunicateHelper";

    /**
     * 发送触摸屏使用状态
     */
    public static void sendTouchStatus(int status, int minute) {
        try {
            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
                BoxIdleBroadcast response = BoxIdleBroadcast.getDefaultInstance().toBuilder()
                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).setMinute(minute).setSerialNode(DeviceUtil.getCupSerial())
                        .setIp(NetWorkUtils.getLocalHostIp()).setStatus(status).build();
                send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H11880001.id, response));
                Log.i(NettyConfig.TAG, "机顶盒无任何操作通知!id：" + NettyMessageEnum.H11880001.id + " 房间:" + VodConfigData.getInstance().getRoomCode()
                        + " status:" + status + " minute:" + minute);
            }
        } catch (Exception e) {
            Log.e(TAG, "机顶盒无任何操作通知，出错了", e);
        }
    }
//
//    /**
//     * 图片通知回复
//     */
//    public static void sendImageMessageResponse(boolean isSuccess, int userId) {
//        try {
//            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
//                ImageMessageResponse response = ImageMessageResponse.getDefaultInstance().toBuilder().setCode(isSuccess ? 1 : 0)
//                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(userId).build();
//                send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000016.id, response));
//                Log.i(NettyConfig.TAG, "图片通知回复!id：" + NettyMessageEnum.H12000016.id + " 房间:" + VodConfigData.getInstance().getRoomCode()
//                        + " code:" + (isSuccess ? 1 : 0) + " user_id:" + userId);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "图片通知回复，出错了", e);
//        }
//    }
//
//    /**
//     * 文字通知回复
//     */
//    public static void sendTextMessageResponse(boolean isSuccess, int userId) {
//        try {
//            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
//                TextMessageResponse response = TextMessageResponse.getDefaultInstance().toBuilder().setCode(isSuccess ? 1 : 0)
//                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(userId).build();
//                send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000014.id, response));
//                Log.i(NettyConfig.TAG, "文字通知回复!id：" + NettyMessageEnum.H12000014.id + " 房间:" + VodConfigData.getInstance().getRoomCode()
//                        + " code:" + (isSuccess ? 1 : 0) + " user_id:" + userId);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "文字通知回复，出错了", e);
//        }
//    }
//
//
//    /**
//     * 火警回复
//     */
//    private static void sendFireAlarmResponse(boolean isSuccess) {
//        try {
//            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
//                FireAlarmResponse response = FireAlarmResponse.getDefaultInstance().toBuilder().setCode(isSuccess ? 1 : 0)
//                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).build();
//                send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H11000014.id, response));
//                Log.i(NettyConfig.TAG, "火警回复!id：" + NettyMessageEnum.H11000014.id + " 房间:" + VodConfigData.getInstance().getRoomCode() + " code:" + (isSuccess ? 1 : 0));
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "火警回复，出错了", e);
//        }
//    }

    /**
     * 发送二维码过期给手机端
     */
    public static void sendExpiredSession() {
        try {
            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
                ExpiredSession session = ExpiredSession.getDefaultInstance().toBuilder().setKtvNetCode(VodConfigData.getInstance().getKtvNetCode())
                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).setRoomSession(NodeRoomInfo.getInstance().getPhoneSession()).build();
                send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H12880012.id, session));
                Log.i(NettyConfig.TAG, "发送二维码过期消息给手机端!id：" + NettyMessageEnum.H12880012.id + " 房间session:" + session.getRoomSession());
            }
        } catch (Exception e) {
            Log.e(TAG, "发送二维码过期给手机端，出错了", e);
        }
    }

    /**
     * 回复截图
     */
    public static void sendScreenShotResponse(VodScreenShotRequest request, int imageId, String imageUrl,
                                              int playType, String playingId, String playingName, String playingAdId, String songVersion, String singerName) {
        try {
            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
                VodScreenShotResponse response = VodScreenShotResponse.getDefaultInstance().toBuilder()
                        .setKtvNetCode(request.getKtvNetCode())
                        .setRoomCode(request.getRoomCode())
                        .setKtvRoomCode(TextUtils.isEmpty(NodeRoomInfo.getInstance().getRoomName()) ? VodConfigData.getInstance().getRoomCode() : NodeRoomInfo.getInstance().getRoomName())
                        .setImageId(imageId)
                        .setImageUrl(imageUrl)
                        .setPlayingType(playType)
                        .setPlayingId(playingId)
                        .setPlayingName(playingName)
                        .setPlayingAdId(playingAdId)
                        .setAddress(request.getAddress())
                        .setPlatform(request.getPlatform())
                        .setSongVersion(TextUtils.isEmpty(songVersion) ? "" : songVersion)
                        .setSingerName(TextUtils.isEmpty(singerName) ? "" : singerName)
                        .setUserId(request.getUserId())
                        .setDate(DateUtil.getDateTime(new Date()))
                        .build();
                ProtoFrame frame = ProtoUtil.createProtoFrame(NettyMessageEnum.H11000020.id, response);
                Log.i(TAG, "回复截图！id：" + frame.id + " 图片：" + response.getImageUrl());
                send2VodServer(frame);
            }
        } catch (Exception e) {
            Log.e(TAG, "回复截图，出错了", e);
        }
    }

    /**
     * 广播二维码信息
     */
    public static void broadcastPhoneSession() {
        try {
            if (!TextUtils.isEmpty(VodConfigData.getInstance().getKtvNetCode()) && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())) {
                ExpiredSession session = ExpiredSession.getDefaultInstance().toBuilder().setKtvNetCode(VodConfigData.getInstance().getKtvNetCode())
                        .setRoomCode(VodConfigData.getInstance().getRoomCode()).setRoomSession(NodeRoomInfo.getInstance().getPhoneSession()).build();
                ProtoFrame frame = ProtoUtil.createProtoFrame(NettyMessageEnum.H12880012.id, session);
                Log.i(NettyConfig.TAG, "广播二维码信息!id：" + NettyMessageEnum.H12880012.id + " 房间session:" + session.getRoomSession());
                send2VodServer(frame);
            }
        } catch (Exception e) {
            Log.e(TAG, "广播二维码信息，出错了", e);
        }
    }

    /**
     * 广播按钮状态给手机端
     *
     * @param buttonStatus 按钮状态
     */
    public static void broadcastButtonStatus(ButtonStatus buttonStatus) {
        try {
            Button button = Button.newBuilder().setIsPause(buttonStatus.isPause == 1).setOriginOn(buttonStatus.isOriginal == 1)
                    .setIsMute(buttonStatus.isMute == 1).setIsServing(buttonStatus.serviceMode > 0).setServiceMode(buttonStatus.serviceMode)
                    .setScoreMode(buttonStatus.scoreMode).setIsLightAuto(buttonStatus.isLightAuto == 1).setIsHdmiBlack(buttonStatus.isHdmiBack == 1)
                    .setIsRecord(buttonStatus.isRecord == 1).build();
            MobileUser mobileUser = MobileUser.newBuilder().setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).setKtvName(VodConfigData.getInstance().getKtvName()).setRoomCode(VodConfigData.getInstance().getRoomCode())
                    .setRoomName(VodConfigData.getInstance().getKtvRoomCode()).setRoomSession(NodeRoomInfo.getInstance().getNodeRoom().room.currentSession).build();
            ButtonStatusBroadcast buttonStatusBroadcast = ButtonStatusBroadcast.newBuilder().setButton(button).setUser(mobileUser).build();
            ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H12880010.id, buttonStatusBroadcast);
            Log.i(NettyConfig.TAG, "广播按钮状态给手机端!id：" + NettyMessageEnum.H12880010.id);
            send2VodServer(protoFrame);
        } catch (Exception e) {
            Log.e(TAG, "广播按钮状态给手机端，出错了", e);
        }
    }

    /**
     * 广播已点列表
     *
     * @param list 已点列表
     */
    public static void broadcastChooseList(List<RoomSongItem> list) {
        broadcastRoomSongList(1, list);
    }

    /**
     * 广播已唱列表
     *
     * @param list 已唱列表
     */
    public static void broadcastSungList(List<RoomSongItem> list) {
        broadcastRoomSongList(2, list);
    }

    /**
     * @param type 1已点，2已唱
     * @param list 歌曲列表
     */
    private static void broadcastRoomSongList(int type, List<RoomSongItem> list) {
        try {
            List<SongSimple> simpleList = new ArrayList<>();
            if (list != null && !list.isEmpty()) {
                for (RoomSongItem item : list) {
                    simpleList.add(SongSimple.getDefaultInstance().toBuilder().setUuid(item.uuid).setSongId(item.songCode)
                            .setSongName(item.simpName).setSingerName(item.singerName).setVideoType(item.songVersion).build());
                }
            }
            MobileUser mobileUser = MobileUser.newBuilder().setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).setKtvName(VodConfigData.getInstance().getKtvName()).setRoomCode(VodConfigData.getInstance().getRoomCode())
                    .setRoomName(VodConfigData.getInstance().getKtvRoomCode()).setRoomSession(NodeRoomInfo.getInstance().getNodeRoom().room.currentSession).build();
            RoomSongList protoSongList = RoomSongList.getDefaultInstance().toBuilder().setType(type).setUser(mobileUser).addAllSong(simpleList).build();
            ProtoFrame protoFrame = ProtoUtil.createProtoFrame(NettyMessageEnum.H12880011.id, protoSongList);
            Log.i(NettyConfig.TAG, "广播" + (type == 1 ? "已点" : "已唱") + "列表!id：" + NettyMessageEnum.H12880011.id + " 列表长度：" + simpleList.size());
            send2VodServer(protoFrame);
        } catch (Exception e) {
            Log.e(TAG, "广播已点已唱信息，出错了", e);
        }
    }

    /**
     * 注册设备信息
     *
     * @param televisionOn 电视屏开关
     */
    public static void sendBoxAuth(Context context, boolean televisionOn) {
        try {
            BoxAuthRequest request = BoxAuthRequest.getDefaultInstance().toBuilder()
                    .setKtvNetCode(VodConfigData.getInstance().getKtvNetCode())
                    .setRoomCode(VodConfigData.getInstance().getRoomCode())
                    .setHdmiEnable(televisionOn ? 1 : 0)
                    .setSerialNo(DeviceUtil.getCupSerial())
                    .setAnufacturer(OkConfig.boxManufacturerName())
                    .setOsVersion(PackageUtil.getSystemVersionCode())
                    .setApkVersionCode(PackageUtil.getVersionCode(context))
                    .setApkVersionName(PackageUtil.getVersionName(context))
                    .setIp(NetWorkUtils.getLocalHostIp())
                    .build();
            send2VodServer(ProtoUtil.createProtoFrame(NettyMessageEnum.H11000010.id, request));
            Log.i(TAG, "发送设备认证信息给VOD服务端！id：" + NettyMessageEnum.H11000010.id + " 电视开关：" + televisionOn);
        } catch (Exception e) {
            Log.e(TAG, "发送设备认证信息给VOD服务端，出错了", e);
        }
    }


    /**
     * 发送信息给Vod服务器
     *
     * @param protoFrame 数据
     */
    private static void send2VodServer(ProtoFrame protoFrame) {
        try {
            NettyVodClient.getInstance().sendMessage(protoFrame);

            Log.i(TAG, "发送给VOD服务器，消息id:" + protoFrame.id);
        } catch (Exception e) {
            Log.e(TAG, "发送信息给Vod服务器，出错了!消息id：" + protoFrame.id, e);
        }
    }
}
