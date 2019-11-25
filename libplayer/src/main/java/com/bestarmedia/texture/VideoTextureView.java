package com.bestarmedia.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bestarmedia.animation.Bubble;
import com.bestarmedia.libopengl.CanvasGL;
import com.bestarmedia.libopengl.ICanvasGL;
import com.bestarmedia.libopengl.canvas.gl.BasicTexture;
import com.bestarmedia.libopengl.canvas.gl.GLPaint;
import com.bestarmedia.libopengl.canvas.gl.RawTexture;
import com.bestarmedia.libopengl.filter.texture.BasicTextureFilter;
import com.bestarmedia.libopengl.filter.texture.TextureFilter;
import com.bestarmedia.libopengl.glview.texture.gles.GLThread;
import com.bestarmedia.renderer.AudioData;
import com.bestarmedia.renderer.CircleBarRenderer;
import com.bestarmedia.renderer.CircleRenderer;
import com.bestarmedia.renderer.FFTData;
import com.bestarmedia.renderer.LineRenderer;
import com.bestarmedia.renderer.Renderer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Chilling on 2018/4/14.
 */
public class VideoTextureView extends MediaPlayerTextureView {

    private int SMALL_WINDOW_WIDTH = 320;
    private int SMALL_WINDOW_HEIGHT = 180;
    //    private static final int INTERVAL_TIME_MS = 16;
    //    private List<Bubble> bubbles = new ArrayList<>();
//    private Wall wallTop = new Wall.WallY(0);
//    private Wall wallLeft = new Wall.WallX(0);
//    private Wall wallBottom;
//    private Wall wallRight;
    private boolean drawCustomBackground;
    private boolean drawScoreView;
    private byte[] mBytes;
    private Rect rect = new Rect();
    private int mSpectrumNum = 48;
    private boolean mFirst = true;
    private Set<Renderer> rendererList = new HashSet<>();
    private byte[] mFFTBytes;
    private byte[] mBgBytes;
    private boolean isPause;
    private Bitmap logoBitmap;
    private int logoWidth;
    private WindowStyle windowstyle = WindowStyle.NORMAL;
    private int scoreViewWidth;
    private int scoreNumColumns = 30;
    private float scoreColumnWidth;
    private float scoreSpace;
    private int scoreBaseY;
    private int micVolume;
    private Bitmap scoreBgBitmap;
    //X轴偏移量（目的：对准背景框）
    private float micVolumeBarXOffset = 30;
    private Bitmap backgroundFrame;
    private int currentScore;

