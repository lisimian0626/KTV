package com.bestarmedia.libwidget.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by J Wong on 2016/1/6 14:07.
 */
public class ResizeAnimation extends Animation {
    final int targetHeight;
    final int targetWidth;

    View view;
    int startHeight;
    int startWidth;

    public ResizeAnimation(View view, int targetWidth, int targetHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        startHeight = view.getHeight();
        startWidth = view.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        int newWidth;
        if (startHeight > targetWidth) {
            newHeight = (int) (startHeight - (startHeight - targetHeight) * interpolatedTime);
            newWidth = (int) (startWidth - (startWidth - targetWidth) * interpolatedTime);
        } else {
            newHeight = (int) (startHeight + targetHeight * interpolatedTime);
            newWidth = (int) (startWidth + targetWidth * interpolatedTime);
        }
        view.getLayoutParams().height = newHeight;
        view.getLayoutParams().width = newWidth;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}