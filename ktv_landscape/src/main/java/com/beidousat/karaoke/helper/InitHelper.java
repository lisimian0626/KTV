package com.beidousat.karaoke.helper;

import android.text.TextUtils;
import android.util.Log;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.data.VideoFileCache;
import com.beidousat.karaoke.serialport.SerialController;
import com.beidousat.karaoke.ui.dialog.DlgInitLoading;
import com.beidousat.karaoke.ui.dialog.setting.DlgMngPwd;
import com.beidousat.karaoke.ui.fragment.setting.FmSetting;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.DeviceInfoData;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.LoginHelper;
import com.bestarmedia.libcommon.helper.NodeRoomTimer;
import com.bestarmedia.libcommon.helper.SafetyHelper;
import com.bestarmedia.libcommon.helper.VodConfigGetter;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.JWTUtil;
import com.bestarmedia.libcommon.interf.CheckDeviceListener;
import com.bestarmedia.libcommon.interf.NodeRoomListener;
import com.bestarmedia.libcommon.interf.SafetyListener;
import com.bestarmedia.libcommon.model.node.Author;
import com.bestarmedia.libcommon.model.security.JWTMessage;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.v4.RoomDevice;
import com.bestarmedia.libcommon.model.vod.NodeRoomV4;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;
import com.bestarmedia.libcommon.security.PrivateKeyUpdater;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.NetChecker;
import com.bestarmedia.pcmrecord.PcmRecorder;

import java.util.List;

public class InitHelper implements NodeRoomListener, CheckDeviceListener, SafetyListener {

    private static InitHelper mInitHelper;
    private final static String TAG = "InitHelper";
    private boolean mIsInitAfterNetAvail;
    private DlgInitLoading mDlgInitLoading;
    private DlgMngPwd mDlgMngPwd;
    private OnInitListener onInitListener;
    private LoginHelper loginHelper;
    private SafetyHelper safetyHelper;

    public static InitHelper getInstance() {
        if (mInitHelper == null) {
            mInitHelper = new InitHelper();
        }
        return mInitHelper;
    }

    private InitHelper() {
        if (loginHelper == null) {
            loginHelper = LoginHelper.getInstance(VodApplication.getVodApplicationContext());
            loginHelper.setCheckDeviceListener(this);
        }
        if (safetyHelper == null) {
            safetyHelper = SafetyHelper.getInstance(VodApplication.getVodApplicationContext());
            safetyHelper.setSafetyListener(this);
        }
    }

    public void start() {
        if (OkConfig.boxManufacturer() == 2) {
            PcmRecorder.getInstance().init();
        }
        checkNetwork();
    }

    public void setOnInitListener(OnInitListener listener) {
        this.onInitListener = listener;
    }

    private void checkNetwork() {
        if (onInitListener != null) {
            onInitListener.initStart();
        }
        showInitDialog(VodApplication.getVodApplicationContext().getString(R.string.checking_network));
        NetChecker.getInstance(VodApplication.getVodApplicationContext()).setOnNetworkStatusListener(status -> {
            Log.d(TAG, "检查网络可用状态:" + status);
            if (onInitListener != null) {
                onInitListener.initProgress(1, "正在检查网络可用状态....");
            }
            if (status) {
                if (!mIsInitAfterNetAvail) {
                    mIsInitAfterNetAvail = true;
                    if (onInitListener != null) {
                        onInitListener.initProgress(2, "当前检查网络可用!");
                    }
                }
            }
        }).check();
    }

    public void initAfterNetAvail() {
        showInitDialog(VodApplication.getVodApplicationContext().getString(R.string.connecting_server));
        getConfig();
    }

