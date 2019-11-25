package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.CoolScreenStatus;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class CoolScreenStatusProtoHandler implements ProtoHandlerInterface<ProtoFrame> {
    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.CoolScreenStatus msg = DeviceProto.CoolScreenStatus.parseFrom(protoFrame.byteBuf);
            Log.i("NettyServerHandler", "收到主屏炫屏状态信息！ :" + msg.getIsOpen());
            CoolScreenStatus screenStatus = new CoolScreenStatus();
            screenStatus.isOpen = msg.getIsOpen();
            screenStatus.isRandom = msg.getIsRandom();
            screenStatus.currentId = msg.getCurrentId();
            screenStatus.currentUrl = msg.getCurrentUrl();
            EventBusUtil.postSticky(EventBusId.ImId.MAIN_COOL_SCREEN_STATUS, screenStatus);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
