package com.bestarmedia.libnetty.handler.device.message;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.AirConStatus;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libnetty.netty.NettyConfig;
import com.bestarmedia.libnetty.netty.ProtoFrame;
import com.bestarmedia.libnetty.netty.ProtoHandlerInterface;
import com.bestarmedia.proto.device.DeviceProto;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;


/**
 * Created by J Wong on 2019/6/21.
 */
public class DownloadSongListProtoHandler implements ProtoHandlerInterface<ProtoFrame> {
    @Override
    public void handle(Channel channel, ProtoFrame protoFrame) {
        try {
            DeviceProto.DownloadSongList msg = DeviceProto.DownloadSongList.parseFrom(protoFrame.byteBuf);
            Log.i("NettyServerHandler", "收到主屏歌曲下载列表 :" + msg.getSongList());
            Map<String, SongSimple> map = new HashMap<>();
            if (msg.getSongList() != null) {
                for (DeviceProto.Song song : msg.getSongList()) {
                    SongSimple simple = new SongSimple();
                    simple.id = song.getId();
                    simple.songName = song.getSongName();
                    simple.singerMid = song.getSingerMidList();
                    simple.singer = song.getSingerList();
                    simple.videoType = song.getVideoType();
                    simple.isHd = song.getIsHd();
                    simple.isCloud = song.getIsCloud();
                    simple.mainLyric = song.getMainLyric();
                    simple.hot = song.getHot();
                    simple.isPay = song.getIsPay();
                    simple.isRedPacket = song.getIsRedPacket();
                    simple.downloadStatus = song.getDownloadStatus();
                    simple.downloadPercent = song.getDownloadPercent();
                    simple.downloadSort = song.getDownloadSort();
                    simple.isShowSingers = song.getIsShowSinger();
                    simple.isToTop = song.getIsToTop();
                    map.put(song.getId(), simple);
                }
            }
            EventBusUtil.postSticky(EventBusId.ImId.MAIN_DOWNLOAD_SONG_LIST, map);
        } catch (Exception e) {
            Log.e(NettyConfig.TAG, "消息解析错误：" + protoFrame.id);
        }
    }

}
