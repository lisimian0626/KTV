package com.bestarmedia.libcommon.eventbus;

public class EventBusId {

    /**
     * 公共id
     */
    public static final class Id {
        // 按钮状态改变了
        public static final int BUTTON_STATUS_CHANGED = 20191001;
        // 添加fragment到栈顶
        public static final int ADD_FRAGMENT = 20191002;
        //音量限制
        public static final int DANCE_VOL_CHANGE = 20191003;
        //fragment退栈
        public static final int BACK_FRAGMENT = 20191004;
        //当前屏保
        public static final int CURRENT_SCREEN_AD = 20191005;
        //炫屏改变了
        public static final int COOL_SCREEN_CHANGED = 20191006;

        //获取配置成功加载banner和新歌模块
        public static final int AFTER_CONFIG = 20191007;
        //
        public static final int CHOOSE_SONG_CHANGED = 44;
        //
        public static final int SUNG_SONG_CHANGED = 45;
        //
        public static final int ROOM_USER_CHANGED = 64;
        //
        public static final int PK_DUEL_CHANGED = 68;
        //
        public static final int SONG_DOWNLOAD_CHANGED = 102;
        // 房态编号
        public static final int ROOM_STATUS_CHANGED = 123;
        //房间号发生改变
        public static final int KTV_ROOM_CODE_CHANGED = 20193031;
        //房间设备发生变化
        public static final int KTV_ROOM_DEVCE_CHANGED = 20193032;
        //皮肤改变
        public static final int SKIN_CHANGED = 20193091;

        //开机启动
        //获取配置完成
        public static final int GET_CONFIG = 20193100;

        //登录成功
        public static final int LOGIN_SUCCEED = 20193101;

        //获取设备信息失败（进入注册流程）
        public static final int GET_DEVICES_FAIL = 20193102;

        //房间消防信息检查不通过
        public static final int SAFETY_ROOM_FAIL = 20193103;

        //房间消防信息检查不通过
        public static final int SAFETY_STORE_FAIL = 20193104;

        //房间消防检查上报成功
        public static final int POST_ROOM_SAFETY_SUCCEED = 20193105;

        //请求检查消防
        public static final int CHECK_SAFETY = 201950020;

        //请求检查房间是否付费
        public static final int CHECK_PAY = 201950021;

        //
        public static final int AFTER_ROOM_INFO = 201950022;

        //网络连接状态变化
        public static final int NETWORK_CHANGE = 201950023;
        //调用付费平台
        public static final int PAY_INTERFACE_ERROR = 201950024;
        //检查活动
        public static final int CHECK_BNS_ACTIVITY = 201950025;
        //检查活动数据变化了
        public static final int BNS_ACTIVITY_CHANGED = 201950026;
    }

    /**
     * 电视屏相关id
     */
    public static final class PresentationId {
        //显示图标
        public static final int SHOW_CENTER_ICON = 20192000;
        // 系统音量变化
        public static final int SYSTEM_VOL_CHANGED = 20192001;
        //电视屏搜索
        public static final int PRESENTATION_MARGIN_CHANGED = 20192002;
        //        //炫屏
//        public static final int PRESENTATION_HYUN_CHANGED = 20192003;
        //显示角标
        public static final int PRESENTATION_SHOW_AD_CORNER = 20192004;
        //电视屏停止播放
        public static final int MAIN_PLAYER_STOP = 20192005;
        //电视屏弹幕
        public static final int PRESENTATION_BARRAGE = 20192006;
        //火警警报开关
        public static final int FIRE_ACTION = 20192007;
        //电视屏恢复播放
        public static final int MAIN_PLAYER_RESUME = 20192008;
        //播放内置表情
        public static final int PLAY_EMOJI = 20192009;
        //呼叫服务状态
        public static final int SERVICE_STATUS = 20192010;
        //电视屏黑屏状态
        public static final int HDMI_BLACK_STATUS = 20192011;
        //播放红包广告
        public static final int PLAY_RED_PACKET = 20192012;
        //播放自制MV图片
        public static final int PLAY_MV_GALLERIES = 20192013;
        //清屏
        public static final int CLEAR = 20192014;
        //显示评分结果
        public static final int SHOW_SCORE_RESULT = 20192015;
        //显示暂停广告
        public static final int SHOW_AD_PAUSE = 20192016;
        //当前得分
        public static final int CURRENT_SCORE = 20192017;
        //评分显示与否
        public static final int SCORE_VISIBILITY_STATUS = 20192018;
        //显示手机点歌二维码
        public static final int SHOW_PHONE_QR_CODE = 20192019;
        //播放文本通知
        public static final int PLAY_NOTIFICATION_TEXT = 20192020;
        //播放图片通知
        public static final int PLAY_NOTIFICATION_IMAGE = 20192021;
        //播放视频通知
        public static final int PLAY_NOTIFICATION_VIDEO = 20192022;
        //播放视频通知
        public static final int PLAY_NOTIFICATION_VIDEO_SMALL = 20192023;
        //发送弹幕
        public static final int PRESENTATION_BARRAGE_NEW = 20192024;
        //播放动态表情
        public static final int PLAY_EMOJI_DYNAMIC = 20192025;
        //电视屏显示店家Logo
        public static final int SHOW_STORE_LOGO_ON_TV = 20192026;
    }

