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
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.TextMessageResponse;

import io.netty.channel.Channel;


/**
 * 推送文字到屏幕请求
 */
public class TextMessageHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            TextMessageRequest msg = TextMessageRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "推送文字到屏幕请求 push_to_queue:" + msg.getPushToQueue() + " push_to_queue_position:" + msg.getPushToQueuePosition()
                    + " text:" + msg.getText().getContent() + " 播放时长：" + msg.getText().getPlayTime() + " 轮播次数：" + msg.getText().getPlayCount() + " 颜色：" + msg.getText().getColor());
            if (msg.getPushToQueue() == 1) {//加入队列
                NotificationMessageData.getInstance().addTextMessage(msg, msg.getPushToQueuePosition() == 1);
            } else {
                EventBusUtil.postSticky(EventBusId.ImId.TEXT_MESSAGE, msg);
            }
            //回复
            TextMessageResponse response = TextMessageResponse.getDefaultInstance().toBuilder().setCode(1)
                    .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(msg.getUserId()).build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000014.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
