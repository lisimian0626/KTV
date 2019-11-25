package com.beidousat.karaoke.ui.fragment.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.play.DlgGameLevel;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.data.GameStatus;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.proto.device.DeviceProto;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;


/**
 * Created by J Wong on 2016/6/26.
 */
public class FmGame extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private ImageView mIvLevel1Status, mIvLevel2Status, mIvLevel3Status, mIvLevel4Status;
    private ImageView mIvLevel1Title, mIvLevel2Title, mIvLevel3Title, mIvLevel4Title;
    private ImageView mIvLevel1Icon, mIvLevel2Icon, mIvLevel3Icon, mIvLevel4Icon;
    private ImageView mIvLevel1Start, mIvLevel2Start, mIvLevel3Start, mIvLevel4Start;
    private ImageView mIvLevel1Bg, mIvLevel2Bg, mIvLevel3Bg, mIvLevel4Bg;
    private GameStatus mGameStatus;
    private int mLevel = -1, mCurPage = -1, mSelPs = -1;
    private DlgGameLevel mDlgGameLevel;

    /**
     * 是否开始游戏
     *
     * @param level   <=0 不开始游戏
     *                关卡
     * @param curPage 第几页
     * @param selPs   第几首
     * @return fragment
     */
    public static FmGame newInstance(int level, int curPage, int selPs) {
        FmGame fragment = new FmGame();
        Bundle args = new Bundle();
        args.putInt("level", level);
        args.putInt("curPage", curPage);
        args.putInt("selPs", selPs);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLevel = getArguments().getInt("level", -1);
            mCurPage = getArguments().getInt("curPage", -1);
            mSelPs = getArguments().getInt("selPs", -1);
        }
        mGameStatus = GameStatus.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_level1_start:
                if (mDlgGameLevel == null || !mDlgGameLevel.isShowing()) {
                    mDlgGameLevel = new DlgGameLevel(getActivity(), 1, -1, -1);
                    mDlgGameLevel.show();
                }
                break;
            case R.id.iv_level2_start:
                if (mDlgGameLevel == null || !mDlgGameLevel.isShowing()) {
                    mDlgGameLevel = new DlgGameLevel(getActivity(), 2, -1, -1);
                    mDlgGameLevel.show();
                }
                break;
            case R.id.iv_level3_start:
                if (mDlgGameLevel == null || !mDlgGameLevel.isShowing()) {
                    mDlgGameLevel = new DlgGameLevel(getActivity(), 3, -1, -1);
                    mDlgGameLevel.show();
                }
                break;
            case R.id.iv_level4_start:
                if (mDlgGameLevel == null || !mDlgGameLevel.isShowing()) {
                    mDlgGameLevel = new DlgGameLevel(getActivity(), 4, -1, -1);
                    mDlgGameLevel.show();
                }
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_game, null);

        mIvLevel1Title = mRootView.findViewById(R.id.iv_level1_name);
        mIvLevel2Title = mRootView.findViewById(R.id.iv_level2_name);
        mIvLevel3Title = mRootView.findViewById(R.id.iv_level3_name);
        mIvLevel4Title = mRootView.findViewById(R.id.iv_level4_name);

        mIvLevel1Icon = mRootView.findViewById(R.id.iv_level1_icon);
        mIvLevel2Icon = mRootView.findViewById(R.id.iv_level2_icon);
        mIvLevel3Icon = mRootView.findViewById(R.id.iv_level3_icon);
        mIvLevel4Icon = mRootView.findViewById(R.id.iv_level4_icon);

        mIvLevel1Status = mRootView.findViewById(R.id.iv_level1_status);
        mIvLevel2Status = mRootView.findViewById(R.id.iv_level2_status);
        mIvLevel3Status = mRootView.findViewById(R.id.iv_level3_status);
        mIvLevel4Status = mRootView.findViewById(R.id.iv_level4_status);

        mIvLevel1Start = mRootView.findViewById(R.id.iv_level1_start);
        mIvLevel1Start.setOnClickListener(this);
        mIvLevel2Start = mRootView.findViewById(R.id.iv_level2_start);
        mIvLevel2Start.setOnClickListener(this);
        mIvLevel3Start = mRootView.findViewById(R.id.iv_level3_start);
        mIvLevel3Start.setOnClickListener(this);
        mIvLevel4Start = mRootView.findViewById(R.id.iv_level4_start);
        mIvLevel4Start.setOnClickListener(this);

        mIvLevel1Bg = mRootView.findViewById(R.id.iv_level1_bg);
        mIvLevel2Bg = mRootView.findViewById(R.id.iv_level2_bg);
        mIvLevel3Bg = mRootView.findViewById(R.id.iv_level3_bg);
        mIvLevel4Bg = mRootView.findViewById(R.id.iv_level4_bg);


        initViews();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        startGame();
        return mRootView;
    }

    private void startGame() {
        if (mLevel > 0 && mCurPage >= 0 && mSelPs >= 0) {
            if (mDlgGameLevel == null || !mDlgGameLevel.isShowing()) {
                FragmentUtil.addFragment(new FmGame(), false, true, true, true);
                mDlgGameLevel = new DlgGameLevel(getContext(), mLevel, mCurPage, mSelPs);
                mDlgGameLevel.show();
            } else {
                mDlgGameLevel.loadAndStart(mLevel, mCurPage, mSelPs);
            }
        }
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEven(BusEvent event) {
        switch (event.id) {
            case EventBusId.Game.GAME_LEVEL_PASS_CHANGED:
                initViews();
                break;
            case EventBusId.ImId.MAIN_GAME_STATUS:
                DeviceProto.GameStatus gameStatus = (DeviceProto.GameStatus) event.data;
                if (gameStatus.getPlayStatus() == 4) {
                    if (mDlgGameLevel != null) {
                        mDlgGameLevel.exitGame();
                    }
                }
                //通过关卡
                mGameStatus.setPassLevel(gameStatus.getPassLevel(), new Random().nextInt(3));
                break;
            case EventBusId.DeviceId.GAME_START:
                try {
                    DeviceProto.GameStart msg = (DeviceProto.GameStart) event.data;
                    mLevel = msg.getLevel();
                    mCurPage = msg.getPage();
                    mSelPs = msg.getPosition();
                    startGame();
                } catch (Exception e) {
                    Logger.d("FmGame", "GAME_START ex:" + e.toString());
                }
                break;
            default:
                break;
        }
    }

    public int getPassTutor(int tutorRes) {
        int resId = 0;
        switch (tutorRes) {
            case 0:
                resId = R.drawable.play_game_level_pass_star01;
                break;
            case 1:
                resId = R.drawable.play_game_level_pass_star02;
                break;
            case 2:
                resId = R.drawable.play_game_level_pass_star03;
                break;
            case 3:
                resId = R.drawable.play_game_level_pass_star04;
                break;
        }
        return resId;
    }

    private void initViews() {
        int level = mGameStatus.getPassLevel();
        switch (level) {
            case 0:
                mIvLevel1Status.setVisibility(View.GONE);
                mIvLevel1Bg.setEnabled(true);
                mIvLevel1Title.setEnabled(true);
                mIvLevel1Start.setEnabled(true);
                mIvLevel1Icon.setImageResource(R.drawable.play_game_level_new);

                mIvLevel2Status.setVisibility(View.GONE);
                mIvLevel2Bg.setEnabled(false);
                mIvLevel2Title.setEnabled(false);
                mIvLevel2Start.setEnabled(false);
                mIvLevel2Icon.setImageResource(R.drawable.play_game_level_disable);

                mIvLevel3Status.setVisibility(View.GONE);
                mIvLevel3Bg.setEnabled(false);
                mIvLevel3Title.setEnabled(false);
                mIvLevel3Start.setEnabled(false);
                mIvLevel3Icon.setImageResource(R.drawable.play_game_level_disable);

                mIvLevel4Status.setVisibility(View.GONE);
                mIvLevel4Bg.setEnabled(false);
                mIvLevel4Title.setEnabled(false);
                mIvLevel4Start.setEnabled(false);
                mIvLevel4Icon.setImageResource(R.drawable.play_game_level_disable);
                break;
            case 1:
                mIvLevel1Status.setVisibility(View.VISIBLE);
                mIvLevel1Bg.setEnabled(true);
                mIvLevel1Title.setEnabled(true);
                mIvLevel1Start.setEnabled(true);
                mIvLevel1Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(level)));

                mIvLevel2Status.setVisibility(View.GONE);
                mIvLevel2Bg.setEnabled(true);
                mIvLevel2Title.setEnabled(true);
                mIvLevel2Start.setEnabled(true);
                mIvLevel2Icon.setImageResource(R.drawable.play_game_level_new);

                mIvLevel3Status.setVisibility(View.GONE);
                mIvLevel3Bg.setEnabled(false);
                mIvLevel3Title.setEnabled(false);
                mIvLevel3Start.setEnabled(false);
                mIvLevel3Icon.setImageResource(R.drawable.play_game_level_disable);

                mIvLevel4Status.setVisibility(View.GONE);
                mIvLevel4Bg.setEnabled(false);
                mIvLevel4Title.setEnabled(false);
                mIvLevel4Start.setEnabled(false);
                mIvLevel4Icon.setImageResource(R.drawable.play_game_level_disable);
                break;
            case 2:
                mIvLevel1Status.setVisibility(View.VISIBLE);
                mIvLevel1Bg.setEnabled(true);
                mIvLevel1Title.setEnabled(true);
                mIvLevel1Start.setEnabled(true);
                mIvLevel1Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(1)));

                mIvLevel2Status.setVisibility(View.VISIBLE);
                mIvLevel2Bg.setEnabled(true);
                mIvLevel2Title.setEnabled(true);
                mIvLevel2Start.setEnabled(true);
                mIvLevel2Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(2)));

                mIvLevel3Status.setVisibility(View.GONE);
                mIvLevel3Bg.setEnabled(true);
                mIvLevel3Title.setEnabled(true);
                mIvLevel3Start.setEnabled(true);
                mIvLevel3Icon.setImageResource(R.drawable.play_game_level_new);

                mIvLevel4Status.setVisibility(View.GONE);
                mIvLevel4Bg.setEnabled(false);
                mIvLevel4Title.setEnabled(false);
                mIvLevel4Start.setEnabled(false);
                mIvLevel4Icon.setImageResource(R.drawable.play_game_level_disable);
                break;
            case 3:
                mIvLevel1Status.setVisibility(View.VISIBLE);
                mIvLevel1Bg.setEnabled(true);
                mIvLevel1Title.setEnabled(true);
                mIvLevel1Start.setEnabled(true);
                mIvLevel1Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(1)));

                mIvLevel2Status.setVisibility(View.VISIBLE);
                mIvLevel2Bg.setEnabled(true);
                mIvLevel2Title.setEnabled(true);
                mIvLevel2Start.setEnabled(true);
                mIvLevel2Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(2)));

                mIvLevel3Status.setVisibility(View.VISIBLE);
                mIvLevel3Bg.setEnabled(true);
                mIvLevel3Title.setEnabled(true);
                mIvLevel3Start.setEnabled(true);
                mIvLevel3Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(3)));

                mIvLevel4Status.setVisibility(View.GONE);
                mIvLevel4Bg.setEnabled(true);
                mIvLevel4Title.setEnabled(true);
                mIvLevel4Start.setEnabled(true);
                mIvLevel4Icon.setImageResource(R.drawable.play_game_level_new);
                break;
            case 4:
                mIvLevel1Status.setVisibility(View.VISIBLE);
                mIvLevel1Bg.setEnabled(true);
                mIvLevel1Title.setEnabled(true);
                mIvLevel1Start.setEnabled(true);
                mIvLevel1Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(1)));

                mIvLevel2Status.setVisibility(View.VISIBLE);
                mIvLevel2Bg.setEnabled(true);
                mIvLevel2Title.setEnabled(true);
                mIvLevel2Start.setEnabled(true);
                mIvLevel2Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(2)));

                mIvLevel3Status.setVisibility(View.VISIBLE);
                mIvLevel3Bg.setEnabled(true);
                mIvLevel3Title.setEnabled(true);
                mIvLevel3Start.setEnabled(true);
                mIvLevel3Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(3)));

                mIvLevel4Status.setVisibility(View.VISIBLE);
                mIvLevel4Bg.setEnabled(true);
                mIvLevel4Title.setEnabled(true);
                mIvLevel4Start.setEnabled(true);
                mIvLevel4Icon.setImageResource(getPassTutor(mGameStatus.getPassTutor(4)));
                break;
        }
    }
}
