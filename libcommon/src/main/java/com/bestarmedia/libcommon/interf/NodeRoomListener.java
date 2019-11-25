package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.vod.NodeRoomV4;

/**
 * Created by J Wong on 2018/10/9.
 */

public interface NodeRoomListener {

    //第一次加载房间信息
    void onNodeRoomLoaded(NodeRoomV4 nodeRoomV4);

    //房间状态变化
    void onRoomSessionChanged(String preSession, String curSession);

    void onNodeRoomReturn(NodeRoomV4 v4);

}
