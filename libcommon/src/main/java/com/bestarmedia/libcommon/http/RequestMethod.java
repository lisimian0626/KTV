package com.bestarmedia.libcommon.http;

/**
 * Created by J Wong on 2015/10/19 11:41.
 */
public class RequestMethod {

    private final static String NODE_API = "v4.0/vod-node/";
    /**
     * 设备入库
     */
    public static final String DEVICE_STORE = "Localstore/warehouse";
    //版本升级
    public final static String VOD_VERSION = "v4.0/vod-api/version";
    //版本历史记录
    public final static String VOD_VERSION_LIST = "v4.0/vod-api/version-list";

    /*以下接口为 node-api */
    /**
     * 获取配置
     */
    public final static String NODE_PROFILE = NODE_API + "profile";
    /**
     * 设置房间名
     */
    public final static String NODE_ROOM_NAME = NODE_API + "node-room";
    /**
     * 房间编号
     */
    public final static String NODE_ROOM = NODE_API + "node-room/room-code";
    /**
     * 通过包房号获取包房开房信息
     */
    public final static String NODE_ROOM_INFO = NODE_API + "node-room/info";
    /**
     * 获取房间已点已唱歌单
     */
    public static final String NODE_ROOM_SONG_LIST = NODE_API + "room-song-list";
    /**
     * 查询已点列表
     */
    public static final String NODE_ROOM_SONG_LIST_CHOOSE = NODE_API + "room-song-list/choose";
    /**
     * 查询已唱列表
     */
    public static final String NODE_ROOM_SONG_LIST_SUNG = NODE_API + "room-song-list/sung";
    /**
     * 记录播放开始时间
     */
    public static final String NODE_ROOM_SONG_RECORD_PLAY_START = NODE_API + "room-song-list/record-play-start";

    /**
     * 打乱已点歌单
     */
    public static final String NODE_ROOM_SONG_LIST_SHUFFLE = NODE_API + "room-song-list/shuffle";
    /**
     * 获取酒水分类
     */
    public static final String NODE_PURCHASE_CATEGORY = NODE_API + "purchase/category";
    /**
     * 获取酒水列表GET/酒水下单POST
     */
    public static final String NODE_PURCHASE = NODE_API + "purchase";
    /**
     * 获取赠送酒水列表
     */
    public static final String NODE_PURCHASE_FREE = NODE_API + "purchase/free";
    /**
     * 获取特殊口味列表
     */
    public static final String NODE_PURCHASE_SPECIAL_TASTE = NODE_API + "purchase/special-taste";
    /**
     * 获取房间总消费
     */
    public static final String NODE_PURCHASE_BILL = NODE_API + "purchase/bill";
    /**
     * 获取房间消费明细
     */
    public static final String NODE_PURCHASE_BILL_DETAIL = NODE_API + "purchase/bill/detail";
    /**
     * 获取例送套餐列表
     */
    public static final String NODE_PURCHASE_COMBO = NODE_API + "purchase/combo";
    /**
     * 例送方案物品请求
     */
    public static final String NODE_PURCHASE_COMBO_ITEM = NODE_API + "purchase/combo/item";
    /**
     * 验证ERP账号密码
     */
    public static final String NODE_ERP_ACCESS = NODE_API + "erp/access";
    /**
     * 呼叫服务
     */
    public static final String NODE_ERP_ROOM_SERVICE = NODE_API + "erp-room/service";
    /**
     * 呼叫服务
     */
    public static final String NODE_ERP_ROOM_SERVICE_RESPONSE = NODE_API + "erp-room/service-response";

    /**
     * 公关打卡
     */
    public static final String NODE_ERP_ROOM_CHECK = NODE_API + "erp-room/check";

    /**
     * 公关打卡列表
     */
    public static final String NODE_ERP_ROOM_CHECK_LIST = NODE_API + "erp-room/checklist";

