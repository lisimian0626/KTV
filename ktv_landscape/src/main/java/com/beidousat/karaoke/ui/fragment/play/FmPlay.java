package com.beidousat.karaoke.ui.fragment.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libskin.SkinManager;


/**
 * Created by J Wong on 2015/12/16 16:13.
 */
public class FmPlay extends BaseFragment implements View.OnClickListener {

    private View mRootView;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_play, null);
        mRootView.findViewById(R.id.wm_pk).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_mv).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_game).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_live).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_hyun).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_adventure).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_skin).setOnClickListener(this);
        SkinManager.getInstance().register(mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wm_pk:
                PromptDialogSmall promptDialogSmall = new PromptDialogSmall(getContext());
                promptDialogSmall.setMessage(R.string.ready_tips);
                promptDialogSmall.show();
//                Toast.makeText(getContext(),getString(R.string.ready_tips),Toast.LENGTH_SHORT).show();
//                FragmentUtil.addFragment(FmPk.newInstance(), false, false, true, false);
                break;
            case R.id.wm_mv:
                FragmentUtil.addFragment(FmMv.newInstance(), false, false, false, false);
                break;
            case R.id.wm_game:
                FragmentUtil.addFragment(FmGame.newInstance(-1, -1, -1), false, true, true, false);
                break;
            case R.id.wm_live:
                FragmentUtil.addFragment(new FmLives(), false, false, false, false);
                break;
            case R.id.wm_adventure:
                if (DeviceHelper.isMainVod(getContext())) {
                    FragmentUtil.addFragment(new FmGameLuckyMonkey(), false, true, true, true);
                } else {
                    PromptDialogSmall promptDialog = new PromptDialogSmall(getContext());
                    promptDialog.setMessage(R.string.play_on_main_only_tip);
                    promptDialog.show();
                }
                break;
            case R.id.wm_hyun:
                FragmentUtil.addFragment(new FmHyun(), false, false, true, false);
                break;
            case R.id.wm_skin:
                FragmentUtil.addFragment(new FmSkin(), false, false, false, false);
                break;
        }
    }

//    private DlgPassword mDlgPassword;
//
//    private void showPwd() {
//        if (mDlgPassword == null || !mDlgPassword.isShowing()) {
//            mDlgPassword = new DlgPassword(Main.mMainActivity, VodConfigData.getInstance().moviePassword());
//            mDlgPassword.setTitle(R.string.pls_input_pwd);
//            mDlgPassword.setOnMngPwdListener(new DlgPassword.OnMngPwdListener() {
//                @Override
//                public void onPass() {
//                    FmMovies fmMovies = FmMovies.newInstance();
//                    FragmentUtil.addFragment(fmMovies, false);
//                }
//            });
//            mDlgPassword.show();
//        }
//    }
}
