package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.danmaku.BarrageView;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.texture.VideoTextureView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by J Wong on 2015/10/9 17:19.
 */
public class PkPresentation extends Presentation {

    private static final String TAG = PkPresentation.class.getSimpleName();

    private VideoTextureView surfaceView;
    private TextView mTvCenter;
    private RecyclerImageView mIvService;
    private BarrageView barrageView;
    private View mViewTimer;
    private RecyclerImageView mIvAvatar1, mIvAvatar2, mIvDlgAvatar1, mIvDlgAvatar2;
    private TextView mTvUser1, mTvUser2, mTvDlgUser1, mTvDlgUser2;
    private TextView mTvScore1, mTvScore2;
    private ProgressBar mPgb1, mPgb2;
    private TextView mTvTimer, mTvSongName;
    private View mViewResult;
    private RecyclerImageView ivWinner, ivResultUser1, ivResultUser2;
    private TextView tvWinner, tvResultUser1, tvResultUser2, tvResultUser1Score, tvResultUser2Score;
    private TextView tvGiveUpTimer;
    private View mViewGiveUp;
    private int mHdmiW;
    private int mHdmiH;
    private RelativeLayout mRootView;

    public PkPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        mHdmiW = realSize.x;
        mHdmiH = realSize.y;
    }

    @Deprecated
    public int getHdmiWidth() {
        return mHdmiW;
    }

    @Deprecated
    public int getHdmiHeight() {
        return mHdmiH;
    }

    public VideoTextureView getSurfaceView() {
        return surfaceView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_pk);
        mRootView = findViewById(R.id.root);
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
        linearParams.height = mHdmiH;
        linearParams.width = mHdmiW;
        mRootView.setLayoutParams(linearParams);
        surfaceView = findViewById(R.id.video_texture);
        mIvService = findViewById(R.id.iv_service);
        mTvCenter = findViewById(R.id.tv_player_center);
        mViewTimer = findViewById(R.id.rl_timer);
        mIvAvatar1 = findViewById(R.id.iv_avatar1);
        mIvAvatar2 = findViewById(R.id.iv_avatar2);
        mIvDlgAvatar1 = findViewById(R.id.iv_dlg_user1);
        mIvDlgAvatar2 = findViewById(R.id.iv_dlg_user2);
        mTvUser1 = findViewById(R.id.tv_user1);
        mTvUser2 = findViewById(R.id.tv_user2);
        mTvDlgUser1 = findViewById(R.id.tv_dlg_user1);
        mTvDlgUser2 = findViewById(R.id.tv_dlg_user2);
        mTvScore1 = findViewById(R.id.tv_score1);
        mTvScore2 = findViewById(R.id.tv_score2);
        mPgb1 = findViewById(R.id.pgb_1);
        mPgb2 = findViewById(R.id.pgb_2);
        mTvTimer = findViewById(R.id.tv_timer);
        mTvSongName = findViewById(R.id.tv_dlg_song_name);
        mViewResult = findViewById(R.id.rl_game_result);
        tvWinner = findViewById(R.id.tv_winner);
        tvResultUser1 = findViewById(R.id.tv_result_user1);
        tvResultUser2 = findViewById(R.id.tv_result_user2);
        tvResultUser1Score = findViewById(R.id.tv_result_score1);
        tvResultUser2Score = findViewById(R.id.tv_result_score2);
        ivWinner = findViewById(R.id.iv_winner);
        ivResultUser1 = findViewById(R.id.iv_result_user1);
        ivResultUser2 = findViewById(R.id.iv_result_user2);
        mViewGiveUp = findViewById(R.id.rl_give_up);
        tvGiveUpTimer = findViewById(R.id.tv_give_timer);

        barrageView = findViewById(R.id.barrage);
        barrageView.initDanmaku();

        EventBus.getDefault().register(this);

    }

    @Override
    public void show() {
        super.show();
    }

    public void setTimer(int time) {
        mTvTimer.setText(String.valueOf(time));
    }

    public void showTimeView(boolean show) {
        mViewTimer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setUsers(final UserBase user1, final UserBase user2) {
        mTvDlgUser1.setText(user1.name);
        mTvUser1.setText(user1.name);
        tvResultUser1.setText(user1.name);
        mTvDlgUser2.setText(user2.name);
        mTvUser2.setText(user2.name);
        tvResultUser2.setText(user2.name);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user1.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, mIvAvatar1);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user1.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, mIvDlgAvatar1);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user1.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, ivResultUser1);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user2.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, mIvAvatar2);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user2.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, mIvDlgAvatar2);
        GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(user2.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, false, ivResultUser2);
    }

    public void showScoreLines(boolean show) {
        surfaceView.setDrawScoreView(show);
    }

    public void showResult(int myScore, int score, UserBase winner) {
        showScoreLines(false);
        mViewResult.setVisibility(View.VISIBLE);
        tvResultUser1Score.setText(String.valueOf(myScore));
        tvResultUser2Score.setText(String.valueOf(score));
        if (winner == null) {//平手
            tvWinner.setVisibility(View.INVISIBLE);
            ivWinner.setVisibility(View.INVISIBLE);
            findViewById(R.id.iv_cup).setVisibility(View.INVISIBLE);
            findViewById(R.id.iv_win_tag).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_tie).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tv_tie).setVisibility(View.GONE);
            tvWinner.setText(String.valueOf(winner.name));
            GlideUtils.loadCornersImage(getContext(), ServerFileUtil.getImageUrl(winner.avatar), R.drawable.ic_avatar, R.drawable.ic_avatar, 100, true, ivWinner);

        }
    }


    public void setScore1(final int score1) {
        mTvScore1.setText(String.valueOf(score1 >= 10 ? score1 : ("0" + score1)));
        mPgb1.setProgress(score1);
    }

    public void setScore2(final int score2) {
        mTvScore2.setText(String.valueOf(score2 >= 10 ? score2 : ("0" + score2)));
        mPgb2.setProgress(score2);
    }

    public void setSong(Song song) {
        mTvSongName.setText("《" + song.songName + "》");
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }


    public void tipOperation(int resId, int resText, boolean autoDismiss) {
        tipOperation(resId, resText <= 0 ? "" : getContext().getString(resText), autoDismiss);
    }

    public void tipOperation(int resId, String text, boolean autoDismiss) {
        mTvCenter.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mTvCenter.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
        if (TextUtils.isEmpty(text)) {
            mTvCenter.setText("");
        } else {
            mTvCenter.setText(text);
        }
        if (autoDismiss) {
            mTvCenter.removeCallbacks(runOperation);
            mTvCenter.postDelayed(runOperation, 2000);
        }
    }

    public Runnable runOperation = new Runnable() {
        @Override
        public void run() {
            mTvCenter.setVisibility(View.GONE);
        }
    };

    @Subscribe
    public void onEventMainThread(BusEvent event) {
//        Logger.i(TAG, "onEventMainThread ID:" + event.id);
        switch (event.id) {
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
                //TODO 发生给对方

                break;
            default:
                break;
        }
    }


    public void showService(boolean show) {
        mIvService.setVisibility(show ? View.VISIBLE : View.GONE);
        tipOperation(show ? R.drawable.tv_service_on : R.drawable.tv_service_cancel, show ? R.string.service_on : R.string.service_cancel, true);
    }

    private OnPkTvListener mOnPkTvListener;

    public void setOnPkTvListener(OnPkTvListener listener) {
        this.mOnPkTvListener = listener;
    }

    public interface OnPkTvListener {
        void onPkDanmaku(UserBase userBase, String text);
    }


    public void showGiveUpTimer() {
        mViewGiveUp.setVisibility(View.VISIBLE);
    }

    public void setTvGiveUpTimer(int time) {
        tvGiveUpTimer.setText(String.valueOf(time));
    }


}

