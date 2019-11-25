package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/29.
 */

public class NodeCrash implements Serializable {

    @Expose
    public String ip;

    @Expose
    public String room_code;

    @Expose
    public String room_name;

    @Expose
    public String device_code;

    @Expose
    public String device_mac;

    @Expose
    public String device_brand;

    @Expose
    public String app_name;

    @Expose
    public int app_version_code;

    @Expose
    public String app_version_name;

    @Expose
    public int system_number;

    @Expose
    public String crash_log;

    public NodeCrash(String ip, String room_code, String room_name, String deviceCode, String deviceMac,
                     String deviceBrand, String appName, int appVersionCode, String appVersionName, int systemNumber, String crashLog) {
        this.ip = ip;
        this.room_code = room_code;
        this.room_name = room_name;
        this.device_code = deviceCode;
        this.device_mac = deviceMac;
        this.device_brand = deviceBrand;
        this.app_name = appName;
        this.app_version_code = appVersionCode;
        this.app_version_name = appVersionName;
        this.system_number = systemNumber;
        this.crash_log = crashLog;
    }
}
