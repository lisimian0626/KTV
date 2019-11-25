package com.beidousat.karaoke.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.helper.BnsBeaconHelper;
import com.beidousat.karaoke.helper.DeviceMessageHandler;
import com.beidousat.karaoke.helper.InitHelper;
import com.beidousat.karaoke.helper.KaraokeController;
import com.beidousat.karaoke.helper.MobileMessageHandler;
import com.beidousat.karaoke.helper.UpdateHelper;
import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.beidousat.karaoke.interf.OnKaraokePlayerListener;
import com.beidousat.karaoke.service.UsbService;
import com.beidousat.karaoke.service.VodService;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.DlgAdScreen;
import com.beidousat.karaoke.ui.dialog.DlgFireAlarm;
import com.beidousat.karaoke.ui.dialog.DlgRoomQrCode;
import com.beidousat.karaoke.ui.dialog.PopVersionInfo;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.dialog.main.DlgAtmosphere;
import com.beidousat.karaoke.ui.dialog.main.DlgCallService;
import com.beidousat.karaoke.ui.dialog.main.DlgLightController;
import com.beidousat.karaoke.ui.dialog.main.DlgScoreSwitcher;
import com.beidousat.karaoke.ui.dialog.main.DlgWifi;
import com.beidousat.karaoke.ui.dialog.play.DlgPay;
import com.beidousat.karaoke.ui.dialog.safety.DlgRoomSafety;
import com.beidousat.karaoke.ui.dialog.safety.DlgStoreSafety;
import com.beidousat.karaoke.ui.dialog.setting.DlgDeviceStore;
import com.beidousat.karaoke.ui.dialog.setting.DlgMngPwd;
import com.beidousat.karaoke.ui.dialog.setting.DlgTune;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.dialog.vod.SelectCollectDialog;
import com.beidousat.karaoke.ui.dialog.web.DlgWebView;
import com.beidousat.karaoke.ui.fragment.play.FmGame;
import com.beidousat.karaoke.ui.fragment.play.FmPlay;
import com.beidousat.karaoke.ui.fragment.service.FmService;
import com.beidousat.karaoke.ui.fragment.setting.FmSetting;
import com.beidousat.karaoke.ui.fragment.setting.FmSettingRoomRegist;
import com.beidousat.karaoke.ui.fragment.song.FmChooseList;
import com.beidousat.karaoke.ui.fragment.song.FmSearch;
import com.beidousat.karaoke.ui.fragment.song.FmSong;
import com.beidousat.karaoke.ui.fragment.topic.FmMood;
import com.beidousat.karaoke.ui.presentation.MainPresentation;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.util.UIUtils;
import com.beidousat.karaoke.widget.ArcProgress;
import com.beidousat.karaoke.widget.FloatView;
import com.beidousat.karaoke.widget.MarqueePlayer;
import com.beidousat.karaoke.widget.WidgetAdBannerPlayer;
import com.beidousat.karaoke.widget.WidgetRoomNameLogo;
import com.bestarmedia.libcommon.ad.AdLocalBenefitVideo;
import com.bestarmedia.libcommon.config.KtvPermissionConfig;
import com.bestarmedia.libcommon.data.BnsActivityCache;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.BnsActivityHelper;
import com.bestarmedia.libcommon.helper.LoginHelper;
import com.bestarmedia.libcommon.helper.PayApiHelper;
import com.bestarmedia.libcommon.helper.SafetyHelper;
import com.bestarmedia.libcommon.model.activity.BnsActivityBean;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.node.Author;
import com.bestarmedia.libcommon.model.view.FragmentModel;
import com.bestarmedia.libcommon.model.vod.LightItem;
import com.bestarmedia.libcommon.model.vod.NodeRoomV4;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.FileUtil;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.LogFileUtil;
import com.bestarmedia.libcommon.util.LogRecorder;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.UsbFileUtil;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.anim.AnimatorUtils;
import com.bestarmedia.libwidget.floatview.FloatingMagnetView;
import com.bestarmedia.libwidget.floatview.FloatingView;
import com.bestarmedia.libwidget.floatview.MagnetViewListener;
import com.bestarmedia.libwidget.image.ScaleRecyclerImage;
import com.bestarmedia.libwidget.seekbar.PhasedListener;
import com.bestarmedia.libwidget.seekbar.PhasedSeekBar;
import com.bestarmedia.libwidget.seekbar.SimplePhasedAdapter;
import com.bestarmedia.libwidget.text.GradientBaseTextView;
import com.bestarmedia.libwidget.text.GradientTextView;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.bestarmedia.proto.device.DeviceProto;
import com.bestarmedia.proto.node.PayMessageRequest;
import com.bestarmedia.proto.node.RoomStatusBroadcast;
import com.bestarmedia.proto.node.Text;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.proto.vod.MobileUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Main extends BaseActivity implements View.OnClickListener, PhasedListener, InitHelper.OnInitListener, OnKaraokePlayerListener {

    private final static String TAG = "Main";
    private final SparseArray<MainPresentation> mActivePresentations = new SparseArray<>();
    private View mActRoot, contentTop, contentLeft, contentBottom, ll_bottom;
    private WidgetRoomNameLogo wLogo;
    private PhasedSeekBar phasedSeekBar;
    private FragmentManager fragmentManager;
    private LinkedList<FragmentModel> mFragments = new LinkedList<>();
    private SimplePhasedAdapter mAdtLeftTab;
    private FloatView floatMinor;
    private RelativeLayout mWidgetMinor;
    private ArcProgress voiceProgress;
    private GradientTextView tvOriginalAccompany, tvPausePlay, tvChoose, tvService, tvWiFi, tvMute;
    private GradientBaseTextView tvPlayInfo, tvRecord, tvScore;
    private ScaleRecyclerImage ivBack, ivHome;
    private ProgressBar playerProgress;
    private DisplayManager mDisplayManager;
    private MainPresentation mPresentation;
    private MarqueePlayer marqueePlayer;
    private boolean mIsOpenMinor;
    private DlgDeviceStore mDlgDeviceStore;
    private int mLogoHits = 0;
    private DlgMngPwd mDlgMngPwd;
    private DlgFireAlarm dlgFireAlarm;
    private ADModel mCurScreenAd;
    private DlgAdScreen mDlgAdScreen;
    private WidgetAdBannerPlayer mWidgetAdBannerPlayer;
    private BaseDialog qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().register(this);
        UIUtils.hideNavibar(this, true);
        setContentView(R.layout.act_main);
        permissions();
        //初始化Application
        VodApplication vodApplication = ((VodApplication) getApplication());
        vodApplication.init();
        vodApplication.addActivity(this);
        registerUsbReceiver();
        registerNetworkReceiver();
        //打开密钥权限
        FileUtil.chmod777File(KaraokeSdHelper.getSongSecurityKeyFile());
        //刪除日志文件
        LogFileUtil.deleteFiles();
        initView();
        init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            phasedSeekBar.setPosition(0);
            back2Main(0);
            KaraokeController.getInstance().init();
        } catch (Exception e) {
            Log.w(TAG, "侧边栏setPosition异常", e);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (VodApplication.getKaraokeController() != null) {
            VodApplication.getKaraokeController().release();
        }
        wLogo.stopPlayer();
        marqueePlayer.stopPlayer();
        floatMinor.removeFromWindow();
        unbindVodService();
        stopBeacon();
        unregisterUsbReceiver();
        unregisterNetworkReceiver();
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            return;
        }
        super.onBackPressed();
        try {
            mFragments.remove(mFragments.size() - 1);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FragmentModel fragment = mFragments.get(mFragments.size() - 1);
            transaction.show(fragment.fragment).commit();
            setBarsVisible(fragment);
        } catch (Exception e) {
            Log.e(TAG, "onBackPressed ex:" + e.toString());
        }
        if (!checkMyStackBackStack()) {//出现重影了
            Log.e(TAG, "出现重影了 >>>>>>>>>>>>>>>>> ");
            back2Main(phasedSeekBar.getCurrentItem());
        }
    }

    private void initView() {
        mActRoot = findViewById(R.id.root);
        contentTop = findViewById(R.id.top);
        contentLeft = findViewById(R.id.left);
        contentBottom = findViewById(R.id.bottom);
        wLogo = findViewById(R.id.iv_logo);
        tvService = findViewById(R.id.tv_service);
        tvRecord = findViewById(R.id.tv_record);
        tvWiFi = findViewById(R.id.tv_wifi);
        phasedSeekBar = findViewById(R.id.psb_tab);
        voiceProgress = findViewById(R.id.voice_progress);
        tvOriginalAccompany = findViewById(R.id.tv_acc);
        tvPausePlay = findViewById(R.id.tv_pause);
        tvMute = findViewById(R.id.iv_mute);
        tvScore = findViewById(R.id.tv_score);
        ivBack = findViewById(R.id.iv_back);
        ivHome = findViewById(R.id.iv_home);
        tvChoose = findViewById(R.id.tv_choose);
        marqueePlayer = findViewById(R.id.tv_marquee);
        tvPlayInfo = findViewById(R.id.tv_play_info);
        tvPlayInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //打开网页
                DlgWebView dlgWebView = new DlgWebView(Main.this, "http://192.168.1.245:8086/lottery/#/?session=123123");
                dlgWebView.show();
//                DlgInputUrl dlgInputUrl = new DlgInputUrl(Main.this);
//                dlgInputUrl.show();
//                JsbridgeWebFragment jsbridgeWebFragment=new JsbridgeWebFragment();
//                jsbridgeWebFragment.show(getSupportFragmentManager(),"webview");
                return false;
            }
        });
        tvPlayInfo.getBackground().setAlpha(25);
        tvPlayInfo.setSelected(true);
        playerProgress = findViewById(R.id.player_progress);
        mWidgetAdBannerPlayer = findViewById(R.id.wm_ad_banner);
        ll_bottom = findViewById(R.id.bottom_tools);
        RelativeLayout bottomTools = findViewById(R.id.ll_buttom_tools);
        bottomTools.getBackground().setAlpha(50);
        wLogo.setOnClickListener(this);
        wLogo.setOnLongClickListener(view -> {
            PopVersionInfo versionInfo = new PopVersionInfo(Main.this);
            versionInfo.show();
            return false;
        });
        GradientTextView tvNext = findViewById(R.id.tv_next);
        tvNext.setOnTouchEvent(new GradientTextView.OnGradientTouchEvent() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvChoose.postDelayed(runNextPressedTimer, 5000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        tvChoose.removeCallbacks(runNextPressedTimer);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPressedEffect(boolean pressed) {
                Log.d(TAG, "onPressedEffect >>>>>>>>>>>> " + pressed);
            }
        });
    }

    private Runnable runNextPressedTimer = () ->
            AdLocalBenefitVideo.getInstance(getApplicationContext()).setPlayLocalOnly(true);

    private void init() {
        fragmentManager = getSupportFragmentManager();
        setLeftTabDrawable(getLeftTabDrawables());
        mActRoot.postDelayed(() -> {
            phasedSeekBar.setPosition(0);
            back2Main(0);
            KaraokeController.getInstance().init();
        },5000);
        phasedSeekBar.setListener(this);
        mDisplayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        VodApplication.getKaraokeController().setOnKaraokePlayerListener(this);
        InitHelper.getInstance().setOnInitListener(this);
        InitHelper.getInstance().start();
    }

    @Override
    public void initStart() {
        Log.d(TAG, "初始化开始....");
    }

    @Override
    public void initProgress(int step, String msg) {
        Log.d(TAG, "初始化 step:" + step + " msg:" + msg);
        switch (step) {
            case 1:
                break;
            case 2:
                runOnUiThread(() -> {
                    new UpdateHelper(Main.this).checkUpdate();//检查版本更新
                    checkDeviceStore();//检测U盘入库
                    InitHelper.getInstance().initAfterNetAvail();
                });
                break;
            case 3:
                EventBusUtil.postSticky(EventBusId.Id.AFTER_CONFIG, "");
                break;
            case 11:
                //安全检查通过，关掉对话框,并通知检查付费
                closeSafetyDialog();
                EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, "");
                break;
        }
    }

    @Override
    public void initCompletion() {
        Log.d(TAG, "初始化完成！");
    }

    private void initAfterRoomInfo(String roomName) {
        wLogo.loadData(VodConfigData.getInstance().getTouchScreenLogo(), roomName);
        wLogo.startPlayer();
        try {
            phasedSeekBar.setPosition(0);
            back2Main(0);
            EventBusUtil.postSticky(EventBusId.Id.AFTER_ROOM_INFO, "");
        } catch (Exception e) {
            Log.w(TAG, "侧边栏setPosition异常", e);
        }
    }


    @Override
    public void onNodeRoomLoaded(NodeRoomV4 nodeRoomV4) {
        Log.d(TAG, "加载房间信息成功！");
        runOnUiThread(() -> {
            initAfterRoomInfo(NodeRoomInfo.getInstance().getRoomName());
            if (!VodConfigData.getInstance().getJwtMessage().deviceSerialNo.equalsIgnoreCase(DeviceUtil.getCupSerial())
                    || !VodConfigData.getInstance().getJwtMessage().ktvName.equalsIgnoreCase(VodConfigData.getInstance().getKtvName())
                    || !VodConfigData.getInstance().getJwtMessage().ktvNetCode.equalsIgnoreCase(VodConfigData.getInstance().getKtvNetCode())
                    || !VodConfigData.getInstance().getJwtMessage().ktvRoomCode.equalsIgnoreCase(nodeRoomV4.room.ktvRoomCode)
                    || !VodConfigData.getInstance().getJwtMessage().roomCode.equalsIgnoreCase(nodeRoomV4.room.roomCode)) {
                Logger.e(TAG, "jwt 发生改变,需要重新登录");
                LoginHelper.getInstance(this).getToken(VodConfigData.getInstance().getKtvNetCode(), DeviceUtil.getCupSerial());
            }
            VodApplication.getKaraokeController().init();
            fullViewMinor(false);
            startMainPlayer();
            marqueePlayer.startPlayer();
            bindVodService();
            startBeacon();
            //获取授权
            InitHelper.getInstance().getAuthor();
            Log.d(TAG, "通知检查付费");
//            EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, "");
        });
    }

    @Override
    public void onRoomSessionChanged(final String preSession, final String curSession) {
        Log.d(TAG, "房间状态发生变化 >>>>>>>>>>>>>>>>>>");
        runOnUiThread(() -> {
            if (TextUtils.isEmpty(curSession)) {//当前为关房
                Logger.d(TAG, "onRoomClose 房间已关闭 清理数据 >>>>>>>>>>>>>>>>>>>>>>>> ");
                if (!TextUtils.isEmpty(preSession)) {//从开房到关房
                    VodApplication.getKaraokeController().roomClosed();
                    if (dlgPay != null) {
                        dlgPay.dismiss();
                        dlgPay = null;
                    }
                }
            } else {//当前为开房
                if (TextUtils.isEmpty(preSession)) {//从关房到开房
                    Logger.d(TAG, "onRoomClose 房间已开房 >>>>>>>>>>>>>>>>>>>>>>>> ");
                    VodApplication.getKaraokeController().roomOpened();
                    if (VodConfigData.getInstance().isPayMode() && VodConfigData.getInstance().isSafetyPass()) {
                        EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, "");
                    }
                }
            }
        });
    }

    @Override
    public void onAuthor(Author author) {
        if (author.authStatus != 1) {
            marqueePlayer.playPushMsg(author.authDesc, 3, author.authColor);
            TextMessageRequest textMessageRequest = TextMessageRequest.getDefaultInstance().toBuilder()
                    .setText(Text.getDefaultInstance().toBuilder().setSource(0).setContent(author.authDesc)
                            .setPlayCount(3).setColor(author.authColor).build()).build();
            EventBusUtil.postSticky(EventBusId.PresentationId.PLAY_NOTIFICATION_TEXT, textMessageRequest);
        }
    }

    @Override
    public void onPlayInfo(String playing, String next) {
        runOnUiThread(() -> {
            String playingInfo = getString(R.string.playing_x, playing) + "      "
                    + (TextUtils.isEmpty(next) ? "" : (getString(R.string.next_x, next)));
            tvPlayInfo.setText(playingInfo);
            if (mPresentation != null) {
                mPresentation.setCurrentNext(getString(R.string.current_x, playing),
                        TextUtils.isEmpty(next) ? "" : (getString(R.string.next_x, next)));
            }
        });
    }

