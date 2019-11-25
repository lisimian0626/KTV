package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.monitor.BusinessBroadcast;

import io.netty.channel.Channel;


/**
 * 店家营业成功
 */
public class BusinessBroadcastHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            BusinessBroadcast msg = BusinessBroadcast.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "店家营业成功！打卡时间:" + msg.getCheckAt() + " ktv_net_code:" + msg.getKtvNetCode());
            EventBusUtil.postSticky(EventBusId.ImId.BUSINESS_STATUS_CHANGED, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
