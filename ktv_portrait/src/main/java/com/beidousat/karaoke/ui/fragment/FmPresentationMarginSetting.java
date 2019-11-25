package com.beidousat.karaoke.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import java.text.DecimalFormat;

/**
 * Created by J Wong on 2019/6/13.
 */
public class FmPresentationMarginSetting extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private RecyclerImageView mRivOriginal;
    private TextView mTvOriginal, mTvEnlarge, mTvShrink, mTvPercent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_presentation_margin_setting, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mTvPercent = mRootView.findViewById(R.id.tv_percent);

        mRivOriginal = mRootView.findViewById(R.id.riv_original);
        mRivOriginal.setOnClickListener(this);

        mTvOriginal = mRootView.findViewById(R.id.tv_origin);
        mTvOriginal.setOnClickListener(this);

        mTvEnlarge = mRootView.findViewById(R.id.tv_enlarge);
        mTvEnlarge.setOnClickListener(this);

        mTvShrink = mRootView.findViewById(R.id.tv_shrink);
        mTvShrink.setOnClickListener(this);

        resizeImg();

        return mRootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.tv_enlarge:
                float scale = PrefData.getHdmiVideoScale2(getContext().getApplicationContext());
                scale = scale + 0.01f;
                if (scale <= 1.0f) {
                    PrefData.setHdmiVideoScale2(getContext().getApplicationContext(), scale);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, scale);
                    resizeImg();
                }
                break;
            case R.id.tv_origin:
                PrefData.setHdmiVideoScale2(getContext().getApplicationContext(), 1.0f);
                EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, 1.0f);
                resizeImg();
                break;
            case R.id.tv_shrink:
                scale = PrefData.getHdmiVideoScale2(getContext().getApplicationContext());
                scale = scale - 0.01f;
                if (scale > 0.8f) {
                    PrefData.setHdmiVideoScale2(getContext().getApplicationContext(), scale);
                    EventBusUtil.postSticky(EventBusId.PresentationId.PRESENTATION_MARGIN_CHANGED, scale);
                    resizeImg();
                }
                break;
        }
    }

    private void resizeImg() {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mRivOriginal.getLayoutParams();
        float videoScale = PrefData.getHdmiVideoScale2(getContext().getApplicationContext());
        linearParams.height = (int) (DensityUtil.dip2px(getContext(), 262) * videoScale);
        linearParams.width = (int) (DensityUtil.dip2px(getContext(), 456) * videoScale);
        mRivOriginal.setLayoutParams(linearParams);
        DecimalFormat df = new DecimalFormat("#%");
        String percent = df.format((videoScale));
        mTvPercent.setText(percent);
    }

}
