package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;

import java.util.List;

/**
 * Created by J Wong on 2018/10/9.
 */

public interface SafetyListener {

    //房间消防检查不通过
    void onRoomSafeFail();

    //店家消防检查通过
    void onSafeSucceed();

    //房间消防检查上报成功
    void onSafeUploadSucceed(boolean isSucced);
    //场所消防检查不通过
    void onStoreSafeFail();
    //离线申请成功
    void offlineSucceed();
}
