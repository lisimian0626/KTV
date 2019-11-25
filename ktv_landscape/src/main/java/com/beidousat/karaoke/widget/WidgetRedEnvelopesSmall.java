package com.beidousat.karaoke.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.CircleImageView;
import com.bumptech.glide.Glide;

/**
 * Created by J Wong on 2015/10/15 13:35.
 */
public class WidgetRedEnvelopesSmall extends RelativeLayout {

    private View mRootView;
    private CircleImageView rivLogo;
    private View mShake;

    public WidgetRedEnvelopesSmall(Context context) {
        super(context);
        initView();
    }

    public WidgetRedEnvelopesSmall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public WidgetRedEnvelopesSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_red_envelopes_small, this);
        rivLogo = mRootView.findViewById(R.id.riv_ad_small);
        mShake = mRootView.findViewById(R.id.riv_shake);
    }

    public void loadLogo(ADModel ad) {
        Glide.with(getContext()).load(ServerFileUtil.getImageUrl(ad.getAdContent())).error(R.drawable.logo).into(rivLogo);
    }

    public void startShake() {
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.amin_shake_red_packet);
        shake.setFillAfter(true);
        mShake.startAnimation(shake);
    }

    public void stopShake() {
        mShake.clearAnimation();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mShake.clearAnimation();
    }
}
