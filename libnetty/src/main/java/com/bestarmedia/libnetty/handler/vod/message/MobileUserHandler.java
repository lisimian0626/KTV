package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.vod.MobileUser;

import io.netty.channel.Channel;


/**
 * 手机操作相关
 */
public class MobileUserHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            MobileUser msg = MobileUser.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "手机用户扫码登录:" + msg.getName());
            EventBusUtil.postSticky(EventBusId.PhoneId.MOBILE_LOGIN, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