    /**
     * 上传crash日志
     */
    public static final String NODE_CRASH = NODE_API + "node-crash";

    /**
     * 上传手机点歌二维码session
     */
    public static final String NODE_QR_CODE = NODE_API + "qr-code";

    /**
     * 获取直播列表
     */
    public final static String NODE_LIVE = NODE_API + "live-program";

    /**
     * 获取版权收费套餐
     */
    public final static String NODE_TRADE_ITEM = NODE_API + "trade/item";

    /**
     * 生成下单二维码
     */
    public final static String NODE_TRADE = NODE_API + "trade";

    /**
     * 店家促销广告
     */
    public final static String NODE_PROMOTION = NODE_API + "sales-promotion";

    /**
     * 消防逃生图
     */
    public static final String NODE_EVACUATION = NODE_API + "evacuation-lan";

    /**
     * 自定义火警
     */
    public static final String NODE_FIRE_ALARM = NODE_API + "fire-alarm";

    /**
     * 店家公播广告
     */
    public static final String NODE_PUBLIC_BROADCASTING = NODE_API + "public-broadcasting/playing-list";

    /**
     * 文件上传带本地VOD服务器
     */
    public static final String NODE_MATERIAL = NODE_API + "material";

    /**
     * POST上传录音文件到云端,
     */
    public static final String NODE_MEDIA_RECORD = NODE_API + "media-record";

    /**
     * GET查询上传完成状态
     */
    public static final String NODE_MEDIA_RECORD_UPLOAD_STATUS = NODE_API + "media-record/upload-status";

    /**
     * 创建用户录音记录
     */
    public static final String NODE_MEDIA_RECORD_LOG = NODE_API + "media-record/record-log";

    /**
     * 获取房间列表
     */
    public static final String NODE_ROOM_LIST = NODE_API + "node-room-list";

    /**
     * 获取定时走马灯
     */
    public static final String NODE_RANGE_NOTICE = NODE_API + "range-notice";
    /**
     * 广告接口
     */
    public static final String NODE_AD = NODE_API + "ad";

    /**
     * 广告播放记录
     */
    public static final String NODE_AD_RECORD = NODE_AD + "/record";

    /**
     * 新建下载（新，注意传的参数为JSON数组）
     */
    public static final String NODE_CLOUD_DOWNLOAD = NODE_API + "cloud-download";

    /**
     * 下载进度
     */
    public static final String NODE_CLOUD_DOWNLOAD_PROGRESS = NODE_API + "cloud-download/progress";

    /**
     * 统计小编推荐点击
     */
    public static final String NODE_STATISTICS_TOPIC = NODE_API + "statistics/topic";


    //获取店家授权信息
    public static final String NODE_AUTHORIZATION = NODE_API + "profile/authorization-info";

    public final class V4 {

        private final static String V4_NODE = "v4.1/vod-node/";

        private final static String V4_API = "v4.1/vod-api/";

        //获取歌曲语种列表
        public static final String V4_API_LANGUAGE = V4_API + "language";

        //获取歌星地区列表
        public static final String V4_API_PART = V4_API + "part";

        //获取歌曲列表
        public static final String V4_API_SONG = V4_API + "song";

        //获取歌星列表
        public static final String V4_API_MUSICIAN = V4_API + "musician";

        //获取分类歌单
        public static final String V4_API_SONG_SONG_TYPE = V4_API + "song-type/";

        /**
         * JWT授权
         */
        public static final String V4_NODE_AUTH_TOKEN = V4_NODE + "auth/token";
        /**
         * 设备信息
         */
        public static final String V4_NODE_ROOM_DEVICE = V4_NODE + "room-device";