//    private boolean progressChangedFromUser = false;
//    private float targetProgressFloat = 0;
//
//    @Override
//    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//        if (fromUser) {
//            Log.d(TAG, "onProgressChanged progress:" + progress + " progressFloat:" + progressFloat + " fromUser");
//            progressChangedFromUser = true;
//            this.targetProgressFloat = progressFloat;
//        }
//    }
//
//    @Override
//    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//        Log.d(TAG, "getProgressOnFinally progress:" + progress + " progressFloat:" + progressFloat);
//        if (progressChangedFromUser && this.targetProgressFloat > 0) {
//             VodApplication.getKaraokeController().seekTo(targetProgressFloat / bubbleSeekBar.getMax());
//            progressChangedFromUser = false;
//            targetProgressFloat = 0;
//        }
//    }
//
//    @Override
//    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//        if (fromUser) {
//            Log.d(TAG, "getProgressOnFinally progress:" + progress + " progressFloat:" + progressFloat + " fromUser");
//        }
//    }

    @Override
    public void onPlayProgress(long duration, long current) {
        runOnUiThread(() -> {
            try {
//                float progress = (playerProgress.getMax() * current) / duration;
                playerProgress.setMax((int) duration);
                playerProgress.setProgress((int) current);
            } catch (Exception e) {
                Log.e(TAG, "更新进度条出错了", e);
            }
        });
    }

    public void addFragment(FragmentModel fragment) {
        if (mFragments != null && mFragments.size() > 0 && mFragments.get(mFragments.size() - 1).tag.equals(fragment.tag)) {
            return;
        }
        if (fragment.isCleanStacks) {
            cleanFragmentStacks();
            mFragments.clear();
            System.gc();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = mFragments.size() - 1; i > -1; i--) {
            FragmentModel fgm = mFragments.get(i);
            if (fragment.tag.equals(fgm.tag)) {
                transaction.remove(fgm.fragment);
                mFragments.remove(i);
            } else {
                transaction.hide(fgm.fragment);
            }
        }
        transaction.commit();
        fragmentManager.beginTransaction().add(R.id.contentPanel, fragment.fragment, fragment.tag).addToBackStack("main_frag").commit();
        Log.d(TAG, "addFragment tag:" + fragment.tag);
        mFragments.add(fragment);
        setBarsVisible(fragment);
    }

    private void setBarsVisible(FragmentModel fragment) {
        contentTop.setVisibility(fragment.isHideTopBar ? View.GONE : View.VISIBLE);
        marqueePlayer.setVisibility(fragment.isHideTopBar ? View.INVISIBLE : View.VISIBLE);
        contentLeft.setVisibility(fragment.isHideLeftBar ? View.GONE : View.VISIBLE);
        contentBottom.setVisibility(fragment.isHideBottomBar ? View.GONE : View.VISIBLE);
        ll_bottom.setVisibility(fragment.isHideBottomBar ? View.GONE : View.VISIBLE);
        ivBack.setVisibility(mFragments.size() > 1 ? View.VISIBLE : View.GONE);
        ivHome.setVisibility(fragment.tag.equals(FmSong.class.getName()) ? View.GONE : View.VISIBLE);
        if (fragment.isHideLeftBar) {
            mWidgetAdBannerPlayer.stopPlay();
            mWidgetAdBannerPlayer.setVisibility(View.GONE);
        } else {
            mWidgetAdBannerPlayer.startPlay();
            mWidgetAdBannerPlayer.setVisibility(View.VISIBLE);
            mWidgetAdBannerPlayer.loadAd();
        }
        hideMinor(fragment.isHideBottomBar || fragment.isHideTopBar);
        try {
            if (ivBack != null) {
                if (!ivBack.isShown()) {
                    checkBnsActivity();
                } else {
                    setBnsActivityHide(true);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "显示/隐藏 活动logo出错了", e);
        }
    }

    private void hideMinor(boolean hide) {
        try {
            if (hide) {
                floatMinor.updateFloatViewPosition(0, 0, 4, 3);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(4, 3);
                mWidgetMinor.setLayoutParams(params);
            } else {
                fullViewMinor(false);
            }
        } catch (Exception e) {
            Log.d(TAG, "隐藏幻影异常：" + e.toString());
        }
    }

    private void cleanFragmentStacks() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    private boolean checkMyStackBackStack() {
        try {
            return getSupportFragmentManager().getBackStackEntryCount() == mFragments.size();
        } catch (Exception e) {
            Log.d(TAG, "checkMyStackBackStack e:" + e.toString());
        }
        return false;
    }

    private void back2Main(int position) {
        switch (position) {
            case 0:
                addFragment(new FragmentModel(new FmSong(), true, false, false, false));
                break;
            case 1:
                addFragment(new FragmentModel(new FmMood(), true, false, false, false));
                break;
            case 2:
                addFragment(new FragmentModel(new FmPlay(), true, false, false, false));
                break;
            case 3:
                addFragment(new FragmentModel(new FmService(), true, false, false, false));
                break;
//            case 4:
//                addFragment(new FragmentModel(new FmNearby(), true, false, false, false));
//                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_logo:
                if (mLogoHits == 0) {
                    mActRoot.postDelayed(() -> mLogoHits = 0, 3000);
                }
                mLogoHits++;
                if (mLogoHits >= 5 && (mDlgMngPwd == null || !mDlgMngPwd.isShowing())) {
                    mDlgMngPwd = new DlgMngPwd(this);
                    mDlgMngPwd.setOnMngPwdListener(() ->
                            FragmentUtil.addFragment(new FmSetting(), false, true, true, true)
                    );
                    mDlgMngPwd.show();
                }

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_home:
                phasedSeekBar.setPosition(0);
                back2Main(0);
                break;
            case R.id.tv_next:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().next(true);
                }
                break;
            case R.id.tv_acc:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().originalAccompany();
                }
                break;
            case R.id.tv_pause:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().pauseStart();
                }
                break;
            case R.id.voice_sub:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().volumeDown();
                }
                break;
            case R.id.voice_plus:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().volumeUp();
                }
                break;
            case R.id.iv_mute:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().muteCancelMute();
                }
                break;
            case R.id.tv_replay:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().replay();
                }
                break;
            case R.id.tv_tune:
                DlgTune dlgTune = new DlgTune(this);
                dlgTune.show();
                break;
            case R.id.tv_score:
                if (!VodConfigData.getInstance().isOpenScore()) {
                    PromptDialogSmall dialog = new PromptDialogSmall(this);
                    dialog.setMessage(R.string.score_no_open_tip);
                    dialog.show();
                    tvScore.setSelected(VodApplication.getKaraokeController().getButtonStatus().scoreMode > 0);
                } else {
                    int scoreMode = VodApplication.getKaraokeController().getButtonStatus().scoreMode;
                    if (scoreMode == 0) {
                        DlgScoreSwitcher dlgScoreSwitcher = new DlgScoreSwitcher(this);
                        dlgScoreSwitcher.setOnDismissListener(dialog ->
                                tvScore.setSelected(VodApplication.getKaraokeController().getButtonStatus().scoreMode > 0));
                        dlgScoreSwitcher.show();
                    } else {
                        VodApplication.getKaraokeController().setScoreMode(0);
                        tvScore.setSelected(false);
                    }
                }
                break;
            case R.id.tv_choose:
                addFragment(FmChooseList.newInstance(0), false, true, false);
                break;
            case R.id.iv_phone:
                qrCode = new DlgRoomQrCode(this);
                qrCode.show();
                break;
            case R.id.tv_record:
                VodApplication.getKaraokeController().setRecord(VodApplication.getKaraokeController().getButtonStatus().isRecord == 1 ? 0 : 1);
                break;
            case R.id.tv_light:
                DlgLightController controller = new DlgLightController(this);
                controller.setOnLightControllerListener(new DlgLightController.OnLightControllerListener() {
                    @Override
                    public void onLightAuto(int isAuto) {
                        VodApplication.getKaraokeController().setAutoLight(isAuto);
                    }

                    @Override
                    public void onLightModeSelect(LightItem lightItem) {
                        VodApplication.getKaraokeController().sendLightCode(lightItem.outCode, lightItem.name);
                    }

                    @Override
                    public void onLightUp(int isUp) {
                        VodApplication.getKaraokeController().lightBrightness(isUp, true);
                    }

                    @Override
                    public void onHdmiBack(int isBack) {
                        VodApplication.getKaraokeController().setTelevision(isBack, true);
                    }
                });
                controller.show();
                break;
            case R.id.tv_atmo:
                DlgAtmosphere dlgAtmosphere = new DlgAtmosphere(this);
                dlgAtmosphere.show();
                break;
            case R.id.tv_service:
                if (VodApplication.getKaraokeController().getButtonStatus().serviceMode >= 0) {
                    VodApplication.getKaraokeController().setServiceMode(-1, true);
                } else {
                    DlgCallService dlgCallService = new DlgCallService(this);
                    dlgCallService.show();
                }
                break;
            case R.id.iv_search:
                FragmentUtil.addFragment(new FmSearch(), false, false, true, false);
                break;
            case R.id.tv_wifi:
                DlgWifi dlgWifi = new DlgWifi(this);
                dlgWifi.setData(VodConfigData.getInstance().getProfileDetailV4().nodeProfileDetail.configuration.wifiList);
                dlgWifi.show();
                break;
            default:
                break;
        }
    }


    @Subscribe
    public void onEventMainThread(BusEvent event) {
        try {
            switch (event.id) {
                case EventBusId.Id.CHOOSE_SONG_CHANGED:
                    tvChoose.setText(Integer.valueOf(event.data.toString()) < 10 ? "0" + event.data.toString() : event.data.toString());
                    break;
                case EventBusId.Id.BACK_FRAGMENT:
                    onBackPressed();
                    break;
                case EventBusId.Id.ADD_FRAGMENT:
                    addFragment((FragmentModel) event.data);
                    break;
                case EventBusId.Id.BUTTON_STATUS_CHANGED:
                    ButtonStatus status = (ButtonStatus) event.data;
                    updateButtonStatus(status);
                    break;
                case EventBusId.SongOperationId.SONG_SELECT:
                    SongOperation songOperation = (SongOperation) event.data;
                    if (songOperation.view != null) {
                        AnimatorUtils.playParabolaAnimator((ViewGroup) mActRoot, songOperation.view, tvChoose, songOperation.song.songName, R.drawable.selector_song_item);
                    }
                    break;
                case EventBusId.SongOperationId.SONG_PREVIEW:
                    songOperation = (SongOperation) event.data;
                    PreviewDialog dialog = new PreviewDialog(this, songOperation.song, songOperation.chooseId);
                    dialog.show();
                    break;
                case EventBusId.SongOperationId.SONG_COLLECT:
                    songOperation = (SongOperation) event.data;
                    SelectCollectDialog collectDialog = new SelectCollectDialog(this, songOperation.song);
                    collectDialog.show();
                    break;
                case EventBusId.PresentationId.SYSTEM_VOL_CHANGED:
                    runOnUiThread(() -> voiceProgress.setProgress(Integer.valueOf(event.data.toString())));
                    break;
                case EventBusId.Id.KTV_ROOM_CODE_CHANGED:
                    initAfterRoomInfo(NodeRoomInfo.getInstance().getRoomName());
                    Logger.e(TAG, "jwt 发生改变,需要重新登录");
                    LoginHelper.getInstance(this).getToken(VodConfigData.getInstance().getKtvNetCode(), DeviceUtil.getCupSerial());
                    break;
                case EventBusId.PresentationId.MAIN_PLAYER_STOP:
                    Log.d(TAG, "停止播放 >>>>>>>>>>>>>>>>>>>>> ");
                    stopMainPlayer();
                    break;
                case EventBusId.PresentationId.MAIN_PLAYER_RESUME:
                    Log.d(TAG, "恢复播放 >>>>>>>>>>>>>>>>>>>>> ");
                    stopMainPlayer();
                    startMainPlayer();
                    break;
                case EventBusId.PhoneId.MOBILE_MESSAGE:
                    if (VodApplication.getKaraokeController() != null && DeviceHelper.isMainVod(getApplicationContext())) {//主屏幕才处理手机点歌业务
                        runOnUiThread(() -> MobileMessageHandler.getInstance().handle(VodApplication.getKaraokeController(), (MobileMessageBroadcast) event.data));
                    }
                    break;
                case EventBusId.PhoneId.MOBILE_LOGIN:
                    MobileUser mobileUser = (MobileUser) event.data;
                    if (VodApplication.getKaraokeController() != null) {
                        runOnUiThread(() ->
                                MobileMessageHandler.getInstance().handleMobileLogin(VodApplication.getKaraokeController(), mobileUser));
                    }
                    break;
                case EventBusId.Id.CURRENT_SCREEN_AD:
                    runOnUiThread(() -> {
                        mCurScreenAd = (ADModel) event.data;
                        if (mDlgAdScreen != null && mDlgAdScreen.isVisible() && mCurScreenAd != null && !TextUtils.isEmpty(mCurScreenAd.getAdContent())) {
                            String[] images = mCurScreenAd.getAdContent().split("\\|");
                            if (images.length > 1 && !TextUtils.isEmpty(images[1])) {
                                mDlgAdScreen.showAdScreen(mCurScreenAd);
                            }
                        }
                    });
                    break;
                case EventBusId.ImId.FIRE_ALARM:
                    runOnUiThread(() -> {
                        int fireOp = Integer.valueOf(event.data.toString());
                        if (fireOp == 1) {//启动火警
                            Log.e(TAG, "启动火警>>>>>>>>>>>>>>>>");
                            if (dlgFireAlarm == null) {
                                dlgFireAlarm = new DlgFireAlarm();
                                dlgFireAlarm.showNow(getSupportFragmentManager(), "dialogFireAlarm");
                            }
                        } else {//关闭
                            Log.i(TAG, "关闭火警>>>>>>>>>>>>>>>>");
                            if (dlgFireAlarm != null && dlgFireAlarm.isAdded()) {
                                dlgFireAlarm.dismiss();
                                dlgFireAlarm = null;
                            }
                        }
                    });
                    break;
                case EventBusId.Id.CHECK_SAFETY:
                    if (VodConfigData.getInstance().isNeedSafety()) {
                        InitHelper.getInstance().checkSafety();
                    } else {
                        Logger.d(TAG, "跳过消防检查");
                        closeSafetyDialog();
                    }
                    break;
                case EventBusId.ImId.PAY_NOTIFIY://支付推送
                    PayMessageRequest payMsg = (PayMessageRequest) event.data;
                    if (VodConfigData.getInstance().getRoomCode().equals(payMsg.getRoomCode())) {
                        EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, "");
                    }
                    break;
                case EventBusId.Id.CHECK_PAY:
                    if (VodConfigData.getInstance().isPayMode() && NodeRoomInfo.getInstance().isRoomOpen() && VodConfigData.getInstance().isSafetyPass()) {
                        PayApiHelper payApiHelper = new PayApiHelper(getApplicationContext());
                        payApiHelper.setPayApiListener(payInfo ->
                                runOnUiThread(() -> {
                                    if (payInfo.isPay == 0 && (dlgRoomSafety == null || !dlgRoomSafety.isShowing()) && (dlgStoreSafety == null || !dlgStoreSafety.isShowing())) {
                                        if (dlgPay == null) {
                                            dlgPay = new DlgPay();
                                            dlgPay.setPayInfo(payInfo);
                                            dlgPay.showNow(getSupportFragmentManager(), "dialogPay");
                                        } else {
                                            dlgPay.setPayInfo(payInfo);
                                        }
                                    } else {
                                        if (dlgPay != null) {
                                            dlgPay.dismiss();
                                            dlgPay = null;
                                        }
                                    }
                                }));
                        payApiHelper.checkPay(VodConfigData.getInstance().getRoomCode(), NodeRoomInfo.getInstance().getRoomSession());
                    } else {
                        if (dlgPay != null) {
                            runOnUiThread(() -> {
                                if (dlgPay != null) {
                                    dlgPay.dismiss();
                                    dlgPay = null;
                                }
                            });
                        }
                    }
                    break;
                case EventBusId.DeviceId.OPERATION:
                    if (VodApplication.getKaraokeController() != null) {
                        runOnUiThread(() -> DeviceMessageHandler.getInstance().handleOperation(VodApplication.getKaraokeController(), (DeviceProto.Operation) event.data));
                    } else {
                        Log.w(TAG, "副屏操作：karaokeController is null ");
                    }
                    break;
                case EventBusId.DeviceId.GAME_START:
                    DeviceProto.GameStart gameStart = (DeviceProto.GameStart) event.data;
                    runOnUiThread(() -> FragmentUtil.addFragment(FmGame.newInstance(gameStart.getLevel(), gameStart.getPage(), gameStart.getPosition()),
                            false, true, true, false));
                    break;
                case EventBusId.ImId.VOD_NETTY_CONNECTION:
                    int isConnected = Integer.valueOf(event.data.toString());
                    if (isConnected == 1) {//已连接
                        VodCommunicateHelper.sendBoxAuth(getApplicationContext(), mPresentation != null);
                    } else {//已断开
                        Log.e(TAG, "即时通讯长连接已断开！");
                    }
                    break;
                case EventBusId.Id.SKIN_CHANGED:
//                        loadLogo(VodConfigData.getInstance().getTouchScreenLogo());
                    setLeftTabDrawable(getLeftTabDrawables());
                    voiceProgress.skinChanged(true);
                    break;
                case EventBusId.ImId.ROOM_STATUS_CHANGED:
                    RoomStatusBroadcast room = (RoomStatusBroadcast) event.data;
                    if (VodConfigData.getInstance().getRoomCode().equalsIgnoreCase(room.getRoomCode())) {
                        //延迟1秒再获取房间信息，避免服务端修改数据前获取数据
                        tvChoose.postDelayed(() ->
                                InitHelper.getInstance().getRoomDetail(), 1000);
                    }
                    break;
                case EventBusId.Id.LOGIN_SUCCEED:
                    //登录后，获取房间信息
                    if (InitHelper.getInstance() != null) {
                        InitHelper.getInstance().getDeviceInfo();
                    }
                    break;
                case EventBusId.Id.GET_DEVICES_FAIL:
                    PrefData.setJWT(Main.this, "");
                    //弹窗提示需要注册
                    PromptDialogSmall promptDialogSmall = new PromptDialogSmall(this);
                    promptDialogSmall.setTitle(R.string.setting_room_device_null);
                    promptDialogSmall.setMessage(R.string.setting_room_device_null_tips);
                    promptDialogSmall.setOkButton(true, "马上注册", view ->
                            FragmentUtil.addFragment(new FmSettingRoomRegist(), false, true, true, true)
                    );
                    promptDialogSmall.show();
                    break;
                case EventBusId.Id.SAFETY_ROOM_FAIL:
                    Logger.d(SafetyHelper.Tag, "房间检查失败");
                    if (dlgRoomSafety == null || !dlgRoomSafety.isShowing()) {
                        runOnUiThread(() -> {
                            dlgRoomSafety = new DlgRoomSafety(Main.this);
                            dlgRoomSafety.setCanceledOnTouchOutside(false);
                            dlgRoomSafety.setOnDismissListener(dialogRoom ->
                                    EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, ""));
                            dlgRoomSafety.setOkButton(true, getString(R.string.ok), () -> {
                                PromptDialogSmall tipDialog = new PromptDialogSmall(Main.this);
                                tipDialog.showIvClose(false);
                                tipDialog.setTitle(getString(R.string.dlg_safety_check_pass_title));
                                tipDialog.setMessage(getString(R.string.dlg_safety_check_pass_content));
                                tipDialog.setOkButton(true, getString(R.string.ok), view -> {
                                    if (dlgRoomSafety != null && dlgRoomSafety.isShowing()) {
                                        dlgRoomSafety.dismiss();
                                        dlgRoomSafety = null;
                                    }
                                    EventBusUtil.postSticky(EventBusId.Id.CHECK_SAFETY, "");
                                });
                                tipDialog.show();
                            });
                            dlgRoomSafety.show();
                            if (dlgPay != null) {
                                dlgPay.dismiss();
                                dlgPay = null;
                            }
                        });
                    }
                    break;
                case EventBusId.Id.SAFETY_STORE_FAIL:
                    Logger.d(SafetyHelper.Tag, "场所检查失败");
                    if (dlgStoreSafety == null || !dlgStoreSafety.isShowing()) {
                        dlgStoreSafety = new DlgStoreSafety(this);
                        dlgStoreSafety.setCanceledOnTouchOutside(false);
                        dlgStoreSafety.setOnDismissListener(dialogStore ->
                                EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, ""));
                        dlgStoreSafety.show();
                        if (dlgPay != null) {
                            dlgPay.dismiss();
                            dlgPay = null;
                        }
                    }
                    break;
                case EventBusId.ImId.BUSINESS_STATUS_CHANGED:
                    Logger.d(SafetyHelper.Tag, "场所检查通过");
                    EventBusUtil.postSticky(EventBusId.Id.CHECK_SAFETY, "");
                    break;
                case EventBusId.ImId.BATTLE_START://启动斗歌
                    Log.i(TAG, "启动斗歌 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
                    break;
                case EventBusId.PresentationId.PLAY_NOTIFICATION_TEXT:
                    TextMessageRequest textMessageRequest = (TextMessageRequest) event.data;
                    if (textMessageRequest.getText() != null && textMessageRequest.getText().getSource() != 1) {//走马灯推送
                        marqueePlayer.playPushMsg(textMessageRequest.getText().getContent(), textMessageRequest.getText().getPlayCount(), textMessageRequest.getText().getColor());
                    }
                    break;
                case EventBusId.Id.CHECK_BNS_ACTIVITY:
                    new BnsActivityHelper(getApplicationContext()).check();
                    break;
                case EventBusId.Id.BNS_ACTIVITY_CHANGED:
                    Log.d(TAG, "活动变化产生变化了 >>>>>>>>>>> ");
                    if (ivBack != null && !ivBack.isShown())
                        checkBnsActivity();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "处理event bus id :" + event.id + " 消息出错了", e);
        }
    }


    private void updateButtonStatus(ButtonStatus status) {
        runOnUiThread(() -> {
            if (status.isOriginal == 1) {
                Drawable dOriginalOn = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_original_on");
                if (dOriginalOn != null) {
                    tvOriginalAccompany.setCompoundDrawablesWithIntrinsicBounds(dOriginalOn, null, null, null);
                } else {
                    tvOriginalAccompany.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selector_main_original_on, 0, 0, 0);
                }
                tvOriginalAccompany.setText(R.string.accompany);
            } else {
                Drawable dOriginalOff = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_original_off");
                if (dOriginalOff != null) {
                    tvOriginalAccompany.setCompoundDrawablesWithIntrinsicBounds(dOriginalOff, null, null, null);
                } else {
                    tvOriginalAccompany.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selector_main_original_off, 0, 0, 0);
                }
                tvOriginalAccompany.setText(R.string.original);
            }
            if (status.isPause == 1) {
                Drawable dPlay = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_play");
                if (dPlay != null) {
                    tvPausePlay.setCompoundDrawablesWithIntrinsicBounds(null, dPlay, null, null);
                } else {
                    tvPausePlay.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_main_play, 0, 0);
                }
                tvPausePlay.setText(R.string.play);
            } else {
                Drawable dPause = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_pause");
                if (dPause != null) {
                    tvPausePlay.setCompoundDrawablesWithIntrinsicBounds(null, dPause, null, null);
                } else {
                    tvPausePlay.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_main_pause, 0, 0);
                }
                tvPausePlay.setText(R.string.pause);
            }
            if (status.isMute == 1) {
                Drawable dMuteCancel = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_mute_cancel");
                if (dMuteCancel != null) {
                    tvMute.setCompoundDrawablesWithIntrinsicBounds(null, dMuteCancel, null, null);
                } else {
                    tvMute.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_main_mute_cancel, 0, 0);
                }
                tvMute.setText(R.string.muting);
            } else {
                Drawable dMute = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_main_mute");
                if (dMute != null) {
                    tvMute.setCompoundDrawablesWithIntrinsicBounds(null, dMute, null, null);
                } else {
                    tvMute.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_main_mute, 0, 0);
                }
                tvMute.setText(R.string.mute);
            }
            tvScore.setSelected(status.scoreMode > 0);
            tvRecord.setSelected(status.isRecord > 0);
            tvService.setSelected(status.serviceMode >= 0);
            voiceProgress.setProgress(status.currentVolume);

            tvScore.setVisibility(VodConfigData.getInstance().isOpenScore() ? View.VISIBLE : View.INVISIBLE);
            tvWiFi.setVisibility(VodConfigData.getInstance().getProfileDetailV4().nodeProfileDetail.configuration.wifiList != null
                    && VodConfigData.getInstance().getProfileDetailV4().nodeProfileDetail.configuration.wifiList.size() > 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void setLeftTabDrawable(Drawable[] drawables) {
        if (mAdtLeftTab == null) {
            mAdtLeftTab = new SimplePhasedAdapter(drawables);
        } else {
            mAdtLeftTab.setDrawables(getLeftTabDrawables());
        }
        phasedSeekBar.setAdapter(mAdtLeftTab);
        phasedSeekBar.invalidate();
    }

    private Drawable[] getLeftTabDrawables() {
        Drawable dSong = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_sidebar_song_v");
        Drawable dList = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_sidebar_list_v");
        Drawable dPlay = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_sidebar_play_v");
        Drawable dService = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_sidebar_service_v");

        if (dSong == null) {
            dSong = getDrawable(R.drawable.selector_sidebar_song_v);
        }
        if (dList == null) {
            dList = getDrawable(R.drawable.selector_sidebar_list_v);
        }
        if (dPlay == null) {
            dPlay = getDrawable(R.drawable.selector_sidebar_play_v);
        }
        if (dService == null) {
            dService = getDrawable(R.drawable.selector_sidebar_service_v);
        }
        return new Drawable[]{dSong, dList, dPlay, dService};
    }

    @Override
    public void onPositionSelected(int position) {
        Log.i(TAG, "onPositionSelected position:" + position);
        back2Main(position);
    }

    /**
     * 全屏幻影
     */
    private void fullViewMinor(boolean show) {
        if (DeviceHelper.isMainVod(getApplicationContext())) {
            if (mWidgetMinor != null && DeviceHelper.isMainVod(getApplicationContext())) {
                if (show) {
                    int[] widthHeight = DensityUtil.getScreenWidthHeight(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthHeight[0], widthHeight[1]);
                    mWidgetMinor.setPadding(0, 0, 0, 0);
                    mWidgetMinor.setLayoutParams(params);
                    View child = mWidgetMinor.getChildAt(0);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
                    layoutParams.width = widthHeight[0];
                    layoutParams.height = 10 * widthHeight[0] / 16;//16:10(稍微加高一点，竖屏上下空白太大不好看)
                    child.setLayoutParams(layoutParams);
                    floatMinor.updateFloatViewPosition(0, 0, widthHeight[0], widthHeight[1]);
                    mIsOpenMinor = true;
                } else {
                    floatMinor.updateFloatViewPosition(getResources().getDimensionPixelSize(R.dimen.minor_x),
                            getResources().getDimensionPixelSize(R.dimen.minor_y),
                            DeviceHelper.isMainVod(getApplicationContext()) ? getResources().getDimensionPixelSize(R.dimen.minor_width) : 1,
                            DeviceHelper.isMainVod(getApplicationContext()) ? getResources().getDimensionPixelSize(R.dimen.minor_height) : 1);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceHelper.isMainVod(getApplicationContext()) ? getResources().getDimensionPixelSize(R.dimen.minor_width) : 1,
                            DeviceHelper.isMainVod(getApplicationContext()) ? getResources().getDimensionPixelSize(R.dimen.minor_height) : 1);
                    int padding = DensityUtil.dip2px(getApplicationContext(), 2);
                    mWidgetMinor.setPadding(padding, padding, padding, padding);
                    params.setMargins(getResources().getDimensionPixelSize(R.dimen.minor_margin_left), 0, 0, getResources().getDimensionPixelSize(R.dimen.minor_margin_bottom));
                    mWidgetMinor.setLayoutParams(params);
                    View child = mWidgetMinor.getChildAt(0);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
                    layoutParams.width = params.width;
                    layoutParams.height = params.height;
                    child.setLayoutParams(layoutParams);
                    mIsOpenMinor = false;
                }
            }
        }
    }

    private void startMainPlayer() {
        if (DeviceHelper.isMainVod(getApplicationContext())) {
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (Display display : displays) {
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        Point outSize = new Point();
                        display.getSize(outSize);
                        Point realSize = new Point();
                        display.getRealSize(realSize);
                        showPresentation(display);
                        break;
                    }
                }
                mDisplayManager.registerDisplayListener(mDisplayListener, null);
                runOnUiThread(() -> {
                    if (mPresentation != null) {
                        mWidgetMinor = (RelativeLayout) View.inflate(this, R.layout.layout_minor_merge_video_texture, null);
                        floatMinor = new FloatView(this, 0, 400, mWidgetMinor);
                        floatMinor.setIsAllowTouch(true);
                        floatMinor.setFloatViewClickListener(() -> fullViewMinor(!mIsOpenMinor));
                        floatMinor.addToWindow();
                        fullViewMinor(false);
                        VodApplication.getKaraokeController().initPlayer(mPresentation.getVideoTextureView(), mWidgetMinor.findViewById(R.id.merge_video_texture));
                        VodCommunicateHelper.sendBoxAuth(getApplicationContext(), true);
                    } else {
                        mWidgetMinor = (RelativeLayout) View.inflate(this, R.layout.layout_minor_video_texture, null);
                        floatMinor = new FloatView(this, 0, 400, mWidgetMinor);
                        floatMinor.setIsAllowTouch(true);
                        floatMinor.setFloatViewClickListener(() -> fullViewMinor(!mIsOpenMinor));
                        floatMinor.addToWindow();
                        fullViewMinor(false);
                        VodApplication.getKaraokeController().initPlayer(mWidgetMinor.findViewById(R.id.video_texture), null);
                        VodCommunicateHelper.sendBoxAuth(getApplicationContext(), false);
                    }
                });
            }
        }
    }


    private void stopMainPlayer() {
        if (VodApplication.getKaraokeController() != null) {
            VodApplication.getKaraokeController().releasePlayer();
        }
        if (floatMinor != null) {
            floatMinor.removeFromWindow();
            floatMinor = null;
        }
        mWidgetMinor = null;
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
        mActivePresentations.clear();
    }

    private void showPresentation(Display display) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }
        try {
            mPresentation = new MainPresentation(getApplicationContext(), display);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (mPresentation.getWindow() != null)
                    mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                if (mPresentation.getWindow() != null)
                    mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            mPresentation.show();
            mActivePresentations.put(displayId, mPresentation);
        } catch (Exception e) {
            Log.e(TAG, "Presentation投放到HDMI异常：" + e.toString());
            LogRecorder.addHdmiLog(getApplicationContext(), "Presentation show ex:" + e.toString());
            requestDrawOverLays();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    protected boolean requestDrawOverLays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 11100);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11100 && requestDrawOverLays()) {
            //允许悬浮窗
            startMainPlayer();
        }
    }

    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int displayId) {
            Toast.makeText(getApplicationContext(), "HDMI 已连接", Toast.LENGTH_SHORT).show();
            stopMainPlayer();
            startMainPlayer();
        }

        public void onDisplayChanged(int displayId) {
        }

        public void onDisplayRemoved(int displayId) {
            Toast.makeText(getApplicationContext(), "HDMI 已断开", Toast.LENGTH_SHORT).show();
            LogRecorder.addHdmiLog(getApplicationContext(), "HDMI 已断开");
            stopMainPlayer();
            startMainPlayer();
        }
    };


    private void checkDeviceStore() {
        boolean isStore = UsbFileUtil.isUsbExitBoxCode();
        Logger.d(TAG, "checkDeviceStore ==== " + isStore);
        if (isStore) {
            if (mDlgDeviceStore == null || !mDlgDeviceStore.isShowing()) {
                mDlgDeviceStore = new DlgDeviceStore(this, UsbFileUtil.readKBoxCode());
                mDlgDeviceStore.show();
            }
        } else {
            if (mDlgDeviceStore != null && mDlgDeviceStore.isShowing()) {
                mDlgDeviceStore.dismiss();
            }
        }
    }

    private void bindVodService() {
        Intent intent = new Intent(this, VodService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void unbindVodService() {
        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "ServiceConnection onServiceConnected ComponentName:" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ServiceConnection ComponentName ComponentName:" + name);
        }

    };

    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//表明sd对象是存在并具有读/写权限
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);//SDCard已卸掉,如果SDCard是存在但没有被安装
        filter.addDataScheme("file"); // 必须要有此行，否则无法收到广播
        registerReceiver(usbStateReceiver, filter);
    }

    private final BroadcastReceiver usbStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                Logger.d(TAG, "usbStateReceiver action:" + action);
                if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                    checkDeviceStore();
                } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                    checkDeviceStore();
                    Uri uri = intent.getData();
                    String path = uri != null ? uri.getPath() : "无法检测到新插入USB设备路径";
                    Log.e(TAG, "usb path===" + path + "  ");
                }
            } catch (Exception e) {
                Log.e(TAG, "USB状态处理异常", e);
            }

        }
    };

    private void startBeacon() {
        if (DeviceHelper.isMainVod(getApplicationContext())) {
            setFilters();  // Start listening notifications from UsbService
            startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
        }
    }

    private void stopBeacon() {
        if (DeviceHelper.isMainVod(getApplicationContext())) {
            unregisterReceiver(mUsbReceiver);
            unbindService(usbConnection);
        }
    }

    private void unregisterUsbReceiver() {
        unregisterReceiver(usbStateReceiver);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_READY);
        filter.addAction(UsbService.ACTION_USB_OPENING);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            UsbService usbService = ((UsbService.UsbBinder) arg1).getService();
            BnsBeaconHelper bnsBeaconHelper = new BnsBeaconHelper(usbService);
            VodApplication.getKaraokeController().setBnsBeaconHelper(bnsBeaconHelper);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(getApplicationContext(), "onServiceDisconnected", Toast.LENGTH_SHORT).show();
            VodApplication.getKaraokeController().setBnsBeaconHelper(null);
            BnsBeaconHelper.setHaveBeacon(getApplicationContext(), false);
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null && intent.getAction() != null)
                    switch (intent.getAction()) {
                        case UsbService.ACTION_USB_OPENING: // USB PERMISSION GRANTED
                            Logger.d(TAG, "USB Beacon ACTION_USB_OPENING");
                            break;
                        case UsbService.ACTION_USB_READY:
                            Logger.d(TAG, "USB Beacon ACTION_USB_READY");
                            break;
                        case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                            Logger.d(TAG, "USB Permission not granted");
                            break;
                        case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                            Logger.d(TAG, "No USB connected");
                            break;
                        case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                            Logger.d(TAG, "USB disconnected");
                            break;
                        case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                            Toast.makeText(context, "Beacon设备不支持", Toast.LENGTH_SHORT).show();
                            Logger.d(TAG, "USB device not supported");
                            break;
                        default:
                            break;
                    }
            } catch (Exception e) {
                Log.e(TAG, "USB口插入出错了", e);
            }
        }
    };

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = UsbService.getIntent(Main.this);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onTvQrCodeShow() {
        VodApplication.getKaraokeController().showTelevisionQrCode();
    }


    @Override
    public void onEnterScreenAd() {
        String[] images;
        if (!mIsOpenMinor && (mDlgAdScreen == null || !mDlgAdScreen.isVisible()) && mCurScreenAd != null && !TextUtils.isEmpty(mCurScreenAd.getAdContent())
                && (images = mCurScreenAd.getAdContent().split("\\|")).length > 1 && !TextUtils.isEmpty(images[1])) {
            runOnUiThread(() -> {
                mDlgAdScreen = new DlgAdScreen();
                mDlgAdScreen.setOnDismissListener(adScreenDismissListener);
                mDlgAdScreen.showAdScreen(mCurScreenAd);
                mDlgAdScreen.show(getSupportFragmentManager(), "dialogAdScreen");
                hideMinor(true);
            });
        }
        super.onEnterScreenAd();
    }

    private DialogInterface.OnDismissListener adScreenDismissListener = dialog -> {
        hideMinor(false);
        mDlgAdScreen = null;
    };

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    private List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSIONS_REQUEST = 2;

    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < KtvPermissionConfig.ktvPermissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, KtvPermissionConfig.ktvPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(KtvPermissionConfig.ktvPermissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                Toast.makeText(this, "已经授权", Toast.LENGTH_LONG).show();
            } else {//请求权限方法
                String[] permissions = mPermissionList.toArray(new String[0]);//将List转为数组
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(getApplicationContext(), "权限未申请", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Manifest.permission.ACCESS_NETWORK_STATE);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    private void unregisterNetworkReceiver() {
        unregisterReceiver(networkReceiver);
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                EventBusUtil.postSticky(EventBusId.Id.NETWORK_CHANGE, NetWorkUtils.isNetworkAvailable(getApplicationContext()));
            } catch (Exception e) {
                Log.e(TAG, "监听网络状态出错了", e);
            }
        }
    };

    private void checkBnsActivity() {
        if (BnsActivityCache.getInstance().isReloadActivityImage()) {
            loadBnsActivity();
        } else {
            setBnsActivityHide(false);
        }
    }

    private void loadBnsActivity() {
        if (BnsActivityCache.getInstance().getActivities() != null && BnsActivityCache.getInstance().getActivities().size() > 0) {
            for (int i = 0; i < BnsActivityCache.getInstance().getActivities().size(); i++) {
                int key = BnsActivityCache.getInstance().getActivities().keyAt(i);
                BnsActivityBean activityBean = BnsActivityCache.getInstance().getActivities().get(key);
                //test
                activityBean.image = "http://mt.beidousat.com/image/d7e3dac2faf6125bdd8c4757a9c0fc78.gif";
                FloatingView.get().add(activityBean.id, activityBean.imageWidth, activityBean.imageHeight);
                if (activityBean.image.endsWith(".gif")) {
                    Log.d(TAG, "当前活动图片 gif>>>>>>>>>>> " + activityBean.image);
                    GlideUtils.loadGif(this, activityBean.image, FloatingView.get().getView(activityBean.id).getImageView());
                } else {
                    Log.d(TAG, "当前活动图片 >>>>>>>>>>> " + activityBean.image);
                    Glide.with(this).load(activityBean.image).error(R.drawable.logo).into(FloatingView.get().getView(activityBean.id).getImageView());
                }
                FloatingView.get().listener(activityBean.id, new MagnetViewListener() {
                    @Override
                    public void onRemove(int key, FloatingMagnetView magnetView) {
                        Toast.makeText(getApplicationContext(), "我没了:" + key, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClick(int key, FloatingMagnetView magnetView) {
                        BnsActivityBean bean = BnsActivityCache.getInstance().getBnsActivity(key);
                        Toast.makeText(getApplicationContext(), "点到我了:" + bean.activityUrl, Toast.LENGTH_SHORT).show();
                    }
                });
                FloatingView.get().movie(activityBean.id, activityBean.verticalX, activityBean.verticalY);
            }
            BnsActivityCache.getInstance().setReloadActivityImage(false);
        } else {
            FloatingView.get().removeAll();
            Log.d(TAG, "当前无活动 >>>>>>>>>>> ");
        }
    }

    private void setBnsActivityHide(boolean hide) {
        FloatingView.get().hideShowAll(hide);
    }
}
