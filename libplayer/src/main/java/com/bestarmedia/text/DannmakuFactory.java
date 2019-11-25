package com.bestarmedia.text;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by Chilling on 2018/4/14.
 */
public class DannmakuFactory extends ObjectFactory<Dannmaku> {

    private final Random random = new Random();
    private static final float VX_MULTIPLIER = 0.001f;
    private static final float VY_MULTIPLIER = 0.03f; // px/ms
    private final int width;
    private final int height;
    private Bitmap bitmap;
    private String text;
    private int color;

    public DannmakuFactory(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setDannmaku(Bitmap bitmap, String text, int color) {
        this.bitmap = bitmap;
        this.text = text;
        this.color = color;
    }

    @Override
    protected Dannmaku produce(Dannmaku dannmaku) {
        float vx = -(20 + random.nextInt(30)) * VY_MULTIPLIER;
        float y = 30 + random.nextInt(height / 2);
        if (dannmaku == null) {
            dannmaku = new Dannmaku(new PointF(width, y), vx);
        } else {
            dannmaku.reset(width, y, vx);
        }
        dannmaku.setBitmap(bitmap);
        dannmaku.setText(text);
        dannmaku.setColor(color);
        return dannmaku;
    }
}
