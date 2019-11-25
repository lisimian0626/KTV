package com.beidousat.karaoke.serialport;

import android.text.TextUtils;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.ControlBoxGetter;
import com.bestarmedia.libcommon.interf.ControlBoxListener;
import com.bestarmedia.libcommon.model.vod.LightItem;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libserial.SerialSendRecvHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/11/5 10:18.
 */
public class SerialController implements ControlBoxListener, SerialSendRecvHelper.OnSerialReceiveListener {

    private static SerialController mSerialController;
    private SerialSendRecvHelper mSerialHelper;
    private ControlBoxGetter mSerialCode;
    private SerialPortCode mSerialPortCode;
    private SerialReceiver mSerialReceiver;
    private String mPreLightCode = "";

    private final static String TAG = SerialController.class.getSimpleName();

    public static SerialController getInstance() {
        if (mSerialController == null) {
            synchronized (SerialController.class) {
                if (mSerialController == null)
                    mSerialController = new SerialController();
            }
        }
        return mSerialController;
    }

    private SerialController() {
        mSerialCode = new ControlBoxGetter(this);
        mSerialCode.getControlBoxInfo();
    }

    public SerialPortCode getSerialPortCode() {
        return mSerialPortCode;
    }

    public void requestControlBox() {
        mSerialCode.getControlBoxInfo();
    }

    @Override
    public void onControlBox(SerialPortCode code) {
        if (code != null) {
            mSerialReceiver = SerialReceiver.getInstance(code);
            mSerialPortCode = code;
            mSerialHelper = SerialSendRecvHelper.getInstance();
            try {
                mSerialHelper.setOnSerialReceiveListener(this);
                if (TextUtils.isEmpty(code.baudrate)) {
                    code.baudrate = String.valueOf(4800);
                }
                mSerialHelper.open(OkConfig.boxManufacturer() == 1 ? "/dev/ttyS1" : "/dev/ttyS3", Integer.valueOf(code.baudrate));
                //开机默认灯光
                onIntelLightsBoot();
            } catch (Exception ex) {
                Logger.w(TAG, "onControlBox open ex:" + ex.toString());
            }
        }
    }

