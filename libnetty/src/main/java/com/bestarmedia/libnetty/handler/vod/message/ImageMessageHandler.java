package com.bestarmedia.libnetty.handler.vod.message;

import android.util.Log;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.NettyMessageEnum;
import com.bestarmedia.libnetty.netty.NotificationMessageData;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.ProtoUtil;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.node.ImageMessageResponse;

import io.netty.channel.Channel;


/**
 * 推送图片到屏幕请求
 */
public class ImageMessageHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            ImageMessageRequest msg = ImageMessageRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "推送图片到屏幕请求 push_to_queue:" + msg.getPushToQueue()
                    + " push_to_queue_position:" + msg.getPushToQueuePosition() + " img url:" + msg.getImage().getUrl());
            if (msg.getPushToQueue() == 1) {//加入队列
                NotificationMessageData.getInstance().addImageMessage(msg, msg.getPushToQueuePosition() == 1);
            } else {
                EventBusUtil.postSticky(EventBusId.ImId.IMAGE_MESSAGE, msg);
            }
            //回复
            ImageMessageResponse response = ImageMessageResponse.getDefaultInstance().toBuilder().setCode(1)
                    .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(msg.getUserId()).build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000016.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
