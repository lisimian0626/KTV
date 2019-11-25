package com.bestarmedia.libcommon.data;

import android.text.TextUtils;

import com.bestarmedia.libcommon.model.vod.NodeRoom;
import com.bestarmedia.libcommon.model.vod.NodeRoomSession;
import com.bestarmedia.libcommon.model.vod.NodeRoomV4;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;


/**
 * Created by J Wong on 2018/10/9 09:47.
 */
public class NodeRoomInfo {

    private volatile static NodeRoomInfo mRomInfo;

    private NodeRoomV4 mNodeRoom = null;

    private String mRoomCode = null;

    public static NodeRoomInfo getInstance() {
        if (mRomInfo == null) {
            synchronized (NodeRoomInfo.class) {
                if (mRomInfo == null)
                    mRomInfo = new NodeRoomInfo();
            }
        }

        return mRomInfo;
    }

    private NodeRoomInfo() {

    }

    public void setRoomCode(String roomCode) {
        mRoomCode = roomCode;
    }

    public void setNodeRoom(NodeRoomV4 nodeRoom) {
        mNodeRoom = nodeRoom;
    }

    public NodeRoomV4 getNodeRoom() {
        return mNodeRoom;
    }

    private NodeRoomSession getNodeRoomSession() {
        return getNodeRoom() != null ? getNodeRoom().roomSession : null;
    }

    public NodeRoom getRoom() {
        return getNodeRoom() != null ? getNodeRoom().room : null;
    }

    /**
     * 获取房间编号
     * 默认 BO
     *
     * @return
     */
//    public String getRoomCode() {
//        if (getRoom() != null && !TextUtils.isEmpty(getRoom().roomCode)) {
//            return getRoom().roomCode;
//        }
//        return !TextUtils.isEmpty(mRoomCode) ? mRoomCode : "B0";
//    }

    /**
     * 获取房间名称
     *
     * @return
     */
    public String getRoomName() {
        return getRoom() != null ? getRoom().ktvRoomCode : "";
    }

    /**
     * 获取开房session
     *
     * @return
     */
    public String getRoomSession() {
        return getNodeRoomSession() != null ? getNodeRoomSession().id : "";
    }

    /**
     * 获取KTV编号
     *
     * @return
     */
//    public String getKtvCode() {
//        return getNodeRoomSession() != null ? getNodeRoomSession().ktvNetCode : "";
//    }

    /**
     * 获取KTV名称
     *
     * @return
     */
    public String getKtvName() {
        return getNodeRoomSession() != null ? getNodeRoomSession().ktvName : "";
    }


    /**
     * 获取开房session
     *
     * @return
     */
    public String getPhoneSession() {
        return getRoom() != null ? getRoom().vodToken : "";
    }

    /**
     * 是否已开房
     */
    public boolean isRoomOpen() {
        return getNodeRoom() != null && getNodeRoom().room != null && mNodeRoom.room.status == 1;
    }


    /**
     * 是否能点酒水
     *
     * @return
     */
    public boolean isPurchaseEnabled() {
        return getNodeRoomSession() != null && getNodeRoomSession().isPurchaseEnabled == 1;
    }

    /**
     * 是否能点歌，有开房session才能点歌
     *
     * @return
     */
    public boolean canChooseSong() {
        return getNodeRoom() != null && getNodeRoom().room != null && !TextUtils.isEmpty(getNodeRoom().room.currentSession);
    }

    /**
     * 获取中控码值
     *
     * @return
     */
    public SerialPortCode getControlBox() {
        return getRoom() != null ? getRoom().controlBoxScheme : null;
    }
}