    /**
     * 播放器相关
     */
    public static final class PlayerId {
        //切歌
//        public static final int PLAYER_NEXT = 20193001;
        //原唱
//        public static final int PLAYER_ORIGINAL = 20193002;
        //伴唱
//        public static final int PLAYER_ACCOMPANY = 20193003;
        //暂停
//        public static final int PLAYER_PAUSE = 20193004;
        //播放
        public static final int PLAYER_START = 20193005;
        //音量-
//        public static final int PLAYER_VOL_DOWN = 20193006;
        //音量+
//        public static final int PLAYER_VOL_UP = 20193007;
        //静音
//        public static final int PLAYER_MUTE = 20193008;
        //取消静音
//        public static final int PLAYER_CANCEL_MUTE = 20193009;
        //重唱
//        public static final int PLAYER_REPLAY = 20193010;
        //降调
//        public static final int PLAYER_TONE_DOWN = 20193011;
        //升调
//        public static final int PLAYER_TONE_UP = 20193012;
        //原调
//        public static final int PLAYER_TONE_DEFAULT = 20193013;
        //播放歌曲
        public static final int PLAYER_PLAY_SONG = 20193014;
        //进度拖动
        public static final int PLAYER_SEEK_TO = 20193017;
        //插播视频
//        public static final int INTERPOLATION_VIDEO = 20193018;

    }

    /**
     * 点歌相关
     */
    public static final class SongOperationId {
        //点歌
        public static final int SONG_SELECT = 20194001;
        //        优先
//        public static final int SONG_TOP = 20194002;
        //预览
        public static final int SONG_PREVIEW = 20194003;
        //收藏
        public static final int SONG_COLLECT = 20194004;
    }

    /**
     * 即时通讯
     */
    public static final class ImId {
        //VOD服务器连接状态
        public static final int VOD_NETTY_CONNECTION = 20195000;
        //按钮状态变化
        public static final int BUTTON_STATUS = 20195001;
        //主屏空调状态变化
        public static final int MAIN_AIR_CON_STATUS = 20195002;
        //火警
        public static final int FIRE_ALARM = 20195003;
        //定时降音量
        public static final int VOLUME_CONTROL = 20195004;
        //关机、重启
        public static final int BOX_CONTROL = 20195005;
        //截图
        public static final int SCREEN_SHOT = 20195006;
        //文本消息请求
        public static final int TEXT_MESSAGE = 20195007;
        //图片消息请求
        public static final int IMAGE_MESSAGE = 20195008;
        //视频消息请求
        public static final int VIDEO_MESSAGE = 20195009;
        //已点已唱列表变化
        public static final int ROOM_SONG_LIST_CHANGED = 20195010;
        //空调状态变化
        public static final int AIR_CON_STATUS = 20195011;
        //主屏下载列表变化
        public static final int MAIN_DOWNLOAD_SONG_LIST = 20195012;
        //主屏广告同步
        public static final int MAIN_SYNC_AD = 20195013;
        //主屏闯关状态变化
        public static final int MAIN_GAME_STATUS = 20195014;
        //炫屏状态变化
        public static final int MAIN_COOL_SCREEN_STATUS = 20195015;
        //房态变化
        public static final int ROOM_STATUS_CHANGED = 20195016;
        //炫屏状态变化
        public static final int COOL_SCREEN_CHANGED = 20195017;
        //斗歌启动
        public static final int BATTLE_START = 20195018;
        //斗歌放弃
        public static final int BATTLE_GIVE_UP = 20195019;
        //对方斗歌结果
        public static final int BATTLE_OPPONENT_RESULT = 20195020;
        //斗歌结果
        public static final int BATTLE_RESULT = 20195021;
        //对方斗歌得分
        public static final int BATTLE_SCORE = 20195022;
        //对方斗歌弹幕
        public static final int BATTLE_BARRAGE = 20195023;
        //对方斗歌最终得分
        public static final int BATTLE_RESULT_SCORE = 20195024;
        //营业状态变化
        public static final int BUSINESS_STATUS_CHANGED = 20195025;
        //文本通知队列变化
        public static final int NOTIFICATION_TEXT_CHANGED = 20195026;
        //图片通知队列变化
        public static final int NOTIFICATION_IMAGE_CHANGED = 20195027;
        //视频通知队列变化
        public static final int NOTIFICATION_VIDEO_CHANGED = 20195028;
        //视频通知队列变化
        public static final int NOTIFICATION_VIDEO_SMALL_CHANGED = 20195029;

        //支付通知
        public static final int PAY_NOTIFIY = 20195030;

        public static final int SCENES_TYPE_CHANGED = 20195085;

        public static final int CURRENT_SECENCE_MODE_CHANGED = 20195088;


    }

    public static final class PhoneId {
        //手机扫码登录
        public static final int MOBILE_LOGIN = 20196001;
        //手机消息
        public static final int MOBILE_MESSAGE = 20196002;
    }

    //游戏
    public static final class Game {
        //闯关（等级）
        public static final int GAME_LEVEL_PASS_CHANGED = 20197057;

        //闯关游戏开始指令
        public static final int GAME_START = 20197001;
    }

    //中控
    public static final class Serial {

        public static final int LIGHT_MODE_CHANGED = 20198001;
    }

    /**
     * 设备间通信
     */
    public static final class DeviceId {
        //操作指令
        public static final int OPERATION = 20199001;
        //闯关游戏操作指令
        public static final int GAME_OPERATION = 20199002;
        //闯关游戏开始指令
        public static final int GAME_START = 20199003;

    }
}
