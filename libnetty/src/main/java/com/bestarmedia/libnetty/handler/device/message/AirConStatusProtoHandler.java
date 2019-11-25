package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.AirConStatus;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.device.DeviceProto;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class AirConStatusProtoHandler implements ProtoHandlerInterface<ProtoFrame> {
    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.AirConStatus msg = DeviceProto.AirConStatus.parseFrom(protoFrame.byteBuf);
            Log.i("NettyServerHandler", "收到主屏空调状态信息！open :" + msg.getAirConOpen());
            AirConStatus buttonStatus = new AirConStatus();
            buttonStatus.airConOpen = msg.getAirConOpen();
            buttonStatus.mode = msg.getMode();
            buttonStatus.windOpen = msg.getWindOpen();
            buttonStatus.windSpeed = msg.getWindSpeed();
            EventBusUtil.postSticky(EventBusId.ImId.MAIN_AIR_CON_STATUS, buttonStatus);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
