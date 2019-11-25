package com.beidousat.karaoke.helper;

import android.util.Log;

import com.beidousat.karaoke.data.RoomUsers;
import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.proto.node.BarrageRequest;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.proto.vod.MobileUser;

public class MobileMessageHandler {

    private static MobileMessageHandler mobileMessageHandler;

    private final static String TAG = "MobileMessageHandler";

    public static MobileMessageHandler getInstance() {
        if (mobileMessageHandler == null) {
            mobileMessageHandler = new MobileMessageHandler();
        }
        return mobileMessageHandler;
    }

    public void handleMobileLogin(KaraokeController controller, MobileUser mobileUser) {
        Log.i(TAG, "处理手机手机扫码登录");
        boolean isAddSuccess = RoomUsers.getInstance().addUserNotExist(new UserBase(mobileUser.getUserId(), mobileUser.getName(), mobileUser.getAvatar()));
        controller.broadcastChooseList();
        if (isAddSuccess) {
            BarrageRequest barrageRequest = BarrageRequest.getDefaultInstance().toBuilder().setUserId(mobileUser.getUserId())
                    .setName(mobileUser.getName()).setAvatar(mobileUser.getAvatar()).setText("扫码进入房间").build();
            EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_BARRAGE_NEW, barrageRequest);
        }
    }

    public void handle(KaraokeController controller, MobileMessageBroadcast msg) {
        Log.i(TAG, "处理手机消息指令：" + msg.getDirective());
        try {
            String current = NodeRoomInfo.getInstance().getPhoneSession();
            if (!current.equalsIgnoreCase(msg.getUser().getRoomSession())) {
                Log.e(TAG, "用户二维码session已过期! 当前session：" + current + " 用户session：" + msg.getUser().getRoomSession());
                VodCommunicateHelper.sendExpiredSession();
                return;
            }
            switch (msg.getDirective()) {
                /**
                 * 遥控
                 */
                case 11000101://原伴唱
                    controller.originalAccompany();
                    break;
                case 11010101://切歌
                    controller.next(true);
                    break;
                case 11020101://暂停/播放
                    controller.pauseStart();
                    break;
                case 11030101://重唱
                    controller.replay();
                    break;
                case 11040101://麦克风音量+
                    controller.micUp(true);
                    break;
                case 11050101://麦克风音量-
                    controller.micDown(true);
                    break;
                case 11060101://音乐音量+
                    controller.volumeUp();
                    break;
                case 11070101://音乐音量-
                    controller.volumeDown();
                    break;
                case 11080101://静音/取消静音
                    controller.muteCancelMute();
                    break;
                case 11110101://原调
                    controller.toneDefault();
                    break;
                case 11100101://降调
                    controller.toneDown();
                    break;
                case 11120101://升调
                    controller.toneUp();
                    break;
                case 11130101://原唱(语音)
                    controller.original(true);
                    break;
                case 11140101://伴唱（语音）
                    controller.accompany(true);
                    break;
                case 11150101://播放（语音）
                    controller.start();
                    break;
                case 11160101://暂停（语音）
                    controller.pause();
                    break;
                case 11170101://静音（语音）
                    controller.mute();
                    break;
                case 11180101://取消静音（语音）
                    controller.cancelMute();
                    break;
                case 11190101://评分-关
                    controller.setScoreMode(0);
                    break;
                case 11200101://评分-开-娱乐模式
                    controller.setScoreMode(1);
                    break;
                case 11210101://评分-专业模式
                    controller.setScoreMode(2);
                    break;
                case 11280101://发送表情
                    Log.i(TAG, "收到表情：" + msg.getId());
                    controller.emojiDynamic(msg.getId());
                    break;
                case 11290101://发送弹幕
                    Log.i(TAG, "弹幕：" + msg.getText().getContent() + " color:" + msg.getText().getColor());
                    controller.playBarrage(msg);
                    break;
                case 11300101://发送图片
                    Log.i(TAG, "图片：" + msg.getImage().getUrl() + " width:" + msg.getImage().getWidth());
                    ImageMessageRequest request = ImageMessageRequest.getDefaultInstance().toBuilder()
                            .setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).setRoomCode(VodConfigData.getInstance().getRoomCode())
                            .setImage(msg.getImage()).build();
                    EventBusUtil.postSticky(EventBusId.ImId.IMAGE_MESSAGE, request);
                    break;
                case 11480101:// 气氛表情
                    Log.i(TAG, "气氛表情：" + msg.getId());
                    controller.atmosphere(Integer.valueOf(msg.getId()), true);
//                    //test
//                    int defaultVideoWidth = 1280 / 3;
//                    int defaultVideoHeight = 720 / 3;
//                    int defaultVideoEndX = (1280 - defaultVideoWidth) / 2;
//                    int defaultVideoEndY = (720 - defaultVideoHeight) / 2;
//                    MimeProto.Video video = MimeProto.Video.getDefaultInstance().toBuilder().setType(2)
//                            .setUrl("http://172.30.1.230:10230/data/necessary/score_result.mp4")
//                            .setWidth(defaultVideoWidth).setHeight(defaultVideoHeight).setStartX(0).setStartY(0)
//                            .setEndX(defaultVideoEndX).setEndY(defaultVideoEndY)
//                            .build();
//                    VideoMessageRequest videoMessageRequest = VideoMessageRequest.getDefaultInstance().toBuilder()
//                            .setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).setRoomCode(VodConfigData.getInstance().getRoomCode())
//                            .setVideo(video).build();
//                    EventBusUtil.postSticky(EventBusId.ImId.VIDEO_MESSAGE, videoMessageRequest);
                    break;
                /**
                 * 呼叫
                 */
                case 11090101://呼叫服务
                case 11360101://呼叫服务员
                    controller.setServiceMode(0, true);
                    break;
                case 11370101://呼叫-DJ
                    controller.setServiceMode(1, true);
                    break;
                case 11380101://呼叫-清洁
                    controller.setServiceMode(2, true);
                    break;
                case 11390101://呼叫-保安
                    controller.setServiceMode(3, true);
                    break;
                case 11400101://呼叫-买单
                    controller.setServiceMode(4, true);
                    break;
                case 11410101://呼叫-催单
                    controller.setServiceMode(5, true);
                    break;
                case 11420101://呼叫-唛套
                    controller.setServiceMode(6, true);
                    break;
                case 11430101://呼叫-加冰
                    controller.setServiceMode(7, true);
                    break;
                case 11440101://呼叫-取消
                    controller.setServiceMode(-1, true);
                    break;
                /**
                 * 点歌
                 */
                case 11310101://点自制MV
                    Log.i(TAG, "点自制MV id：" + msg.getId());
                    controller.selectMv(false, Integer.valueOf(msg.getId()),
                            new UserBase(msg.getUser().getUserId(), msg.getUser().getName(), msg.getUser().getAvatar()), null);
                    break;
                case 11320101://优先自制MV
                    Log.i(TAG, "优先自制MV id：" + msg.getId());
                    controller.selectMv(true, Integer.valueOf(msg.getId()),
                            new UserBase(msg.getUser().getUserId(), msg.getUser().getName(), msg.getUser().getAvatar()), null);
                    break;
                case 11330101://点歌
                    Log.i(TAG, "点歌，歌曲id：" + msg.getId() + " use_id:" + msg.getUser().getUserId() + " user_name:" + msg.getUser().getName() + " user_avatar:" + msg.getUser().getAvatar());
                    controller.selectSong(false, msg.getId(),
                            new UserBase(msg.getUser().getUserId(), msg.getUser().getName(), msg.getUser().getAvatar()), null);
                    break;
                case 11340101://优先歌曲
                    Log.i(TAG, "优先歌曲，歌曲id：" + msg.getId());
                    controller.selectSong(true, msg.getId(),
                            new UserBase(msg.getUser().getUserId(), msg.getUser().getName(), msg.getUser().getAvatar()), null);
                    break;
                case 11460101://保存录音
                    break;
                case 11620101://优先已点列表歌曲
                    Log.i(TAG, "优先已点列表歌曲，记录id：" + msg.getId());
                    controller.priorityChoose(msg.getId());
                    break;
                case 11610101://删除已点列表歌曲
                    Log.i(TAG, "删除已点列表歌曲，记录id：" + msg.getId());
                    controller.deleteChoose(msg.getId());
                    break;
                case 11630101://请求机顶盒推送已点已唱列表（扫码后调用）
                    Log.i(TAG, "请求机顶盒推送已点已唱列表>>>>");
                    controller.broadcastChooseList();
                    controller.broadcastSungList();
                    break;
                case 11470101://退出房间
                    RoomUsers.getInstance().removeUser(msg.getUser().getUserId());
                    break;
                default:
                    Log.e(TAG, "未知遥控指令:" + msg.getDirective());
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "处理消息：" + msg.getDirective() + "异常", e);
        }
    }
}
