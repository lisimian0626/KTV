package com.bestarmedia.libwidget.image;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bestarmedia.libwidget.interf.DefaultClickEffectScaleAnimate;
import com.bestarmedia.libwidget.interf.OnClickEffectTouchListener;

public class ScaleRecyclerImage extends RecyclerImageView {

    public ScaleRecyclerImage(Context context) {
        super(context);
        init();
    }

    public ScaleRecyclerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleRecyclerImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        OnClickEffectTouchListener onClickEffectTouchListener = new OnClickEffectTouchListener();
        onClickEffectTouchListener.setViewClickEffect(new DefaultClickEffectScaleAnimate());
        this.setOnTouchListener(onClickEffectTouchListener);
    }
}
