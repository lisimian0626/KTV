package com.bestarmedia.libcommon.data;


import android.text.TextUtils;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.security.JWTMessage;
import com.bestarmedia.libcommon.model.vod.Configuration;
import com.bestarmedia.libcommon.model.vod.ProfileDetailV4;
import com.bestarmedia.libcommon.util.Logger;


/**
 * Created by J Wong on 2017/5/15.
 */

public class VodConfigData {

    private static VodConfigData mServerConfigData;
    private Configuration mServerConfig;
    private ProfileDetailV4 profileDetailV4;
    private JWTMessage jwtMessage;
    private boolean isSafetyPass;

    public static VodConfigData getInstance() {
        if (mServerConfigData == null)
            mServerConfigData = new VodConfigData();
        return mServerConfigData;
    }


    public void setConfigData(Configuration configData) {
        Configuration preConfiguration = getServerConfig();
        mServerConfig = configData;
        if (preConfiguration != null && preConfiguration.danceVolume != configData.danceVolume) {
            Logger.d("VodConfigData", "setConfigData danceVolChange:" + configData.danceVolume);
            EventBusUtil.postSticky(EventBusId.Id.DANCE_VOL_CHANGE, configData.danceVolume);
        }
    }

    public Configuration getServerConfig() {
        return mServerConfig;
    }

    public void setProfileDetailV4(ProfileDetailV4 profileDetailV4) {
        this.profileDetailV4 = profileDetailV4;
    }

    public ProfileDetailV4 getProfileDetailV4() {
        return profileDetailV4;
    }


    public void setJwtMessage(JWTMessage jwtMessage) {
        this.jwtMessage = jwtMessage;
    }

    public JWTMessage getJwtMessage() {
        return jwtMessage;
    }

    public String getKtvNetCode() {
        return getProfileDetailV4() != null && getProfileDetailV4().nodeProfileDetail != null ? getProfileDetailV4().nodeProfileDetail.ktvNetCode : "";
    }

    public String getRoomCode() {
        return jwtMessage != null ? jwtMessage.roomCode : "";
    }

    public String getKtvRoomCode() {
        return jwtMessage != null ? jwtMessage.ktvRoomCode : "";
    }

    public String getKtvName() {
        return getProfileDetailV4() != null && getProfileDetailV4().nodeProfileDetail != null ? getProfileDetailV4().nodeProfileDetail.name : "";
    }

    /**
     * 是否有赠送模块
     *
     * @return 是否有赠送模块
     */
    public boolean hasGift() {
        if (getServerConfig() != null) {
            return getServerConfig().giveAway == 1;
        }
        return false;
    }

    /**
     * 赠品总数是否需要乘以购物车数量，零度、维达、王牌 不需要，其他需要
     *
     * @return 是否需要乘以购物车数量
     */
    public boolean isTotalGiftMultiplyCount() {
        if (getServerConfig() != null) {
            return getServerConfig().erpNeedBalance == 1;
        }
        return true;
    }

    /**
     * @return 例送总数是否根据勾选物品变化，王牌需要
     */
    public boolean isChangeGiftTotalCount() {
        if (getServerConfig() != null) {
            return "wangpai".equalsIgnoreCase(getServerConfig().erpVendor);
        }
        return true;
    }

    /**
     * 关房是否切歌
     *
     * @return 关房是否切歌
     */
    public boolean closeRoomCutSong() {
        return getServerConfig() != null && getServerConfig().away == 1;
    }

    /**
     * 手机点歌二维码间隔
     *
     * @return 二维码间隔
     */
    public int getQrCodeInterval() {
        return getServerConfig() != null ? getServerConfig().qrInterval : 30 * 60;
    }


    /**
     * 手机点歌二维码间隔尺寸比例
     *
     * @return 二维码间隔尺寸比例
     */
    public int getQrCodeSize() {
        return getServerConfig() != null ? getServerConfig().qrSize : 100;
    }