    private void getConfig() {
        VodConfigGetter serverConfigHelper = VodConfigGetter.getInstance(VodApplication.getVodApplicationContext());
        serverConfigHelper.setOnServerCallback(profileDetail ->
                {
                    JWTMessage jwtMessage = JWTUtil.jwt(PrefData.getJWT(VodApplication.getVodApplicationContext()));
                    VodConfigData.getInstance().setJwtMessage(jwtMessage);
                    BaseHttpRequest.setToken(PrefData.getJWT(VodApplication.getVodApplicationContext()));
                    if (mDlgInitLoading != null && mDlgInitLoading.isShowing()) {
                        mDlgInitLoading.dismiss();
                    }
                    if (onInitListener != null) {
                        onInitListener.initProgress(3, "获取配置信息成功！");
                    }
                    //获取设备信息
                    loginHelper.getDeviceInfo(DeviceUtil.getCupSerial());
                }
        );
        serverConfigHelper.getConfig();
    }

    public void getAuthor(){
        loginHelper.getAuthorInfo();
    }

    private void checkJwt() {
        if (TextUtils.isEmpty(PrefData.getJWT(VodApplication.getVodApplicationContext()))) {
            Log.d(TAG, "本机jwt为空！");
            //登录
            if (onInitListener != null) {
                onInitListener.initProgress(5, "本机JWT不存在,正在登陆...");
            }
            loginHelper.getToken(VodConfigData.getInstance().getKtvNetCode(), DeviceUtil.getCupSerial());
        } else {
            getDeviceInfo();
        }
    }

    /**
     * 必须先获取房间设备信息才可以初始化，主副屏设备信息从房间设备得知
     */
    public void getDeviceInfo() {
        if (onInitListener != null) {
            onInitListener.initProgress(6, "获取包房设备信息...");
        }
        LoginHelper loginHelper = LoginHelper.getInstance(VodApplication.getVodApplicationContext());
        loginHelper.setCheckDeviceListener(this);
        loginHelper.getRoomInfo(VodConfigData.getInstance().getRoomCode());
    }

    private void initAfterGetConfig() {
        if (onInitListener != null) {
            onInitListener.initProgress(8, "开始下载秘钥文件....");
        }
        PrivateKeyUpdater privateKeyUpdater = new PrivateKeyUpdater();
        privateKeyUpdater.updateKey();

        //去掉背景视频，用画布实现动态背景
//        if (onInitListener != null) {
//            onInitListener.initProgress(6, "初始化MP3背景视频....");
//        }
//        AudioBackgroundTemp.getInstance(activity.getApplicationContext()).init();
        if (onInitListener != null) {
            onInitListener.initProgress(9, "开始下载缓存文件....");
        }
        loadCacheFile();
        //初始化音量
        PrefData.setInitVol(VodApplication.getVodApplicationContext(), VodConfigData.getInstance().getInitialVolume());
        if (onInitListener != null) {
            onInitListener.initCompletion();
        }
        getRoomDetail();
        EventBusUtil.postSticky(EventBusId.Id.CHECK_SAFETY, "");
    }

