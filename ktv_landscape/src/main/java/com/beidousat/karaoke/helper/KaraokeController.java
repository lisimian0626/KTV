package com.beidousat.karaoke.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.data.ChooseSongs;
import com.beidousat.karaoke.data.RoomUsers;
import com.beidousat.karaoke.im.DeviceCommunicateHelper;
import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.beidousat.karaoke.interf.OnKaraokePlayerListener;
import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.serialport.SerialController;
import com.beidousat.score.OnScoreListener;
import com.bestarmedia.libcommon.ad.AdGetterV4;
import com.bestarmedia.libcommon.ad.AdModelDefault;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodBoxForPhoneQrCode;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.MvDetailHelper;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.helper.ServiceCallHelper;
import com.bestarmedia.libcommon.helper.SongDetailHelper;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.dto.AirConStatus;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.dto.CoolScreenStatus;
import com.bestarmedia.libcommon.model.dto.PlayItem;
import com.bestarmedia.libcommon.model.dto.PresentationCenterIcon;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.LightItem;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.model.vod.Movie;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.model.vod.ScoreRanking;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libcommon.util.UUIDUtil;
import com.bestarmedia.liblame.AudioRecordFileUtil;
import com.bestarmedia.liblame.BnsAudioRecorder;
import com.bestarmedia.libnetty.netty.NotificationMessageData;
import com.bestarmedia.player.KaraokeTexturePlayer;
import com.bestarmedia.proto.device.DeviceProto;
import com.bestarmedia.proto.node.BoxControlRequest;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.node.Text;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.VideoMessageRequest;
import com.bestarmedia.proto.node.VodScreenShotRequest;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.proto.vod.MobileUser;
import com.bestarmedia.texture.MergeVideoTextureView;
import com.bestarmedia.texture.VideoTextureView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

public class KaraokeController implements OnBasePlayerListener, OnScoreListener {

    private final static String TAG = "KaraokeController";
    private static KaraokeController karaokeController;
    private AudioManager audioManager;
    private KaraokeTexturePlayer karaokePlayer;
    private ButtonStatus buttonStatus = new ButtonStatus();
    private AirConStatus airConStatus = new AirConStatus();
    private CoolScreenStatus coolScreenStatus = new CoolScreenStatus();

    private Handler handler = new Handler();
    private RoomSongItem playingItem;
    private RoomSongItem nextPlayItem;

    private OnKaraokePlayerListener onKaraokePlayerListener;
    private long preCallbackPlayerProgress;
    private long playerProgressIntervalSec;
    private int callbackAdCornerSecond;//角标回调
    private ADModel adRedPacket, adCorner;
    private BnsBeaconHelper bnsBeaconHelper;

    private final int toneRange = OkConfig.boxManufacturer() == 1 ? 3 : 1;//音调增减幅度晨芯默认1，音诺恒默认5
    private final int defaultTone = OkConfig.boxManufacturer() == 1 ? 100 : 0;//音调晨芯默认0，音诺恒默认100
    private int currentTone = OkConfig.boxManufacturer() == 1 ? 100 : 0;

    private int limitVolume;//定时限制音量
    private int songTypeLimitVolume;//曲种音量限制
    private int callScoreResultTime = 0;
    private long preNextTime = 0;
    private long replayTime = 0;
    private long loadCoolScreenTime;

    private boolean isPlayingNotificationText;
    private boolean isPlayingNotificationImage;
    private boolean isPlayingNotificationVideo;

    public static KaraokeController getInstance() {
        if (karaokeController == null) {
            karaokeController = new KaraokeController();
        }
        return karaokeController;
    }


    private KaraokeController() {
        this.audioManager = (AudioManager) VodApplication.getVodApplication().getSystemService(Context.AUDIO_SERVICE);
        this.limitVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        this.songTypeLimitVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public ButtonStatus getButtonStatus() {
        return buttonStatus;
    }

    public AirConStatus getAirConStatus() {
        return airConStatus;
    }

    public CoolScreenStatus getCoolScreenStatus() {
        return coolScreenStatus;
    }

    public void setOnKaraokePlayerListener(OnKaraokePlayerListener listener) {
        this.onKaraokePlayerListener = listener;
    }

    public void initPlayer(VideoTextureView videoTextureView, MergeVideoTextureView mergeVideoTextureView) {
        if (karaokePlayer == null) {
            this.karaokePlayer = new KaraokeTexturePlayer();
        }
        karaokePlayer.initPlayer(VodApplication.getVodApplicationContext(), videoTextureView, mergeVideoTextureView);
        handler.postDelayed(() -> ChooseSongs.getInstance().init(), 3000);
    }

    public void init() {
        registerEventBus();
        resetInitVolume();//初始化音量
        buttonStatus.scoreMode = VodConfigData.getInstance().isOpenScore() ? 1 : 0;
        //初始化歌曲灯光
        setAutoLight(VodConfigData.getInstance().isAutoLight() ? 1 : 0);
        broadcastButtonStatus(buttonStatus);
    }


    public void play(RoomSongItem roomSongItem, RoomSongItem nextPlayItem) {
        resetParams();
        this.playingItem = roomSongItem;
        this.nextPlayItem = nextPlayItem;
        if (playingItem == null) {
            Log.e(TAG, "播放视频失败！播放对象为空 >>>>>>>>>>>>>>>>>>>>>>>>> ");
            return;
        }
        if (karaokePlayer == null) {
            Log.e(TAG, "播放视频失败！播放器未初始化 >>>>>>>>>>>>>>>>>>>>>>>>> ");
            this.karaokePlayer = new KaraokeTexturePlayer();
        }
        if (onKaraokePlayerListener != null) {
            onKaraokePlayerListener.onPlayInfo(playingItem.simpName, nextPlayItem != null ? nextPlayItem.simpName : "");
        }
        karaokePlayer.setRecordFileName(playingItem.recordFile);
        karaokePlayer.setScore(playingItem.getGradeLibFile1(), playingItem.getGradeLibFile2());
        karaokePlayer.setVol((float) playingItem.volume / 100);
        karaokePlayer.setBasePlayerListener(this);
        karaokePlayer.setOnScoreListener(this);
        karaokePlayer.playMedia(playingItem.songUrl, nextPlayItem != null ? nextPlayItem.songUrl : playingItem.songUrl);
        if (buttonStatus.isPause == 1) {
            buttonStatus.isPause = 0;
            broadcastButtonStatus(buttonStatus);
        }
    }

    private void resetParams() {
        adCorner = null;
        adRedPacket = null;
        preCallbackPlayerProgress = 0;
        playerProgressIntervalSec = 0;
        callbackAdCornerSecond = 0;
        callScoreResultTime = 0;
        EventBusUtil.postSticky(EventBusId.PresentationId.CLEAR, 1);
        if (bnsBeaconHelper != null) {//重置beacon
            bnsBeaconHelper.setBeaconInfo(VodConfigData.getInstance().getBeaconUuid(), VodConfigData.getInstance().getBeaconMajor(), VodConfigData.getInstance().getBeaconMinor());
        }
    }

    public void release() {
        releasePlayer();
        unregisterEventBus();
    }

    public void releasePlayer() {
        if (karaokePlayer != null) {
            karaokePlayer.release();
            karaokePlayer = null;
        }
    }

    private void setNextPlayItem(RoomSongItem firstSong, RoomSongItem item) {
        this.playingItem = firstSong;
        this.nextPlayItem = item;
        if (onKaraokePlayerListener != null) {
            onKaraokePlayerListener.onPlayInfo(playingItem != null ? playingItem.simpName : "", nextPlayItem != null ? nextPlayItem.simpName : "");
        }
    }

    /**
     * 切歌
     */
    public void next(boolean showTip) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            long curTime;
            if ((curTime = System.currentTimeMillis()) - preNextTime >= 2500) {
                preNextTime = curTime;
                ChooseSongs.getInstance().moveChoose2Sung(playingItem != null && playingItem.type >= 0 ? (int) playingItem.duration : 0,
                        playingItem != null && playingItem.type >= 0 ? (int) playingItem.current : 0);
                if (showTip) {
                    showTipOnTelevision(R.drawable.tv_next, R.string.switch_song, true);
                }
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(1, 0);
        }
    }

