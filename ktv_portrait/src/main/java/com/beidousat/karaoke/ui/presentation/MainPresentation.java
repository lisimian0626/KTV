package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.widget.MarqueePlayer;
import com.beidousat.karaoke.widget.WidgetAdCorner;
import com.beidousat.karaoke.widget.WidgetAnimImageText;
import com.beidousat.karaoke.widget.WidgetImageNotification;
import com.beidousat.karaoke.widget.WidgetMvImagePlayer;
import com.beidousat.karaoke.widget.WidgetRedEnvelopes;
import com.beidousat.karaoke.widget.WidgetRedEnvelopesSmall;
import com.beidousat.karaoke.widget.WidgetScoreResult;
import com.beidousat.karaoke.widget.WidgetTextNotification;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.dto.PresentationCenterIcon;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libwidget.anim.AnimatorUtils;
import com.bestarmedia.libwidget.danmaku.BarrageView;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.bestarmedia.player.DecodeTexturePlayer;
import com.bestarmedia.proto.node.BarrageRequest;
import com.bestarmedia.proto.node.Image;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.node.Text;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.Video;
import com.bestarmedia.proto.node.VideoMessageRequest;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.texture.VideoTextureView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import yanzhikai.textpath.PathView;
import yanzhikai.textpath.SyncTextPathView;

public class MainPresentation extends Presentation {

    private VideoTextureView videoTextureView;
    private RecyclerImageView rivBlackBackground, rivAdPause;
    private TextView tvCenter;
    private BubbleSeekBar sbVolume;
    private View sbVolumeContent;
    private MarqueePlayer marqueePlayer;
    private LinearLayout llStatic;
    private WidgetRedEnvelopes widgetRedEnvelopes;
    private WidgetRedEnvelopesSmall widgetRedEnvelopesSmall;
    private WidgetScoreResult widgetScoreResult;
    private WidgetAdCorner widgetAdCorner;
    private ConstraintLayout mRootView;
    private BarrageView barrageView;
    private WidgetMvImagePlayer widgetMvImagePlayer;
    private SyncTextPathView tvScore;
    private WidgetAnimImageText wLogo, wQrCode;

    private int mHdmiW;
    private int mHdmiH;
    private float videoScale = 1.0f;
    private DecodeTexturePlayer decodeTexturePlayer;
    private List<ImageChildView> imageChildViews = new ArrayList<>();
    private Animation mAnimCornerIn, mAnimCornerOut;
    private SVGAImageView animationView;
    private final static String TAG = "MainPresentation";