    private void loadCacheFile() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            VideoFileCache videoFileCache = VideoFileCache.getInstance();
            videoFileCache.loadVideo();
        }
    }

    private void showInitDialog(String text) {
        if (mDlgInitLoading == null || !mDlgInitLoading.isShowing()) {
            mDlgInitLoading = new DlgInitLoading(VodApplication.getVodApplication().getStackTopActivity());
            mDlgInitLoading.setMessage(text);
            mDlgInitLoading.setPositiveButton(VodApplication.getVodApplicationContext().getString(R.string.setting_network), v -> {
                mDlgMngPwd = new DlgMngPwd(VodApplication.getVodApplication().getStackTopActivity());
                mDlgMngPwd.setOnMngPwdListener(() ->
                        FragmentUtil.addFragment(new FmSetting(), false, true, true, true));
                mDlgMngPwd.show();
            });
            mDlgInitLoading.show();
        } else {
            mDlgInitLoading.setMessage(text);
        }
    }

    public void getRoomDetail() {
        if (onInitListener != null) {
            onInitListener.initProgress(10, "获取房间信息");
        }
        NodeRoomTimer.getInstance(VodApplication.getVodApplicationContext()).addNodeRoomListener(this);
        NodeRoomTimer.getInstance(VodApplication.getVodApplicationContext()).startTimer();
    }

    @Override
    public void onNodeRoomLoaded(NodeRoomV4 nodeRoomV4) {
        if (onInitListener != null) {
            onInitListener.onNodeRoomLoaded(nodeRoomV4);
        }
        //初始化中控
        SerialController.getInstance().requestControlBox();
    }

    @Override
    public void onRoomSessionChanged(String preSession, String curSession) {
        if (onInitListener != null) {
            onInitListener.onRoomSessionChanged(preSession, curSession);
        }
    }

    @Override
    public void onNodeRoomReturn(NodeRoomV4 v4) {

    }

    @Override
    public void onDeviceInfoSucced(DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            Logger.d(TAG, "deviceInfo:" + deviceInfo.toString());
            if (TextUtils.isEmpty(deviceInfo.roomCode)) {
                EventBusUtil.postSticky(EventBusId.Id.GET_DEVICES_FAIL, "");
            } else {
                if (onInitListener != null) {
                    onInitListener.initProgress(4, "设备存在,检查JWT");
                }
                checkJwt();
            }
        }
    }

    @Override
    public void onDeviceInfoFail(String msg) {
        EventBusUtil.postSticky(EventBusId.Id.GET_DEVICES_FAIL, "");
    }

    @Override
    public void onRegister(boolean isSucced, String msg) {

    }

    @Override
    public void onGetJwt(String jwt) {
        getDeviceInfo();
    }

    @Override
    public void onNodeRoom(NodeRoom nodeRoom) {
        DeviceInfoData.getInstance().setNodeRoom(nodeRoom);
        boolean haslocalDevice = false;
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            for (RoomDevice roomDevice : nodeRoom.roomDeviceList) {
                if (roomDevice.serialNo.equals(DeviceUtil.getCupSerial())) {
                    if (onInitListener != null) {
                        onInitListener.initProgress(7, "获取包房设备成功,开始初始化...");
                    }
                    initAfterGetConfig();
                    haslocalDevice = true;
                }
            }
            if (!haslocalDevice) {
                EventBusUtil.postSticky(EventBusId.Id.GET_DEVICES_FAIL, "");
            }
        } else {
            EventBusUtil.postSticky(EventBusId.Id.GET_DEVICES_FAIL, "");
        }
    }

    public void checkSafety() {
        if (safetyHelper != null) {
            safetyHelper.setSafetyListener(this);
            safetyHelper.getRoomSatety(VodConfigData.getInstance().getKtvNetCode(), VodConfigData.getInstance().getRoomCode());
        }
    }

    @Override
    public void onLogout(boolean succed, String msg) {
        if (succed) {
            Log.d(TAG, "注销成功....");
            PrefData.setJWT(VodApplication.getVodApplicationContext(), "");
        }
    }

    @Override
    public void onDeviceType(List<DeviceType> deviceTypeList) {

    }

    @Override
    public void onAuthor(Author author) {
        if (onInitListener != null) {
            onInitListener.onAuthor(author);
        }
    }

    @Override
    public void onRoomSafeFail() {
        EventBusUtil.postSticky(EventBusId.Id.SAFETY_ROOM_FAIL, "");
    }

    @Override
    public void onSafeSucceed() {
        if (onInitListener != null) {
            onInitListener.initProgress(11, "消防检查通过,允许营业...");
        }
    }

    @Override
    public void onSafeUploadSucceed(boolean success) {

    }

    @Override
    public void onStoreSafeFail() {
        EventBusUtil.postSticky(EventBusId.Id.SAFETY_STORE_FAIL, "");
    }

    @Override
    public void offlineSucceed() {

    }


    public interface OnInitListener {
        void initStart();

        void initProgress(int step, String msg);

        void initCompletion();

        void onNodeRoomLoaded(NodeRoomV4 nodeRoomV4);

        void onRoomSessionChanged(String preSession, String curSession);

        void onAuthor(Author author);
    }
}
