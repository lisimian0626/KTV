package com.bestarmedia.libwidget.interf;

import android.view.View;

/**
 * View点击效果接口
 */
public interface ViewClickEffect {
    /**
     * 按下去的效果
     *
     * @param view
     */
    void onPressedEffect(View view);

    /**
     * 释放的效果
     *
     * @param view
     */
    void onUnPressedEffect(View view);
}
