package com.bestarmedia.libwidget.interf;

import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 点击时缩小动画
 */
public class DefaultClickEffectScaleAnimate implements ViewClickEffect {
    /**
     * 先快后慢的动画效果,可自行替换其它效果
     */
    private TimeInterpolator interpolator = new DecelerateInterpolator();

    /**
     * 点击时缩小的比例
     */
    private static final float scale = 0.9f;

    /**
     * 透明度
     private static final float alpha = 0.7f;
     */

    /**
     * 点击动画持续时间
     */
    private static final int duration = 0;


    @Override
    public void onPressedEffect(View view) {
        view.animate().scaleX(scale).scaleY(scale).setDuration(duration).setInterpolator(interpolator);
    }

    @Override
    public void onUnPressedEffect(View view) {
        view.animate().scaleX(1).scaleY(1).setDuration(duration).setInterpolator(interpolator);
    }
}
