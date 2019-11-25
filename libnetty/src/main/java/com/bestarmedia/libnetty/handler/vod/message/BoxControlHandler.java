package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.node.BoxControlRequest;

import io.netty.channel.Channel;


/**
 * 关机、重启
 */
public class BoxControlHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            BoxControlRequest msg = BoxControlRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "机顶盒关机/重启请求 op:" + msg.getOp() + " 设备IP:" + msg.getIp());
            channel.close();//关闭连接
            EventBusUtil.postSticky(EventBusId.ImId.BOX_CONTROL, msg);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
