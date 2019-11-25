package com.bestarmedia.libwidget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bestarmedia.libwidget.interf.DefaultClickEffectScaleAnimate;
import com.bestarmedia.libwidget.interf.OnClickEffectTouchListener;


/**
 * Created by J Wong on 2018/11/30.
 * <p>
 * 就像上面的显示效果一样一束白光闪过，这种效果主要还是使用了LinearGradient类来进行的
 * LinearGradient也称作线性渲染，LinearGradient的作用是实现某一区域内颜色的线性渐变效果
 */

public class GradientTextView extends GradientBaseTextView {

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onClickEffectTouchListener.setViewClickEffect(defaultClickEffectScaleAnimate);
        this.setOnTouchListener(onClickEffectTouchListener);
    }

    private OnGradientTouchEvent touchEvent;

    public void setOnTouchEvent(OnGradientTouchEvent touchEvent) {
        this.touchEvent = touchEvent;
    }

    public interface OnGradientTouchEvent {
        void onTouchEvent(MotionEvent event);

        void onPressedEffect(boolean pressed);
    }

    private OnClickEffectTouchListener onClickEffectTouchListener = new OnClickEffectTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchEvent != null)
                touchEvent.onTouchEvent(event);
            return super.onTouch(v, event);
        }
    };
    private DefaultClickEffectScaleAnimate defaultClickEffectScaleAnimate = new DefaultClickEffectScaleAnimate() {
        @Override
        public void onPressedEffect(View view) {
            super.onPressedEffect(view);
            if (touchEvent != null)
                touchEvent.onPressedEffect(true);
        }

        @Override
        public void onUnPressedEffect(View view) {
            super.onUnPressedEffect(view);
            if (touchEvent != null)
                touchEvent.onPressedEffect(false);
        }
    };

}
