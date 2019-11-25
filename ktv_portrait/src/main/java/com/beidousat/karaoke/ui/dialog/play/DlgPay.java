package com.beidousat.karaoke.ui.dialog.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.ui.BaseActivity;
import com.beidousat.karaoke.ui.presentation.PayPresentation;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.PayApiHelper;
import com.bestarmedia.libcommon.model.vod.PayInfo;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.anim.Anim;
import com.bestarmedia.libwidget.anim.EnterAnimUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.player.TexturePlayer;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J Wong on 2019/11/1.
 */

public class DlgPay extends DialogFragment implements OnBasePlayerListener {

    private RelativeLayout rlPay, rlTip;
    private RecyclerImageView ivBackground, ivQrCode, ivBestarAd;
    private TexturePlayer player;
    private PayPresentation mPresentation;
    private final SparseArray<PayPresentation> mActivePresentations = new SparseArray<>();
    private String payUrl;
    private double fee;
    private PayInfo payInfo;
    private String[] videoUrls, imgUrls;
    private int indexVideo = 0, indexImage = 0;
    private int payContentX, payContentY,
            payContentW = DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[0],
            payContentH = DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[1];
    private final static String TAG = "DlgPay";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.presentation_pay, container);
        ivBackground = view.findViewById(R.id.iv_background);
        ivQrCode = view.findViewById(R.id.iv_qr_code);
        rlPay = view.findViewById(R.id.rl_pay);
        rlTip = view.findViewById(R.id.rl_tip);
        ivBestarAd = view.findViewById(R.id.iv_ad);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        init();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        init();
        return super.show(transaction, tag);
    }

    @Override
    public void showNow(FragmentManager manager, String tag) {
        super.showNow(manager, tag);
        init();
    }

    public void setPayInfo(PayInfo payInfo) {
        boolean payInfoChanged = this.payInfo == null || (payInfo != null && !payInfo.payUrl.equalsIgnoreCase(this.payInfo.payUrl));
        if (payInfoChanged) {
            indexVideo = 0;
            indexImage = 0;
            this.payInfo = payInfo;
            if (this.payInfo.mediaContent != null) {
                if (!TextUtils.isEmpty(this.payInfo.mediaContent.videoUrl)) {
                    this.videoUrls = payInfo.mediaContent.videoUrl.contains(",") ? this.payInfo.mediaContent.videoUrl.split(",") : new String[]{this.payInfo.mediaContent.videoUrl};
                } else if (!TextUtils.isEmpty(this.payInfo.mediaContent.imgUrl)) {
                    this.imgUrls = this.payInfo.mediaContent.imgUrl.contains(",") ? this.payInfo.mediaContent.imgUrl.split(",") : new String[]{this.payInfo.mediaContent.imgUrl};
                }
                if (isAdded()) {
                    startPlayMedia();
                }
            }
        }
    }

    private void startPlayMedia() {
        if (this.payInfo != null && this.payInfo.mediaContent != null) {
            showPayInfo();
            if (videoUrls != null && videoUrls.length > 0) {
                initPlayer();
                if (mPresentation != null && player != null && ivBackground != null) {
                    ivBackground.postDelayed(() -> {
                        if (player != null) {
                            player.setVolume(0.3f);
                            player.play(videoUrls[indexVideo]);
                        }
                    }, 5000);
                }
            } else if (imgUrls != null && imgUrls.length > 0) {
                playImageOnTelevision(imgUrls[indexImage]);
                if (imgUrls.length > 1) {
                    startScreenTimer();
                }
            } else {
                Glide.with(this).load(R.drawable.bg_pay_horizontal).skipMemoryCache(false).into(mPresentation.getIvBackground());
            }
        } else {
            Glide.with(this).load(R.drawable.bg_pay_horizontal).skipMemoryCache(false).into(mPresentation.getIvBackground());
        }
    }

    private void showPayInfo() {
        if (getContext() != null && getActivity() != null && isAdded() && payInfo != null && payInfo.mediaContent != null) {
            if (!TextUtils.isEmpty(payInfo.mediaContent.vbgFileName)) {
                Glide.with(this).load(ServerFileUtil.getFileUrl(payInfo.mediaContent.vbgFileName))
                        .placeholder(R.drawable.bg_pay_vertical).error(R.drawable.bg_pay_vertical).skipMemoryCache(false).into(ivBackground);
                new PayApiHelper(getContext().getApplicationContext()).record(1, ServerFileUtil.getFileName(payInfo.mediaContent.vbgFileName));
            } else {
                Glide.with(this).load(R.drawable.bg_pay_vertical).skipMemoryCache(false).into(ivBackground);
            }
            if (!TextUtils.isEmpty(payInfo.mediaContent.qrCodeUrl)) {
                if (!payInfo.mediaContent.qrCodeUrl.equals(payUrl)) {
                    Bitmap bitmap = QrCodeUtil.createQRCode(payInfo.mediaContent.qrCodeUrl);
                    if (bitmap != null) {
                        boolean haveBestarAd = false;
                        int contentX = 0, contentY = 0, contentW = DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[0],
                                contentH = DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[1];
                        if (payInfo.adPay != null && !TextUtils.isEmpty(payInfo.adPay.horizontalFileUrl)) {
                            try {
                                haveBestarAd = true;
                                String[] positions = payInfo.adPay.horizontalContentPosition.split(",");
                                contentX = Integer.valueOf(positions[0]);
                                contentY = Integer.valueOf(positions[1]);
                                contentW = Integer.valueOf(positions[2]);
                                contentH = Integer.valueOf(positions[3]);
                            } catch (Exception e) {
                                Log.w(TAG, "二维码坐标转换出错了", e);
                            }
                        }

                        int x = 946, y = 352, w = 180, h = 180;
                        try {
                            String[] positions = payInfo.mediaContent.position.split(",");
                            x = Integer.valueOf(positions[0]);
                            y = Integer.valueOf(positions[1]);
                            w = Integer.valueOf(positions[2]);
                            h = Integer.valueOf(positions[3]);
                        } catch (Exception e) {
                            Log.w(TAG, "二维码坐标转换出错了", e);
                        }
                        if (mPresentation != null) {
                            if (haveBestarAd && payInfo.adPay.isTvShow == 1) {
                                mPresentation.setPayContentPositions(contentX, contentY, contentW, contentH);
                                Glide.with(this).load(ServerFileUtil.getFileUrl(payInfo.adPay.horizontalFileUrl)).skipMemoryCache(false).into(mPresentation.getIvAd());
                            }
                            mPresentation.getIvQrCode().setImageBitmap(bitmap);
                            mPresentation.setPosition(x, y, w, h);
                        }

                        int tx = 421, ty = 947, tw = 180, th = 180;
                        try {
                            String[] positions = payInfo.mediaContent.positionVertical.split(",");
                            tx = Integer.valueOf(positions[0]);
                            ty = Integer.valueOf(positions[1]);
                            tw = Integer.valueOf(positions[2]);
                            th = Integer.valueOf(positions[3]);
                        } catch (Exception e) {
                            Log.w(TAG, "二维码坐标转换出错了", e);
                        }
                        if (payInfo.adPay != null && payInfo.adPay.isTouchShow == 1 && !TextUtils.isEmpty(payInfo.adPay.verticalFileUrl)) {
                            try {
                                String[] positions = payInfo.adPay.verticalContentPosition.split(",");
                                setPayContentPositions(Integer.valueOf(positions[0]), Integer.valueOf(positions[1]), Integer.valueOf(positions[2]), Integer.valueOf(positions[3]));
                                Glide.with(this).load(ServerFileUtil.getFileUrl(payInfo.adPay.verticalFileUrl)).skipMemoryCache(false).into(ivBestarAd);
                            } catch (Exception e) {
                                Log.w(TAG, "二维码坐标转换出错了", e);
                            }
                        }
                        ivQrCode.setImageBitmap(bitmap);
                        setPosition(tx, ty, tw, th);
                        payUrl = payInfo.mediaContent.qrCodeUrl;
                    }
                }
            }
            if (payInfo.amount != this.fee) {
//            DecimalFormat df = new DecimalFormat("#0.00");
//            tvFee.setText(df.format(payInfo.amount));
                this.fee = payInfo.amount;
            }
        }
    }

    @Override
    public void dismiss() {
        close();
        stopScreenTimer();
        BaseActivity.mCanShowAd = true;
        VodApplication.getKaraokeController().resetInitVolume();
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
            mActivePresentations.clear();
        }
        super.dismiss();
    }


    private void close() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            mActivePresentations.clear();
        }
        if (player != null) {
            player.release();
            player = null;
        }
        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
    }

    @Override
    public void onPlayStart(String path) {
        if (player != null) {
            player.setVolume(0.3f);
        }
        if (getContext() != null)
            new PayApiHelper(getContext().getApplicationContext()).record(1, ServerFileUtil.getFileName(path));
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {

    }

    @Override
    public void onPlayCompletion(String path) {
        indexVideo++;
        player.play(videoUrls[indexVideo % videoUrls.length]);
    }

    @Override
    public void onPlayError(String path, String error) {
        Log.e(TAG, "播放异常:" + error);
    }

    private void init() {
        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
        BaseActivity.mCanShowAd = false;
        if (player == null && getContext() != null && isAdded()) {
            DisplayManager mDisplayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
            assert mDisplayManager != null;
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (Display display : displays) {
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        showPresentation(display);
                        break;
                    }
                }
                startPlayMedia();
            } else {
                Log.d(TAG, " Display[] is null");
            }
        }
    }

    private void playImageOnTelevision(String imgUrl) {
        if (mPresentation != null) {
            if (!TextUtils.isEmpty(imgUrl)) {
                Glide.with(this).load(ServerFileUtil.getFileUrl(imgUrl)).skipMemoryCache(false).into(mPresentation.getIvBackground());
                if (getContext() != null)
                    new PayApiHelper(getContext().getApplicationContext()).record(1, ServerFileUtil.getFileName(imgUrl));
            } else {
                Glide.with(this).load(R.drawable.bg_pay_horizontal).skipMemoryCache(false).into(mPresentation.getIvBackground());
            }
            Anim anim = EnterAnimUtil.getRandomEnterAnim(mPresentation.getEnterAnimLayout());
            anim.startAnimation(800);
        }
    }

    private void initPlayer() {
        if (mPresentation != null) {
            if (player == null) {
                player = new TexturePlayer(mPresentation.getVideoTextureView(), null);
                player.setOnBasePlayerListener(this);
            }
        }
    }

    private void showPresentation(final Display display) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }
        mPresentation = new PayPresentation(getContext(), display);
        if (mPresentation.getWindow() != null) {
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mPresentation.show();
        mActivePresentations.put(displayId, mPresentation);
    }

    /**
     * 以720P为标准
     */
    private float getScale() {
        return (float) DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[0] / 720;
    }

    private ScheduledExecutorService mScheduledExecutorService;

    private void startScreenTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);

    }

    private void stopScreenTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        int playTime = payInfo != null && payInfo.mediaContent != null && payInfo.mediaContent.playTime > 0 ? payInfo.mediaContent.playTime : 15;
        service.scheduleAtFixedRate(() ->
                ivBackground.post(() -> {
                    indexImage++;
                    playImageOnTelevision(imgUrls[indexImage % imgUrls.length]);
                }), playTime, playTime, TimeUnit.SECONDS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.NETWORK_CHANGE) {
            boolean isEnable = Boolean.valueOf(event.data.toString());
            setTipShow(!isEnable);
            if (isEnable && (player != null && !player.isPlaying())) {
                indexVideo++;
                player.play(videoUrls[indexVideo % videoUrls.length]);
            }
        } else if (event.id == EventBusId.Id.PAY_INTERFACE_ERROR) {
            int isError = Integer.valueOf(event.data.toString());
            setTipShow(isError == 1);
        }
    }

    private void setTipShow(boolean isShow) {
        rlTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setPayContentPositions(int payContentX, int payContentY, int payContentW, int payContentH) {
        this.payContentX = payContentX;
        this.payContentY = payContentY;
        this.payContentW = payContentW;
        this.payContentH = payContentH;
    }

    private int getPayContentX() {
        return this.payContentX;
    }

    private int getPayContentY() {
        return this.payContentY;
    }

    private int getPayContentWidth() {
        return this.payContentW;
    }

    private int getPayContentHeight() {
        return this.payContentH;
    }

    public void setPosition(int x, int y, int w, int h) {
        x = (int) ((float) getPayContentWidth() * x / DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[0]);
        y = (int) ((float) getPayContentHeight() * y / DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[1]);
        w = (int) ((float) getPayContentWidth() * w / DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[0]);
        h = (int) ((float) getPayContentHeight() * h / DensityUtil.getScreenWidthHeight(VodApplication.getVodApplicationContext())[1]);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivQrCode.getLayoutParams();
        layoutParams.setMargins(x, y, 0, 0);
        layoutParams.width = w;
        layoutParams.height = h;
        ivQrCode.setLayoutParams(layoutParams);
        initScreenSize();
    }

    private void initScreenSize() {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) rlPay.getLayoutParams();
        linearParams.width = (int) (getPayContentWidth() * getScale());
        linearParams.height = (int) (getPayContentHeight() * getScale());
        linearParams.setMargins((int) (getPayContentX() * getScale()), (int) (getPayContentY() * getScale()), 0, 0);
        rlPay.setLayoutParams(linearParams);
//        resizeQrCode();
    }
}