    /**
     * 手机点歌二维码显示时长
     *
     * @return 二维码显示时长
     */
    public int getQrCodeDuration() {
        return getServerConfig() != null ? getServerConfig().qrDuration * 1000 : 30 * 1000;
    }


    /**
     * disco模块是否需要密码
     *
     * @return disco模块是否需要密码
     */
    public boolean isDiscoNeedPassword() {
        return getServerConfig() != null && getServerConfig().discoStatus == 1;
    }

    /**
     * disco模块密码
     *
     * @return disco模块密码
     */
    public String discoPassword() {
        return getServerConfig() != null ? getServerConfig().discoPw : "";
    }


    /**
     * 电影模块是否需要密码
     *
     * @return 电影模块是否需要密码
     */
    public boolean isMovieNeedPassword() {
        return getServerConfig() != null && getServerConfig().movieStatus == 1;
    }

    /**
     * 电影模块密码
     *
     * @return 电影模块密码
     */
    public String moviePassword() {
        return getServerConfig() != null ? getServerConfig().moviePw : "";
    }

    /**
     * 公关打卡模块是否启用
     *
     * @return 公关打卡模块是否启用
     */
    public boolean haveSlotCard() {
        return getServerConfig() != null && getServerConfig().slotCard == 1;
    }

    /**
     * 进入酒水是否需要验证密码
     *
     * @return 进入酒水是否需要验证密码
     */
    public boolean verifyPasswordEnterWine() {
        return getServerConfig() != null && getServerConfig().needPasswordBeforeView == 1;
    }

    /**
     * 酒水下单是否需要验证密码
     *
     * @return 酒水下单是否需要验证密码
     */
    public boolean verifyPasswordOrderWine() {
        return getServerConfig() != null && getServerConfig().needPasswordBeforeOrder == 1;
    }

    /**
     * 获取音量控制类型,0全部控制，1单曲种控制
     *
     * @return 控制类型, 0全部控制，1单曲种控制
     */
    public String getDanceType() {
        return getServerConfig() != null ? getServerConfig().danceType : "";
    }

    /**
     * 获取音量控制的音量值
     *
     * @return 获取音量控制的音量值
     */
    public int getDanceVolume() {
        return getServerConfig() != null ? getServerConfig().danceVolume : 15;
    }

    /**
     * 获取音量控制曲种
     *
     * @return 获取音量控制曲种
     */
    private String getDanceSong() {
        return getServerConfig() != null ? getServerConfig().danceSong : "";
    }


    public int getSongTypeVolume(int songType) {
        if (songType > 0) {
            if ("0".equalsIgnoreCase(getDanceType())) {//全部控制
                return getDanceVolume();
            } else if ("1".equalsIgnoreCase(getDanceType())) {//曲种控制
                if (!TextUtils.isEmpty(getDanceSong())) {
                    if (getDanceSong().contains(",")) {
                        String[] types = getDanceSong().split(",");
                        if (types.length > 0) {
                            for (String type : types) {
                                if (songType == Integer.valueOf(type)) {
                                    return getDanceVolume();
                                }
                            }
                        }
                    } else {
                        if (songType == Integer.valueOf(getDanceSong())) {
                            return getDanceVolume();
                        }
                    }
                }
            }
        }
        return 15;
    }

    /**
     * @return 是否开启评分功能
     */
    public boolean isOpenScore() {
        return getServerConfig() != null && getServerConfig().isGradeLib == 1;
    }

    /**
     * @return 获取电视屏左上角logo
     */
    public String getTvLogo() {
        return getServerConfig() != null ? getServerConfig().logo : "";
    }

    /**
     * 获取触摸屏logo
     *
     * @return 摸屏logo
     */
    public String getTouchScreenLogo() {
        return getServerConfig() != null ? getServerConfig().screenLogo : "";
    }

