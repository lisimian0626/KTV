package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.im.UserBase;

/**
 * Created by J Wong on 2018/12/5.
 */

public class PkScore extends PkSocketBase {

    public UserBase user;

    public int score;

    public PkScore(UserBase user, int score, String sendKtvNetCode, String sendKtvRoomCode, String receiveKtvNetCode, String receiveKtvRoomCode) {
        this.user = user;
        this.score = score;
        this.send_ktv_net_code = sendKtvNetCode;
        this.send_ktv_room_code = sendKtvRoomCode;
        this.receive_ktv_net_code = receiveKtvNetCode;
        this.receive_ktv_room_code = receiveKtvRoomCode;
    }
}
