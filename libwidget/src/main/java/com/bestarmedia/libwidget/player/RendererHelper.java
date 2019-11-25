package com.bestarmedia.libwidget.player;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.bestarmedia.libwidget.player.renderer.BarGraphRenderer;
import com.bestarmedia.libwidget.player.renderer.CircleBarRenderer;
import com.bestarmedia.libwidget.player.renderer.CircleRenderer;
import com.bestarmedia.libwidget.player.renderer.LineRenderer;

import java.util.Random;

public class RendererHelper {

    public static void addRendererRandom(VisualizerView visualizerView) {
        addBarGraphRenderer(visualizerView);
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:
                addCircleBarRenderer(visualizerView);
                break;
            case 1:
                addCircleRenderer(visualizerView);
                break;
            default:
                addLineRenderer(visualizerView);
                break;
        }
    }

    /**
     * 上下立柱式
     *
     * @param visualizerView
     */
    private static void addBarGraphRenderer(VisualizerView visualizerView) {
        Paint paint2 = new Paint();
        paint2.setStrokeWidth(50f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(200, 181, 111, 233));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(16, paint2, true);
        visualizerView.addRenderer(barGraphRendererTop);

        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16, paint, false);
        visualizerView.addRenderer(barGraphRendererBottom);
    }

    /**
     * 圆形块状（中心分散）
     *
     * @param visualizerView
     */
    private static void addCircleBarRenderer(VisualizerView visualizerView) {
        Paint paint = new Paint();
        paint.setStrokeWidth(16f);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32, true);
        visualizerView.addRenderer(circleBarRenderer);
    }

    /**
     * 圆形波纹线条
     *
     * @param visualizerView
     */
    private static void addCircleRenderer(VisualizerView visualizerView) {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        visualizerView.addRenderer(circleRenderer);
    }

    /**
     * 音频震动频率
     *
     * @param visualizerView
     */
    private static void addLineRenderer(VisualizerView visualizerView) {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        visualizerView.addRenderer(lineRenderer);
    }

}
