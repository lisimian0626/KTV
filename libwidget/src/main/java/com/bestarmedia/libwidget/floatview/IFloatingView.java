package com.bestarmedia.libwidget.floatview;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Yunpeng Li on 2018/3/15.
 */

public interface IFloatingView {

    FloatingView removeAll();

    FloatingView remove(int key);

    FloatingView add(int key, int width, int height);

    FloatingView attach(Activity activity);

    FloatingView attach(FrameLayout container);

    FloatingView detach(Activity activity);

    FloatingView detach(FrameLayout container);

    EnFloatingView getView(int key);

    FloatingView icon(int key, @DrawableRes int resId);

    FloatingView layoutParams(int key, ViewGroup.LayoutParams params);

    FloatingView listener(int key, MagnetViewListener magnetViewListener);

}
