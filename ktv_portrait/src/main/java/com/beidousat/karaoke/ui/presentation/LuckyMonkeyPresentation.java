package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.widget.game.LuckyMonkeyPanelView;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.dto.ButtonStatus;
import com.bestarmedia.libcommon.util.RandomUtil;
import com.bestarmedia.libwidget.anim.PushPullAnimation;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

/**
 * Created by J Wong on 2018/3/29.
 */

public class LuckyMonkeyPresentation extends Presentation implements LuckyMonkeyPanelView.OnGameLuckyListener {

    private LuckyMonkeyPanelView luckyMonkeyPanelView;
    private View halfTransparent, vGameResultTop, vGameResultBottom;
    private TextView tvRole, tvAdventure;
    private TextView mTvLucky;
    private LinearLayout llStatic;
    private View sbVolumeContent;
    private BubbleSeekBar sbVolume;
    private int mCount;
    private int mHdmiH;

    public LuckyMonkeyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        mHdmiH = realSize.y;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_game_lucky_monkey);
        llStatic = findViewById(R.id.ll_static);
        mTvLucky = findViewById(R.id.tv_lucky_num);
        luckyMonkeyPanelView = findViewById(R.id.lucky_panel);
        luckyMonkeyPanelView.setOnGameLuckyListener(this);
        halfTransparent = findViewById(R.id.riv_half);
        vGameResultTop = findViewById(R.id.rl_game_result_top);
        vGameResultBottom = findViewById(R.id.rl_game_result_bottom);
        tvRole = findViewById(R.id.tv_role);
        tvAdventure = findViewById(R.id.tv_adventure);
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
        setStaticInfo();
    }

    private int getHdmiHeight() {
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
        sbVolumeContent.removeCallbacks(runnableVolDismiss);
    }

    @Override
    public void onGameLuckyChanged(int position) {
        int x = 1 + (int) (Math.random() * mCount);
        mTvLucky.setText(String.valueOf(x));
    }

    @Override
    public void onGameLuckyFinish(String text) {
        tvRole.setText(getContext().getString(R.string.left_people_x, mTvLucky.getText()));
        tvAdventure.setText(text);
        halfTransparent.setVisibility(View.VISIBLE);

        vGameResultTop.setVisibility(View.VISIBLE);
        vGameResultTop.startAnimation(PushPullAnimation.create(PushPullAnimation.DOWN, true, 1000));

        vGameResultBottom.setVisibility(View.VISIBLE);
        vGameResultBottom.startAnimation(PushPullAnimation.create(PushPullAnimation.UP, true, 1000));
    }

    private String[] getGameText(int level) {
        String[] randomStrings;
        String[] games = new String[8];
        if (level == 2) {
            String[] texts = getResources().getStringArray(R.array.game_lv2);
            randomStrings = new RandomUtil().randomStrings(texts);
        } else if (level == 3) {
            String[] texts = getResources().getStringArray(R.array.game_lv3);
            randomStrings = new RandomUtil().randomStrings(texts);
        } else {
            String[] texts = getResources().getStringArray(R.array.game_lv1);
            randomStrings = new RandomUtil().randomStrings(texts);
        }
        if (randomStrings != null && randomStrings.length >= 8)
            System.arraycopy(randomStrings, 0, games, 0, 8);
//        for (int i = 0; i < 8; i++) {
//                games[i] = randomStrings[i];
//            }
        return games;
    }


    public void startGame(int level, int count) {
        if (!luckyMonkeyPanelView.isGameRunning()) {
            mCount = count;
            int x = 1 + (int) (Math.random() * mCount);
            mTvLucky.setText(String.valueOf(x));
            luckyMonkeyPanelView.setTexts(getGameText(level));
            tvAdventure.setText("");
            halfTransparent.setVisibility(View.GONE);
            vGameResultTop.setVisibility(View.GONE);
            vGameResultTop.startAnimation(PushPullAnimation.create(PushPullAnimation.UP, false, 1000));

            vGameResultBottom.setVisibility(View.GONE);
            vGameResultBottom.startAnimation(PushPullAnimation.create(PushPullAnimation.DOWN, false, 1000));

            luckyMonkeyPanelView.startGame();
            luckyMonkeyPanelView.postDelayed(autoStop, 5 * 1000);

        }
    }

    private Runnable autoStop = () -> stopGame();

    private void stopGame() {
        if (luckyMonkeyPanelView.isGameRunning()) {
            int stayIndex = new Random().nextInt(8);
            luckyMonkeyPanelView.tryToStop(stayIndex);
        }
    }

    @Override
    public void show() {
        super.show();
        luckyMonkeyPanelView.startBgMusic();
    }

    @Override
    public void dismiss() {
        luckyMonkeyPanelView.stopBgMusic();
        super.dismiss();
    }

    public void showFireTip(int status) {
        if (status == 1) {//火警开启
            luckyMonkeyPanelView.stopBgMusic();
            luckyMonkeyPanelView.stopSound();
            luckyMonkeyPanelView.stopMarquee();
        }
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
        if (event.id == EventBusId.Id.BUTTON_STATUS_CHANGED) {
            setStaticInfo();
        } else if (event.id == EventBusId.PresentationId.SYSTEM_VOL_CHANGED)
            sbVolumeContent.post(() -> {
                int currentVol = Integer.valueOf(event.data.toString());
                sbVolumeContent.setVisibility(View.VISIBLE);
                sbVolume.setProgress(currentVol);
                sbVolumeContent.removeCallbacks(runnableVolDismiss);
                sbVolumeContent.postDelayed(runnableVolDismiss, 3000);
            });
    }

    private Runnable runnableVolDismiss = new Runnable() {
        @Override
        public void run() {
            sbVolumeContent.setVisibility(View.GONE);
        }
    };
}
