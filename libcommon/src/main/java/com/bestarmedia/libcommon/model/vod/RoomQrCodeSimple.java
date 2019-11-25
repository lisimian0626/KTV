package com.bestarmedia.libcommon.model.vod;


import android.support.annotation.NonNull;
import android.util.Log;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.util.UUIDUtil;

/**
 * Created by admin on 2016/7/20.
 */
public class RoomQrCodeSimple {

    public String session;

    public String roomCode;

    public String ktvCode;

    public RoomQrCodeSimple(String session, String roomCode, String ktvCode) {
        this.session = session;
        this.roomCode = roomCode;
        this.ktvCode = ktvCode;
    }


    @Override
    @NonNull
    public String toString() {
        String uuid = UUIDUtil.getRandomUUID();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(VodConfigData.getInstance().getCloudHost()).append("/mobile/?");
        stringBuilder.append(session).append("|");
        stringBuilder.append(uuid.substring(0, 2)).append("|");
        stringBuilder.append(uuid.substring(2, 4)).append("|");
        stringBuilder.append(uuid.substring(4, 6)).append("|");
        stringBuilder.append(roomCode).append("|");
        stringBuilder.append(ktvCode).append("|");
        stringBuilder.append("5.0");
        String qrCode = stringBuilder.toString();
        Log.d(getClass().getSimpleName(), "qrCode:" + qrCode);
        return qrCode;
    }

}