    public VideoTextureView(Context context) {
        super(context);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        SMALL_WINDOW_WIDTH = width / 5;
        SMALL_WINDOW_HEIGHT = height / 5;
        scoreViewWidth = width / 6;
        micVolumeBarXOffset = (float) scoreViewWidth / 10;
        logoWidth = width / 15;
//        wallBottom = new Wall.WallY(height);
//        wallRight = new Wall.WallX(width);
        initBackgroundRenderer();
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    /*
     * 自定义背景
     *
     * @param customBackground 是否绘画自定义
     */
    public void setDrawCustomBackground(boolean customBackground) {
        drawCustomBackground = customBackground;
    }

    /**
     * 画评分
     *
     * @param drawScoreView
     */
    public void setDrawScoreView(boolean drawScoreView) {
        this.drawScoreView = drawScoreView;
    }

    public void setBubbles(List<Bubble> bubbles) {
//        this.bubbles.addAll(bubbles);
    }

    public void setBackgroundFrame(Bitmap backgroundFrame) {
        this.backgroundFrame = backgroundFrame;
        if (this.backgroundFrame != null) {
            setWindowStyle(WindowStyle.ZOOM_IN);
        } else {
            setWindowStyle(WindowStyle.NORMAL);
        }
    }

    public void setLogoBitmap(Bitmap bitmap) {
        this.logoBitmap = bitmap;
    }

    public void setScoreBitmap(Bitmap bitmap) {
        this.scoreBgBitmap = bitmap;
    }

    public void setWindowStyle(WindowStyle windowStyle) {
        this.windowstyle = windowStyle;
    }

    @Override
    protected int getRenderMode() {
        return GLThread.RENDERMODE_CONTINUOUSLY;
    }

    private TextureFilter textureFilter = new BasicTextureFilter();

    @Override
    protected void onGLDraw(ICanvasGL canvas, SurfaceTexture producedSurfaceTexture, RawTexture producedRawTexture,
                            @Nullable SurfaceTexture sharedSurfaceTexture, @Nullable BasicTexture sharedTexture) {
        if (drawCustomBackground) {
//            drawBitmapWithOtherMatrix(canvas);
//            drawBitmapWithMatrix(canvas);
//            drawCircle(canvas);
//            drawRectAndLine(canvas);
            drawLogo(canvas);
            drawBarRender(canvas);
            drawBackgroundRenderer(canvas);
        } else {
            if (windowstyle == WindowStyle.ZOOM_IN) {
                producedRawTexture.setIsFlippedVertically(true);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT, getWidth() - SMALL_WINDOW_WIDTH, getHeight() - SMALL_WINDOW_HEIGHT, textureFilter);
                drawBackgroundFrame(canvas);
            } else if (windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_OUTSIDE || windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_INSIDE) {
                producedRawTexture.setIsFlippedVertically(true);
                if (windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_INSIDE) {
                    canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, 0, 0, getWidth(), getHeight(), textureFilter);
                } else {
                    canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT, getWidth() - SMALL_WINDOW_WIDTH, getHeight() - SMALL_WINDOW_HEIGHT, textureFilter);
                }
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, getWidth() - SMALL_WINDOW_WIDTH, getHeight() - SMALL_WINDOW_HEIGHT, getWidth(), getHeight(), textureFilter);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, getWidth() - SMALL_WINDOW_WIDTH, 0, getWidth(), SMALL_WINDOW_HEIGHT, textureFilter);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, 0, getHeight() - SMALL_WINDOW_HEIGHT, SMALL_WINDOW_WIDTH, getHeight(), textureFilter);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, 0, 0, SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT, textureFilter);
            } else if (windowstyle == WindowStyle.THREE_X_THREE || windowstyle == WindowStyle.TWO_X_TWO) {//按行列均分
                producedRawTexture.setIsFlippedVertically(true);
                int rows = windowstyle == WindowStyle.THREE_X_THREE ? 3 : 2;
                int cols = windowstyle == WindowStyle.THREE_X_THREE ? 3 : 2;
                int windowHeight = getHeight() / rows;
                int windowWidth = getWidth() / cols;
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int left = col * windowWidth;
                        int top = row * windowHeight;
                        canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, left, top, left + windowWidth, top + windowHeight, textureFilter);
                    }
                }
            } else if (windowstyle == WindowStyle.ONE_BIG_ON_SMALL_RIGHT) {
                producedRawTexture.setIsFlippedVertically(true);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, 0, 0, getWidth() - SMALL_WINDOW_WIDTH, getHeight(), textureFilter);
                canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture,
                        getWidth() - SMALL_WINDOW_WIDTH, (getHeight() - SMALL_WINDOW_HEIGHT) / 2, getWidth(), (getHeight() + SMALL_WINDOW_HEIGHT) / 2, textureFilter);
            } else {//默认单个画面，全屏
                super.onGLDraw(canvas, producedSurfaceTexture, producedRawTexture, sharedSurfaceTexture, sharedTexture);
            }
            //以下为横向并排显示
