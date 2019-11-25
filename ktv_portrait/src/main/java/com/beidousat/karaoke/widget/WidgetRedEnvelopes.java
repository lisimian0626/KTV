package com.beidousat.karaoke.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.anim.FlipAnimation;
import com.bestarmedia.libwidget.image.CircleImageView;
import com.bumptech.glide.Glide;

/**
 * Created by J Wong on 2015/10/15 13:35.
 */
public class WidgetRedEnvelopes extends RelativeLayout {

    private View mRootView;
    private TextView mTvAdName;
    private CircleImageView rivAd;
    private View rivGold, rlAd, llRedEnvelope;
    private Animation mAnimCornerIn, mAnimCornerOut;
    private final static int ANIM_DURATION = 1000;


    public WidgetRedEnvelopes(Context context) {
        super(context);
        initView();
    }

    public WidgetRedEnvelopes(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public WidgetRedEnvelopes(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cleanAll();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == INVISIBLE || visibility == GONE) {
            //cleanAll();
        }
    }

    public void cleanAll() {
        rivGold.clearAnimation();
        rlAd.clearAnimation();
        llRedEnvelope.clearAnimation();
        handler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        mAnimCornerOut = AnimationUtils.loadAnimation(getContext(), R.anim.amin_red_envelopes_move_right);
        mAnimCornerIn = AnimationUtils.loadAnimation(getContext(), R.anim.amin_red_envelopes_move_left);
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_red_envelopes, this);
        rivGold = mRootView.findViewById(R.id.riv_gold);
        llRedEnvelope = mRootView.findViewById(R.id.ll_red_envelopes);
        rlAd = mRootView.findViewById(R.id.ll_ad);
        mTvAdName = mRootView.findViewById(R.id.tv_ad_name);
        rivAd = mRootView.findViewById(R.id.riv_ad);
    }


    public void showRedEnvelopes(ADModel ad) {
        cleanAll();
        rlAd.setVisibility(GONE);
        rivGold.setVisibility(GONE);
        llRedEnvelope.setVisibility(VISIBLE);
        llRedEnvelope.startAnimation(FlipAnimation.create(FlipAnimation.LEFT, true, ANIM_DURATION));
        handler.postDelayed(runnable, 2000);
        loadAd(ad);
    }

    public void setRedPacketAnim(boolean enter) {
        if (enter) {
            llRedEnvelope.startAnimation(mAnimCornerIn);
        } else {
            llRedEnvelope.startAnimation(mAnimCornerOut);
            handler.postDelayed(() -> {
                rivGold.setVisibility(GONE);
                rlAd.setVisibility(GONE);
                AnimationSet aniSet = new AnimationSet(true);
                ScaleAnimation scaleAni = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAni.setDuration(ANIM_DURATION); // 设置动画效果时间
                aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中
                llRedEnvelope.startAnimation(aniSet);
                handler.postDelayed(() -> {
                    llRedEnvelope.setVisibility(GONE);
                    cleanAll();
                    if (mRedEnvelopesListener != null)
                        mRedEnvelopesListener.onRedEnvelopeDismiss();
                }, ANIM_DURATION);
            }, mAnimCornerOut.getDuration());
        }
    }


    public void setAdAnim(View v, boolean enter) {
        AnimationSet aniSet = new AnimationSet(true);
        if (enter) {
            // 尺寸变化动画，设置尺寸变化
            ScaleAnimation scaleAni = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAni.setDuration(ANIM_DURATION); // 设置动画效果时间
            aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中
            // 透明度变化
            AlphaAnimation alphaAni = new AlphaAnimation(0.0f, 1.0f);
            alphaAni.setDuration(ANIM_DURATION); // 设置动画效果时间
            aniSet.addAnimation(alphaAni);  // 将动画效果添加到动画集中
        } else {
            // 尺寸变化动画，设置尺寸变化
            ScaleAnimation scaleAni = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAni.setDuration(ANIM_DURATION); // 设置动画效果时间
            aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中
            // 透明度变化
            AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0.0f);
            alphaAni.setDuration(ANIM_DURATION); // 设置动画效果时间
            aniSet.addAnimation(alphaAni);  // 将动画效果添加到动画集中
        }
        v.startAnimation(aniSet);       // 添加光效动画到控件
    }

    private void setGoldAnim(View v, boolean enter) {
        AnimationSet aniSet = new AnimationSet(true);
        if (enter) {
            ScaleAnimation scaleAni = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAni.setDuration(ANIM_DURATION); // 设置动画效果时间
            aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中

            AlphaAnimation alphaAni = new AlphaAnimation(0.0f, 1.0f);
            alphaAni.setDuration(ANIM_DURATION);  // 设置动画效果时间
            aniSet.addAnimation(alphaAni);  // 将动画效果添加到动画集中

            // 旋转
            RotateAnimation rotateAni = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAni.setDuration(ANIM_DURATION);
            aniSet.addAnimation(rotateAni);

        } else {
//            ScaleAnimation scaleAni = new ScaleAnimation(0.1f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            scaleAni.setDuration(duration);  // 设置动画效果时间
//            aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中

            AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0.0f);
            alphaAni.setDuration(ANIM_DURATION);  // 设置动画效果时间
            aniSet.addAnimation(alphaAni);  // 将动画效果添加到动画集中

//            // 旋转
//            RotateAnimation rotateAni = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            rotateAni.setDuration(duration);
//            aniSet.addAnimation(rotateAni);

        }

        v.startAnimation(aniSet);       // 添加光效动画到控件
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setRedPacketAnim(true);
            rlAd.setVisibility(VISIBLE);
            rivGold.setVisibility(VISIBLE);
            setAdAnim(rlAd, true);
            setGoldAnim(rivGold, true);
            handler.postDelayed(runnableDismiss, 10 * 1000);
        }
    };


    private Runnable runnableDismiss = new Runnable() {
        @Override
        public void run() {
            setAdAnim(rlAd, false);
            setGoldAnim(rivGold, false);
            handler.postDelayed(() -> {
                rivGold.setVisibility(INVISIBLE);
                rlAd.setVisibility(INVISIBLE);
                setRedPacketAnim(false);
            }, 1000);
        }
    };

    private RedEnvelopesListener mRedEnvelopesListener;

    public void setRedEnvelopesListener(RedEnvelopesListener listener) {
        this.mRedEnvelopesListener = listener;
    }

    public void loadAd(ADModel ad) {
        Uri img = ServerFileUtil.getImageUrl(ad.getAdContent());
        Glide.with(getContext()).load(img).error(R.drawable.logo).into(rivAd);
        mTvAdName.setText(getContext().getString(R.string.red_packet_tip_x, ad.getBrandName()));
    }

    public interface RedEnvelopesListener {
        void onRedEnvelopeDismiss();
    }
}