    public MainPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        mHdmiW = realSize.x;
        mHdmiH = realSize.y;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoScale = PrefData.getHdmiVideoScale2(getContext().getApplicationContext());
        mAnimCornerOut = AnimationUtils.loadAnimation(getContext(), R.anim.amin_corner_out);
        mAnimCornerIn = AnimationUtils.loadAnimation(getContext(), R.anim.amin_corner_in);
        setContentView(R.layout.main_presentation);
        mRootView = findViewById(R.id.root);
        llStatic = findViewById(R.id.ll_static);
        videoTextureView = findViewById(R.id.video_texture);
        rivBlackBackground = findViewById(R.id.riv_background);
        marqueePlayer = findViewById(R.id.roll_text_view);
        rivAdPause = findViewById(R.id.iv_ad_pause);
        widgetRedEnvelopes = findViewById(R.id.w_red_envelopes);
        widgetRedEnvelopesSmall = findViewById(R.id.w_red_envelopes_small);
        widgetScoreResult = findViewById(R.id.w_score_result);
        widgetRedEnvelopes.setRedEnvelopesListener(redEnvelopesListener);
        widgetAdCorner = findViewById(R.id.w_ad_corner);
        tvCenter = findViewById(R.id.tv_player_center);
        sbVolume = findViewById(R.id.sb_volume);
        sbVolumeContent = findViewById(R.id.ll_volume);
        wLogo = findViewById(R.id.w_logo);
        wQrCode = findViewById(R.id.w_qr_code);
        sbVolume.getConfigBuilder().min(0).max(15).progress(3).sectionCount(5).trackSize((int) (getScale() * 10))
                .trackColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray))
                .secondTrackColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light))
                .thumbColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray))
                .sectionTextSize((int) (getScale() * 38))
                .showThumbText()
                .thumbTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark))
                .thumbTextSize((int) (getScale() * 38))
                .bubbleColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark))
                .bubbleTextSize((int) (getScale() * 38)).showSectionMark().seekStepSection().touchToSeek()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();
        barrageView = findViewById(R.id.barrage);
        barrageView.initDanmaku();

        widgetMvImagePlayer = findViewById(R.id.w_mv_player);
        tvScore = findViewById(R.id.tv_score);
        tvScore.setRepeatStyle(PathView.NONE);
        tvScore.setFillColor(true);
        initScreenSize();
        InitSvgaView();
    }

    public VideoTextureView getVideoTextureView() {
        return videoTextureView;
    }

    private int getHdmiWidth() {
        if (videoScale < 1) {
            return (int) (mHdmiW - 2 * (mHdmiH * (1 - videoScale)));
        }
        return (int) (mHdmiW * videoScale);
    }

    private int getHdmiHeight() {
        if (videoScale < 1) {
            return (int) (mHdmiH - 2 * (mHdmiH * (1 - videoScale)));
        }
        return (int) (mHdmiH * videoScale);
    }


    private void InitSvgaView() {
        animationView = new SVGAImageView(getContext());
        RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relLayoutParams.addRule(Gravity.CENTER);
        mRootView.addView(animationView, relLayoutParams);
    }

    private void initScreenSize() {
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
        if (videoScale < 1.0f) {//缩小
            linearParams.height = mHdmiH;
            linearParams.width = mHdmiW;
            mRootView.setLayoutParams(linearParams);
            int paddingTB = (int) (mHdmiH * (1 - videoScale));
            int paddingLR = (int) (mHdmiW * (1 - videoScale));
            mRootView.setPadding(paddingLR, paddingTB, paddingLR, paddingTB);
        } else {//原始==1.0f
            linearParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            linearParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            mRootView.setLayoutParams(linearParams);
            mRootView.setPadding(0, 0, 0, 0);
        }
        resizeQrCode();
        Logger.d(TAG, "mRootView w" + mRootView.getWidth() + "  h:" + mRootView.getHeight());
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        marqueePlayer.postDelayed(() -> marqueePlayer.startPlayer()
                , 10 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        marqueePlayer.stopPlayer();
        stopViewsClearTimer();
        wLogo.removeCallbacks(runLogo);
    }

    private void setStaticInfo() {
        ButtonStatus status = VodApplication.getKaraokeController().getButtonStatus();
        llStatic.removeAllViews();
        int paddingRight = DensityUtil.dip2px(getContext(), 10);
        int wh = getContext().getResources().getDimensionPixelSize(R.dimen.television_logo_width);
        if (status.isMute == 1) {
            RecyclerImageView imageView = new RecyclerImageView(getContext());
            imageView.setImageResource(R.drawable.tv_muting);
            imageView.setPadding(0, 0, paddingRight, 0);
            llStatic.addView(imageView, wh + paddingRight, wh);
        }
        if (status.serviceMode >= 0) {
            RecyclerImageView imageView = new RecyclerImageView(getContext());
            imageView.setImageResource(R.drawable.tv_calling_service);
            imageView.setPadding(0, 0, paddingRight, 0);
            llStatic.addView(imageView, wh + paddingRight, wh);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        try {
            switch (event.id) {
                case EventBusId.Id.BUTTON_STATUS_CHANGED:
                    setStaticInfo();
                    break;
                case EventBusId.PresentationId.CLEAR:
                    clearChildViews();
                    break;
                case EventBusId.PresentationId.SHOW_STORE_LOGO_ON_TV://播放脚标广告
                    showStoreLogo(Integer.valueOf(event.data.toString()));
                    break;
                case EventBusId.PresentationId.PRESENTATION_SHOW_AD_CORNER://播放脚标广告
                    ADModel adModel = (ADModel) event.data;
                    showAdCorner(adModel);
                    break;
                case EventBusId.PresentationId.CURRENT_SCORE:
                    int score = Integer.valueOf(event.data.toString());
                    if (tvScore.getVisibility() == View.VISIBLE) {
                        tvScore.setText(String.valueOf(score < 10 ? ("0" + score) : score));
                        tvScore.startAnimation(0, 1);
                    }
                    break;
                case EventBusId.PresentationId.SCORE_VISIBILITY_STATUS:
                    tvScore.setVisibility(Integer.valueOf(event.data.toString()) == 1 ? View.VISIBLE : View.GONE);
                    break;
                case EventBusId.PresentationId.SHOW_SCORE_RESULT:
                    RoomSongItem item = (RoomSongItem) event.data;
                    if (item != null) {
                        showScoreResult(item);
                    }
                    break;
                case EventBusId.PlayerId.PLAYER_START://关闭暂停
                    rivAdPause.setVisibility(View.GONE);
                    break;
                case EventBusId.PresentationId.SHOW_AD_PAUSE:
                    showAdPause((ADModel) event.data);
                    break;
                case EventBusId.PresentationId.PLAY_RED_PACKET://红包广告
                    playRedPacket((ADModel) event.data);
                    break;
                case EventBusId.PresentationId.PLAY_MV_GALLERIES:
                    String galleries = event.data.toString();
                    if (!TextUtils.isEmpty(galleries)) {
                        setMvImage(galleries);
                    }
                    break;
                case EventBusId.PresentationId.SHOW_CENTER_ICON:
                    tipOperation((PresentationCenterIcon) event.data);
                    break;
                case EventBusId.PresentationId.SHOW_PHONE_QR_CODE:
                    requestQrCode();
                    break;
                case EventBusId.PresentationId.SYSTEM_VOL_CHANGED:
                    sbVolumeContent.post(() -> {
                        int currentVol = Integer.valueOf(event.data.toString());
                        sbVolumeContent.setVisibility(View.VISIBLE);
                        sbVolume.setProgress(currentVol);
                        sbVolumeContent.removeCallbacks(runnableVolDismiss);
                        sbVolumeContent.postDelayed(runnableVolDismiss, 5000);
                    });
                    break;
                case EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED:
                    videoScale = PrefData.getHdmiVideoScale2(getContext().getApplicationContext());
                    initScreenSize();
                    break;
                case EventBusId.PresentationId.PRESENTATION_BARRAGE:
                    MobileMessageBroadcast msg = (MobileMessageBroadcast) event.data;
                    int color = BarrageView.DEFAULT_TEXT_COLOR;
                    int direction = BarrageView.DEFAULT_DIRECTION;
                    try {
                        if (!TextUtils.isEmpty(msg.getText().getColor())) {
                            color = Color.parseColor(msg.getText().getColor());
                        }
                        if (msg.getText().getPlayDirection() > 0) {
                            direction = msg.getText().getPlayDirection();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "弹幕颜色解析出错了", e);
                    }
                    barrageView.playDanmanku(msg.getUser().getName(), msg.getUser().getAvatar(), msg.getText().getContent(),
                            color, direction, BarrageView.DEFAULT_TEXT_SIZE, BarrageView.DEFAULT_BACKGROUND_COLOR, BarrageView.DEFAULT_DURATION);
                    break;
                case EventBusId.PresentationId.PRESENTATION_BARRAGE_NEW:
                    BarrageRequest barrageRequest = (BarrageRequest) event.data;
                    int barrageColor = BarrageView.DEFAULT_TEXT_COLOR;
                    int barrageDirection = BarrageView.DEFAULT_DIRECTION;
                    barrageView.playDanmanku(barrageRequest.getName(), barrageRequest.getAvatar(), barrageRequest.getText(),
                            barrageColor, barrageDirection, BarrageView.DEFAULT_TEXT_SIZE, BarrageView.DEFAULT_BACKGROUND_COLOR, BarrageView.DEFAULT_DURATION);
                    break;
                case EventBusId.PresentationId.PLAY_NOTIFICATION_TEXT:
                    TextMessageRequest textMessageRequest = (TextMessageRequest) event.data;
                    if (textMessageRequest.getText() != null) {
                        playRollText(textMessageRequest.getText());
                    }
                    break;
                case EventBusId.PresentationId.PLAY_NOTIFICATION_IMAGE:
                    ImageMessageRequest imageMessageRequest = (ImageMessageRequest) event.data;
                    Image image = imageMessageRequest.getImage();
                    if (image != null) {
                        Text text = image.getText();
                        int playTime = 0;
                        if (text != null && !TextUtils.isEmpty(text.getContent())) {
                            playTime = playRollText(text);
                        }
                        playImage(image, playTime);
                    }
                    break;
                case EventBusId.PresentationId.PLAY_NOTIFICATION_VIDEO_SMALL:
                    VideoMessageRequest videoMessageRequest = (VideoMessageRequest) event.data;
                    if (videoMessageRequest.getVideo() != null && !TextUtils.isEmpty(videoMessageRequest.getVideo().getUrl())) {
                        playVideo(videoMessageRequest.getVideo());
                        if (null != videoMessageRequest.getVideo().getImage()) {
                            playImage(videoMessageRequest.getVideo().getImage(), 0);
                        }
                    } else {
                        Log.d(TAG, "小窗口视频内容为空 >>>>>>>>>>>>>>>>>>>>>> ");
                    }
                    break;
                case EventBusId.PresentationId.PLAY_EMOJI:    //播放内置表情
                    switch (Integer.valueOf(event.data.toString())) {
                        case 0:
                            playGif(R.raw.tv_emoji_applause);
                            break;
                        case 1:
                            playGif(R.raw.tv_emoji_cheers);
                            break;
                        case 2:
                            playGif(R.raw.tv_emoji_whooped);
                            break;
                        case 3:
                            playGif(R.raw.tv_emoji_unpleasant);
                            break;
                        case 4:
                            playGif(R.raw.tv_emoji_hooting);
                            break;
                        default:
                            break;
                    }
                    break;
                case EventBusId.PresentationId.PLAY_EMOJI_DYNAMIC:
                    String fileUrl = event.data.toString();
                    if (!TextUtils.isEmpty(fileUrl)) {
                        if (fileUrl.toLowerCase().endsWith(".gif")) {
                            playGif(fileUrl);
                        } else if (fileUrl.toLowerCase().endsWith(".svga")) {
                            playSvga(fileUrl);
                        }
                    }
                    break;
                case EventBusId.PresentationId.HDMI_BLACK_STATUS:
                    int blackStatus = Integer.valueOf(event.data.toString());
                    rivBlackBackground.setVisibility(blackStatus == 1 ? View.VISIBLE : View.GONE);
                    if (blackStatus == 1) {
                        marqueePlayer.stopPlayer();
                    } else {
                        marqueePlayer.startPlayer();
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "处理EventBus消息：" + event.id + "发送异常", e);
        }
    }

    public void setCurrentNext(String current, String next) {
        widgetAdCorner.setCurText(current);
        widgetAdCorner.setNextText(next);
    }

    private void clearChildViews() {
        showMvPlayer(false);
        tvCenter.removeCallbacks(runOperation);
        tvCenter.setVisibility(View.GONE);
        rivAdPause.setVisibility(View.GONE);
        widgetAdCorner.setVisibility(View.GONE);
        widgetScoreResult.setVisibility(View.GONE);
        widgetRedEnvelopes.setVisibility(View.GONE);
        widgetRedEnvelopesSmall.stopShake();
        widgetRedEnvelopesSmall.setVisibility(View.GONE);
        cleanDelayChildView();
    }

    private void showAdCorner(ADModel adModel) {
        widgetAdCorner.loadAd(adModel);
        widgetAdCorner.setVisibility(View.VISIBLE);
        widgetAdCorner.startAnimation(mAnimCornerIn);
        widgetAdCorner.removeCallbacks(runAdCorner);
        widgetAdCorner.postDelayed(runAdCorner, 15000);
    }

    private Runnable runAdCorner = new Runnable() {
        @Override
        public void run() {
            widgetAdCorner.startAnimation(mAnimCornerOut);
            widgetAdCorner.setVisibility(View.GONE);
        }
    };

    private void showAdPause(ADModel adModel) {
        tvCenter.setVisibility(View.GONE);
        GlideUtils.LoadImage(getContext(), adModel.getAdContent(), false, rivAdPause);
        rivAdPause.setVisibility(View.VISIBLE);
        new AdPlayRecorder(getContext().getApplicationContext()).recordAdPlay(adModel);
    }

    private void showScoreResult(RoomSongItem item) {
        widgetScoreResult.setVisibility(View.VISIBLE);
        widgetScoreResult.setScoreResult(item.simpName, item.score, item.winPercent);
    }

    private void tipOperation(PresentationCenterIcon icon) {
        tvCenter.post(() -> {
            tvCenter.setVisibility(View.VISIBLE);
            tvCenter.setCompoundDrawablesWithIntrinsicBounds(0, icon.iconId, 0, 0);
            if (TextUtils.isEmpty(icon.text)) {
                tvCenter.setText("");
            } else {
                tvCenter.setText(icon.text);
            }
        });
        tvCenter.removeCallbacks(runOperation);
        if (icon.autoDismiss) {
            tvCenter.postDelayed(runOperation, 2000);
        }
    }

    private Runnable runOperation = new Runnable() {
        @Override
        public void run() {
            tvCenter.setVisibility(View.GONE);
        }
    };

    private Runnable runnableVolDismiss = new Runnable() {
        @Override
        public void run() {
            sbVolumeContent.setVisibility(View.GONE);
        }
    };

    private void playRedPacket(ADModel adModel) {
        if (adModel.getBeacon() != null) {
            if (widgetRedEnvelopes.getVisibility() != View.VISIBLE) {
                widgetRedEnvelopes.setVisibility(View.VISIBLE);
                widgetRedEnvelopesSmall.setVisibility(View.INVISIBLE);
                widgetRedEnvelopesSmall.loadLogo(adModel);
                widgetRedEnvelopes.showRedEnvelopes(adModel);
                new AdPlayRecorder(getContext().getApplicationContext()).recordAdPlay(adModel);
            }
        }
    }

    private WidgetRedEnvelopes.RedEnvelopesListener redEnvelopesListener = new WidgetRedEnvelopes.RedEnvelopesListener() {
        @Override
        public void onRedEnvelopeDismiss() {
            widgetRedEnvelopes.setVisibility(View.GONE);
            AnimatorUtils.playParabolaAnimator(mRootView, widgetRedEnvelopesSmall, R.drawable.bg_red_envelopes);
            widgetRedEnvelopesSmall.postDelayed(runRedPacketShow, 900);
        }
    };
    private Runnable runRedPacketShow = new Runnable() {
        @Override
        public void run() {
            widgetRedEnvelopesSmall.setVisibility(View.VISIBLE);
            widgetRedEnvelopesSmall.startShake();
        }
    };


    private int[] getRandomStart() {
        Random random = new Random();
        return new int[]{random.nextInt(getHdmiWidth()), random.nextInt(getHdmiHeight())};
    }

    private void playGif(int resId) {
        RecyclerImageView recyclerImageView = new RecyclerImageView(getContext());
        recyclerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtils.loadLocalGif(getContext(), resId, recyclerImageView);
        ImageChildView childView = new ImageChildView(recyclerImageView, System.currentTimeMillis() + 5 * 1000, 1);
        int width = (int) (getScale() * 320);
        int height = (int) (getScale() * 320);

        mRootView.addView(recyclerImageView, width, height);
        imageChildViews.add(childView);
        AnimatorUtils.moveView2Target(recyclerImageView, getRandomStart(),
                new int[]{(getHdmiWidth() - width) / 2, (getHdmiHeight() - height) / 3});
        startViewsClearTimer();
    }

    private void playGif(String url) {
        RecyclerImageView recyclerImageView = new RecyclerImageView(getContext());
        recyclerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtils.loadGif(getContext(), url, recyclerImageView);
        ImageChildView childView = new ImageChildView(recyclerImageView, System.currentTimeMillis() + 5 * 1000, 1);
        int width = (int) (getScale() * 320);
        int height = (int) (getScale() * 320);

        mRootView.addView(recyclerImageView, width, height);
        imageChildViews.add(childView);
        AnimatorUtils.moveView2Target(recyclerImageView, getRandomStart(),
                new int[]{(getHdmiWidth() - width) / 2, (getHdmiHeight() - height) / 3});
        startViewsClearTimer();
    }

    private int playRollText(Text text) {
        if (text.getSource() == 1) {//来自监管系统
            int width = (int) (getScale() * (text.getRightX() - text.getLeftX()));
            int height = DensityUtil.dip2px(getContext(), 80);
            int playTime = text.getPlayTime();
            Log.d(TAG, "playRollText 原始播放时间:" + playTime + " 播放次数：" + text.getPlayCount());
            if (playTime <= 0) {
                float scale = (float) width / getHdmiWidth();
                playTime = (int) (35 * scale) + (int) (1.1f * text.getContent().length());//第一个字从最右边到最左边滚动耗时+每个字1秒
                if (text.getPlayCount() > 0) {
                    playTime = text.getPlayCount() * playTime;
                }
            }
            Log.d(TAG, "playRollText 经过计算后播放时间:" + playTime);
            WidgetTextNotification textNotification = new WidgetTextNotification(getContext());
            ImageChildView childView = new ImageChildView(textNotification, System.currentTimeMillis() + playTime * 1000, 2);
            mRootView.addView(textNotification, width, height);
            imageChildViews.add(childView);
            AnimatorUtils.moveView2Target(textNotification, new int[]{0, 0}, new int[]{(getHdmiWidth() - width) / 2, 80});
            textNotification.play(text.getBackground(), text.getContent(), text.getColor(), text.getSource() == 1);
            startViewsClearTimer();
            return playTime;
        } else {//来自本地功能
            marqueePlayer.playPushMsg(text.getContent(), text.getPlayCount(), text.getColor());
        }
        return 0;
    }


    /**
     * 以720P为标准
     */
    private float getScale() {
        return (float) getHdmiHeight() / 720;
    }

    private void playImage(Image image, int stayTime) {
        int playTime = stayTime > 0 ? stayTime : image.getStayTime();
        if (playTime <= 0) {//默认显示10秒
            playTime = 5;
        }
        WidgetImageNotification imageNotification = new WidgetImageNotification(getContext());
        ImageChildView childView = new ImageChildView(imageNotification, System.currentTimeMillis() + playTime * 1000, 3);
        Log.d(TAG, "图片宽度：" + image.getWidth() + " 高度：" + image.getHeight() + " 播放时长：" + playTime
                + " 开始坐标（" + image.getStartX() + "," + image.getStartY() + ")" + " 结束坐标(" + image.getEndX() + "," + image.getEndY() + ")");
        int imageWidth = (int) (image.getWidth() > 0 ? getScale() * image.getWidth() : 0.5f * getHdmiWidth());
        int imageHeight = (int) (image.getHeight() > 0 ? getScale() * image.getHeight() : 0.5f * getHdmiHeight());
        int startX = (int) (getScale() * image.getStartX());
        int startY = (int) (getScale() * image.getStartY());
        int endX = (int) (getScale() * image.getEndX());
        int endY = (int) (getScale() * image.getEndY());
        if (endX <= 0) {
            endX = (getHdmiWidth() - imageWidth) / 2;
        }
        if (endY <= 0) {
            endY = (getHdmiHeight() - imageHeight) / 3;
        }
        Log.d(TAG, "imageWidth:" + imageWidth + " imageHeight:" + imageHeight + " endX:" + endX + " endY:" + endY);
        mRootView.addView(imageNotification, imageWidth, imageHeight);
        imageChildViews.add(childView);
        AnimatorUtils.moveView2Target(imageNotification, new int[]{startX, startY}, new int[]{endX, endY});
        imageNotification.play("", image.getUrl(), image.getSource() == 1);
        startViewsClearTimer();
    }

    private void cleanDelayChildView() {
        boolean haveTextPlaying = false;
        boolean haveImagePlaying = false;
        for (int i = imageChildViews.size() - 1; i >= 0; i--) {
            ImageChildView imageChildView = imageChildViews.get(i);
            if (System.currentTimeMillis() >= imageChildView.removeTime) {
                mRootView.removeView(imageChildView.view);
                imageChildViews.remove(i);
            } else {
                if (imageChildView.type == 2) {//文本通知
                    haveTextPlaying = true;
                } else if (imageChildView.type == 3) {//图片通知
                    haveImagePlaying = true;
                }
            }
        }
        if (!haveTextPlaying) {//无文本通知在播放了
            VodApplication.getKaraokeController().setIsPlayingNotificationText(false);
        }
        if (!haveImagePlaying) {//无图片通知播放了，检查队列
            VodApplication.getKaraokeController().setIsPlayingNotificationImage(false);
        }
    }

    private void playVideo(Video video) {
        Log.d(TAG, "播放小窗口播放视频 >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (decodeTexturePlayer != null) {
            Log.e(TAG, "已经有小窗口播放视频，不可以再开窗口播放视频了！！！");
            return;
        }
        Log.d(TAG, "url:" + video.getUrl() + " width:" + video.getWidth() + " height:" + video.getHeight());
        int videoWidth = (int) (getScale() * (video.getWidth() > 0 ? video.getWidth() : 426));
        int videoHeight = (int) (getScale() * (video.getHeight() > 0 ? video.getHeight() : 240));

        final VideoTextureView smallVideoTextureView = new VideoTextureView(getContext());
        mRootView.addView(smallVideoTextureView, videoWidth, videoHeight);
        AnimatorUtils.moveView2Target(smallVideoTextureView, new int[]{(int) (getScale() * video.getStartX()), (int) (getScale() * video.getStartY())},
                new int[]{(int) (getScale() * (video.getEndX() > 0 ? video.getEndX() : 426)), (int) getScale() * (video.getEndY() > 0 ? video.getEndY() : 240)});
        decodeTexturePlayer = new DecodeTexturePlayer(smallVideoTextureView, null);
        decodeTexturePlayer.setOnBasePlayerListener(new OnBasePlayerListener() {
            @Override
            public void onPlayStart(String path) {
                decodeTexturePlayer.setVolume(0);//要静音
            }

            @Override
            public void onPlayProgress(String path, long duration, long current) {
            }

            @Override
            public void onPlayCompletion(String path) {
                decodeTexturePlayer.release();
                decodeTexturePlayer = null;
                mRootView.removeView(smallVideoTextureView);
                VodApplication.getKaraokeController().setIsPlayingNotificationVideo(false);
            }

            @Override
            public void onPlayError(String path, String error) {
                decodeTexturePlayer.release();
                decodeTexturePlayer = null;
                mRootView.removeView(smallVideoTextureView);
                Log.e(TAG, "播放小窗口视频发生错误:" + error);
                VodApplication.getKaraokeController().setIsPlayingNotificationVideo(false);
            }
        });
        smallVideoTextureView.postDelayed(() -> decodeTexturePlayer.play(video.getUrl())
                , 1000);
    }

    private void setMvImage(String images) {
        widgetMvImagePlayer.setImages(images);
        showMvPlayer(true);
    }

    private void showMvPlayer(boolean show) {
        Logger.d(TAG, "showMvPlayer show:" + show);
        widgetMvImagePlayer.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            widgetMvImagePlayer.startPlayer();
        } else {
            widgetMvImagePlayer.stopPlayer();
        }

    }

    private static class ImageChildView {
        private View view;
        private long removeTime;
        private int type;

        private ImageChildView(View view, long removeTime, int type) {
            this.view = view;
            this.removeTime = removeTime;
            this.type = type;
        }
    }


    private ScheduledExecutorService mScheduledExecutorService;

    private void startViewsClearTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);
    }

    private void stopViewsClearTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() ->
                mRootView.post(() -> {
                    cleanDelayChildView();
                    if (imageChildViews == null || imageChildViews.isEmpty()) {
                        stopViewsClearTimer();
                    }
                }), 2, 2, TimeUnit.SECONDS);
    }


    private void requestQrCode() {
        QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
            @Override
            public void onQrCode(RoomQrCodeSimple code) {
                Bitmap bitmap;
                if (code != null && (bitmap = QrCodeUtil.createQRCode(code.toString())) != null) {
                    wQrCode.setImageText(bitmap, getContext().getString(R.string.wechat_scan_qr_code));
                    wQrCode.setVisibility(View.VISIBLE);
                    wQrCode.removeCallbacks(runQrCode);
                    wQrCode.postDelayed(runQrCode, VodConfigData.getInstance().getQrCodeDuration());
                }
            }

            @Override
            public void onQrCodeFail(String error) {
            }
        });
        request.requestCode();
    }

    private Runnable runQrCode = new Runnable() {
        @Override
        public void run() {
            wQrCode.setVisibility(View.GONE);
        }
    };

    private void showStoreLogo(int showTime) {
        wLogo.removeCallbacks(runLogo);
        Log.d(TAG, "显示店家logo:" + VodConfigData.getInstance().getTvLogo());
        if (!TextUtils.isEmpty(VodConfigData.getInstance().getTvLogo())) {
            wLogo.setVisibility(View.VISIBLE);
            wLogo.setImageText(VodConfigData.getInstance().getTvLogo(), "");
            wLogo.postDelayed(runLogo, showTime * 1000);
        }
    }

    private Runnable runLogo = new Runnable() {
        @Override
        public void run() {
            wLogo.setVisibility(View.GONE);
        }
    };

    private void resizeQrCode() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) wQrCode.getLayoutParams();
        float percent = (float) VodConfigData.getInstance().getQrCodeSize() / 100;
        layoutParams.width = (int) (percent * getContext().getResources().getDimensionPixelSize(R.dimen.television_qr_code_width));
        layoutParams.height = (int) (percent * getContext().getResources().getDimensionPixelSize(R.dimen.television_qr_code_height));
        wQrCode.setLayoutParams(layoutParams);
        wQrCode.postInvalidate();
    }

    private void playSvga(String url) {
//        if(animationView.isAnimating()){
//            animationView.stopAnimation(true);
//        }
        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    animationView.setVideoItem(videoItem);
                    animationView.setClearsAfterStop(true);
                    animationView.setLoops(1);
                    animationView.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {
                            Log.d(TAG, "svga call back onPause");
                            animationView.stopAnimation(true);
                        }

                        @Override
                        public void onFinished() {
                            Log.d(TAG, "svga call back onFinished");
                            animationView.stopAnimation(true);
                        }

                        @Override
                        public void onRepeat() {
                            Log.d(TAG, "svga call back onRepeat");
                        }

                        @Override
                        public void onStep(int i, double v) {
//                            Log.d(TAG, "svga call back onStep i:" + i + " v:" + v);
                        }
                    });
                    animationView.stepToFrame(0, true);

                }

                @Override
                public void onError() {

                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
