package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;

import io.netty.channel.Channel;


/**
 * 手机操作相关
 */
public class MobileMessageBroadcastHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            MobileMessageBroadcast msg = MobileMessageBroadcast.parseFrom(protoFrame.byteBuf);
//            String current = NodeRoomInfo.getInstance().getPhoneSession();
//            if (!current.equalsIgnoreCase(msg.getUser().getRoomSession())) {
//                Log.e(NettyConfig.TAG, "用户二维码session已过期! 当前session：" + current + " 用户session：" + msg.getUser().getRoomSession());
//                sendExpiredSession();
//                return;
//            }
            EventBusUtil.postSticky(EventBusId.PhoneId.MOBILE_MESSAGE, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }


}