    @Override
    public void OnSerialReceive(String data) {
        Logger.d(TAG, "OnSerialReceive :" + data + "");
        mSerialReceiver.dealCode(data);
//        Message msg = new Message();
//        msg.obj = data;
//        handler.sendMessage(msg);
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            mSerialReceiver.dealCode(msg.obj.toString());
//        }
//    };

    private void onPreLight() {
        if (!TextUtils.isEmpty(mPreLightCode)) {
            onLightByCode(mPreLightCode);
        }
    }

    /**
     * 音乐音量+
     *
     public void onMusicVolUp() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.MusicVolUpD))
     mSerialHelper.send(mSerialPortCode.MusicVolUpD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 音乐音量-
     *
     public void onMusicVolDown() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.MusicVolDownD))
     mSerialHelper.send(mSerialPortCode.MusicVolDownD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     } */

    /**
     * 服务灯-开
     */
    public void onService() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.ServiceLightOnD))
                    mSerialHelper.send(mSerialPortCode.ServiceLightOnD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 服务灯-关
     */
    public void offService() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.ServiceLightOffD))
                    mSerialHelper.send(mSerialPortCode.ServiceLightOffD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 麦克风+
     */
    public void onMicVolUp() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.MicVolUpD)) {
                    mSerialHelper.send(mSerialPortCode.MicVolUpD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 麦克风-
     */
    public void onMicVolDown() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.MicVolDownD)) {
                    mSerialHelper.send(mSerialPortCode.MicVolDownD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 音乐升调
     *
     public void onMusicPitchUp() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchUpD))
     mSerialHelper.send(mSerialPortCode.MusicPitchUpD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 音乐降调
     *
     public void onMusicPitchDown() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchDownD))
     mSerialHelper.send(mSerialPortCode.MusicPitchDownD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 音乐原调
     *
     public void onMusicPitchOri() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchOriD))
     mSerialHelper.send(mSerialPortCode.MusicPitchOriD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 麦克风升调
     */
    public void onMicPitchUp() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.MicPitchUpD))
                    mSerialHelper.send(mSerialPortCode.MicPitchUpD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 麦克风降调
     */
    public void onMicPitchDown() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.MicPitchDownD))
                    mSerialHelper.send(mSerialPortCode.MicPitchDownD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 麦克风原调
     */
    public void onMicPitchOri() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.MicPitchOriD))
                    mSerialHelper.send(mSerialPortCode.MicPitchOriD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 音效-唱将
     */
    public void onEffectSing() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectSingD))
                    mSerialHelper.send(mSerialPortCode.EffectSingD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 音效-搞怪
     */
    public void onEffectBlame() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectBlameD))
                    mSerialHelper.send(mSerialPortCode.EffectBlameD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 音效-整蛊
     */
    public void onEffectTrick() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectTrickD))
                    mSerialHelper.send(mSerialPortCode.EffectTrickD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 音效-和声
     */
    public void onEffectHarmony() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectHarmonyD))
                    mSerialHelper.send(mSerialPortCode.EffectHarmonyD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 混响+
     */
    public void onEffectUp() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectUpD))
                    mSerialHelper.send(mSerialPortCode.EffectUpD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 混响-
     */
    public void onEffectDown() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.EffectDownD))
                    mSerialHelper.send(mSerialPortCode.EffectDownD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-标准
     */
    public void onLightLightStandard() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsStandardD)) {
                    onLightByCode(mSerialPortCode.LightsStandardD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-明亮
     */
    public void onLightBright() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsBrightD)) {
                    onLightByCode(mSerialPortCode.LightsBrightD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-柔和
     */
    public void onLightSoft() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsSoftD)) {
                    onLightByCode(mSerialPortCode.LightsSoftD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-抒情
     */
    public void onLightLyric() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsLyricD)) {
                    onLightByCode(mSerialPortCode.LightsLyricD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-动感
     */
    public void onLightDynamic() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsDynamicD)) {
                    onLightByCode(mSerialPortCode.LightsDynamicD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-浪漫
     */
    public void onLightRomantic() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightRomanticD)) {
                    onLightByCode(mSerialPortCode.LightRomanticD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 手动灯光-朦胧
     */
    public void onLightDim() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightDimD)) {
                    onLightByCode(mSerialPortCode.LightDimD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 手动灯光-演唱会
     */
    public void onLightConcert() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightConcertD)) {
                    onLightByCode(mSerialPortCode.LightConcertD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 手动灯光-激情
     */
    public void onLightPassion() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightPassionD)) {
                    onLightByCode(mSerialPortCode.LightPassionD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 手动灯光-清洁
     */
    public void onLightClean() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightCleanD)) {
                    onLightByCode(mSerialPortCode.LightCleanD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-加亮
     */
    public void onLightUp() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightUpD))
                    mSerialHelper.send(mSerialPortCode.LightUpD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 灯光-调暗
     */
    public void onLightDown() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightDownD))
                    mSerialHelper.send(mSerialPortCode.LightDownD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    public void onLightOn() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsOnD)) {
                    onLightByCode(mSerialPortCode.LightsOnD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    public void onLightOff() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.LightsOffD)) {
                    onLightByCode(mSerialPortCode.LightsOffD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 发灯光码
     */
    public void onLightByCode(String code) {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(code)) {
                    mSerialHelper.send(code);
                    setCurrentLightCode(code);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 当前灯光码值
     */
    private void setCurrentLightCode(String lightCode) {
        if (!mPreLightCode.equalsIgnoreCase(lightCode)) {
            mPreLightCode = lightCode;
            EventBusUtil.postSticky(EventBusId.Serial.LIGHT_MODE_CHANGED, mPreLightCode);
        }
    }

    /**
     * 智能灯光-开机默认
     */
    private void onIntelLightsBoot() {
        Logger.d(TAG, "onIntelLightsBoot >>>>>>>>>>>>>>>>>>> ");
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsBootD)
                        && !mSerialPortCode.IntelLightsBootD.equalsIgnoreCase(mPreLightCode)) {
                    onLightByCode(mSerialPortCode.IntelLightsBootD);
                }
            } else {
                Logger.d(TAG, "mSerialPortCode is null  >>>>>>>>>>>>>>>>>>> ");
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-开房
     */
    public void onIntelLightsRoomOpen() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsRoomOpenD)
                        && !mSerialPortCode.IntelLightsRoomOpenD.equalsIgnoreCase(mPreLightCode)) {
                    onLightByCode(mSerialPortCode.IntelLightsRoomOpenD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-关房
     */
    public void onIntelLightsRoomClose() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsRoomCloseD)
                        && !mSerialPortCode.IntelLightsRoomCloseD.equalsIgnoreCase(mPreLightCode)) {
                    onLightByCode(mSerialPortCode.IntelLightsRoomCloseD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 公播（下行）
     */
    public void onIntelLightsPublic() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsPublicD)
                        && !mSerialPortCode.IntelLightsPublicD.equalsIgnoreCase(mPreLightCode)) {
                    onLightByCode(mSerialPortCode.IntelLightsPublicD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-开电视
     */
    public void onIntelLightsTvOn() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsTvOnD)) {
                    onLightByCode(mSerialPortCode.IntelLightsTvOnD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-关电视
     */
    public void onIntelLightsTvOff() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsTvOffD)) {
                    onLightByCode(mSerialPortCode.IntelLightsTvOffD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 智能灯光-原唱
     *
     public void onIntelLightsOri() {
     try {
     if (mSerialPortCode != null) {
     if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsOriD)
     && !mSerialPortCode.IntelLightsOriD.equalsIgnoreCase(mPreLightCode)) {
     onLightByCode(mSerialPortCode.IntelLightsOriD);
     }
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 智能灯光-伴唱
     *
     public void onIntelLightsAccom() {
     try {
     if (mSerialPortCode != null) {
     if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsAccD)
     && !mSerialPortCode.IntelLightsAccD.equalsIgnoreCase(mPreLightCode)) {
     onLightByCode(mSerialPortCode.IntelLightsAccD);
     }
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 智能灯光-暂停
     */
    public void onIntelLightsPause() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsPauseD)) {
                    mSerialHelper.send(mSerialPortCode.IntelLightsPauseD);
                }
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-播放
     */
    public void onIntelLightsPlay() {
        try {
            if (mSerialPortCode != null) {
                onPreLight();
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 智能灯光-静音
     */
    public void onIntelLightsMute() {
        try {
            if (mSerialPortCode != null) {
                if (VodConfigData.getInstance().isIntelligentLight() && !TextUtils.isEmpty(mSerialPortCode.IntelLightsMuteD))
                    mSerialHelper.send(mSerialPortCode.IntelLightsMuteD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 智能灯光-取消静音
     */
    public void onIntelLightsMuteCancel() {
        try {
            if (mSerialPortCode != null) {
                onPreLight();
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 气氛-喝彩
     */
    public void onAtCheers() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AtCheersD))
                    mSerialHelper.send(mSerialPortCode.AtCheersD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 气氛-欢呼
     */
    public void onAtWhooped() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AtWhoopedD))
                    mSerialHelper.send(mSerialPortCode.AtWhoopedD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 气氛-掌声
     */
    public void onAtApplause() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AtApplauseD))
                    mSerialHelper.send(mSerialPortCode.AtApplauseD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 气氛-倒彩
     */
    public void onAtHooting() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AtHootingD))
                    mSerialHelper.send(mSerialPortCode.AtHootingD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 气氛-难听
     */
    public void onAtUnpleasant() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AtUnpleasantD))
                    mSerialHelper.send(mSerialPortCode.AtUnpleasantD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 空调模式-自动
     */
    public void onAirConModeAuto() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConModeAutoD))
                    mSerialHelper.send(mSerialPortCode.AirConModeAutoD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调模式-制冷
     */
    public void onAirConModeCool() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConModeCoolD))
                    mSerialHelper.send(mSerialPortCode.AirConModeCoolD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调模式-制热
     */
    public void onAirConModeHot() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConModeHotD))
                    mSerialHelper.send(mSerialPortCode.AirConModeHotD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调模式-送风
     */
    public void onAirConModeWind() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConModeWindD))
                    mSerialHelper.send(mSerialPortCode.AirConModeWindD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调风速-自动
     */
    public void onAirConWindAuto() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConWindAutoD))
                    mSerialHelper.send(mSerialPortCode.AirConWindAutoD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调风速-高风
     */
    public void onAirConWindHigh() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConWindHighD))
                    mSerialHelper.send(mSerialPortCode.AirConWindHighD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调风速-中风
     */
    public void onAirConWindMid() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConWindMidD))
                    mSerialHelper.send(mSerialPortCode.AirConWindMidD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调风速-低风
     */
    public void onAirConWindLow() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConWindLowD))
                    mSerialHelper.send(mSerialPortCode.AirConWindLowD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调-温度+
     */
    public void onAirConTempUp() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConTempUpD))
                    mSerialHelper.send(mSerialPortCode.AirConTempUpD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调-温度-
     */
    public void onAirConTempDown() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConTempDownD))
                    mSerialHelper.send(mSerialPortCode.AirConTempDownD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 排风-开
     */
    public void onAirConAirOn() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConAirOnD))
                    mSerialHelper.send(mSerialPortCode.AirConAirOnD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 排风-关
     */
    public void onAirConAirOff() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConAirOffD))
                    mSerialHelper.send(mSerialPortCode.AirConAirOffD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调-开
     */
    public void onAirConOn() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConOnD))
                    mSerialHelper.send(mSerialPortCode.AirConOnD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 空调-关
     */
    public void onAirConOff() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.AirConOffD))
                    mSerialHelper.send(mSerialPortCode.AirConOffD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }


    /**
     * 门牌灯-开
     *
     public void onDoorplateOn() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.DoorplateOnD))
     mSerialHelper.send(mSerialPortCode.DoorplateOnD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 门牌灯-关
     *
     public void onDoorplateOff() {
     try {
     if (mSerialPortCode != null) {
     if (!TextUtils.isEmpty(mSerialPortCode.DoorplateOffD))
     mSerialHelper.send(mSerialPortCode.DoorplateOffD);
     } else {
     mSerialCode.getControlBoxInfo();
     }
     } catch (Exception e) {
     Logger.w(TAG, e.toString());
     }
     }*/

    /**
     * 喷烟雾
     */
    public void onSmoke() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.SmokeD))
                    mSerialHelper.send(mSerialPortCode.SmokeD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 喷泡泡
     */
    public void onBubble() {
        try {
            if (mSerialPortCode != null) {
                if (!TextUtils.isEmpty(mSerialPortCode.BubbleD))
                    mSerialHelper.send(mSerialPortCode.BubbleD);
            } else {
                mSerialCode.getControlBoxInfo();
            }
        } catch (Exception e) {
            Logger.w(TAG, e.toString());
        }
    }

    /**
     * 获取手动灯光列表
     */
    public List<LightItem> getLightItems() {
        List<LightItem> items = new ArrayList<>();
        SerialPortCode serialPortCode = getSerialPortCode();
        if (!TextUtils.isEmpty(serialPortCode.LightsStandardD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.standard), serialPortCode.LightsStandardU, serialPortCode.LightsStandardD,0));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsBrightD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.bright), serialPortCode.LightsBrightU, serialPortCode.LightsBrightD,1));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsSoftD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.soft), serialPortCode.LightsSoftU, serialPortCode.LightsSoftD,2));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsLyricD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.lyric), serialPortCode.LightsLyricU, serialPortCode.LightsLyricD,3));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsDynamicD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.montion), serialPortCode.LightsDynamicU, serialPortCode.LightsDynamicD,4));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightRomanticD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.romantic), serialPortCode.LightRomanticU, serialPortCode.LightRomanticD,5));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightDimD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.dim), serialPortCode.LightDimU, serialPortCode.LightDimD,6));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightConcertD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.concert), serialPortCode.LightConcertU, serialPortCode.LightConcertD,7));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightPassionD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.passion), serialPortCode.LightPassionU, serialPortCode.LightPassionD,8));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightCleanD)) {
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.clean), serialPortCode.LightCleanU, serialPortCode.LightCleanD,9));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsOnD)) {//开
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.all_on), serialPortCode.LightsOnU, serialPortCode.LightsOnD,10));
        }
        if (!TextUtils.isEmpty(serialPortCode.LightsOffD)) {//关
            items.add(new LightItem(VodApplication.getVodApplication().getResources().getString(R.string.all_off), serialPortCode.LightsOffU, serialPortCode.LightsOffD,11));
        }
        return items;
    }


}