        public static final String NODE_ROOM_DEVICE = NODE_API + "room-device";
        //获取设备类型
        public static final String V4_NODE_ROOM_DEVICE_TYPE = V4_NODE + "room-device/type";
        //获取房间信息
        public static final String V4_NODE_ROOM = V4_NODE + "node-room";
        //推荐位广告
        public static final String VOD_RECOMMEND = V4_API + "recommend";
        //从外网获取挑战广场
        public static final String CLOUD_SONG_DUEL = V4_NODE + "cloud/song-duel";
        //从外网获取斗歌排行榜
        public static final String CLOUD_SONG_DUEL_RANKING = V4_NODE + "cloud/song-duel-ranking";
        //斗歌-应战
        public static final String CLOUD_SONG_DUEL_ACCEPT = V4_NODE + "cloud/song-duel-accept";
        //斗歌结果
        public static final String CLOUD_SONG_DUEL_COMPLETED = V4_NODE + "cloud/song-duel-completed";
        //斗歌放弃
        public static final String CLOUD_SONG_DUEL_GIVE_UP = V4_NODE + "cloud/song-duel-give-up";
        //删除斗歌记录
        public static final String CLOUD_SONG_DUEL_DELETE = V4_NODE + "cloud/song-duel/delete";
        // 评分歌曲得分排名情况（战胜百分比）
        public static final String CLOUD_SONG_SCORE_RANKING = V4_NODE + "cloud/song-score-ranking";
        //收藏歌曲
        public static final String CLOUD_FAVORITES_SONG = V4_NODE + "cloud/user-favorites-song";
        //曲库更新记录
        public static final String SONG_UPDATE_LOG = V4_API + "song/update-log";
        //V4.0获取歌曲统计数据
        public final static String VOD_SONG_STATISTICS = V4_API + "statistics";
        //歌单列表
        public static final String SONG_LIST = V4_API + "song-list";
        //获取歌单中歌曲
        public static final String SONG_LIST_SONG = V4_API + "song-list-song";
        //获取炫屏列表
        public static final String COOL_SCREEN = V4_API + "cool-screen";
        //获取闯关歌曲列表
        public static final String BREAKTHROUGH_SONG = V4_API + "breakthrough-song";
        //获取自制MV
        public static final String CLOUD_USER_MV = V4_NODE + "cloud/user-mv";
        //获取用户收藏夹歌曲列表
        public static final String CLOUD_USER_FAVORITES = V4_NODE + "cloud/user-favorites";
        //房间设备安全上报详情
        public static final String ROOM_EQUIPMENT_SAFETY_INFO = V4_NODE + "business/room-info";
        //房间设备安全上报
        public static final String ROOM_EQUIPMENT_UPLOAD = V4_NODE + "business/room";
        //检查场所安全检查状态
        public static final String STORE_EQUIPMENT = V4_NODE + "business";

        public static final String STORE_QRCODE = "/safety/check.html?ktv_net_code=";

        //根据曲种获取歌曲灯光
        public static final String SONG_TYPE_LIGHT = V4_API + "song-type-light";

        //获取皮肤列表
        public static final String SKIN_LIST = V4_API + "skin";


        //获取角标样式
        public static final String CORNER_STYLE = V4_API + "corner";

        //获取当前皮肤（机顶盒开机皮肤）
        public static final String SKIN_CURRENT = V4_API + "skin-current";

        public static final String REQUEST_OFFLINE = V4_NODE + "business";

        //新歌
        public static final String VOD_NEW_SONG = V4_API + "recommend-song";

        //获取表情列表
        public static final String EMOJI = V4_API + "emoji";

        //获取表情包中的表情
        public static final String EMOJI_DETAIL = V4_API + "emoji-detail";

        //获取弹幕列表
        public static final String BARRAGE = V4_API + "barrage";

        //获取包房付费情况
        public static final String PAY = V4_NODE + "pay";

        //广告资源
        public static final String PAY_RESOURCE = V4_NODE + "pay/resource";
    }

    public final class CLOUD {
        //活动信息
        public static final String BNS_ACTIVITY = "/v4.0/eshop/vod-activity";
    }
}
