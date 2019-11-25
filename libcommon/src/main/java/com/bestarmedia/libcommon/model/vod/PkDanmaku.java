package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.im.UserBase;

/**
 * Created by J Wong on 2018/12/5.
 */

public class PkDanmaku extends PkSocketBase {

    public UserBase user;

    public String text;

    public PkDanmaku(UserBase user, String text, String sendKtvNetCode, String sendKtvRoomCode, String receiveKtvNetCode, String receiveKtvRoomCode) {
        this.user = user;
        this.text = text;
        this.send_ktv_net_code = sendKtvNetCode;
        this.send_ktv_room_code = sendKtvRoomCode;
        this.receive_ktv_net_code = receiveKtvNetCode;
        this.receive_ktv_room_code = receiveKtvRoomCode;
    }
}
