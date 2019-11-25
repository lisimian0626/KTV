package com.beidousat.karaoke.ui.dialog.play;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.im.DeviceCommunicateHelper;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.presentation.GamePresentation;
import com.beidousat.karaoke.widget.viewpager.WidgetGameSongPager;
import com.beidousat.score.OnScoreListener;
import com.bestarmedia.libcommon.data.GameStatus;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.SongDetailHelper;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.CloudSongDownloadListener;
import com.bestarmedia.libcommon.model.dto.PresentationCenterIcon;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.RandomUtil;
import com.bestarmedia.libwidget.anim.LeafLoading;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.player.KaraokeTexturePlayer;
import com.bestarmedia.proto.device.DeviceProto;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

public class DlgGameLevel extends BaseDialog implements OnScoreListener, View.OnClickListener, HttpRequestListener, OnBasePlayerListener, OnPageScrollListener, CloudSongDownloadListener {

    private final String TAG = DlgGameLevel.class.getSimpleName();
    private Context mContext;
    private RecyclerImageView ivPause, ivGameStart;
    private boolean isGamePlaying, isPause = false;
    private TextView mTvRules, tvPages;
    private RecyclerImageView ivPre, ivNext;
    private WidgetGameSongPager mPager;
    private LeafLoading mLeafLoading;
    private RecyclerImageView mIvService;
    private int mTotalPage;
    private int mCurPage = 0;
    private Map<String, String> mRequestParam;
    private int mLevel;
    private GameStatus mGameStatus;
    private GamePresentation mPresentation;
    private KaraokeTexturePlayer player;
    private int mAudioChannelFlag;
    private final static int GAME_TIME = 120 * 1000;
    private int mCurScorePercent;
    private final static int GAME_LEVEL1 = 60;
    private final static int GAME_LEVEL2 = 65;
    private final static int GAME_LEVEL3 = 75;
    private final static int GAME_LEVEL4 = 90;
    private int[] mTurnOrder;
    private int mCurPageSelected;
    private int mSelectedSongPs;
    private String mPlayingUrl;
    private final SparseArray<GamePresentation> mActivePresentations = new SparseArray<>();
    private PromptDialogSmall mDialogDownload, mPromptDialog, mDlgExit;
    private int mCurrentItem, mSelectedPs;
    private Song mSong;
    private int mProgress;

