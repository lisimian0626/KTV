package com.bestarmedia.libcommon.data;


import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;


/**
 * Created by J Wong on 2018/10/9 09:47.
 */
public class DeviceInfoData {
    private volatile static DeviceInfoData deviceInfoData;
    private DeviceInfo deviceInfo;
    private NodeRoom nodeRoom;
    private String localId;
    public static DeviceInfoData getInstance() {
        if (deviceInfoData == null) {
            synchronized (DeviceInfoData.class) {
                if (deviceInfoData == null)
                    deviceInfoData = new DeviceInfoData();
            }
        }

        return deviceInfoData;
    }

    public NodeRoom getNodeRoom() {
        return nodeRoom;
    }

    public void setNodeRoom(NodeRoom nodeRoom) {
        this.nodeRoom = nodeRoom;
    }

    private DeviceInfoData() {

    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
