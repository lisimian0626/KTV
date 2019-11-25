package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.node.PayMessageRequest;
import com.bestarmedia.proto.node.VolumeRequest;

import io.netty.channel.Channel;


/**
 * 支付通知
 */
public class PayHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            PayMessageRequest msg = PayMessageRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "支付通知 ktv_code:" + msg.getKtvNetCode()
                    + " room_code:" + msg.getRoomCode() + " pay_url:" + msg.getPayUrl());
            EventBusUtil.postSticky(EventBusId.ImId.PAY_NOTIFIY, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
