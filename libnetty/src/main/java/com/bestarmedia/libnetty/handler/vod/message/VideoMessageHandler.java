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
import com.bestarmedia.proto.node.VideoMessageRequest;
import com.bestarmedia.proto.node.VideoMessageResponse;

import io.netty.channel.Channel;


/**
 * 推送视频到屏幕请求
 */
public class VideoMessageHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            VideoMessageRequest msg = VideoMessageRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, "推送视频到屏幕请求 push_to_queue:" + msg.getPushToQueue() + " push_to_queue_position:" + msg.getPushToQueuePosition()
                    + " type:" + msg.getVideo().getType() + " play_type:" + msg.getVideo().getPlayType() + " img url:" + msg.getVideo().getUrl());
            if (msg.getPushToQueue() == 1) {
                NotificationMessageData.getInstance().addVideoMessage(msg, msg.getPushToQueuePosition() == 1);
            } else {
                EventBusUtil.postSticky(EventBusId.ImId.VIDEO_MESSAGE, msg);
            }
            //回复
            VideoMessageResponse response = VideoMessageResponse.getDefaultInstance().toBuilder().setCode(1)
                    .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(msg.getUserId()).build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000018.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
