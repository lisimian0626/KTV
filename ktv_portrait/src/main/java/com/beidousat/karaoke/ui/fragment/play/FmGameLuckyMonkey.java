package com.beidousat.karaoke.ui.fragment.play;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.dialog.play.DlgGameLuckyCount;
import com.beidousat.karaoke.ui.dialog.play.DlgGameLuckyText;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.ui.presentation.LuckyMonkeyPresentation;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by J Wong on 2018/4/24.
 */

public class FmGameLuckyMonkey extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private LuckyMonkeyPresentation mPresentation;
    private final SparseArray<LuckyMonkeyPresentation> mActivePresentations = new SparseArray<>();
    private final static String TAG = "FmGameLuckyMonkey";
    private boolean mIsStarted = false;
    private TextView mTvPeoples, mTvMode1, mTvMode2, mTvMode3;
    private RecyclerImageView mRivService;
    private int mGameLevel = 1;
    private Display mHdmiDisplay = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_game_lucky_monkey, null);
        mRootView.findViewById(R.id.riv_exit).setOnClickListener(this);
        mRootView.findViewById(R.id.riv_go).setOnClickListener(this);
        mRootView.findViewById(R.id.riv_vol_down).setOnClickListener(this);
        mRootView.findViewById(R.id.riv_vol_up).setOnClickListener(this);
        mRivService = mRootView.findViewById(R.id.riv_service);
        mRivService.setOnClickListener(this);

        mTvPeoples = mRootView.findViewById(R.id.tv_male);
        mTvPeoples.setOnClickListener(this);

        mTvMode1 = mRootView.findViewById(R.id.tv_mode1);
        mTvMode2 = mRootView.findViewById(R.id.tv_mode2);
        mTvMode3 = mRootView.findViewById(R.id.tv_mode3);
        mTvMode1.setSelected(true);
        mTvMode1.setOnClickListener(this);
        mTvMode2.setOnClickListener(this);
        mTvMode3.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.PresentationId.FIRE_ACTION) {
            int fireStatus = Integer.valueOf(event.data.toString());
            if (mPresentation != null) {
                mPresentation.showFireTip(fireStatus);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_vol_down:
                VodApplication.getKaraokeController().volumeDown();
                break;
            case R.id.riv_vol_up:
                VodApplication.getKaraokeController().volumeUp();
                break;
            case R.id.riv_service:
                if (VodApplication.getKaraokeController().getButtonStatus().serviceMode >= 0) {
                    VodApplication.getKaraokeController().setServiceMode(-1, true);
                    mRivService.setSelected(false);
                } else {
                    VodApplication.getKaraokeController().setServiceMode(0, true);
                    mRivService.setSelected(true);
                }
                break;
            case R.id.riv_exit:
                closeGame();
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.riv_go:
                initHdmiDisplay();
                if (mHdmiDisplay == null) {
                    PromptDialogSmall dialog = new PromptDialogSmall(getContext());
                    dialog.setMessage(R.string.game_no_tv_tip);
                    dialog.show();
                    return;
                }
                final int peopleCount = Integer.valueOf(mTvPeoples.getText().toString());
                if (peopleCount <= 0) {
                    final DlgGameLuckyText dlgGameLuckyText = new DlgGameLuckyText(getActivity());
                    dlgGameLuckyText.setTestResId(R.drawable.play_adventure_text01);
                    dlgGameLuckyText.setOnNolListener(v1 -> dlgGameLuckyText.dismiss());
                    dlgGameLuckyText.setOnOkListener(v1 -> dlgGameLuckyText.dismiss());
                    dlgGameLuckyText.show();
                    return;
                }
                if (mIsStarted) {
                    if (mPresentation != null) {
                        mPresentation.startGame(mGameLevel, peopleCount);
                    }
                } else {
                    final DlgGameLuckyText dlgGameLuckyTextStart = new DlgGameLuckyText(getActivity());
                    dlgGameLuckyTextStart.setOnOkListener(v1 -> {
                        startGame();
                        if (mPresentation != null) {
                            dlgGameLuckyTextStart.dismiss();
                            mIsStarted = true;
                            mRootView.postDelayed(() -> {
                                if (mPresentation != null)
                                    mPresentation.startGame(mGameLevel, peopleCount);
                            }, 1500);
                        }
                    });
                    dlgGameLuckyTextStart.setOnNolListener(v1 -> dlgGameLuckyTextStart.dismiss());
                    dlgGameLuckyTextStart.show();
                }
                break;
            case R.id.tv_male:
                DlgGameLuckyCount dlgGameLuckyCount = new DlgGameLuckyCount(getActivity());
                dlgGameLuckyCount.setOnGameLuckNumListener(count ->
                        mTvPeoples.setText(String.valueOf(count)));
                dlgGameLuckyCount.show();
                break;
            case R.id.tv_mode1:
                mGameLevel = 1;
                mTvMode1.setSelected(true);
                mTvMode2.setSelected(false);
                mTvMode3.setSelected(false);
                break;
            case R.id.tv_mode2:
                mGameLevel = 2;
                mTvMode1.setSelected(false);
                mTvMode2.setSelected(true);
                mTvMode3.setSelected(false);
                break;
            case R.id.tv_mode3:
                mGameLevel = 3;
                mTvMode1.setSelected(false);
                mTvMode2.setSelected(false);
                mTvMode3.setSelected(true);
                break;
        }
    }


    private void initHdmiDisplay() {
        if (mHdmiDisplay == null && getContext() != null) {
            DisplayManager mDisplayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (int i = 0; i < displays.length; i++) {
                    final Display display = displays[i];
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        mHdmiDisplay = display;
                        break;
                    }
                }
            } else {
                Logger.d(TAG, " Display[] is null");
            }
        }
    }

    private void startGame() {
        if (mHdmiDisplay != null) {
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
            showPresentation(mHdmiDisplay);
        } else {
            Logger.d(TAG, " Display[] is null");
        }
    }


    private void closeGame() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
            mActivePresentations.clear();
        }
    }

    private final DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (mPresentation != null) {
                mPresentation.dismiss();
                mPresentation = null;
            }
            mActivePresentations.clear();
        }
    };

    private void showPresentation(final Display display) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }
        Logger.d(TAG, " Display display.getName():" + display.getName() + "  show");
        mPresentation = new LuckyMonkeyPresentation(VodApplication.getVodApplicationContext(), display);
        if (mPresentation.getWindow() != null) {
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mPresentation.show();
        mPresentation.setOnDismissListener(mOnDismissListener);
        mActivePresentations.put(displayId, mPresentation);
    }
}
