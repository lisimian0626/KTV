package com.bestarmedia.libwidget.material;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bestarmedia.libwidget.interf.DefaultClickEffectScaleAnimate;
import com.bestarmedia.libwidget.interf.OnClickEffectTouchListener;

public class RelativeLayoutMaterial extends RelativeLayout {

    public RelativeLayoutMaterial(Context context) {
        super(context);
        init();
    }

    public RelativeLayoutMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelativeLayoutMaterial(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        OnClickEffectTouchListener onClickEffectTouchListener = new OnClickEffectTouchListener();
        onClickEffectTouchListener.setViewClickEffect(new DefaultClickEffectScaleAnimate());
        this.setOnTouchListener(onClickEffectTouchListener);
    }
}
