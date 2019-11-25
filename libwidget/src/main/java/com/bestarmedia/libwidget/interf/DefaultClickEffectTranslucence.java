package com.bestarmedia.libwidget.interf;

import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 点击时缩小动画变半透明动画
 */
public class DefaultClickEffectTranslucence implements ViewClickEffect {
    /**
     * 先快后慢的动画效果,可自行替换其它效果
     */
    private TimeInterpolator interpolator = new DecelerateInterpolator();
    /**
     * 点击时缩小的比例
     */
    private static final float alpha = 0.7f;
    /**
     * 点击动画持续时间
     */
    private static final int duration = 100;


    @Override
    public void onPressedEffect(View view) {
        view.animate().alpha(alpha).setDuration(duration).setInterpolator(interpolator);
    }

    @Override
    public void onUnPressedEffect(View view) {
        view.animate().alpha(1).setDuration(duration).setInterpolator(interpolator);
    }
}
