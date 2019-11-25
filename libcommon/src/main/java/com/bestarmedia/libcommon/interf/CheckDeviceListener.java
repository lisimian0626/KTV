package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.node.Author;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;

import java.util.List;

/**
 * Created by J Wong on 2018/10/9.
 */

public interface CheckDeviceListener {

    //获取设备信息成功
    void onDeviceInfoSucced(DeviceInfo deviceInfo);

    //获取设备信息失败
    void onDeviceInfoFail(String msg);

    //设备注册
    void onRegister(boolean isSucced,String msg);

    //获取jwt
    void onGetJwt(String jwt);

    //获取房间信息
    void onNodeRoom(NodeRoom nodeRoom);

    void onLogout(boolean succed,String msg);

    void onDeviceType(List<DeviceType> deviceTypeList);
    //店家授权信息
    void onAuthor(Author author);
}
