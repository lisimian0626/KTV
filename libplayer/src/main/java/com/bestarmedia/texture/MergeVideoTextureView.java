package com.bestarmedia.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.bestarmedia.animation.Bubble;
import com.bestarmedia.animation.MovableObj;
import com.bestarmedia.animation.Wall;
import com.bestarmedia.libopengl.CanvasGL;
import com.bestarmedia.libopengl.ICanvasGL;
import com.bestarmedia.libopengl.canvas.gl.GLPaint;
import com.bestarmedia.libopengl.glview.texture.GLMultiTexConsumerView;
import com.bestarmedia.libopengl.glview.texture.GLTexture;
import com.bestarmedia.libopengl.glview.texture.gles.GLThread;
import com.bestarmedia.renderer.AudioData;
import com.bestarmedia.renderer.CircleBarRenderer;
import com.bestarmedia.renderer.CircleRenderer;
import com.bestarmedia.renderer.FFTData;
import com.bestarmedia.renderer.LineRenderer;
import com.bestarmedia.renderer.Renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Only used to consume textures from others.
 */
public class MergeVideoTextureView extends GLMultiTexConsumerView {

    private int SMALL_WINDOW_WIDTH = 320;
    private int SMALL_WINDOW_HEIGHT = 180;
    private static final int INTERNVAL_TIME_MS = 8;
    private List<Bubble> bubbles = new ArrayList<>();
    private Wall wallTop = new Wall.WallY(0);
    private Wall wallLeft = new Wall.WallX(0);
    private Wall wallBottom;
    private Wall wallRight;
    private boolean drawCustomBackground;
    private byte[] mBytes;
    private Rect mRect = new Rect();
    private int mSpectrumNum = 48;
    private boolean mFirst = true;
    private Set<Renderer> mRenderers = new HashSet<>();
    private byte[] mFFTBytes;
    private byte[] mBgBytes;
    private boolean isPause;
    //    private IAndroidCanvasHelper drawTextHelper = IAndroidCanvasHelper.Factory.createAndroidCanvasHelper(IAndroidCanvasHelper.MODE.MODE_SYNC);
//    private DannmakuFactory dannmakuFactory;
//    private Paint dannmakuTextPaint;
    private Bitmap logoBitmap;
    private WindowStyle windowstyle = WindowStyle.NORMAL;


    public MergeVideoTextureView(Context context) {
        super(context);
    }

    public MergeVideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MergeVideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    /**
     * 自定义背景
     *
     * @param customBackground 是否绘画自定义
     */
    public void setDrawCustomBackground(boolean customBackground) {
        drawCustomBackground = customBackground;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        SMALL_WINDOW_WIDTH = width / 8;
        SMALL_WINDOW_HEIGHT = height / 8;
//        dannmakuFactory = new DannmakuFactory(width, height);
//        drawTextHelper.init(width, height);
//        dannmakuTextPaint = new Paint();
//        dannmakuTextPaint.setColor(Color.WHITE);
//        dannmakuTextPaint.setTextSize(SMALL_WINDOW_HEIGHT / 20);
        wallBottom = new Wall.WallY(height);
        wallRight = new Wall.WallX(width);
        initBackgroundRenderer();
    }

    public void setBubbles(List<Bubble> bubbles) {
        this.bubbles.addAll(bubbles);
    }


    public void setLogoBitmap(Bitmap bitmap) {
        this.logoBitmap = bitmap;
    }

    public void setWindowStyle(WindowStyle windowStyle) {
        this.windowstyle = windowStyle;
    }