    /**
     * 原/伴唱
     */
    public void originalAccompany() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (buttonStatus.isOriginal == 1) {
                accompany(true);
            } else {
                original(true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(2, 0);
        }
    }

    /**
     * 原唱
     */
    public void original(boolean isTip) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null && playingItem != null) {
                try {
                    Log.d(TAG, "playingItem.audioTrack:" + playingItem.audioTrack);
                    if (playingItem.audioTrack == 1) {//一轨原唱
                        karaokePlayer.selectTrack(0);
                    } else if (playingItem.audioTrack == 2) {//二轨原唱
                        karaokePlayer.selectTrack(1);
                    } else if (playingItem.audioTrack == 3) {//左原唱
                        karaokePlayer.setVolChannel(1);
                    } else if (playingItem.audioTrack == 4) {//右原唱
                        karaokePlayer.setVolChannel(2);
                    }
                    buttonStatus.isOriginal = 1;
                    if (isTip) {
                        showTipOnTelevision(R.drawable.tv_original_on, R.string.original, true);
                    }
                    broadcastButtonStatus(buttonStatus);
                } catch (Exception e) {
                    Log.e(TAG, "切换原唱发生异常：" + e.toString());
                }
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(2, 1);
        }
    }

    /**
     * 伴唱
     */
    public void accompany(boolean isTip) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null && playingItem != null) {
                try {
                    Log.d(TAG, "playingItem.audioTrack:" + playingItem.audioTrack);
                    if (playingItem.audioTrack == 1) {//一轨原唱
                        karaokePlayer.selectTrack(1);
                    } else if (playingItem.audioTrack == 2) {//二轨原唱
                        karaokePlayer.selectTrack(0);
                    } else if (playingItem.audioTrack == 3) {//左原唱
                        karaokePlayer.setVolChannel(2);
                    } else if (playingItem.audioTrack == 4) {//右原唱
                        karaokePlayer.setVolChannel(1);
                    }
                    buttonStatus.isOriginal = 0;
                    if (isTip) {
                        showTipOnTelevision(R.drawable.tv_original_off, VodApplication.getVodApplicationContext().getString(R.string.accompany), true);
                    }
                    broadcastButtonStatus(buttonStatus);
                } catch (Exception e) {
                    Log.e(TAG, "切换伴唱发生异常：" + e.toString());
                }
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(2, -1);
        }
    }

    /**
     * 暂停/播放
     */
    public void pauseStart() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (buttonStatus.isPause == 1) {
                start();
            } else {
                pause();
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(3, 0);
        }
    }


    /**
     * 播放
     */
    public void start() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                karaokePlayer.start(buttonStatus.isMute == 0);
                buttonStatus.isPause = 0;
                SerialController.getInstance().onIntelLightsPlay();
                showTipOnTelevision(R.drawable.tv_play, R.string.play, true);
                broadcastButtonStatus(buttonStatus);
            }
            EventBusUtil.postSticky(EventBusId.PlayerId.PLAYER_START, 1);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(3, 1);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                karaokePlayer.pause();
                buttonStatus.isPause = 1;
                SerialController.getInstance().onIntelLightsPause();
                showTipOnTelevision(R.drawable.tv_pause, R.string.pause, false);
                broadcastButtonStatus(buttonStatus);
            }
            if (playingItem != null) {
                requestAdPause(playingItem.type, playingItem.singerId);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(3, -1);
        }
    }

    /**
     * 静音/取消静音
     */
    public void muteCancelMute() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (buttonStatus.isMute == 1) {
                cancelMute();
            } else {
                mute();
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(4, 0);
        }
    }

    /**
     * 静音
     */
    public void mute() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                karaokePlayer.volOff();
                buttonStatus.isMute = 1;
                SerialController.getInstance().onIntelLightsMute();
                showTipOnTelevision(R.drawable.tv_mute, R.string.mute, false);
                broadcastButtonStatus(buttonStatus);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(4, -1);
        }
    }

    /**
     * 取消静音
     */
    public void cancelMute() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                karaokePlayer.volOn();
                buttonStatus.isMute = 0;
                SerialController.getInstance().onIntelLightsMuteCancel();
                showTipOnTelevision(R.drawable.tv_mute_cancel, R.string.mute2, true);
                broadcastButtonStatus(buttonStatus);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(4, 1);
        }
    }

    /**
     * 音量-
     */
    public void volumeDown() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume > 0) {
                currentVolume--;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                setStreamVolume(currentVolume);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(5, -1);
        }
    }

    /**
     * 音量+
     */
    public void volumeUp() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                currentVolume++;
                setStreamVolume(currentVolume);
            }
            if (buttonStatus.isMute == 1) {
                cancelMute();
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(5, 1);
        }
    }

    /**
     * 麦克风-
     */
    public void micDown(boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                SerialController.getInstance().onMicVolDown();
            }
            showTipOnTelevision(R.drawable.tv_mic, VodApplication.getVodApplicationContext().getString(R.string.mic_vol_down), true);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(9, -1);
        }
    }

    /**
     * 麦克风+
     */
    public void micUp(boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                SerialController.getInstance().onMicVolUp();
            }
            showTipOnTelevision(R.drawable.tv_mic, VodApplication.getVodApplicationContext().getString(R.string.mic_vol_up), true);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(9, 1);
        }
    }

    /**
     * 原调
     */
    public void toneDefault() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                karaokePlayer.setTone(defaultTone);
                showTipOnTelevision(R.drawable.tv_music, R.string.tone_default, true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(11, 0);
        }
    }

    /**
     * 降调
     */
    public void toneDown() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                //音调步长1，音诺恒步长5
                currentTone = currentTone - toneRange;
                karaokePlayer.setTone(currentTone);
                showTipOnTelevision(R.drawable.tv_music, R.string.tone_down, true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(11, -1);
        }
    }

    /**
     * 升调
     */
    public void toneUp() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                //音调步长1，音诺恒步长5
                currentTone = currentTone + toneRange;
                karaokePlayer.setTone(currentTone);
                showTipOnTelevision(R.drawable.tv_music, R.string.tone_up, true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(11, 1);
        }
    }


    /**
     * 重唱
     */
    public void replay() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            long curTime;
            if ((curTime = System.currentTimeMillis()) - replayTime >= 2500) {
                replayTime = curTime;
                play(playingItem, nextPlayItem);
                showTipOnTelevision(R.drawable.tv_replay, R.string.replay, true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(6, 0);
        }
    }

    /**
     * 评分模式
     */
    public void setScoreMode(int scoreMode) {
        if (VodConfigData.getInstance().isOpenScore()) {
            if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                if (karaokePlayer != null) {
                    if (buttonStatus.scoreMode != scoreMode) {
                        karaokePlayer.setScoreMode(scoreMode);
                        buttonStatus.scoreMode = scoreMode;
                        PresentationCenterIcon centerIcon = new PresentationCenterIcon(R.drawable.tv_score_mode_normal, VodApplication.getVodApplicationContext().getString(R.string.score_mode_normal), true);
                        if (scoreMode == 0) {//关闭
                            centerIcon = new PresentationCenterIcon(R.drawable.tv_score_closed, VodApplication.getVodApplicationContext().getString(R.string.score_closed), true);
                        } else if (scoreMode == 2) {//专业
                            centerIcon = new PresentationCenterIcon(R.drawable.tv_score_mode_professional, VodApplication.getVodApplicationContext().getString(R.string.score_mode_professional), true);
                        }
                        EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_CENTER_ICON, centerIcon);
                        broadcastButtonStatus(buttonStatus);
                    }
                }
            } else {
                DeviceCommunicateHelper.sendPlayerOperate2Main(7, scoreMode);
            }
        }
    }


    /**
     * 录音开关
     */
    public void setRecord(int enable) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (karaokePlayer != null) {
                if (buttonStatus.isRecord != enable) {
                    BnsAudioRecorder.getInstance().setEncodeMP3(enable == 1);
                    buttonStatus.isRecord = enable;
//                        PresentationCenterIcon centerIcon = new PresentationCenterIcon(R.drawable.tv_score_mode_normal, VodApplication.getVodApplicationContext().getString(R.string.score_mode_normal), true);
//                        if (recordEnable == 0) {//关闭
//                            centerIcon = new PresentationCenterIcon(R.drawable.tv_score_closed, VodApplication.getVodApplicationContext().getString(R.string.score_closed), true);
//                        } else if (recordEnable == 1) {//录音开
//                            centerIcon = new PresentationCenterIcon(R.drawable.tv_score_mode_professional, VodApplication.getVodApplicationContext().getString(R.string.score_mode_professional), true);
//                        }
//                        EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_CENTER_ICON, centerIcon);
                    broadcastButtonStatus(buttonStatus);
                }
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(8, enable);
        }
    }

    /**
     * 呼叫服务
     *
     * @param mode             -1取消服务； 0：呼叫服务员；1：DJ呼叫；2：清洁呼叫；3：保安呼叫；4：买单呼叫；5：催单呼叫；6：唛套呼叫；7：加冰呼叫；
     * @param sendToController 是否向中控发码
     */
    public void setServiceMode(int mode, boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                if (mode >= 0) {
                    SerialController.getInstance().onService();
                } else {
                    SerialController.getInstance().offService();
                }
            }
            buttonStatus.serviceMode = mode;
            if (mode >= 0) {//服务
                new ServiceCallHelper(VodApplication.getVodApplicationContext()).callService(mode);
                showTipOnTelevision(R.drawable.tv_service_on, VodApplication.getVodApplicationContext().getString(R.string.call_service), true);
            } else {//取消服务
                new ServiceCallHelper(VodApplication.getVodApplicationContext()).cancelService();
                showTipOnTelevision(R.drawable.tv_service_cancel, VodApplication.getVodApplicationContext().getString(R.string.service_cancel), true);
            }
            EventBusUtil.postSticky(EventBusId.PresentationId.SERVICE_STATUS, buttonStatus.serviceMode);
            broadcastButtonStatus(buttonStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(14, mode);
        }
    }

    /**
     * 进度拖动
     */
    public void seekTo(float seek) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            EventBusUtil.postSticky(EventBusId.PlayerId.PLAYER_SEEK_TO, seek);
        }
    }


    /**
     * 设置模式
     *
     * @param isOpen   是否开启炫屏
     * @param isRandom 是否开启随机
     */
    public void setCoolScreenStatus(int isOpen, int isRandom) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            coolScreenStatus.isOpen = isOpen;
            coolScreenStatus.isRandom = isRandom;
            if (karaokePlayer != null) {
                if (coolScreenStatus.isOpen == 0) {
                    coolScreenStatus.currentId = 0;
                    coolScreenStatus.currentUrl = "";
                    karaokePlayer.setBackgroundFrame(null);
                } else {
                    getRandomCoolScreen();
                }
            }
            broadcastCoolScreenStatus2SecOnly(coolScreenStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(37, isOpen, String.valueOf(isRandom));
        }
    }


    /**
     * 炫屏
     */
    public void setCoolScreen(int id, String url) {
        long curTime = System.currentTimeMillis();
        if (curTime - loadCoolScreenTime < 2000) {
            Toast.makeText(VodApplication.getVodApplicationContext(),
                    VodApplication.getVodApplicationContext().getString(R.string.switching_cool_screen_tip), Toast.LENGTH_SHORT).show();
            return;
        }
        loadCoolScreenTime = curTime;
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            coolScreenStatus.isOpen = 1;
            coolScreenStatus.currentId = id;
            coolScreenStatus.currentUrl = url;
            if (karaokePlayer != null) {
                if (!TextUtils.isEmpty(url)) {
                    Glide.with(VodApplication.getVodApplicationContext()).load(url).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                            Canvas canvas = new Canvas(bitmap);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            drawable.draw(canvas);
                            karaokePlayer.setBackgroundFrame(bitmap);
                        }
                    });
                }
            }
            broadcastCoolScreenStatus2SecOnly(coolScreenStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(21, id, url);
        }

    }

    /**
     * 自动灯光开/关
     * isAuto 0关/开
     */
    public void setAutoLight(int isAuto) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (buttonStatus.isLightAuto != isAuto) {
                buttonStatus.isLightAuto = isAuto;
                showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.light_auto)
                        + VodApplication.getVodApplicationContext().getString(buttonStatus.isLightAuto == 1 ? R.string.on : R.string.off), true);
                broadcastButtonStatus2SecOnly(buttonStatus);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(22, isAuto);
        }
    }

    /**
     * MIC原调
     */
    public void micToneDefault(boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                SerialController.getInstance().onMicPitchOri();
            }
            showTipOnTelevision(R.drawable.tv_mic, VodApplication.getVodApplicationContext().getString(R.string.tone_default), true);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(10, 0);
        }
    }

    /**
     * MIC降调
     */
    public void micToneDown(boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                SerialController.getInstance().onMicPitchDown();
            }
            showTipOnTelevision(R.drawable.tv_mic, VodApplication.getVodApplicationContext().getString(R.string.tone_down), true);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(10, -1);
        }
    }

    /**
     * MIC升调
     */
    public void micToneUp(boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (sendToController) {
                SerialController.getInstance().onMicPitchUp();
            }
            showTipOnTelevision(R.drawable.tv_mic, VodApplication.getVodApplicationContext().getString(R.string.tone_up), true);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(10, 1);
        }
    }

    /**
     * 音效；0唱将，1整蛊，2搞怪，3和声
     */
    public void effect(int mode, boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            switch (mode) {
                case 0:
                    if (sendToController)
                        SerialController.getInstance().onEffectSing();
                    showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_sing), true);
                    break;
                case 1:
                    if (sendToController)
                        SerialController.getInstance().onEffectBlame();
                    showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_blame), true);
                    break;
                case 2:
                    if (sendToController)
                        SerialController.getInstance().onEffectTrick();
                    showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_trick), true);
                    break;
                case 3:
                    if (sendToController)
                        SerialController.getInstance().onEffectHarmony();
                    showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_harmony), true);
                    break;
                default:
                    break;
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(23, mode);
        }
    }

    /**
     * 混响；0混响-，1混响+
     */
    public void reverberation(int mode, boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (mode > 0) {
                if (sendToController) {
                    SerialController.getInstance().onEffectUp();
                }
                showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_up), true);
            } else {
                if (sendToController) {
                    SerialController.getInstance().onEffectDown();
                }
                showTipOnTelevision(R.drawable.tv_music, VodApplication.getVodApplicationContext().getString(R.string.effect_down), true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(24, mode);
        }
    }

    /**
     * 打乱
     */
    public void shuffle() {
        ChooseSongs.getInstance().shuffle();
    }

    /**
     * 发灯光码
     */
    public void sendLightCode(String code, String name) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            SerialController.getInstance().onLightByCode(code);
            showTipOnTelevision(R.drawable.tv_light, name, true);
        } else {
            if (SerialController.getInstance().getLightItems() != null) {
                for (LightItem item : SerialController.getInstance().getLightItems()) {
                    if (code.equalsIgnoreCase(item.outCode)) {
                        DeviceCommunicateHelper.sendPlayerOperate2Main(26, item.mode);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 灯光模式；0标准，1明亮，2柔和，3抒情，4动感，5浪漫，6朦胧，7演唱会，8激情，9清洁，10全开，11全关
     */
    public void lightMode(int mode, boolean sendToControlBox, boolean tipOnTelevision) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            switch (mode) {
                case 0://标准
                    if (sendToControlBox)
                        SerialController.getInstance().onLightLightStandard();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.standard), true);
                    break;
                case 1://明亮
                    if (sendToControlBox)
                        SerialController.getInstance().onLightBright();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.bright), true);
                    break;
                case 2://柔和
                    if (sendToControlBox)
                        SerialController.getInstance().onLightSoft();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.soft), true);
                    break;
                case 3://抒情
                    if (sendToControlBox)
                        SerialController.getInstance().onLightLyric();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.lyric), true);
                    break;
                case 4://动感
                    if (sendToControlBox)
                        SerialController.getInstance().onLightDynamic();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.montion), true);
                    break;
                case 5://浪漫
                    if (sendToControlBox)
                        SerialController.getInstance().onLightRomantic();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.romantic), true);
                    break;
                case 6://朦胧
                    if (sendToControlBox)
                        SerialController.getInstance().onLightDim();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.dim), true);
                    break;
                case 7://演唱会
                    if (sendToControlBox)
                        SerialController.getInstance().onLightConcert();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.concert), true);
                    break;
                case 8://激情
                    if (sendToControlBox)
                        SerialController.getInstance().onLightPassion();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.passion), true);
                    break;
                case 9://清洁
                    if (sendToControlBox)
                        SerialController.getInstance().onLightClean();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.clean), true);
                    break;
                case 10://全开
                    if (sendToControlBox)
                        SerialController.getInstance().onLightOn();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.light_on), true);
                    break;
                case 11://全关
                    if (sendToControlBox)
                        SerialController.getInstance().onLightOff();
                    if (tipOnTelevision)
                        showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.light_off), true);
                    break;
                default:
                    break;
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(26, mode);
        }
    }

    /**
     * 灯光亮度；0减暗，1加亮
     */
    public void lightBrightness(int mode, boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (mode > 0) {
                if (sendToController)
                    SerialController.getInstance().onLightUp();
                showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.light_up), true);
            } else {
                if (sendToController)
                    SerialController.getInstance().onLightDown();
                showTipOnTelevision(R.drawable.tv_light, VodApplication.getVodApplicationContext().getString(R.string.light_down), true);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(27, mode);
        }
    }

    /**
     * 内置表情 气氛；0喝彩，1欢呼，2掌声，3倒彩，4难听
     */
    public void atmosphere(int mode, boolean send2Controller) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            switch (mode) {
                case 0:
                    if (send2Controller)
                        SerialController.getInstance().onAtWhooped();
                    break;
                case 1:
                    if (send2Controller)
                        SerialController.getInstance().onAtCheers();
                    break;
                case 2:
                    if (send2Controller)
                        SerialController.getInstance().onAtApplause();
                    break;
                case 3:
                    if (send2Controller)
                        SerialController.getInstance().onAtHooting();
                    break;
                case 4:
                    if (send2Controller)
                        SerialController.getInstance().onAtUnpleasant();
                    break;
                default:
                    break;
            }
            EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_EMOJI, mode);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(13, mode);
        }
    }

    /**
     * 播放动态表情
     */
    public void emojiDynamic(String emojiUrl) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_EMOJI_DYNAMIC, emojiUrl);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(38, emojiUrl);
        }
    }

    /**
     * 电视屏开/关
     *
     * @param isBlack          0正常，1黑屏
     * @param sendToController 是否发码
     */
    public void setTelevision(int isBlack, boolean sendToController) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (isBlack > 0) {//黑屏
                if (sendToController)
                    SerialController.getInstance().onIntelLightsTvOff();
                buttonStatus.isHdmiBack = 1;
            } else {//正常
                if (sendToController)
                    SerialController.getInstance().onIntelLightsTvOn();
                buttonStatus.isHdmiBack = 0;
            }
            EventBusUtil.postSticky(EventBusId.PresentationId.HDMI_BLACK_STATUS, buttonStatus.isHdmiBack);
            broadcastButtonStatus2SecOnly(buttonStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(28, isBlack);
        }
    }

    /**
     * 根据歌曲id点歌
     *
     * @param isPriority 是否优先
     * @param songId     歌曲id
     * @param userBase   用户
     */
    public void selectSong(boolean isPriority, String songId, UserBase userBase, View view) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            SongDetailHelper helper = new SongDetailHelper(VodApplication.getVodApplicationContext(), songId);
            helper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
                @Override
                public void onSongDetail(Song songDetail) {
                    selectSong(new SongOperation(isPriority ? 1 : 0, songDetail, view, "", userBase, null), false);
                }

                @Override
                public void onFail(String error) {
                    Log.e(TAG, "点歌失败：" + error);
                }
            });
            helper.getSongDetail();
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(isPriority ? 18 : 17, songId);
        }
    }

    /**
     * 根据自制MV id点歌
     *
     * @param isPriority 是否优先
     * @param mvId       自制MV id
     * @param userBase   用户
     */
    public void selectMv(boolean isPriority, int mvId, UserBase userBase, View view) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            MvDetailHelper helper = new MvDetailHelper(VodApplication.getVodApplicationContext(), mvId);
            helper.setOnMvDetailListener(new MvDetailHelper.OnMvDetailListener() {
                @Override
                public void onMvDetail(MvInfo mvInfo) {
                    selectSong(new SongOperation(isPriority ? 1 : 0, mvInfo, view, "", userBase, mvInfo), false);
                    if (userBase != null && mvInfo != null) {
                        String text = VodApplication.getVodApplicationContext().getString(R.string.upload_mv_x, mvInfo.songName);
                        MobileMessageBroadcast mobileMessageBroadcast = MobileMessageBroadcast.getDefaultInstance().toBuilder()
                                .setUser(MobileUser.getDefaultInstance().toBuilder().setUserId(userBase.id).setAvatar(userBase.avatar).setName(userBase.name).build())
                                .setText(Text.getDefaultInstance().toBuilder().setContent(text)).build();
                        VodApplication.getKaraokeController().playBarrage(mobileMessageBroadcast);
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
            helper.getDetail();
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(isPriority ? 16 : 15, mvId);
        }
    }

    /**
     * 优先已点歌曲
     *
     * @param uuid 点歌记录id
     */
    public void priorityChoose(String uuid) {
        ChooseSongs.getInstance().move2TopByUUIDFormChoose(uuid);
    }

    /**
     * 删除已点
     *
     * @param uuid 点歌记录id
     */
    public void deleteChoose(String uuid) {
        ChooseSongs.getInstance().removeSongByUUID(uuid);
    }

    /**
     * 点歌
     */
    public void selectSong(SongOperation songOperation, boolean tip) {
        Log.d(TAG, "点歌：" + songOperation.song.songName + " 是否优先：" + songOperation.isTop);
        if (!TextUtils.isEmpty(songOperation.chooseId) && songOperation.isTop == 1) {//从已点列表优先歌曲
            ChooseSongs.getInstance().move2TopByUUIDFormChoose(songOperation.chooseId);
        } else {
            ChooseSongs.getInstance().addSong(songOperation.song, songOperation.isTop == 1, tip, songOperation.userBase,
                    songOperation.mvInfo != null ? songOperation.mvInfo.mvId : 0, songOperation.mvInfo != null ? songOperation.mvInfo.mvGalleries : "");
        }
        EventBusUtil.postSticky(EventBusId.SongOperationId.SONG_SELECT, songOperation);
    }

    public void resetBoxUUID(boolean isReset) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (isReset) {
                VodBoxForPhoneQrCode.resetVodBoxUUID();
            }
            QrCodeRequest request = new QrCodeRequest(VodApplication.getVodApplicationContext(), null);
            request.requestCode();
            VodCommunicateHelper.broadcastPhoneSession();
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(29, 0);
        }
    }

    /**
     * 弹幕
     */
    public void playBarrage(MobileMessageBroadcast message) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_BARRAGE, message);

        }
    }

    /**
     * 按钮状态变化
     */
    public void broadcastButtonStatus(ButtonStatus status) {
        buttonStatus = status;
        EventBusUtil.postSticky(EventBusId.Id.BUTTON_STATUS_CHANGED, buttonStatus);
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            //发给副屏
            DeviceCommunicateHelper.broadcastButtonStatus(status);
            //发给手机
            VodCommunicateHelper.broadcastButtonStatus(status);
        }
    }

    private void broadcastButtonStatus2SecOnly(ButtonStatus status) {
        buttonStatus = status;
        EventBusUtil.postSticky(EventBusId.Id.BUTTON_STATUS_CHANGED, buttonStatus);
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            //发给副屏
            DeviceCommunicateHelper.broadcastButtonStatus(status);
        }
    }

    private void broadcastAirConStatus2SecOnly(AirConStatus status) {
        airConStatus = status;
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {//发给副屏
            DeviceCommunicateHelper.broadcastAirConStatus(status);
            EventBusUtil.postSticky(EventBusId.ImId.AIR_CON_STATUS, airConStatus);
        }
    }

    private void broadcastCoolScreenStatus2SecOnly(CoolScreenStatus status) {
        coolScreenStatus = status;
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {//发给副屏
            DeviceCommunicateHelper.broadcastCoolScreenStatus(coolScreenStatus);
            EventBusUtil.postSticky(EventBusId.ImId.COOL_SCREEN_CHANGED, coolScreenStatus);
        }
    }


    public void broadcastChooseList() {
        ChooseSongs.getInstance().broadcastChoose();
    }

    public void broadcastSungList() {
        ChooseSongs.getInstance().broadcastSung();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.CHOOSE_SONG_CHANGED:
                setNextPlayItem(ChooseSongs.getInstance().getFirstSong(), ChooseSongs.getInstance().getSecSong());
                break;
            case EventBusId.PlayerId.PLAYER_PLAY_SONG:
                if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    PlayItem playItem = (PlayItem) event.data;
                    if (playItem != null && playItem.play != null && !TextUtils.isEmpty(playItem.play.songUrl)) {
                        play(playItem.play, playItem.next);
                        if (playItem.play.type >= 0 && !TextUtils.isEmpty(playItem.play.uuid)) {//-3插播视频；-2公益广告；-1贴片广告；0歌曲；1电影；2直播；
                            RoomSongListHelper.getInstance().recordPlayStart(playItem.play.uuid);
                        }
                    } else {
                        Log.e(TAG, "播放的文件url为空！>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        if (ChooseSongs.getInstance().getFirstSong() != null) {
                            play(ChooseSongs.getInstance().getFirstSong(), ChooseSongs.getInstance().getSecSong());
                        } else {//播放公播
                            ChooseSongs.getInstance().playPublicVideo();
                        }
                    }
                }
                break;
            case EventBusId.ImId.BUTTON_STATUS:
                ButtonStatus buttonStatus = (ButtonStatus) event.data;
                Log.d(TAG, "收到主屏按钮信息变化信息：" + buttonStatus.toString());
                broadcastButtonStatus(buttonStatus);
                break;
            case EventBusId.ImId.MAIN_AIR_CON_STATUS:
                if (!DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    AirConStatus airConStatus = (AirConStatus) event.data;
                    Log.d(TAG, "收到主屏空调按钮信息变化信息：" + airConStatus);
                    this.airConStatus = airConStatus;
                    EventBusUtil.postSticky(EventBusId.ImId.AIR_CON_STATUS, airConStatus);
                }
                break;
            case EventBusId.ImId.MAIN_COOL_SCREEN_STATUS:
                if (!DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    CoolScreenStatus coolScreenStatus = (CoolScreenStatus) event.data;
                    Log.d(TAG, "收到主屏炫屏变化信息：" + airConStatus);
                    this.coolScreenStatus = coolScreenStatus;
                    EventBusUtil.postSticky(EventBusId.ImId.COOL_SCREEN_CHANGED, coolScreenStatus);
                }
                break;

            case EventBusId.ImId.MAIN_SYNC_AD:
                if (!DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    DeviceProto.SyncAd syncAd = (DeviceProto.SyncAd) event.data;
                    Log.d(TAG, "收到主屏广告同步信息：" + syncAd.getAd().getAdContent());
                    ADModel adModel = new ADModel();
                    adModel.setId(syncAd.getAd().getId());
                    adModel.setName(syncAd.getAd().getName());
                    adModel.setAdPosition(syncAd.getAd().getAdPosition());
                    adModel.setAdContent(syncAd.getAd().getAdContent());
                    adModel.setBrandName(syncAd.getAd().getBrandName());
                    adModel.setAdTypeId(syncAd.getAd().getAdTypeId());
                    adModel.setNamingSongListId(syncAd.getAd().getNamingSongListId());
                    adModel.setNamingSongListType(syncAd.getAd().getNamingSongListType());
                    EventBusUtil.postSticky(EventBusId.Id.CURRENT_SCREEN_AD, adModel);
                }
                break;
            case EventBusId.PlayerId.PLAYER_SEEK_TO:
                if (karaokePlayer != null) {
                    float seek = Float.valueOf(event.data.toString());
                    karaokePlayer.seekTo(seek);
                }
                break;
            case EventBusId.ImId.ROOM_SONG_LIST_CHANGED:
                if (!DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    ChooseSongs.getInstance().init();
                }
                break;
            case EventBusId.Serial.LIGHT_MODE_CHANGED:
                this.buttonStatus.currentLightCode = event.data.toString();
                broadcastButtonStatus2SecOnly(this.buttonStatus);
                break;

            case EventBusId.ImId.MAIN_DOWNLOAD_SONG_LIST:
                if (!DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    @SuppressWarnings("unchecked")
                    Map<String, SongSimple> downloadSongList = (Map<String, SongSimple>) event.data;
                    Log.d(TAG, "收到主屏下载列表：" + downloadSongList);
                    CloudSongDownloadHelper.getInstance(VodApplication.getVodApplicationContext()).setDownloadStatusMap(downloadSongList);
                }
                break;
            case EventBusId.ImId.VOLUME_CONTROL:
                Log.i(TAG, "音量控制>>>>>>>>>>>>>>>>");
                setLimitVolume(Integer.valueOf(event.data.toString()));
                break;
            case EventBusId.ImId.BOX_CONTROL:
                BoxControlRequest boxControlRequest = (BoxControlRequest) event.data;
                if (TextUtils.isEmpty(boxControlRequest.getIp())//为空时表示关闭全部
                        || (!TextUtils.isEmpty(boxControlRequest.getIp()) && NetWorkUtils.getLocalHostIp().equalsIgnoreCase(boxControlRequest.getIp()))) {
                    if (boxControlRequest.getOp() == 0) {
                        Log.e(TAG, "关机>>>>>>>>>>>>>>>>");
                        DeviceUtil.shutdown();
                    } else if (boxControlRequest.getOp() == 1) {
                        Log.e(TAG, "重启>>>>>>>>>>>>>>>>");
                        DeviceUtil.reboot();
                    }
                }
                break;
            case EventBusId.ImId.SCREEN_SHOT:
                if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {//只需要主屏处理
                    screenShot((VodScreenShotRequest) event.data);
                }
                break;
            case EventBusId.ImId.TEXT_MESSAGE://文本通知
                EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_TEXT, event.data);
                this.isPlayingNotificationText = true;
                break;
            case EventBusId.ImId.NOTIFICATION_TEXT_CHANGED://文本通知队列发送变化
                if (!isPlayingNotificationText && !NotificationMessageData.getInstance().getTextQueue().isEmpty()) {
                    TextMessageRequest textMessageRequest = NotificationMessageData.getInstance().getTextQueue().remove(0);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_TEXT, textMessageRequest);
                    this.isPlayingNotificationText = true;
                }
                break;
            case EventBusId.ImId.IMAGE_MESSAGE://图片通知
                EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_IMAGE, event.data);
                this.isPlayingNotificationImage = true;
                break;
            case EventBusId.ImId.NOTIFICATION_IMAGE_CHANGED://图片通知队列发送变化
                if (!isPlayingNotificationImage && !NotificationMessageData.getInstance().getImageQueue().isEmpty()) {
                    ImageMessageRequest imageMessageRequest = NotificationMessageData.getInstance().getImageQueue().remove(0);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_IMAGE, imageMessageRequest);
                    this.isPlayingNotificationImage = true;
                }
                break;
            case EventBusId.ImId.VIDEO_MESSAGE://视频通知
                Log.d(TAG, "视频通知 >>> VIDEO_MESSAGE");
                VideoMessageRequest msg = (VideoMessageRequest) event.data;
                if (msg.getVideo().getType() == 2) {//小窗口
                    Log.d(TAG, "小窗口视频>>>>>>>>>>>>>>>>>>>>>>>>");
                    EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_VIDEO_SMALL, msg);
                    this.isPlayingNotificationVideo = true;
                } else {
                    interpolationVideo(msg.getVideo().getUrl(), "插播视频");
                }
                break;
            case EventBusId.ImId.NOTIFICATION_VIDEO_SMALL_CHANGED://视频通知队列发送变化
                if (!isPlayingNotificationVideo && !NotificationMessageData.getInstance().getVideoSmallQueue().isEmpty()) {
                    VideoMessageRequest videoMessageRequest = NotificationMessageData.getInstance().getVideoSmallQueue().remove(0);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_VIDEO_SMALL, videoMessageRequest);
                    this.isPlayingNotificationVideo = true;
                }
                break;
            case EventBusId.ImId.NOTIFICATION_VIDEO_CHANGED:
                VideoMessageRequest pushVideo = NotificationMessageData.getInstance().getVideoQueueImmediately().remove(0);
                interpolationVideo(pushVideo.getVideo().getUrl(), "插播视频");
                break;
            default:
                break;
        }
    }

    private void showTipOnTelevision(int resId, int textResId, boolean autoDismiss) {
        showTipOnTelevision(resId, VodApplication.getVodApplicationContext().getString(textResId), autoDismiss);
    }

    private void showTipOnTelevision(int resId, String text, boolean autoDismiss) {
        EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_CENTER_ICON, new PresentationCenterIcon(resId,
                text, autoDismiss));
    }

    /**
     * 恢复初始音量
     */
    public void resetInitVolume() {
        setStreamVolume(VodConfigData.getInstance().getInitialVolume());//初始化音量
    }

    /**
     * 系统音量变化
     */
    public void setStreamVolume(int volume) {
        if (limitVolume >= volume && songTypeLimitVolume >= volume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            buttonStatus.currentVolume = volume;
            EventBusUtil.postSticky(EventBusId.PresentationId.SYSTEM_VOL_CHANGED, buttonStatus.currentVolume);
            broadcastButtonStatus2SecOnly(buttonStatus);
        }
    }

    /**
     * 房间已关闭
     */
    public void roomClosed() {
        EventBusUtil.postSticky(EventBusId.Id.ROOM_STATUS_CHANGED, 0);
        resetBoxUUID(true);
        SerialController.getInstance().onIntelLightsRoomClose();
        //删除录音文件夹
        AudioRecordFileUtil.deleteRecordFiles();
        //清除房间人员
        RoomUsers.getInstance().cleanRoomUser();
        if (VodConfigData.getInstance().closeRoomCutSong()) {//关房切歌
            next(false);
        }
    }

    /**
     * 房间已开房
     */
    public void roomOpened() {
        EventBusUtil.postSticky(EventBusId.Id.ROOM_STATUS_CHANGED, 1);
        resetBoxUUID(true);
        SerialController.getInstance().onIntelLightsRoomOpen();
    }

    /**
     * 最多音量限制
     */
    public void setLimitVolume(int volume) {
        this.limitVolume = volume;
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume > limitVolume) {
            setStreamVolume(limitVolume);
        }
    }

    /**
     * 曲种音量限制
     */
    public void setSongTypeLimitVolume(int volume) {
        this.songTypeLimitVolume = volume;
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume > songTypeLimitVolume) {
            setStreamVolume(songTypeLimitVolume);
        }
    }

    @Override
    public void onPlayStart(String path) {
        Log.i(TAG, "onPlayStart >>>>>>>>>>>>>>>>>>>  isMute:" + buttonStatus.isMute + " isOriginal:" + buttonStatus.isOriginal);
        preNextTime = System.currentTimeMillis();
        if (karaokePlayer != null) {
            if (buttonStatus.isMute == 1)
                karaokePlayer.volOff();
            else {
                karaokePlayer.volOn();
            }
        }
        if (buttonStatus.isOriginal == 1) {
            original(false);
        } else {
            accompany(false);
        }
        if (this.playingItem != null) {
            if (playingItem.type == -2) {//-4评分结果,-3插播视频；-2公益广告；-1贴片广告；0歌曲；1电影；2直播；
                SerialController.getInstance().onIntelLightsPublic();
            } else if (playingItem.type >= 0) {
                requestAdCorner(playingItem.type, playingItem.singerId);//查询角标广告
                if (playingItem.type == 0) {// 根据歌曲类型发送灯光码值
                    sendSongLight(playingItem.songType);
                    if (!TextUtils.isEmpty(playingItem.galleries)) {
                        playGalleries(playingItem.galleries);
                    }
                    if (playingItem.isRedPacket == 1 && playingItem.adId > 0) {//查询红包广告
                        requestRedPacket(playingItem.songCode);
                    }
                }
            }
            getSongType(playingItem);
            if (playingItem.type >= 0 && getCoolScreenStatus().isOpen == 1 && getCoolScreenStatus().isRandom == 1) {//随机炫屏
                getRandomCoolScreen();
            }
        }
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {
        long currentMillisTime = System.currentTimeMillis();
        if (playingItem != null && playingItem.type >= 0 && duration > 0 && current > 0) {//记录播放时间
            playingItem.duration = duration;
            playingItem.current = current;
        }
        if (currentMillisTime - preCallbackPlayerProgress > 200 && onKaraokePlayerListener != null) {//降低刷新率
            preCallbackPlayerProgress = currentMillisTime;
            onKaraokePlayerListener.onPlayProgress(duration, current);
            if (playingItem != null && Math.abs(current - playerProgressIntervalSec) >= 500) {//每秒
                playerProgressIntervalSec = current;
                int currentSecond = (int) (current / 1000);
                if (playingItem.type >= 0) {
                    if (Math.abs(currentSecond - callbackAdCornerSecond) > 5) {//超过5秒
                        if (currentSecond == 10 || currentSecond == (duration / 1000) / 2) {//第十秒，进度的一半
                            callbackAdCornerSecond = currentSecond;
                            showAdCornerOnTV(adCorner);
                            if (playingItem.type == 0 && adRedPacket != null) {
                                showRedPacketOnTV(adRedPacket);
                            }
                        }
                    }
                } else if (playingItem.type == -4) {
                    Log.i(TAG, "type :" + playingItem.type + " currentSecond:" + currentSecond + " callScoreResultTime:" + callScoreResultTime);
                    if (Math.abs(currentSecond - callScoreResultTime) > 5 && currentSecond == 8) {//播放评分动画8秒时
                        callScoreResultTime = currentSecond;
                        showScoreResult();
                    }
                }
            }
        }
    }

    @Override
    public void onPlayCompletion(String path) {
        if (getButtonStatus().scoreMode > 0 && playingItem != null && playingItem.type == 0
                && !TextUtils.isEmpty(playingItem.getGradeLibFile1()) && !TextUtils.isEmpty(playingItem.getGradeLibFile2())) {//播放评价结果
            playScoreResult(playingItem.score);
        } else {
            next(false);
        }
    }

    @Override
    public void onPlayError(String path, String error) {
        Log.e(TAG, "onPlayError path:" + path + " error:" + error);
        //播放器报错，进行切歌
        handler.postDelayed(() -> next(false), 3 * 1000);
    }

//    @Override
//    public void onOriginNotes(ArrayList<NoteInfo> noteInfos) {
//        Log.d(TAG, "评分相关回调：onOriginNotes");
//    }
//
//    @Override
//    public void onKeyInfoCallback(KeyInfo[] keys, int totalScore) {
////        Log.d(TAG, "评分相关回调：onKeyInfoCallback");
//    }
//
//    @Override
//    public void onUpdateTime(long msTime) {
////        Log.d(TAG, "评分相关回调：onUpdateTime");
//    }
//
//    @Override
//    public void onRecordData(byte[] data, int bufSize) {
////        Log.d(TAG, "评分相关回调：onRecordData");
//    }


    @Override
    public void onScoreViewVisibility(boolean isShow) {
        EventBusUtil.postSticky(EventBusId.PresentationId.SCORE_VISIBILITY_STATUS, isShow ? 1 : 0);
    }

    @Override
    public void onScoreCallback(int score) {
        playingItem.score = score;
        EventBusUtil.postSticky(EventBusId.PresentationId.CURRENT_SCORE, score);
    }

    public void playLive(Live live) {
        ChooseSongs.getInstance().addLive(live);
    }

    public void playMovie(Movie movie) {
        ChooseSongs.getInstance().addMovie(movie);
    }

    private void playScoreResult(int score) {
        try {
            requestResult(ChooseSongs.getInstance().getFirstSong().songCode, score);
            ChooseSongs.getInstance().getFirstSong().score = score;
            ChooseSongs.getInstance().getFirstSong().winPercent = score;
            ChooseSongs.getInstance().getFirstSong().duration = playingItem.duration;
            ChooseSongs.getInstance().getFirstSong().current = playingItem.current;
            Log.d(TAG, "评分歌曲：" + playingItem.simpName + " 播放完成! 得分：" + score);
            RoomSongItem item = new RoomSongItem();
            item.songCode = "";
            item.uuid = "";
            item.songUrl = ServerFileUtil.getFileUrl(AdModelDefault.getScoreResultVideo());
            item.simpName = playingItem.simpName;
            item.volume = 50;
            item.hasSongFile = 1;
            item.audioTrack = 4;
            item.isToTop = true;
            item.type = -4;
            item.score = score;
            item.duration = playingItem.duration;
            item.current = playingItem.current;
            play(item, nextPlayItem);
        } catch (Exception e) {
            Log.w(TAG, "记录歌曲得分，出错了:", e);
        }
    }

    private void requestResult(String songId, int score) {
        HttpRequestV4 request = new HttpRequestV4(VodApplication.getVodApplicationContext(), RequestMethod.V4.CLOUD_SONG_SCORE_RANKING);
        request.addParam("song_id", songId);
        request.addParam("score", score + "");
        request.setConvert2Class(ScoreRanking.class);
        request.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onStart(String method) {
            }

            @Override
            public void onFailed(String method, Object object) {

            }

            @Override
            public void onError(String method, String error) {

            }

            @Override
            public void onSuccess(String method, Object object) {
                try {
                    if (object instanceof ScoreRanking) {
                        ScoreRanking scoreRanking = (ScoreRanking) object;
                        if (ChooseSongs.getInstance().getFirstSong() != null) {
                            ChooseSongs.getInstance().getFirstSong().winPercent = scoreRanking.percent;
                        }
                    }
                } catch (Exception e) {
                    Logger.d(getClass().getSimpleName(), "requestResult ex:" + e.toString());
                }
            }
        });
        request.get();
    }

    /**
     * 插播视频
     *
     * @param url  播放地址
     * @param name 名称
     */
    public void interpolationVideo(String url, String name) {
        Log.d(TAG, "插播视频 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        ChooseSongs.getInstance().playVideo(UUIDUtil.getRandomUUID(), url, name, 80, -3);
    }

    /**
     * 发送弹幕
     */
    public void onBarrage(String text) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            Text protoText = Text.getDefaultInstance().toBuilder().setContent(text).build();
            MobileMessageBroadcast message = MobileMessageBroadcast.getDefaultInstance().toBuilder().setText(protoText).build();
            playBarrage(message);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(12, text);
        }
    }

    /**
     * 空调模式；0自动，1制冷，2制热，3送风
     */
    public void onAirConMode(int mode) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            switch (mode) {
                case 0:
                    SerialController.getInstance().onAirConModeAuto();
                    airConStatus.mode = 0;
                    break;
                case 1:
                    SerialController.getInstance().onAirConModeCool();
                    airConStatus.mode = 1;
                    break;
                case 2:
                    SerialController.getInstance().onAirConModeHot();
                    airConStatus.mode = 2;
                    break;
                case 3:
                    SerialController.getInstance().onAirConModeWind();
                    airConStatus.mode = 3;
                    break;
                default:
                    break;
            }
            broadcastAirConStatus2SecOnly(airConStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(30, mode);
        }
    }

    /**
     * 空调风速
     *
     * @param speed 0自动，1高风，2中风，3低风
     */
    public void onAirWindSpeed(int speed) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            switch (speed) {
                case 0:
                    SerialController.getInstance().onAirConWindAuto();
                    airConStatus.windSpeed = 0;
                    break;
                case 1:
                    SerialController.getInstance().onAirConWindHigh();
                    airConStatus.windSpeed = 1;
                    break;
                case 2:
                    SerialController.getInstance().onAirConWindMid();
                    airConStatus.windSpeed = 2;
                    break;
                case 3:
                    SerialController.getInstance().onAirConWindLow();
                    airConStatus.windSpeed = 3;
                    break;
            }
            broadcastAirConStatus2SecOnly(airConStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(31, speed);
        }
    }

    /**
     * 排风开/关
     */
    public void onWindSwitch(boolean isOpen) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (isOpen) {
                SerialController.getInstance().onAirConAirOn();
                airConStatus.windOpen = true;
            } else {
                SerialController.getInstance().onAirConAirOff();
                airConStatus.windOpen = false;
            }
            broadcastAirConStatus2SecOnly(airConStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(32, isOpen ? 1 : 0);
        }
    }

    /**
     * 喷泡泡
     */
    public void onBubble() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            SerialController.getInstance().onBubble();
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(33, 0);
        }
    }

    /**
     * 喷烟雾
     */
    public void onSmoke() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            SerialController.getInstance().onSmoke();
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(34, 0);
        }
    }

    /**
     * 空调 开/关
     */
    public void onAirConSwitch(boolean isOpen) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (isOpen) {
                SerialController.getInstance().onAirConOn();
                airConStatus.airConOpen = true;
            } else {
                SerialController.getInstance().onAirConOff();
                airConStatus.airConOpen = false;
            }
            broadcastAirConStatus2SecOnly(airConStatus);
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(35, isOpen ? 1 : 0);
        }
    }

    /**
     * 温度加减
     */
    public void onTempUpDown(boolean isTempUp) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            if (isTempUp) {
                SerialController.getInstance().onAirConTempUp();
            } else {
                SerialController.getInstance().onAirConTempDown();
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(36, isTempUp ? 1 : -1);
        }
    }

    /**
     * 回复截图信息
     */
    public void screenShot(VodScreenShotRequest request) {
        int playType = 1;//1公益广告 2歌曲 3电影 4直播 5贴片广告
        String playingId = "";
        String playingName = "";
        String playingAdId = "";
        String songVersion = "";
        String singerName = "";
        if (playingItem != null) {
            switch (playingItem.type) {//-4评分结果,-3插播视频；-2公益广告；-1贴片广告；0歌曲；1电影；2直播；
                case -3:
                case -2:
                    playType = 1;
                    break;
                case -4:
                case 0:
                    playType = 2;
                    break;
                case 1:
                    playType = 3;
                    break;
                case 2:
                    playType = 4;
                    break;
                case -1:
                    playType = 5;
                    break;
            }
            playingId = playingItem.songCode;
            playingName = playingItem.simpName;
            playingAdId = playingItem.adId + "";
            songVersion = playingItem.songVersion;
            singerName = playingItem.singerName;
        }
        HdmiScreenShotUploader.shotAndUploadHdmi(handler, VodApplication.getVodApplicationContext(), request, playType, playingId, playingName, playingAdId, songVersion, singerName);
    }

    /**
     * 播放自制MV图片
     */
    private void playGalleries(String galleries) {
        EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_MV_GALLERIES, galleries);
    }

    /**
     * 播放角标广告
     */
    private void showAdCornerOnTV(ADModel adModel) {
        EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_SHOW_AD_CORNER, adModel);
    }

    /**
     * 播放红包广告
     */
    private void showRedPacketOnTV(ADModel adModel) {
        if (adModel != null && adModel.getBeacon() != null && bnsBeaconHelper != null) {
            EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_RED_PACKET, adModel);
            bnsBeaconHelper.setBeaconInfo(adModel.getBeacon().uuid, adModel.getBeacon().major, adModel.getBeacon().minor);
        }
    }

    /**
     * 显示评分结果
     */
    private void showScoreResult() {
        EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_SCORE_RESULT, ChooseSongs.getInstance().getFirstSong());
    }

    /**
     * beacon
     */
    public void setBnsBeaconHelper(BnsBeaconHelper bnsBeaconHelper) {
        this.bnsBeaconHelper = bnsBeaconHelper;
    }

    /**
     * 获取角标
     */
    private void requestAdCorner(int type, String singerId) {
        AdGetterV4 adGetterV4 = new AdGetterV4(VodApplication.getVodApplicationContext(), new AdsRequestListenerV4() {
            @Override
            public void onAdsRequestSuccess(List<ADModel> adModelList) {
                ADModel adModel;
                if (adModelList != null && adModelList.size() > 0 && (adModel = adModelList.get(0)) != null
                        && !TextUtils.isEmpty(adModel.getAdContent())) {
                    adCorner = adModel;
                }
            }

            @Override
            public void onAdsRequestFail() {
            }
        });
        adGetterV4.getADbyPosAndID(type == 1 ? "J2" : (type == 2 ? "J3" : "J1"), singerId);
    }

    /**
     * 获取红包广告
     */
    private void requestRedPacket(String songId) {
        if (BnsBeaconHelper.haveBeacon()) {
            AdGetterV4 adGetterV4 = new AdGetterV4(VodApplication.getVodApplicationContext(), new AdsRequestListenerV4() {
                @Override
                public void onAdsRequestSuccess(List<ADModel> adModelList) {
                    ADModel adModel;
                    if (adModelList != null && adModelList.size() > 0 && (adModel = adModelList.get(0)) != null
                            && adModel.getBeacon() != null) {
                        adRedPacket = adModel;
                    }
                }

                @Override
                public void onAdsRequestFail() {
                }
            });
            adGetterV4.getAdBySongId("H1", songId);
        }
    }

    /**
     * 获取暂停广告
     */
    private void requestAdPause(int type, String singerId) {
        AdGetterV4 adGetterV4 = new AdGetterV4(VodApplication.getVodApplicationContext(), new AdsRequestListenerV4() {
            @Override
            public void onAdsRequestSuccess(List<ADModel> adModelList) {
                ADModel adModel;
                if (adModelList != null && adModelList.size() > 0 && (adModel = adModelList.get(0)) != null
                        && !TextUtils.isEmpty(adModel.getAdContent())) {
                    EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_AD_PAUSE, adModel);
                }
            }

            @Override
            public void onAdsRequestFail() {
            }
        });
        adGetterV4.getADbyPosAndID(type == 0 ? "S1" : "S2", singerId);
    }

    /**
     * 发送歌曲灯光
     */
    private void sendSongLight(int songTypeId) {
        if (getButtonStatus().isLightAuto == 1) {
            SongTypeLightHelper helper = new SongTypeLightHelper(VodApplication.getVodApplicationContext());
            helper.setOnSongTypeLightCallback(light ->
                    //灯光模式定义从1开始，所以要-1
                    lightMode(light.songTypeLight.lightMode - 1, true, false)
            );
            helper.getSongTypeLight(songTypeId);
        }
    }

    private void getSongType(RoomSongItem item) {
        if ("0".equalsIgnoreCase(VodConfigData.getInstance().getDanceType())) {//全部控制音量
            setSongTypeLimitVolume(VodConfigData.getInstance().getDanceVolume());
        } else if (item.type == 0) {//单曲种音量限制，只有歌曲有曲种概念
            SongTypeHelper helper = new SongTypeHelper(VodApplication.getVodApplicationContext());
            helper.setOnSongTypeLightCallback(songType -> {
                if (songType != null) {
                    int parentId = songType.parentId > 0 ? songType.parentId : songType.id;//音量限制
                    int volume = VodConfigData.getInstance().getSongTypeVolume(parentId);
                    Log.d(TAG, "getSongType parentId:" + parentId + " 限制音量：" + volume);
                    setSongTypeLimitVolume(volume);
                }
            });
            helper.getSongType(item.songType);
        } else {
            setSongTypeLimitVolume(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        }
    }

    private void getRandomCoolScreen() {
        CoolScreenHelper helper = new CoolScreenHelper(VodApplication.getVodApplicationContext());
        helper.setOnCoolScreenCallback(coolScreen -> setCoolScreen(coolScreen.id, coolScreen.fileUrl));
        helper.get();
    }

    public void showTelevisionQrCode() {
        EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_PHONE_QR_CODE, VodConfigData.getInstance().getQrCodeDuration());
    }

    public void setIsPlayingNotificationText(boolean isPlaying) {
        this.isPlayingNotificationText = isPlaying;
        if (!this.isPlayingNotificationText) {
            EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_TEXT_CHANGED, 0);
        }
    }

    public void setIsPlayingNotificationImage(boolean isPlaying) {
        this.isPlayingNotificationImage = isPlaying;
        if (!this.isPlayingNotificationImage) {
            EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_IMAGE_CHANGED, 0);
        }
    }

    public void setIsPlayingNotificationVideo(boolean isPlaying) {
        this.isPlayingNotificationVideo = isPlaying;
        if (!this.isPlayingNotificationVideo) {
            EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_VIDEO_SMALL_CHANGED, 0);
        }
    }
}
