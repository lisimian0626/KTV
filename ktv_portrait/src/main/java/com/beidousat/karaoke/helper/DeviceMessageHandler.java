package com.beidousat.karaoke.helper;

import android.util.Log;

import com.bestarmedia.proto.device.DeviceProto;

public class DeviceMessageHandler {

    private static DeviceMessageHandler mobileMessageHandler;

    private final static String TAG = "DeviceMessageHandler";

    public static DeviceMessageHandler getInstance() {
        if (mobileMessageHandler == null) {
            mobileMessageHandler = new DeviceMessageHandler();
        }
        return mobileMessageHandler;
    }

    public void handleOperation(KaraokeController controller, DeviceProto.Operation operation) {
        Log.i(TAG, "处理副屏消息指令：" + operation.getOperateType());
        try {
            switch (operation.getOperateType()) {
                case 1://切歌
                    controller.next(true);
                    break;
                case 2://原伴唱
                    controller.originalAccompany();
                    break;
                case 3://暂停/播放
                    controller.pauseStart();
                    break;
                case 4://静音/取消静音
                    controller.muteCancelMute();
                    break;
                case 5://音量
                    if (operation.getOperateValue() > 0) {
                        controller.volumeUp();
                    } else {
                        controller.volumeDown();
                    }
                    break;
                case 6://重唱
                    controller.replay();
                    break;
                case 7://评分
                    controller.setScoreMode(operation.getOperateValue());
                    break;
                case 8://录音
                    controller.setRecord(operation.getOperateValue());
                    break;
                case 9://麦克风
                    if (operation.getOperateValue() > 0) {
                        controller.micUp(true);
                    } else {
                        controller.micDown(true);
                    }
                    break;
                case 10://麦克风音调
                    if (operation.getOperateValue() == 0) {
                        controller.micToneDefault(true);
                    } else if (operation.getOperateValue() > 0) {
                        controller.micToneUp(true);
                    } else {
                        controller.micToneDown(true);
                    }
                    break;
                case 11://音乐音调
                    if (operation.getOperateValue() == 0) {
                        controller.toneDefault();
                    } else if (operation.getOperateValue() > 0) {
                        controller.toneUp();
                    } else {
                        controller.toneDown();
                    }
                    break;
                case 12://弹幕
                    controller.onBarrage(operation.getOperateText());
                    break;
                case 13://表情
                    controller.atmosphere(operation.getOperateValue(), true);
                    break;
                case 14://服务铃
                    controller.setServiceMode(operation.getOperateValue(), true);
                    break;
                case 15://点自制MV
                    controller.selectMv(false, operation.getOperateValue(), null, null);
                    break;
                case 16://优先点自制MV
                    controller.selectMv(true, operation.getOperateValue(), null, null);
                    break;
                case 17://点歌
                    controller.selectSong(false, operation.getOperateText(), null, null);
                    break;
                case 18://优先点歌
                    controller.selectSong(true, operation.getOperateText(), null, null);
                    break;
                case 19://优先已点
                    controller.priorityChoose(operation.getOperateText());
                    break;
                case 20://删除已点
                    controller.deleteChoose(operation.getOperateText());
                    break;
                case 21://炫屏
                    controller.setCoolScreen(operation.getOperateValue(), operation.getOperateText());
                    break;
                case 22://自动灯光
                    controller.setAutoLight(operation.getOperateValue());
                    break;
                case 23://音效
                    controller.effect(operation.getOperateValue(), true);
                    break;
                case 24://混响
                    controller.reverberation(operation.getOperateValue(), true);
                    break;
                case 25://打乱
                    controller.shuffle();
                    break;
                case 26://灯光模式
                    controller.lightMode(operation.getOperateValue(), true, true);
                    break;
                case 27://亮度
                    controller.lightBrightness(operation.getOperateValue(), true);
                    break;
                case 28://电视屏黑屏
                    controller.setTelevision(operation.getOperateValue(), true);
                    break;
                case 29://重置二维码
                    controller.resetBoxUUID(true);
                    break;
                case 30://空调模式
                    controller.onAirConMode(operation.getOperateValue());
                    break;
                case 31://空调风速
                    controller.onAirWindSpeed(operation.getOperateValue());
                    break;
                case 32://排风开关
                    controller.onWindSwitch(operation.getOperateValue() > 0);
                    break;
                case 33://泡泡机
                    controller.onBubble();
                    break;
                case 34://烟雾机
                    controller.onSmoke();
                    break;
                case 35://空调开关
                    controller.onAirConSwitch(operation.getOperateValue() > 0);
                    break;
                case 36://温度加减
                    controller.onTempUpDown(operation.getOperateValue() > 0);
                    break;
                case 37://炫屏状态
                    controller.setCoolScreenStatus(operation.getOperateValue(), Integer.valueOf(operation.getOperateText()));
                    break;
                case 38: //动态表情
                    controller.emojiDynamic(operation.getOperateText());
                    break;
                default:
                    Log.e(TAG, "未知副屏指令:" + operation.getOperateType());
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "处理副屏消息：" + operation.getOperateType() + "异常", e);
        }
    }
}