//                int size = consumedTextures.size();
//                int left = getWidth() * i / size;
//                canvas.drawSurfaceTexture(producedRawTexture, sharedSurfaceTexture, left, 0, left + getWidth() / size, getHeight());
            if (drawScoreView) {
                drawScoreView(canvas);
            }
        }
    }


    /**
     * 初始动态背景
     */
    public void initBackgroundRenderer() {
        GLPaint paintCircleBar = new GLPaint();
        paintCircleBar.setLineWidth(8f);
        paintCircleBar.setStyle(Paint.Style.STROKE);
        paintCircleBar.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paintCircleBar, 32, true);
        rendererList.add(circleBarRenderer);

        GLPaint paint = new GLPaint();
        paint.setLineWidth(3f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        rendererList.add(circleRenderer);

        //心电图
        GLPaint linePaint = new GLPaint();
        linePaint.setLineWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.argb(88, 0, 128, 255));
        GLPaint lineFlashPaint = new GLPaint();
        lineFlashPaint.setLineWidth(2f);
        linePaint.setStyle(Paint.Style.STROKE);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        rendererList.add(lineRenderer);
    }

    /**
     * 绘制动态背景
     */
    private void drawBackgroundRenderer(ICanvasGL canvas) {
//        if (!isPause) {
//            for (Bubble bubble : bubbles) {//漂浮音乐符合
//                bubble.glDraw(canvas);
//                if (wallTop.isTouch(bubble.point, bubble.collisionRadius) || wallBottom.isTouch(bubble.point, bubble.collisionRadius)) {
//                    bubble.onCollision(MovableObj.CollisionListener.DIRECTION_VERTICAL);
//                } else if (wallLeft.isTouch(bubble.point, bubble.collisionRadius) || wallRight.isTouch(bubble.point, bubble.collisionRadius)) {
//                    bubble.onCollision(MovableObj.CollisionListener.DIRECTION_HORIZONTAL);
//                }
//                bubble.updatePosition(INTERVAL_TIME_MS);
//            }
//        }
        rect.set(0, 0, getWidth(), getHeight());
        if (mBgBytes != null) {
            AudioData audioData = new AudioData(mBgBytes);
            for (Renderer r : rendererList) {
                r.render(canvas, audioData, rect);
            }
        }
        if (mFFTBytes != null) {// Render all FFT renderers
            FFTData fftData = new FFTData(mFFTBytes);
            for (Renderer r : rendererList) {
                r.render(canvas, fftData, rect);
            }
        }
    }


    /**
     * 添加MediaPlayer Visualizer FFT数据
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        updateBarVisualizer(bytes);
    }

    /**
     * 添加MediaPlayer Visualizer 数据
     */
    public void updateVisualizer(byte[] bytes) {
        mBgBytes = bytes;
        updateBarVisualizer(bytes);
    }

    /**
     * 添加音频柱状图数据
     */
    private void updateBarVisualizer(byte[] fft) {
        if (mFirst) {
            mFirst = false;
        }
        if (drawCustomBackground) {
            byte[] model = new byte[fft.length / 2 + 1];
            model[0] = (byte) Math.abs(fft[0]);
            for (int i = 2, j = 1; j < mSpectrumNum; ) {
                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
                i += 2;
                j++;
            }
            mBytes = model;
        }
    }

    /**
     * 绘制音频柱状图
     */
    private void drawBarRender(ICanvasGL canvas) {
        if (mBytes == null) {
            return;
        }
        GLPaint paint = new GLPaint();
        paint.setLineWidth(16);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        rect.set(0, 0, getWidth(), getHeight());
        //绘制频谱
        final int baseX = rect.width() / mSpectrumNum;
        final int height = rect.height();
        for (int i = 0; i < mSpectrumNum; i++) {
            if (mBytes[i] < 0) {
                mBytes[i] = 127;
            }
            final int xi = baseX * i + baseX / 2;
            canvas.drawLine(xi, height, xi, height - mBytes[i], paint);
        }
    }

    /**
     * 绘制左上角logo
     */
    private void drawLogo(ICanvasGL canvas) {
        if (logoBitmap != null) {
            CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
            matrix.translate(logoWidth / 2, logoWidth / 2);
            float scale = (float) logoWidth / logoBitmap.getWidth();
            matrix.scale(scale, scale);
            canvas.drawBitmap(logoBitmap, matrix);
            matrix.reset();
        }
    }

    /**
     * 评分
     */
    private void drawScoreView(ICanvasGL canvas) {
        //        rect.set(0, (getHeight() - scoreViewHeight) / 2, scoreViewWidth, scoreViewHeight);
        rect.set(0, 0, getWidth(), getHeight());
        scoreColumnWidth = (float) scoreViewWidth / (float) scoreNumColumns;
        scoreSpace = scoreColumnWidth / 8f;
        scoreBaseY = getHeight() / 2;
        if (scoreBgBitmap != null) {
            CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
            float scale = 1.2f * (float) scoreViewWidth / scoreBgBitmap.getWidth();
            matrix.scale(scale, scale);
            matrix.translate(0, scoreBaseY - scale * scoreBgBitmap.getHeight() / 2);
            canvas.drawBitmap(scoreBgBitmap, matrix);
            matrix.reset();
        }

        GLPaint paint = new GLPaint();
        paint.setColor(Color.argb(255, 255, 255, 255));
        GLPaint paintReflect = new GLPaint();
        paintReflect.setColor(Color.argb(125, 255, 255, 255));
        for (int i = 0; i < scoreNumColumns; i++) {
            float height = 0.5f * getRandomHeight(micVolume);
            float left = i * scoreColumnWidth + scoreSpace;
            float right = (i + 1) * scoreColumnWidth - scoreSpace;
            int drawCount = (int) (height / (right - left));
            if (drawCount == 0) {
                drawCount = 1;
            }
            float drawHeight = height / drawCount;
            // draw each pixel
            for (int j = 0; j < drawCount; j++) {
                float bottom = scoreBaseY - (drawHeight * j) - 1;
                float top = bottom - drawHeight + scoreSpace;
                RectF rect = new RectF(left + micVolumeBarXOffset, top, right + micVolumeBarXOffset, bottom);
                canvas.drawRect(rect, paint);

                top = scoreBaseY + (drawHeight * j) + 1;
                bottom = top + drawHeight - scoreSpace;
                rect = new RectF(left + micVolumeBarXOffset, top, right + micVolumeBarXOffset, bottom);
                float alpha = (float) (drawCount - j) / (float) (drawCount * 4);
                paintReflect.setColor(Color.argb((int) (alpha * 255), 255, 255, 255));
                canvas.drawRect(rect, paintReflect);
            }
        }
//        drawScore(canvas);
    }

    public void setCurrentScore(int score) {
        this.currentScore = score;
    }

    private void drawScore(ICanvasGL canvas) {
        int rectWidth = getWidth() / 12;
        int rectHeight = getHeight() / 12;
        Bitmap textBitmap = Bitmap.createBitmap(rectWidth, rectHeight, Bitmap.Config.ARGB_8888);
        Canvas normalCanvas = new Canvas(textBitmap);
        String text = String.valueOf(currentScore);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStrokeWidth(2.0f);
        textPaint.setFakeBoldText(false);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setShadowLayer(2, 0, 0, Color.BLACK);
        textPaint.setColor(Color.parseColor("#ffff00"));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(60);
//        normalCanvas.drawColor(Color.WHITE);
        normalCanvas.drawText(text, 0.2f * rectWidth, 0.8f * rectHeight, textPaint);
        canvas.drawBitmap(textBitmap, getWidth() / 20, (int) (0.3f * getHeight()));

    }

    private float getRandomHeight(int volume) {
        double randomVolume = Math.random() * volume + 1;
        float height = scoreBaseY;
        return ((height / 80f) * (float) randomVolume);
    }

    public void setMicVolume(final int volume) {
        this.micVolume = volume;
    }

    private void drawCircle(ICanvasGL canvas) {
        //circle
        GLPaint circlePaint = new GLPaint();
        circlePaint.setColor(Color.parseColor("#88FF0000"));
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(430, 30, 30, circlePaint);

        GLPaint strokeCirclePaint = new GLPaint();
        strokeCirclePaint.setColor(Color.parseColor("#88FF0000"));
        strokeCirclePaint.setLineWidth(4);
        strokeCirclePaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(490, 30, 30, strokeCirclePaint);
    }


    /**
     * 绘制炫屏背景
     */
    private void drawBackgroundFrame(ICanvasGL canvas) {
        if (backgroundFrame != null) {
            CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
            matrix.translate(0, 0);
            float scale = (float) getWidth() / backgroundFrame.getWidth();
            matrix.scale(scale, scale);
            canvas.drawBitmap(backgroundFrame, matrix);
            matrix.reset();
        }
    }


}