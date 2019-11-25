package com.bestarmedia.libwidget.anim;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

import com.bestarmedia.libwidget.layout.EnterAnimLayout;

/**
 * Created by wpm on 2017/3/30.
 */

public class AnimShanXingZhanKai extends Anim {
    public AnimShanXingZhanKai(EnterAnimLayout view) {
        super(view);
        float r = (float) Math.sqrt(Math.pow(w, 2)+ Math.pow(h,2));
        oval = new RectF(w/2 -r , h/2-r, w+r-w/2, h+r-h/2);//以长方形对角线为边长的正方形
    }
    Path path1=new Path();
    RectF oval ;
    @Override
    public void handleCanvas(Canvas canvas, float rate) {
        //剪切扇形区域
        path1.reset();
        path1.addArc(oval,270-180*rate,360*rate);
        path1.lineTo(w/2,h/2);
        path1.close();//封闭
        canvas.clipPath(path1);

        canvas.save();
    }
}
