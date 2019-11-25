package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class ButtonStatusProtoHandler implements ProtoHandlerInterface<ProtoFrame> {
    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.ButtonStatus msg = DeviceProto.ButtonStatus.parseFrom(protoFrame.byteBuf);
            Log.i("NettyServerHandler", "收到按钮状态信息！volume:" + msg.getCurrentVolume());
            ButtonStatus buttonStatus = new ButtonStatus();
            buttonStatus.isPause = msg.getIsPause();
            buttonStatus.isOriginal = msg.getOriginOn();
            buttonStatus.isMute = msg.getIsMute();
            buttonStatus.currentVolume = msg.getCurrentVolume();
            buttonStatus.serviceMode = msg.getServiceMode();
            buttonStatus.scoreMode = msg.getScoreMode();
            buttonStatus.isLightAuto = msg.getIsLightAuto();
            buttonStatus.isHdmiBack = msg.getIsHdmiBlack();
            buttonStatus.isRecord = msg.getIsRecord();
            buttonStatus.currentLightCode = msg.getCurrentLightCode();
            EventBusUtil.postSticky(EventBusId.ImId.BUTTON_STATUS, buttonStatus);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