    @Override
    protected int getRenderMode() {
        return GLThread.RENDERMODE_CONTINUOUSLY;
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas, List<GLTexture> consumedTextures) {
        if (drawCustomBackground) {
            drawBlackBackground(canvas);
//            drawBitmapWithOtherMatrix(canvas);
//            drawBitmapWithMatrix(canvas);
//            drawCircle(canvas);
//            drawRectAndLine(canvas);
            drawLogo(canvas);
//            drawBarRender(canvas);
            drawBackgroundRenderer(canvas);
        } else {
            for (int i = 0; i < consumedTextures.size(); i++) {
                GLTexture texture = consumedTextures.get(i);
                if (windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_OUTSIDE || windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_INSIDE) {
                    if (windowstyle == WindowStyle.ONE_BIG_FOUR_SMALL_INSIDE) {
                        canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                                0, 0, getWidth(), getHeight());
                    } else {
                        canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                                SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT, getWidth() - SMALL_WINDOW_WIDTH, getHeight() - SMALL_WINDOW_HEIGHT);
                    }
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            getWidth() - SMALL_WINDOW_WIDTH, getHeight() - SMALL_WINDOW_HEIGHT, getWidth(), getHeight());
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            getWidth() - SMALL_WINDOW_WIDTH, 0, getWidth(), SMALL_WINDOW_HEIGHT);
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            0, getHeight() - SMALL_WINDOW_HEIGHT, SMALL_WINDOW_WIDTH, getHeight());
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            0, 0, SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
                } else if (windowstyle == WindowStyle.THREE_X_THREE || windowstyle == WindowStyle.TWO_X_TWO) {
                    int rows = windowstyle == WindowStyle.THREE_X_THREE ? 3 : 2;
                    int cols = windowstyle == WindowStyle.THREE_X_THREE ? 3 : 2;
                    int windowHeight = getHeight() / rows;
                    int windowWidth = getWidth() / cols;
                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < cols; col++) {
                            int left = col * windowWidth;
                            int top = row * windowHeight;
                            canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(), left, top, left + windowWidth, top + windowHeight);
                        }
                    }
                } else if (windowstyle == WindowStyle.ONE_BIG_ON_SMALL_RIGHT) {
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            0, 0, getWidth() - SMALL_WINDOW_WIDTH, getHeight());
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            getWidth() - SMALL_WINDOW_WIDTH, (getHeight() - SMALL_WINDOW_HEIGHT) / 2, getWidth(), (getHeight() + SMALL_WINDOW_HEIGHT) / 2);
                } else {
                    canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(),
                            0, 0, getWidth(), getHeight());
                }
                //以下为横向并排显示
//                int size = consumedTextures.size();
//                int left = getWidth() * i / size;
//                canvas.drawSurfaceTexture(texture.getRawTexture(), texture.getSurfaceTexture(), left, 0, left + getWidth() / size, getHeight());
            }
        }
