package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.DlgPassword;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.data.VodConfigData;


/**
 * Created by J Wong on 2015/12/16 16:13.
 */
public class FmCategory extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private DlgPassword mDlgPassword;

    //    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return CubeAnimation.create(CubeAnimation.LEFT, enter, 200);
//        //   return FlipAnimation.create(FlipAnimation.LEFT, enter, 200);
//        //   return MoveAnimation.create(MoveAnimation.LEFT, enter, 200);
//// return PushPullAnimation.create(PushPullAnimation.LEFT, enter, 200);
//        //     return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
////        return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
////        return super.onCreateAnimation(transit, enter, nextAnim);
//    }
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_category, null);
        mRootView.findViewById(R.id.wm_pop).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_military).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_dance).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_drama).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_folk_song).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_classial).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_children).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_disco).setOnClickListener(this);
        return mRootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void showPwd() {
        if (mDlgPassword == null || !mDlgPassword.isShowing()) {
            mDlgPassword = new DlgPassword(getActivity(), VodConfigData.getInstance().discoPassword());
            mDlgPassword.setTitle(R.string.pls_input_pwd);
            mDlgPassword.setOnMngPwdListener(() -> {
                FmSongCategory category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.DISCO);
                FragmentUtil.addFragment(category, false, false, true, false);
            });
            mDlgPassword.show();
        }
    }

    @Override
    public void onClick(View view) {
        FmSongCategory category;
        switch (view.getId()) {
            case R.id.wm_pop://流行金曲
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.NORMAL);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_military://军旅
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.RED_SONG);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_dance://舞曲
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.DAMCE);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_drama://戏曲
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.DRAMA);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_folk_song://民歌
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.FOLK);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_children://儿歌
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.CHILDREN);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_classial://古典
                category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.CLASSIC);
                FragmentUtil.addFragment(category, false, false, true, false);
                break;
            case R.id.wm_disco://disco
                if (VodConfigData.getInstance().isDiscoNeedPassword() && !TextUtils.isEmpty(VodConfigData.getInstance().discoPassword())) {//需要密码
                    showPwd();
                } else {
                    category = FmSongCategory.newInstance(FmSongCategory.SongTypeEnum.DISCO);
                    FragmentUtil.addFragment(category, false, false, true, false);
                }
                break;
            default:
                break;
        }
    }
}
