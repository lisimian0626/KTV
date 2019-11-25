package com.bestarmedia.libwidget.anim;

import android.graphics.Canvas;
import android.graphics.Region;

import com.bestarmedia.libwidget.layout.EnterAnimLayout;

/**
 * Created by wpm on 2017/3/30.
 */

public class AnimShiZiXingKuoZhan extends Anim {
    public AnimShiZiXingKuoZhan(EnterAnimLayout view) {
        super(view);
    }

    @Override
    public void handleCanvas(Canvas canvas, float rate) {
        float rectLeft =  0;
        float rectRight = w;
        float rectTop = (h/2 * rate);
        float rectBottom = h - rectTop;

        float rectLeft1 =  w/2*rate;
        float rectRight1 = w-rectLeft1;
        float rectTop1 = 0;
        float rectBottom1 = h;
        canvas.clipRect(rectLeft, rectTop,rectRight , rectBottom, Region.Op.DIFFERENCE);
        canvas.clipRect(rectLeft1, rectTop1,rectRight1 , rectBottom1, Region.Op.DIFFERENCE);

        canvas.save();
    }
}
