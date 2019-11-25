package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.helper.CornerStyleHelper;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;

/**
 * Created by J Wong on 2017/9/26.
 */

public class WidgetAdCorner extends RelativeLayout {

    private View mRootView;
    private RecyclerImageView mRivAd, rivBgTop, rivBgBottom;
    private TextView mTvCurSong, mTvNextSong;

    public WidgetAdCorner(Context context) {
        super(context);
        initView();
    }

    public WidgetAdCorner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetAdCorner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_ad_corner, this);
        rivBgTop = mRootView.findViewById(R.id.bg_cur_next);
        rivBgBottom = mRootView.findViewById(R.id.bg_ad);
        mRivAd = mRootView.findViewById(R.id.iv_ads);
        mTvCurSong = mRootView.findViewById(R.id.tv_current_song);
        mTvNextSong = mRootView.findViewById(R.id.tv_next_song);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == VISIBLE) {
//        }
    }

    public void setCurText(String text) {
        mTvCurSong.setText(text);
    }

    public void setNextText(String text) {
        mTvNextSong.setText(text);
    }

    public void loadAd(ADModel adModel) {
        requestCornerStyle();
        if (adModel != null && !TextUtils.isEmpty(adModel.getAdContent())) {
            GlideUtils.LoadCornersImage(getContext(), adModel.getAdContent(), R.drawable.ad_corner_default, R.drawable.ad_corner_default, 10, false, mRivAd);
            new AdPlayRecorder(getContext().getApplicationContext()).recordAdPlay(adModel);
        } else {
            GlideUtils.LoadImage(getContext(), R.drawable.ad_corner_default, false, mRivAd);
        }
    }

    private void requestCornerStyle() {
        CornerStyleHelper helper = new CornerStyleHelper(getContext());
        helper.setCornerStyleCallback(cornerStyle -> {
            if (!TextUtils.isEmpty(cornerStyle.color)) {
                try {
                    if (!cornerStyle.color.startsWith("#")) {
                        cornerStyle.color = "#" + cornerStyle.color;
                    }
                    mTvCurSong.setTextColor(Color.parseColor(cornerStyle.color));
                } catch (Exception e) {
                    Log.w("WidgetAdCorner", "颜色转换出错了", e);
                }
            }
            if (!TextUtils.isEmpty(cornerStyle.img1Url)) {
                GlideUtils.LoadImage(getContext(), cornerStyle.img1Url, R.drawable.bg_ad_corner_top, R.drawable.bg_ad_corner_top, false, rivBgTop);
            }
            if (!TextUtils.isEmpty(cornerStyle.img2Url)) {
                GlideUtils.LoadImage(getContext(), cornerStyle.img2Url, R.drawable.bg_ad_corner_bottom, R.drawable.bg_ad_corner_bottom, false, rivBgBottom);
            }
        });
        helper.getCornerStyle();
    }
}
