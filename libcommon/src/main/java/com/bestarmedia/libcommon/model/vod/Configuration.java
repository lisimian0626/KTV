package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by J Wong on 2018/10/16.
 */

public class Configuration {
    @SerializedName("erp_control")
    public int erpControl;
    @SerializedName("erp_vendor")
    public String erpVendor;
    @SerializedName("erp_need_balance")
    public int erpNeedBalance;//例送总数是否需要乘以构成对应商品数量，乐点、微达不需要乘以购物车数量

    @SerializedName("slot_card")
    public int slotCard;

    @SerializedName("need_password_before_view")
    public int needPasswordBeforeView;

    @SerializedName("need_password_before_order")
    public int needPasswordBeforeOrder;

    @SerializedName("give_away")
    public int giveAway;

    @SerializedName("skin")
    public Skin skin;

    @SerializedName("alarm_img_a")
    public String alarmImgA;

    @SerializedName("alarm_img_b")
    public String alarmImgB;

    @SerializedName("alarm_audio")
    public String alarmAudio;

    @SerializedName("alarm_time")
    public int alarmTime;

    @SerializedName("alarm_status")
    public int alarmStatus;

    @SerializedName("base_volume_ratio")
    public int baseVolumeRatio;

    @SerializedName("initial_volume")
    public int initialVolume = 4;

    @SerializedName("dance_volume")
    public int danceVolume;

    @SerializedName("broadcast_volume")
    public int broadcastVolume = 4;

    @SerializedName("timingVolume")
    public TimingVolume timingVolume;

    @SerializedName("is_scene")
    public int isScene;

    @SerializedName("is_grade_lib")
    public int isGradeLib;

    //歌曲灯光是否可用，0不可用，1可用
    @SerializedName("is_auto_light_enable")
    public int autoLightEnable;

    //开机默认歌曲灯光开关状态，0关闭，1开启
    @SerializedName("auto_light_default")
    public int autoLightDefault;

    //综合智能灯光开启用状态，0关闭，1开启
    @SerializedName("intelligent_light")
    public int intelligentLight;

    @SerializedName("logo")
    public String logo;

    @SerializedName("screen_logo")
    public String screenLogo;

    @SerializedName("dance_song")//单曲音量控制曲种，多曲种以“,”分隔
    public String danceSong;

    @SerializedName("dance_type")//音量控制类型，0全部，1单曲
    public String danceType;

    @SerializedName("keystone")
    public String keystone;

    @SerializedName("current_time")
    public long currentTime;

    @SerializedName("interval")
    public int interval;

    @SerializedName("away")
    public int away;

    @SerializedName("shutdown")
    public Shutdown shutdown;

    @SerializedName("disco_status")
    public int discoStatus;

    @SerializedName("disco_pw")
    public String discoPw;

    @SerializedName("movie_status")
    public int movieStatus;

    @SerializedName("movie_pw")
    public String moviePw;

    @SerializedName("password")
    public String password;

    @SerializedName("safety_password")
    public String safetyPassword;

    @SerializedName("qr_size")
    public int qrSize;

    @SerializedName("qr_interval")
    public int qrInterval;

    @SerializedName("qr_duration")
    public int qrDuration;

    @SerializedName("song_pay")
    public int songPay;

    @SerializedName("pay_secret_key")
    public String paySecretKey;

    @SerializedName("material_path")
    public String materialPath;

    @SerializedName("media_file_path")
    public String mediaFilePath;

    @SerializedName("beacon_uuid")
    public String beaconUuid = "FD A5 06 93- A4 E2- 4F B1- AF CF- C6 EB 07 64 78 25";

    @SerializedName("beacon_major")
    public String beaconMajor = "10168";

    @SerializedName("beacon_minor")
    public String beaconMinor = "35379";

    @SerializedName("http_host")
    public HttpHost httpHost;

    @SerializedName("monitor")
    public MonitorConfig monitorConfig;

    @SerializedName("wifi")
    public List<WIFI> wifiList;

    @SerializedName("ad_screen_time")
    public int adScreenTime = 120;

    @SerializedName("ad_screen_interval")
    public int adScreenInterval = 15;

    public static class Skin {
        @SerializedName("skin_id")
        public String skinId;
        @SerializedName("skin_type")
        public String skinType;
    }

    public static class Shutdown {
        @SerializedName("shutdown_status")
        public int shutdownStatus;
        @SerializedName("shutdown_time")
        public String shutdownTime;
    }

    public static class TimingVolume {
        @SerializedName("volume_time")
        public String volumeTime;
        @SerializedName("volume_status")
        public int volumeStatus;
        @SerializedName("volume_val")
        public int volumeVal;
    }

    public static class HttpHost {
        @SerializedName("gateway_host")
        public String gatewayHost;
        @SerializedName("cloud_host")
        public String cloudHost;
        @SerializedName("api_host")
        public String apiHost;
        @SerializedName("node_host")
        public String nodeHost;
        @SerializedName("cms_host")
        public String cmsHost;
    }

    public static class MonitorConfig {
        @SerializedName("domain")
        public String domain;
        @SerializedName("frontend")
        public String frontend;
        @SerializedName("host")
        public String host;
        @SerializedName("port")
        public int port;
        @SerializedName("thread_count")
        public int threadCount;
    }

    public static class WIFI {
        @SerializedName("wifi_name")
        public String wifiName;
        @SerializedName("wifi_password")
        public String wifiPassword;
    }
}
