package com.beidousat.karaoke.ui.fragment.setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.PrefSettingUtil;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.text.DecimalFormat;

/**
 * Created by J Wong on 2017/5/19.
 */
public class FmSettingVideo extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private RecyclerImageView mRivOriginal;
    private TextView mTvOriginal, mTvEnlarge, mTvShrink, mTvPercent;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_setting_video, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);

//        mRivEnlarge = (RecyclerImageView) mRootView.findViewById(R.id.riv_enlarge);
//        mRivEnlarge.setOnClickListener(this);
//
//        mRivShrink = (RecyclerImageView) mRootView.findViewById(R.id.riv_shrink);
//        mRivShrink.setOnClickListener(this);
        mTvPercent = mRootView.findViewById(R.id.tv_percent);

        mRivOriginal = mRootView.findViewById(R.id.riv_original);
        mRivOriginal.setOnClickListener(this);

        mTvOriginal = mRootView.findViewById(R.id.tv_origin);
        mTvOriginal.setOnClickListener(this);

        mTvEnlarge = mRootView.findViewById(R.id.tv_enlarge);
        mTvEnlarge.setOnClickListener(this);

        mTvShrink = mRootView.findViewById(R.id.tv_shrink);
        mTvShrink.setOnClickListener(this);

//        changeMode();
        resizeImg();

        return mRootView;
    }

//    private void changeMode() {
//
//        int mode = PrefSettingUtil.getHdmiMode(getContext().getApplicationContext());
//
//        mTvEnlarge.setSelected(mode == 1);
//        mRivEnlarge.setSelected(mode == 1);
//
//        mTvOriginal.setSelected(mode == 0);
//        mRivOriginal.setSelected(mode == 0);
//
//        mTvShrink.setSelected(mode == -1);
//        mRivShrink.setSelected(mode == -1);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.tv_enlarge:
                float scale = PrefSettingUtil.getHdmiVideoScale2(getContext().getApplicationContext());
                scale = scale + 0.01f;
                if (scale < 1.2f) {
                    PrefSettingUtil.setHdmiVideoScale2(getContext().getApplicationContext(), scale);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, scale);
                    resizeImg();
                }
//                if (PrefSettingUtil.getHdmiMode(getContext().getApplicationContext()) != 1) {
//                    PrefSettingUtil.setHdmiMode(getContext().getApplicationContext(), 1);
//                    changeMode();
//                    tipResetHdmi();
//                }
                break;
            case R.id.tv_origin:
                PrefSettingUtil.setHdmiVideoScale2(getContext().getApplicationContext(), 1.0F);
                EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, 1.0F);
                resizeImg();
//                if (PrefSettingUtil.getHdmiMode(getContext().getApplicationContext()) != 0) {
//                    PrefSettingUtil.setHdmiMode(getContext().getApplicationContext(), 0);
//                    changeMode();
//                    tipResetHdmi();
//                }
                break;
            case R.id.tv_shrink:
                scale = PrefSettingUtil.getHdmiVideoScale2(getContext().getApplicationContext());
                scale = scale - 0.01f;
                if (scale > 0.8f) {
                    PrefSettingUtil.setHdmiVideoScale2(getContext().getApplicationContext(), scale);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, scale);
                    resizeImg();
                }
//                if (PrefSettingUtil.getHdmiMode(getContext().getApplicationContext()) != -1) {
//                    PrefSettingUtil.setHdmiMode(getContext().getApplicationContext(), -1);
//                    changeMode();
//                    tipResetHdmi();
//                }
                break;
        }
    }

    private void resizeImg() {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mRivOriginal.getLayoutParams();
        float videoScale = PrefSettingUtil.getHdmiVideoScale2(getContext().getApplicationContext());
        linearParams.height = (int) (DensityUtil.dip2px(getContext(), 262) * videoScale);
        linearParams.width = (int) (DensityUtil.dip2px(getContext(), 456) * videoScale);
        mRivOriginal.setLayoutParams(linearParams);
//        String percent = (videoScale * 100) + "%";
        DecimalFormat df = new DecimalFormat("#%");
        String percent = df.format((videoScale));
        mTvPercent.setText(percent);
    }

    private void tipResetHdmi() {
        PromptDialogSmall dialog = new PromptDialogSmall(getContext());
        dialog.setMessage(R.string.set_video_enlarge_tip);
        dialog.setOkButton(true, getString(R.string.reset_hdmi), v -> {
            //
            exitApp();
        });
        dialog.show();
    }

    private void exitApp() {
        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
        getActivity().finish();
//        ServiceHelper.getInstance(getContext().getApplicationContext()).stopService();
        android.os.Process.killProcess(android.os.Process.myPid());//获取PID
        System.exit(0);//直接结束程序
    }
}
