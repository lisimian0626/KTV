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
import com.bestarmedia.proto.node.BarrageRequest;
import com.bestarmedia.proto.node.BarrageResponse;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.TextMessageResponse;

import io.netty.channel.Channel;


/**
 * 推送文字到屏幕请求
 */
public class BarrageHandler implements ProtoHandlerInterface<ProtoFrame> {

    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            BarrageRequest msg = BarrageRequest.parseFrom(protoFrame.byteBuf);
            Log.i(NettyConfig.TAG, " 发送弹幕:  用户:" + msg.getName()
                    + " text:" + msg.getText()+" roomCode:"+msg.getRoomCode());

            EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_BARRAGE_NEW, msg);

            //回复
            BarrageResponse response = BarrageResponse.getDefaultInstance().toBuilder().setCode(1)
                    .setRoomCode(VodConfigData.getInstance().getRoomCode()).setUserId(msg.getUserId()).setKtvNetCode(VodConfigData.getInstance().getKtvNetCode()).build();
            channel.writeAndFlush(ProtoUtil.createProtoFrame(NettyMessageEnum.H12000020.id, response));
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
