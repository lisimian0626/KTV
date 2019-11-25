package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.model.dto.PresentationCenterIcon;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.texture.VideoTextureView;
import com.bumptech.glide.Glide;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by J Wong on 2016/6/27.
 */
public class GamePresentation extends Presentation {
    private VideoTextureView videoTextureView;
    private RecyclerImageView mGvTutor1, mGvTutor2, mGvTutor3, mGvTutor4;
    private RecyclerImageView mIvResultLogo, mIvResultText, mIvResult, mIvBgResult, mIvBgGame;
    private TextView mTvCenter, mTvTimer;
    private View mViewResult, mViewResultLogo;
    private BubbleSeekBar sbVolume;
    private View sbVolumeContent;
    private LinearLayout llStatic;
    private Map<Integer, Boolean> mTutorTurns = new HashMap<>();
    private int mHdmiW;
    private int mHdmiH;
    private int lastTurnTutor = -1;
    public static boolean mIsPlayingGame;

    public GamePresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        mHdmiW = realSize.x;
        mHdmiH = realSize.y;
    }

    public int getHdmiWidth() {
        return mHdmiW;
    }

    public int getHdmiHeight() {
        return mHdmiH;
    }

    /**
     * 以720P为标准
     *
     * @return 缩放比例
     */
    private float getScale() {
        return (float) getHdmiHeight() / 720;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_game);
        llStatic = findViewById(R.id.ll_static);
        mIvBgGame = findViewById(R.id.iv_bg_game);
        videoTextureView = findViewById(R.id.video_texture);
        mTvCenter = findViewById(R.id.tv_player_center);
        sbVolume = findViewById(R.id.sb_volume);
        sbVolumeContent = findViewById(R.id.ll_volume);
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
        mTvTimer = findViewById(R.id.tv_timer);
        mViewResult = findViewById(R.id.rl_result);
        mIvBgResult = findViewById(R.id.iv_bg_result);
        mViewResultLogo = findViewById(R.id.rl_game_result);
        mIvResultLogo = findViewById(R.id.iv_tutor_big);
        mIvResultText = findViewById(R.id.iv_tutor_text);

        mGvTutor1 = findViewById(R.id.gv_tutor1);
        mGvTutor2 = findViewById(R.id.gv_tutor2);
        mGvTutor3 = findViewById(R.id.gv_tutor3);
        mGvTutor4 = findViewById(R.id.gv_tutor4);
        mIvResult = findViewById(R.id.iv_result);

        Glide.with(getContext()).load(R.drawable.tv_game_bg).skipMemoryCache(false).into(mIvBgGame);
        setStaticInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public VideoTextureView getVideoTextureView() {
        return videoTextureView;
    }


    public void resetViews() {
        Glide.with(getContext()).load(R.drawable.tv_tutor1_1).skipMemoryCache(false).into(mGvTutor1);
        Glide.with(getContext()).load(R.drawable.tv_tutor2_1).skipMemoryCache(false).into(mGvTutor2);
        Glide.with(getContext()).load(R.drawable.tv_tutor3_1).skipMemoryCache(false).into(mGvTutor3);
        Glide.with(getContext()).load(R.drawable.tv_tutor4_1).skipMemoryCache(false).into(mGvTutor4);
        mViewResultLogo.setVisibility(View.GONE);
        mViewResult.setVisibility(View.GONE);
        showResult(false, false, 0, 0);
        mTutorTurns.clear();
    }


    public int getPassSize() {
        return mTutorTurns.size();
    }


    public int getLastTurnTutor() {
        return lastTurnTutor;
    }

    public void turn(final int position) {
        switch (position) {
            case 0:
                if (!mTutorTurns.containsKey(position)) {
                    mTutorTurns.put(position, true);
                    lastTurnTutor = position;
                    mGvTutor1.setImageResource(R.drawable.amin_tutor_1);
                    AnimationDrawable animationDrawable = (AnimationDrawable) mGvTutor1.getDrawable();
                    animationDrawable.start();
                }
                break;
            case 1:
                if (!mTutorTurns.containsKey(position)) {
                    mTutorTurns.put(position, true);
                    lastTurnTutor = position;
                    mGvTutor2.setImageResource(R.drawable.amin_tutor_2);
                    AnimationDrawable animationDrawable = (AnimationDrawable) mGvTutor2.getDrawable();
                    animationDrawable.start();
                }
                break;
            case 2:
                if (!mTutorTurns.containsKey(position)) {
                    mTutorTurns.put(position, true);
                    lastTurnTutor = position;
                    mGvTutor3.setImageResource(R.drawable.amin_tutor_3);
                    AnimationDrawable animationDrawable = (AnimationDrawable) mGvTutor3.getDrawable();
                    animationDrawable.start();
                }
                break;
            case 3:
                if (!mTutorTurns.containsKey(position)) {
                    mTutorTurns.put(position, true);
                    lastTurnTutor = position;
                    mGvTutor4.setImageResource(R.drawable.amin_tutor_4);
                    AnimationDrawable animationDrawable = (AnimationDrawable) mGvTutor4.getDrawable();
                    animationDrawable.start();
                }
                break;
        }
    }

    @Override
    public void show() {
        mIsPlayingGame = true;
        super.show();
    }

    @Override
    public void dismiss() {
        mViewResultLogo.setVisibility(View.GONE);
        mViewResult.setVisibility(View.GONE);
        mIsPlayingGame = false;
        super.dismiss();
    }


    private Runnable runOperation = new Runnable() {
        @Override
        public void run() {
            mTvCenter.setVisibility(View.GONE);
        }
    };

    public void setTimeText(final String text) {
        mTvTimer.post(() -> mTvTimer.setText(text));
    }

    public void showResult(final boolean show, final boolean pass, final int resId, final int level) {
        mViewResultLogo.post(() -> {
            if (show) {
                mViewResultLogo.setVisibility(View.VISIBLE);
                mViewResult.setVisibility(View.VISIBLE);
                if (pass) {
                    Glide.with(getContext()).load(resId).skipMemoryCache(false).into(mIvResultLogo);
                    Glide.with(getContext()).load(R.drawable.tv_game_pass_text).skipMemoryCache(false).into(mIvResultText);
                    Glide.with(getContext()).load(R.drawable.tv_game_result_pass).skipMemoryCache(false).into(mIvBgResult);
                    mIvResult.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(R.drawable.tv_game_pass_1).skipMemoryCache(false).into(mIvResult);
                    int passResId = 0;
                    if (level == 1) {
                        passResId = R.drawable.tv_game_pass_1;
                    } else if (level == 2) {
                        passResId = R.drawable.tv_game_pass_2;
                    } else if (level == 3) {
                        passResId = R.drawable.tv_game_pass_3;
                    } else if (level == 4) {
                        passResId = R.drawable.tv_game_pass_4;
                    }
                    Glide.with(getContext()).load(passResId).skipMemoryCache(false).into(mIvResult);
                } else {
                    Glide.with(getContext()).load(resId).skipMemoryCache(false).into(mIvResultLogo);
                    Glide.with(getContext()).load(R.drawable.tv_game_fail_text).skipMemoryCache(false).into(mIvResultText);
                    Glide.with(getContext()).load(R.drawable.tv_game_result_fail).skipMemoryCache(false).into(mIvBgResult);
                    mIvResult.setVisibility(View.GONE);
                }
            } else {
                mViewResultLogo.setVisibility(View.GONE);
                mViewResult.setVisibility(View.GONE);
            }
        });

        mViewResultLogo.postDelayed(() ->
                mViewResultLogo.setVisibility(View.GONE), 5000);
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
        switch (event.id) {
            case EventBusId.Id.BUTTON_STATUS_CHANGED:
                setStaticInfo();
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
            case EventBusId.PresentationId.SHOW_CENTER_ICON:
                tipOperation((PresentationCenterIcon) event.data);
                break;
        }
    }

    private Runnable runnableVolDismiss = new Runnable() {
        @Override
        public void run() {
            sbVolumeContent.setVisibility(View.GONE);
        }
    };

    private void tipOperation(PresentationCenterIcon icon) {
        mTvCenter.post(() -> {
            mTvCenter.setVisibility(View.VISIBLE);
            mTvCenter.setCompoundDrawablesWithIntrinsicBounds(0, icon.iconId, 0, 0);
            if (TextUtils.isEmpty(icon.text)) {
                mTvCenter.setText("");
            } else {
                mTvCenter.setText(icon.text);
            }
        });
        mTvCenter.removeCallbacks(runOperation);
        if (icon.autoDismiss) {
            mTvCenter.postDelayed(runOperation, 2000);
        }
    }
}
