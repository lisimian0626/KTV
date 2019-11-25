package com.bestarmedia.libwidget.anim;

import android.graphics.Canvas;
import android.graphics.Path;

import com.bestarmedia.libwidget.layout.EnterAnimLayout;

/**
 * Created by wpm on 2017/3/30.
 */

public class AnimBaiYeChuang extends Anim {

    AnimBaiYeChuang(EnterAnimLayout view) {
        super(view);
    }

    private float lines = 6;//百叶窗的行数

    private Path path = new Path();

    @Override
    public void handleCanvas(Canvas canvas, float rate) {
        path.reset();
        //计算百叶窗每一行当前需要展示的左上右下
        for (int i = 0; i < lines; i++) {
            float top = h / lines * i;
            float bottom = top + h / lines * rate;
            path.addRect(0, top, w, bottom, Path.Direction.CW);
        }
        canvas.clipPath(path);
        canvas.save();
    }
}