    public DlgGameLevel(Context context, int level, int page, int position) {
        super(context, R.style.MyDialog);
        mLevel = level;
        mCurPageSelected = page;
        mSelectedSongPs = position;
        mContext = context;
        mGameStatus = GameStatus.getInstance();
        initView();
        setLevel(mLevel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEven(BusEvent event) {
        switch (event.id) {
            case EventBusId.ImId.MAIN_GAME_STATUS:
                DeviceProto.GameStatus gameStatus = (DeviceProto.GameStatus) event.data;
                switch (gameStatus.getPlayStatus()) {
                    case 0:
                        isGamePlaying = false;
                        break;
                    case 1:
                    case 2:
                        isGamePlaying = true;
                        break;
                    case 3:
                        isGamePlaying = false;
                        tvPages.post(() -> {
                            mPager.setCheckable(true);
                            mPager.setScrollable(true);
                            ivPre.setEnabled(true);
                            ivNext.setEnabled(true);
                            ivGameStart.setImageResource(R.drawable.selector_game_start);
                        });
                        break;
                    case 4:
                        closeGame();
                        break;
                }
                isPause = gameStatus.getIsPause() == 1;
                refreshButton();
                break;
            case EventBusId.DeviceId.GAME_OPERATION:
                DeviceProto.GameOperation operation = (DeviceProto.GameOperation) event.data;
                if (operation.getOp() == 1) {
                    pausePlay();
                } else if (operation.getOp() == 2) {
                    closeGame();
                }
                break;
            case EventBusId.PresentationId.SERVICE_STATUS:
                int mode = Integer.valueOf(event.data.toString());
                mIvService.setSelected(mode >= 0);
                break;
            case EventBusId.PresentationId.FIRE_ACTION://火警警报
                int fireStatus = Integer.valueOf(event.data.toString());
                if (player != null) {
                    if (fireStatus == 1) {
                        player.pause();
                    } else {
                        player.start(true);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void refreshButton() {
        if (!isGamePlaying) {
            ivGameStart.setImageResource(R.drawable.selector_game_start);
            mPager.setCheckable(true);
            mPager.setScrollable(true);
            ivPre.setEnabled(true);
            ivNext.setEnabled(true);
        } else {
            ivGameStart.setImageResource(R.drawable.selector_game_exit);
            mPager.setCheckable(false);
            mPager.setScrollable(false);
            ivPre.setEnabled(false);
            ivNext.setEnabled(false);
        }
        ivPause.setImageResource(isPause ? R.drawable.selector_game_play : R.drawable.selector_game_pause);
        mIvService.setSelected(VodApplication.getKaraokeController().getButtonStatus().serviceMode >= 0);
    }


    private void setCurPage(int curPage) {
        mCurPageSelected = curPage;
    }

    private void setSelectSongPs(int selectSongPs) {
        mSelectedSongPs = selectSongPs;
    }

    private void setLevel(int level) {
        mLevel = level;
        ivGameStart.post(() -> {
            switch (mLevel) {
                case 1:
                    mTvRules.setBackgroundResource(R.drawable.play_game_rules_level1);
                    break;
                case 2:
                    mTvRules.setBackgroundResource(R.drawable.play_game_rules_level2);
                    break;
                case 3:
                    mTvRules.setBackgroundResource(R.drawable.play_game_rules_level3);
                    break;
                case 4:
                    mTvRules.setBackgroundResource(R.drawable.play_game_rules_level4);
                    break;
                default:
                    break;
            }
            requestSongs(mLevel);
        });
    }

    private void initView() {
        this.setContentView(R.layout.dlg_game_level);
        if (getWindow() != null) {
            LayoutParams lp = getWindow().getAttributes();
            lp.width = 850;
            lp.height = 620;
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0.7f;
            getWindow().setAttributes(lp);
        }
        setCanceledOnTouchOutside(false);
        mPager = findViewById(R.id.pager);
        mPager.setOnPagerScrollListener(this);
        tvPages = findViewById(R.id.tv_pages);
        ivPre = findViewById(R.id.iv_pre);
        ivPre.setOnClickListener(this);
        ivNext = findViewById(R.id.iv_next);
        ivNext.setOnClickListener(this);
        mTvRules = findViewById(R.id.tv_level_rules);
//        mTvGift = findViewById(R.id.tv_gift);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_mic_down).setOnClickListener(this);
        findViewById(R.id.iv_mic_up).setOnClickListener(this);
        findViewById(R.id.iv_music_down).setOnClickListener(this);
        findViewById(R.id.iv_music_up).setOnClickListener(this);
//        sbVolumeContent = findViewById(R.id.ll_volume);
        mIvService = findViewById(R.id.iv_service);
        mIvService.setOnClickListener(this);
        mLeafLoading = findViewById(R.id.lf_loading);
        ivPause = findViewById(R.id.iv_pause);
        ivPause.setOnClickListener(this);
        ivGameStart = findViewById(R.id.btn_start);
        ivGameStart.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void closeGame() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
            mActivePresentations.clear();
        }
        closeDialog();
    }


    @Override
    public void dismiss() {
        if (isGamePlaying) {
            if (mDlgExit == null || !mDlgExit.isShowing()) {
                mDlgExit = new PromptDialogSmall(mContext);
                mDlgExit.setTitle(R.string.prompt);
                mDlgExit.setMessage(R.string.exit_game_tip);
                mDlgExit.setOkButton(true, mContext.getString(R.string.exit), v -> exitGame());
                mDlgExit.show();
            }
        } else {
            if (DeviceHelper.isMainVod(mContext)) {
                DeviceCommunicateHelper.broadcastGameStatus(mGameStatus.getPassLevel(), 4, isPause ? 1 : 0, mLevel);
                closeGame();
            } else {
                DeviceCommunicateHelper.sendGameOperation2Main(2, 0);
                closeDialog();
            }
        }
    }

    public void exitGame() {
        if (DeviceHelper.isMainVod(mContext)) {
            DeviceCommunicateHelper.broadcastGameStatus(mGameStatus.getPassLevel(), 4, isPause ? 1 : 0, mLevel);
            if (mPresentation != null) {
                mPresentation.dismiss();
                mPresentation = null;
            }
            isGamePlaying = false;
            ivGameStart.setImageResource(R.drawable.selector_game_start);
            mPager.setCheckable(!isGamePlaying);
            mPager.setScrollable(!isGamePlaying);
            ivPre.setEnabled(!isGamePlaying);
            ivNext.setEnabled(!isGamePlaying);
            relasePlayer();
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
        } else {
            DeviceCommunicateHelper.sendGameOperation2Main(2, 0);
        }
        closeDialog();
    }

    private void requestSongs(int level) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.BREAKTHROUGH_SONG);
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        r.addParam("breakthrough_id", String.valueOf(level));
        mRequestParam = r.getParams();
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }

    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

    public void loadAndStart(int level, int curPage, int selectSongPs) {
        setCurPage(curPage);
        setSelectSongPs(selectSongPs);
        setLevel(level);
    }

    private void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mTotalPage = totalPage;
        mCurPage = 0;
        tvPages.setText((mCurPage + 1) + "/" + mTotalPage);
        mPager.initPager(mTotalPage, firstPageSong, params);
        mPager.setCurSelectedSong(0);
        if (mCurPageSelected >= 0 && mSelectedSongPs >= 0) {
            mPager.setCurrentItem(mCurPageSelected);
            mPager.setCurSelectedSong(mSelectedSongPs);
            if (DeviceHelper.isMainVod(mContext)) {
                tvPages.postDelayed(this::startGame, 2000);
            } else {
                ivGameStart.setVisibility(View.GONE);
                mLeafLoading.setVisibility(View.VISIBLE);
                startLoading();
                ivGameStart.setImageResource(R.drawable.selector_game_exit);
                mPager.setCheckable(false);
                mPager.setScrollable(false);
                ivNext.setEnabled(false);
                ivPre.setEnabled(false);
                isGamePlaying = true;
                tvPages.postDelayed(() -> {
                    ivGameStart.setVisibility(View.VISIBLE);
                    mLeafLoading.setVisibility(View.GONE);
                }, 2000);
            }
        }
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mCurPage = position;
        tvPages.setText((mCurPage + 1) + "/" + mTotalPage);
        mPager.cleanSelected();
        mPager.setCurSelectedSong(0);
    }

    @Override
    public void onPageScrollRight() {

    }

    @Override
    public void onPageScrollLeft() {

    }

    @Override
    public void onFailed(String method, Object object) {
//        if (RequestMethod.TO_DOWNLOAD.equalsIgnoreCase(method)) {
//            mSong.downloadStatus = 3;
//        }
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.BREAKTHROUGH_SONG.equalsIgnoreCase(method)) {
            if (object instanceof SongSimplePage) {
                SongSimplePage songSimplePage = (SongSimplePage) object;
                if (songSimplePage.song != null && songSimplePage.song.data != null && songSimplePage.song.data.size() > 0) {
                    initSongPager(songSimplePage.song.last_page, songSimplePage.song.data, mRequestParam);
                }
            }
        }

    }

    @Override
    public void onStart(String method) {

    }

    private void init() {
        if (player == null) {
            DisplayManager mDisplayManager = (DisplayManager) mContext.getSystemService(Context.DISPLAY_SERVICE);
            assert mDisplayManager != null;
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (Display display : displays) {
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        showPresentation(display);
                        break;
                    }
                }
                initPlayer();
            } else {
                Logger.d(TAG, " Display[] is null");
            }
        }
    }

    private final OnDismissListener mOnDismissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (mPresentation != null) {
                mPresentation.dismiss();
                mPresentation = null;
            }
            relasePlayer();
            mActivePresentations.clear();
        }
    };

    private void showPresentation(final Display display) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }
        Logger.d(TAG, " Display display.getName():" + display.getName() + "  show");
        mPresentation = new GamePresentation(mContext, display);
        if (mPresentation.getWindow() != null) {
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mPresentation.show();
        mPresentation.setOnDismissListener(mOnDismissListener);
        mActivePresentations.put(displayId, mPresentation);
    }

    private void closeDialog() {
        CloudSongDownloadHelper.getInstance(mContext).removeCloudSongDownloadListener(this);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    private void initPlayer() {
        if (mPresentation != null) {
            if (player == null) {
                player = new KaraokeTexturePlayer();
                player.initPlayer(mContext, mPresentation.getVideoTextureView(), null);
                player.setBasePlayerListener(this);
                player.setOnScoreListener(this);
                player.setScoreMode(2);
            }
        } else {
            PromptDialogSmall dialog = new PromptDialogSmall(mContext);
            dialog.setMessage(R.string.game_no_tv_tip);
            dialog.setOnDismissListener(dialogInterface -> exitGame());
            dialog.show();
        }
    }

    private void playSong(SongSimple song) {
        SongDetailHelper detailHelper = new SongDetailHelper(mContext, song.id);
        detailHelper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
            @Override
            public void onSongDetail(final Song song) {
                EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
                mAudioChannelFlag = song.audioTrack;
                float vol = song.volume > 0 ? ((float) song.volume / 100) : 0.8f;
                mPlayingUrl = !TextUtils.isEmpty(song.mediaUrl) ? song.mediaUrl : song.mediaFilePath;
                if (player != null) {
                    initVol(vol);
                    player.volOff();
                    player.playMedia(mPlayingUrl, null);
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        detailHelper.getSongDetail();
    }


    private void relasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private boolean startGame() {
        final SongSimple song = mPager.getCurSelectedSong();
        if (song != null) {
            mTurnOrder = RandomUtil.randomArray(0, 3, 4);
            ivGameStart.setVisibility(View.GONE);
            mLeafLoading.setVisibility(View.VISIBLE);
            startLoading();
            ivGameStart.setImageResource(R.drawable.selector_game_exit);
            mPager.setCheckable(false);
            mPager.setScrollable(false);
            ivNext.setEnabled(false);
            ivPre.setEnabled(false);
            isGamePlaying = true;
            init();
            mPager.postDelayed(() -> playSong(song), 50);
            return true;
        } else {
            PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
            promptDialog.setTitle(R.string.prompt);
            promptDialog.setMessage(R.string.pls_select_game_song);
            promptDialog.show();
            return false;
        }
    }

    private void pausePlay() {
        if (DeviceHelper.isMainVod(mContext)) {
            if (player != null) {
                if (isPause) {
                    player.start(true);
                    ivPause.setImageResource(R.drawable.selector_game_pause);
                    EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_CENTER_ICON,
                            new PresentationCenterIcon(R.drawable.tv_play, mContext.getString(R.string.play), true));
                } else {
                    player.pause();
                    ivPause.setImageResource(R.drawable.selector_game_play);
                    EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_CENTER_ICON,
                            new PresentationCenterIcon(R.drawable.tv_pause, mContext.getString(R.string.pause), false));
                }
                isPause = !isPause;
                sendGameButtonStatus();
            }
        } else {
            DeviceCommunicateHelper.sendGameOperation2Main(1, 0);
        }
    }

    private void tipMessage(int resId) {
        try {
            if (mPromptDialog == null || !mPromptDialog.isShowing()) {
                mPromptDialog = new PromptDialogSmall(mContext);
                mPromptDialog.setTitle(R.string.prompt);
                mPromptDialog.setMessage(resId);
                mPromptDialog.show();
            }
        } catch (Exception e) {
            Logger.d(getClass().getSimpleName(), "tipMessage ex:" + e.toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_start:
                if (isGamePlaying) {
                    dismiss();
                } else {
                    if (!NodeRoomInfo.getInstance().canChooseSong()) {//未开房,只能操作服务
                        tipMessage(R.string.can_not_select_song);
                        return;
                    }
                    final int currentItem = mPager.getCurrentItem();
                    final int selectedPs = mPager.getSelectedPs();
                    final SongSimple song = mPager.getCurSelectedSong();
                    if (song != null) {
                        SongDetailHelper helper = new SongDetailHelper(mContext, song.id);
                        helper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
                            @Override
                            public void onSongDetail(final Song song) {
                                if (!TextUtils.isEmpty(song.mediaUrl)) {
                                    begin(song, currentItem, selectedPs);
                                } else {
                                    if (mDialogDownload == null || !mDialogDownload.isShowing()) {
                                        mDialogDownload = new PromptDialogSmall(mContext);
                                        mDialogDownload.setTitle(R.string.prompt);
                                        mDialogDownload.setMessage(R.string.cloud_song_download_tip);
                                        mDialogDownload.setOkButton(true, mContext.getString(R.string.download), view ->
                                                toDownload(song, currentItem, selectedPs));
                                        mDialogDownload.show();
                                    }
                                }
                            }

                            @Override
                            public void onFail(String error) {
                            }
                        });
                        helper.getSongDetail();
                    } else {
                        PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
                        promptDialog.setTitle(R.string.prompt);
                        promptDialog.setMessage(R.string.pls_select_game_song);
                        promptDialog.show();
                    }
                }
                break;
            case R.id.iv_mic_down:
                VodApplication.getKaraokeController().micDown(true);
                break;
            case R.id.iv_mic_up:
                VodApplication.getKaraokeController().micUp(true);
                break;
            case R.id.iv_music_down:
                VodApplication.getKaraokeController().volumeDown();
                break;
            case R.id.iv_music_up:
                VodApplication.getKaraokeController().volumeUp();
                break;
            case R.id.iv_service:
                if (VodApplication.getKaraokeController().getButtonStatus().serviceMode >= 0) {
                    VodApplication.getKaraokeController().setServiceMode(-1, true);
                    mIvService.setSelected(false);
                } else {
                    VodApplication.getKaraokeController().setServiceMode(0, true);
                    mIvService.setSelected(true);
                }
                if (DeviceHelper.isMainVod(mContext))
                    sendGameButtonStatus();
                break;
            case R.id.iv_pause:
                pausePlay();
                break;

            case R.id.iv_pre:
                if (mCurPage > 0) {
                    mCurPage--;
                    mPager.setCurrentItem(mCurPage);
                }
                break;
            case R.id.iv_next:
                if (mCurPage < mTotalPage - 1) {
                    mCurPage++;
                    mPager.setCurrentItem(mCurPage);
                }
                break;
        }
    }

    private void sendGameButtonStatus() {
        DeviceCommunicateHelper.broadcastGameStatus(mGameStatus.getPassLevel(), isGamePlaying ? 2 : 0, isPause ? 1 : 0, mLevel);
    }

    @Override
    public void onScoreViewVisibility(boolean isShow) {

    }

    @Override
    public void onScoreCallback(int score) {
        mCurScorePercent = score;
        //test
//        mCurScorePercent = 81;
        Logger.d(TAG, "onScoreCallback :" + score);
    }

    private boolean checkPass() {
        if (mPresentation != null) {
            int turnedCount = mPresentation.getPassSize();
            if (mLevel == 1 && turnedCount >= 1) {
                return true;
            } else if (mLevel == 2 && turnedCount >= 2) {
                return true;
            } else if (mLevel == 3 && turnedCount >= 3) {
                return true;
            } else return mLevel == 4 && turnedCount >= 4;
        }
        return false;
    }

    private void gameOver() {
        if (player != null) {
            player.pause();
        }
        int tutorLast = mTurnOrder[3];
        int resId = 0;
        if (tutorLast == 0) {
            resId = R.drawable.tv_tutor_1_fail;
        } else if (tutorLast == 1) {
            resId = R.drawable.tv_tutor_2_fail;
        } else if (tutorLast == 2) {
            resId = R.drawable.tv_tutor_3_fail;
        } else if (tutorLast == 3) {
            resId = R.drawable.tv_tutor_4_fail;
        }
        mPresentation.showResult(true, false, resId, mLevel);
        isGamePlaying = false;
        ivGameStart.post(() -> {
            ivGameStart.setImageResource(R.drawable.selector_game_start);
            mPager.setCheckable(!isGamePlaying);
            mPager.setScrollable(!isGamePlaying);
            ivNext.setEnabled(!isGamePlaying);
            ivPre.setEnabled(!isGamePlaying);
        });
        if (DeviceHelper.isMainVod(mContext))
            DeviceCommunicateHelper.broadcastGameStatus(mGameStatus.getPassLevel(), 3, isPause ? 1 : 0, mLevel);
    }

    private void gameLevelPass() {
        if (player != null) {
            player.pause();
        }
        int lastTurnTutor = mPresentation.getLastTurnTutor();
        int resId = 0;
        if (lastTurnTutor == 0) {
            resId = R.drawable.tv_tutor_1_pass;
        } else if (lastTurnTutor == 1) {
            resId = R.drawable.tv_tutor_2_pass;
        } else if (lastTurnTutor == 2) {
            resId = R.drawable.tv_tutor_3_pass;
        } else if (lastTurnTutor == 3) {
            resId = R.drawable.tv_tutor_4_pass;
        }
        mPresentation.showResult(true, true, resId, mLevel);
        mGameStatus.setPassLevel(mLevel, lastTurnTutor);

        isGamePlaying = false;
        ivGameStart.post(() -> {
            mPager.setCheckable(!isGamePlaying);
            mPager.setScrollable(!isGamePlaying);
            ivPre.setEnabled(!isGamePlaying);
            ivNext.setEnabled(!isGamePlaying);
            ivGameStart.setImageResource(R.drawable.selector_game_start);
        });
        if (DeviceHelper.isMainVod(mContext))
            DeviceCommunicateHelper.broadcastGameStatus(mGameStatus.getPassLevel(), 3, isPause ? 1 : 0, mLevel);
    }

    private void initCurrentMode() {
        if (player != null) {
            player.volOn();
            mIvService.postDelayed(this::onAccom, 2000);
        }
    }

    private void onAccom() {
        if (player != null) {
            if (mAudioChannelFlag == 1) {//一轨原唱
                player.selectTrack(1);
            } else if (mAudioChannelFlag == 2) {//二轨原唱
                player.selectTrack(0);
            } else if (mAudioChannelFlag == 3) {//左原唱
                player.setVolChannel(2);
            } else if (mAudioChannelFlag == 4) {//右原唱
                player.setVolChannel(1);
            }
        }
    }

    private void initVol(float percent) {
        setMusicVol(percent);
    }

    private void setMusicVol(float progress) {
        Logger.i(TAG, "setMusicVol progress:" + progress);
        if (player != null)
            player.setVol(progress);
    }

    private void startLoading() {
        mProgress = 0;
        mLeafLoading.setProgress(mProgress);
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mProgress = mProgress + 5;
            mLeafLoading.setProgress(mProgress);
            return false;
        }
    });

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            if (mProgress < 100) {
                handler.postDelayed(this, 100);
            }
        }
    };


    private void begin(SongSimple song, int currentItem, int selectedPs) {
        if (isGamePlaying) {
            return;
        }
        if (DeviceHelper.isMainVod(mContext)) {
            if (startGame()) {
                DeviceCommunicateHelper.broadcastGameStart(mLevel, currentItem, selectedPs);
            }
        } else {
            if (song != null) {
                DeviceCommunicateHelper.sendGameStart2Main(mLevel, currentItem, selectedPs);
                ivGameStart.setVisibility(View.GONE);
                mLeafLoading.setVisibility(View.VISIBLE);
                startLoading();
                ivGameStart.setImageResource(R.drawable.selector_game_exit);
                mPager.setCheckable(false);
                mPager.setScrollable(false);
                ivNext.setEnabled(false);
                ivPre.setEnabled(false);
                isGamePlaying = true;
                tvPages.postDelayed(() -> {
                    ivGameStart.setVisibility(View.VISIBLE);
                    mLeafLoading.setVisibility(View.GONE);
                }, 2000);
            } else {
                PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
                promptDialog.setTitle(R.string.prompt);
                promptDialog.setMessage(R.string.pls_select_game_song);
                promptDialog.show();
            }
        }
    }

    @Override
    public void onSongDownloadStatusChanged(String songId, int status) {
        if (status == 1) {
            mSong.downloadStatus = 1;
            begin(mSong, mCurrentItem, mSelectedPs);
        }
    }

    @Override
    public void onSongDownloadChanged(Map<String, SongSimple> currentDownloadStatus) {

    }

    private void toDownload(Song song, int currentItem, int selectedPs) {
        ivGameStart.setVisibility(View.GONE);
        mLeafLoading.setVisibility(View.VISIBLE);
        ivGameStart.setImageResource(R.drawable.selector_game_exit);
        mPager.setCheckable(false);
        mPager.setScrollable(false);
        ivNext.setEnabled(false);
        ivPre.setEnabled(false);

        mSong = song;
        mSong.downloadStatus = 0;
        mCurrentItem = currentItem;
        mSelectedPs = selectedPs;
        CloudSongDownloadHelper.getInstance(mContext).addCloudSongDownloadListener(this);
        CloudSongDownloadHelper.getInstance(mContext).downloadSong(song);
    }

    @Override
    public void onPlayStart(String path) {
        ivGameStart.setVisibility(View.VISIBLE);
        mLeafLoading.setVisibility(View.GONE);
        mCurScorePercent = 0;
        if (mPresentation != null) {
            mPresentation.resetViews();
        }

        initCurrentMode();

    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {
        if (mPresentation != null) {
            long deltaTime = GAME_TIME - current;
            if (deltaTime < 0) {
                deltaTime = 0;
                if (!checkPass()) {
                    gameOver();
                }
            } else {
                if (current >= 50 * 1000) {
                    ivPause.post(() -> {
                        if (mCurScorePercent > GAME_LEVEL1 && mCurScorePercent < GAME_LEVEL2) {
                            mPresentation.turn(mTurnOrder[0]);
                        } else if (mCurScorePercent >= GAME_LEVEL2 && mCurScorePercent < GAME_LEVEL3) {
                            mPresentation.turn(mTurnOrder[0]);
                            mPresentation.turn(mTurnOrder[1]);
                        } else if (mCurScorePercent >= GAME_LEVEL3 && mCurScorePercent < GAME_LEVEL4) {
                            mPresentation.turn(mTurnOrder[0]);
                            mPresentation.turn(mTurnOrder[1]);
                            mPresentation.turn(mTurnOrder[2]);
                        } else if (mCurScorePercent >= GAME_LEVEL4) {
                            mPresentation.turn(mTurnOrder[0]);
                            mPresentation.turn(mTurnOrder[1]);
                            mPresentation.turn(mTurnOrder[2]);
                            mPresentation.turn(mTurnOrder[3]);
                        }
                        if (checkPass()) {
                            gameLevelPass();
                        }
                    });
                }
            }
            mPresentation.setTimeText(String.valueOf(deltaTime / 1000));
        }
    }

    @Override
    public void onPlayCompletion(String path) {
        if (checkPass()) {
            gameLevelPass();
        } else {
            gameOver();
        }
    }

    @Override
    public void onPlayError(String path, String error) {

    }
}