//        drawDannmaku(canvas);
    }


    /**
     * 初始动态背景
     */
    public void initBackgroundRenderer() {
        GLPaint paintCircleBar = new GLPaint();
        paintCircleBar.setLineWidth(1f);
        paintCircleBar.setStyle(Paint.Style.STROKE);
        paintCircleBar.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paintCircleBar, 32, true);
        mRenderers.add(circleBarRenderer);

        GLPaint paint = new GLPaint();
        paint.setLineWidth(1f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        mRenderers.add(circleRenderer);

        //心电图
        GLPaint linePaint = new GLPaint();
        linePaint.setLineWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.argb(88, 0, 128, 255));
        GLPaint lineFlashPaint = new GLPaint();
        lineFlashPaint.setLineWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        mRenderers.add(lineRenderer);
    }

    /**
     * 绘制动态背景
     *
     * @param canvas
     */
    private void drawBackgroundRenderer(ICanvasGL canvas) {
        if (!isPause) {
            for (Bubble bubble : bubbles) {
                //漂浮音乐符合
                bubble.glDraw(canvas);
                if (wallTop.isTouch(bubble.point, bubble.collisionRadius) || wallBottom.isTouch(bubble.point, bubble.collisionRadius)) {
                    bubble.onCollision(MovableObj.CollisionListener.DIRECTION_VERTICAL);
                } else if (wallLeft.isTouch(bubble.point, bubble.collisionRadius) || wallRight.isTouch(bubble.point, bubble.collisionRadius)) {
                    bubble.onCollision(MovableObj.CollisionListener.DIRECTION_HORIZONTAL);
                }
                bubble.updatePosition(INTERNVAL_TIME_MS);
            }
        }

//        mRect.set(0, 0, getWidth(), getHeight());
//        if (mBgBytes != null) {
//            AudioData audioData = new AudioData(mBgBytes);
//            for (Renderer r : mRenderers) {
//                r.render(canvas, audioData, mRect);
//            }
//        }
//        if (mFFTBytes != null) {// Render all FFT renderers
//            FFTData fftData = new FFTData(mFFTBytes);
//            for (Renderer r : mRenderers) {
//                r.render(canvas, fftData, mRect);
//            }
//        }
    }


    /**
     * 添加MediaPlayer Visualizer FFT数据
     *
     * @param bytes
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        updateBarVisualizer(bytes);
    }

    /**
     * 添加MediaPlayer Visualizer 数据
     *
     * @param bytes
     */
    public void updateVisualizer(byte[] bytes) {
        mBgBytes = bytes;
        updateBarVisualizer(bytes);
    }

    /**
     * 添加音频柱状图数据
     *
     * @param fft
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

    private void drawBlackBackground(ICanvasGL canvas) {
        GLPaint paint = new GLPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    /**
     * 绘制音频柱状图
     *
     * @param canvas
     */
    private void drawBarRender(ICanvasGL canvas) {
        if (mBytes == null) {
            return;
        }
        GLPaint paint = new GLPaint();
        paint.setLineWidth(SMALL_WINDOW_HEIGHT / 10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        mRect.set(0, 0, getWidth(), getHeight());
        //绘制频谱
        final int baseX = mRect.width() / mSpectrumNum;
        final int height = mRect.height();
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
     *
     * @param canvas
     */
    private void drawLogo(ICanvasGL canvas) {
        if (logoBitmap != null) {
            CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
            float scale = 0.3F;
            matrix.scale(scale, scale);
            matrix.translate((getWidth() - scale * logoBitmap.getWidth()) / 2, (getHeight() - scale * logoBitmap.getHeight()) / 2);
            canvas.drawBitmap(logoBitmap, matrix);
            matrix.reset();
        }
    }

    /**
     * 添加弹幕，会引起卡顿（改用别的方案）
     *
     * @param bitmap
     * @param dannmaku
     */
    public void addDannmaku(Bitmap bitmap, String dannmaku, int color) {
//        dannmakuFactory.setDannmaku(bitmap, dannmaku, color);
//        dannmakuFactory.book(1);
    }

//    /**
//     * 绘制弹幕
//     *
//     * @param canvasGL
//     */
//    private void drawDannmaku(final ICanvasGL canvasGL) {
//        drawTextHelper.draw(new IAndroidCanvasHelper.CanvasPainter() {
//            @Override
//            public void draw(Canvas canvas) {
//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                List<Dannmaku> availableItems = dannmakuFactory.getAvailableItems();
//                for (Dannmaku dannmaku : availableItems) {
//                    if (dannmaku.getLength() == 0) {
//                        float length = dannmakuTextPaint.measureText(dannmaku.getText());
//                        dannmaku.setLength(length);
//                    }
//                    dannmaku.updatePosition(INTERNVAL_TIME_MS);
//                    canvas.drawText(dannmaku.getText(), dannmaku.point.x, dannmaku.point.y, dannmakuTextPaint);
//                    dannmakuTextPaint.setColor(dannmaku.getColor());
//                    if (dannmaku.getBitmap() != null) {
//                        CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
//                        matrix.translate(dannmaku.point.x, dannmaku.point.y);
//                        matrix.scale(0.3f, 0.3f);
//                        canvasGL.drawBitmap(dannmaku.getBitmap(), matrix);
////                        matrix.reset();
////                        canvas.drawBitmap(getBitmap(), dannmaku.point.x - dannmaku.getBitmap().getWidth(), dannmaku.point.y, null);
//                    }
//                    if (shouldRecycle(dannmaku)) {
//                        dannmakuFactory.release(dannmaku);
////                        int bookingCnt = 1;
////                        dannmakuFactory.book(bookingCnt);
//                    }
//                }
//            }
//        });
//        Bitmap outputBitmap = drawTextHelper.getOutputBitmap();
//        canvasGL.invalidateTextureContent(outputBitmap);
//        canvasGL.drawBitmap(outputBitmap, 0, 0);
//    }
//
//    private static boolean shouldRecycle(Dannmaku dannmaku) {
//        if (dannmaku.getRight() < 0) {
//            return true;
//        }
//        return false;
//    }


//    private void drawBitmapWithOtherMatrix(ICanvasGL canvas) {
//        if (logoBitmap != null) {
//            CanvasGL.OrthoBitmapMatrix matrix = new CanvasGL.OrthoBitmapMatrix();
//            canvas.drawBitmap(logoBitmap, matrix);
//            matrix.reset();
//
//            matrix.translate(500, 150);
//            matrix.scale(10f, 10f);
//            canvas.drawBitmap(logoBitmap, matrix);
//            matrix.reset();
//
//            matrix.translate(0, 100);
//            matrix.rotateZ(45);
//            canvas.drawBitmap(logoBitmap, matrix);
//        }
//    }
//
//    private void drawCircle(ICanvasGL canvas) {
//        //circle
//        GLPaint circlePaint = new GLPaint();
//        circlePaint.setColor(Color.parseColor("#88FF0000"));
//        circlePaint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(430, 30, 30, circlePaint);
//
//        GLPaint strokeCirclePaint = new GLPaint();
//        strokeCirclePaint.setColor(Color.parseColor("#88FF0000"));
//        strokeCirclePaint.setLineWidth(4);
//        strokeCirclePaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(490, 30, 30, strokeCirclePaint);
//    }
//
//    private void drawRectAndLine(ICanvasGL canvas) {
//        GLPaint paint = new GLPaint();
//        paint.setColor(Color.parseColor("#88FF0000"));
//        paint.setLineWidth(4);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(360, 0, 380, 40, paint);
//
//        GLPaint paint2 = new GLPaint();
//        paint2.setColor(Color.parseColor("#8800FF00"));
//        paint2.setLineWidth(4);
//        paint2.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(560, 40, 760, 180, paint2);
//        canvas.drawLine(360, 80, 360, 120, paint);
//    }
//
//    private void drawText(ICanvasGL canvas) {
//        // text
//        Bitmap textBitmap = Bitmap.createBitmap(180, 100, Bitmap.Config.ARGB_8888);
//        Canvas normalCanvas = new Canvas(textBitmap);
//        String text = "text";
//        Paint textPaint = new Paint();
//        textPaint.setColor(Color.BLUE);
//        textPaint.setStyle(Paint.Style.FILL);
//        textPaint.setTextSize(40);
//        normalCanvas.drawColor(Color.WHITE);
//        normalCanvas.drawText(text, 20, 30, textPaint);
//        canvas.drawBitmap(textBitmap, 500, 80);
//    }
//
//    private void drawBitmapWithMatrix(ICanvasGL canvas) {
//        CanvasGL.BitmapMatrix matrix = new CanvasGL.BitmapMatrix();
//        matrix.scale(1.3f, 1.6f);
//        matrix.rotateX(34);
//        matrix.rotateY(64);
//        matrix.rotateZ(30);
//        matrix.translate(390, 0);
//        canvas.drawBitmap(logoBitmap, matrix);
//        matrix.reset();
//        matrix.translate(28, 19);
//        matrix.rotateZ(30);
//        canvas.drawBitmap(logoBitmap, matrix);
//    }

}