    /**
     * 公播音量百分比
     *
     * @return 公播音量百分比
     */
    public int getBroadcastVolume() {
        float broadcastVol = getServerConfig() != null ? ((float) getServerConfig().broadcastVolume / 15) : 0.3f;
        Logger.d(getClass().getSimpleName(), "getBroadcastVolume broadcastVol:" + broadcastVol);
        return (int) (broadcastVol * 100);
    }

    /**
     * 获取开机初始音量值，0-15
     *
     * @return 初始音量值，0-15
     */
    public int getInitialVolume() {
        int initVol = getServerConfig() != null ? getServerConfig().initialVolume : 4;
        Logger.d(getClass().getSimpleName(), "getInitialVolume initVol:" + initVol);
        return initVol;
    }

    //    public String UUID = "FD A5 06 93- A4 E2- 4F B1- AF CF- C6 EB 07 64 78 25";//北星默认beacon UUID
//
//    public String Major = "10168";//北星默认beacon Major
//
//    public String Minor = "35379";//北星默认beacon Minor

    /**
     * 获取beacon uuid
     *
     * @return 获取beacon uuid
     */
    public String getBeaconUuid() {
        return getServerConfig() != null ? getServerConfig().beaconUuid : "";
    }

    /**
     * 获取beacon Major
     *
     * @return beacon Major
     */
    public String getBeaconMajor() {
        return getServerConfig() != null ? getServerConfig().beaconMajor : "";
    }


    /**
     * 获取beacon Minor
     *
     * @return beacon Minor
     */
    public String getBeaconMinor() {
        return getServerConfig() != null ? getServerConfig().beaconMinor : "";
    }

    /**
     * 获取cms接口地址
     *
     * @return cms接口地址
     */
    public String getCmsHttpHost() {
        return getServerConfig() != null && getServerConfig().httpHost != null ? getServerConfig().httpHost.cmsHost : "";
    }


    /**
     * 是否开启综合智能灯光
     * 开机默认、公播、开房、关房、电视开、电视关、原唱、伴唱、暂停、静音
     *
     * @return 是否开启综合智能灯光
     */
    public boolean isIntelligentLight() {
        return getServerConfig() != null && getServerConfig().intelligentLight == 1;
    }

    /**
     * 是否开启综合智能灯光
     * 开机默认、公播、开房、关房、电视开、电视关、原唱、伴唱、暂停、静音
     *
     * @return 是否开启综合智能灯光
     */
    public boolean isAutoLight() {
        return getServerConfig() != null && getServerConfig().autoLightDefault == 1;
    }

    /**
     * 多久不操作屏幕出现屏保
     *
     * @return 单位：秒
     */
    public int getAdScreenShowNotTouch() {
        return getServerConfig() != null ? getServerConfig().adScreenTime : 120;
    }

    /**
     * 屏保切换频率
     *
     * @return 频率，单位：秒
     */
    public int getAdScreenInterval() {
        return getServerConfig() != null ? getServerConfig().adScreenInterval : 15;
    }

    /**
     * 付费模式
     *
     * @return 付费模式是否开启
     */
    public boolean isPayMode() {
        return getServerConfig() != null && getServerConfig().songPay == 1;
    }

    //没开启消防检查时，要跳过
    public boolean isSafetyPass() {
        return !isNeedSafety() || isSafetyPass;
    }

    public void setSafetyPass(boolean safetyPass) {
        isSafetyPass = safetyPass;
    }

    /**
     * @return 是否需要进行消防检查
     */
    public boolean isNeedSafety() {
        return getServerConfig() != null && getServerConfig().monitorConfig != null
                && !TextUtils.isEmpty(getServerConfig().monitorConfig.domain);
    }

    /**
     * 获取云端地址
     *
     * @return 云端地址
     */
    public String getCloudHost() {
        return getServerConfig() != null && getServerConfig().httpHost != null ? getServerConfig().httpHost.cloudHost : "";
    }
}
